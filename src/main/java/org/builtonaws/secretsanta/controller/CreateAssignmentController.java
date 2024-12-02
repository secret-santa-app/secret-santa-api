package org.builtonaws.secretsanta.controller;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.Objects;
import org.builtonaws.secretsanta.DraftAssigner;
import org.builtonaws.secretsanta.exception.BadRequestException;
import org.builtonaws.secretsanta.exception.NotFoundException;
import org.builtonaws.secretsanta.persistence.DraftRepository;

public class CreateAssignmentController {
    private final DraftRepository draftRepository;
    private final DraftAssigner draftAssigner;

    @Inject
    public CreateAssignmentController(DraftRepository draftRepository, DraftAssigner draftAssigner) {
        this.draftRepository = draftRepository;
        this.draftAssigner = draftAssigner;
    }

    public APIGatewayV2HTTPResponse handle(APIGatewayV2HTTPEvent request)
            throws BadRequestException, NotFoundException {
        var id = Objects.requireNonNull(request.getPathParameters().get("id"));
        var draft = draftRepository.getDraft(id);
        draft = draftAssigner.assign(draft);
        draftRepository.saveDraft(draft);
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(201)
                .withHeaders(Map.of("Location", "/drafts/" + draft.draftId()))
                .build();
    }
}
