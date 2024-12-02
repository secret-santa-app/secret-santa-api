package org.builtonaws.secretsanta.controller;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import java.util.Objects;
import org.builtonaws.secretsanta.exception.InternalServerException;
import org.builtonaws.secretsanta.exception.NotFoundException;
import org.builtonaws.secretsanta.persistence.DraftRepository;

public class GetDraftController {
    private final ObjectMapper objectMapper;
    private final DraftRepository draftRepository;

    @Inject
    public GetDraftController(ObjectMapper objectMapper, DraftRepository draftRepository) {
        this.draftRepository = draftRepository;
        this.objectMapper = objectMapper;
    }

    public APIGatewayV2HTTPResponse handle(APIGatewayV2HTTPEvent request)
            throws InternalServerException, NotFoundException {
        var id = Objects.requireNonNull(request.getPathParameters().get("id"));
        var draft = draftRepository.getDraft(id);
        try {
            return APIGatewayV2HTTPResponse.builder()
                    .withStatusCode(200)
                    .withBody(objectMapper.writeValueAsString(draft))
                    .build();
        } catch (JsonProcessingException e) {
            throw new InternalServerException(e);
        }
    }
}
