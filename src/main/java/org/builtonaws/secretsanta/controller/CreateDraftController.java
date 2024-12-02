package org.builtonaws.secretsanta.controller;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.Objects;
import org.builtonaws.secretsanta.DraftDtoMapper;
import org.builtonaws.secretsanta.dto.NewDraft;
import org.builtonaws.secretsanta.exception.BadRequestException;
import org.builtonaws.secretsanta.persistence.DraftRepository;

public class CreateDraftController {
    private final DraftDtoMapper draftDtoMapper;
    private final ObjectMapper objectMapper;
    private final DraftRepository draftRepository;

    @Inject
    public CreateDraftController(
            DraftDtoMapper draftDtoMapper, ObjectMapper objectMapper, DraftRepository draftRepository) {
        this.draftDtoMapper = draftDtoMapper;
        this.draftRepository = draftRepository;
        this.objectMapper = objectMapper;
    }

    public APIGatewayV2HTTPResponse handle(APIGatewayV2HTTPEvent request) throws BadRequestException {
        try {
            var ownerId = request.getRequestContext()
                    .getAuthorizer()
                    .getJwt()
                    .getClaims()
                    .get("sub");
            Objects.requireNonNull(ownerId);
            var newDraft = objectMapper.readValue(request.getBody(), NewDraft.class);
            var draft = draftDtoMapper.mapNewDraft(newDraft, ownerId);
            draftRepository.saveDraft(draft);
            return APIGatewayV2HTTPResponse.builder()
                    .withStatusCode(201)
                    .withHeaders(Map.of("Location", "/drafts/" + draft.draftId()))
                    .build();
        } catch (JsonProcessingException e) {
            throw new BadRequestException(e);
        }
    }
}
