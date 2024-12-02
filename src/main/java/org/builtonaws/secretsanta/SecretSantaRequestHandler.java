package org.builtonaws.secretsanta;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import org.builtonaws.secretsanta.dagger.ControllersComponent;
import org.builtonaws.secretsanta.dagger.DaggerControllersComponent;
import org.builtonaws.secretsanta.exception.SecretSantaException;
import org.slf4j.LoggerFactory;

public class SecretSantaRequestHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private static final ControllersComponent controllersComponent = DaggerControllersComponent.create();
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SecretSantaRequestHandler.class);

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent input, Context context) {
        log.info("Received request: {}", input.getRouteKey());
        try {
            if (input.getRouteKey().equalsIgnoreCase("POST /drafts")) {
                return controllersComponent.createDraftController().handle(input);
            } else if (input.getRouteKey().equalsIgnoreCase("GET /drafts/{id}")) {
                return controllersComponent.getDraftController().handle(input);
            } else if (input.getRouteKey().equalsIgnoreCase("POST /drafts/{id}/draw")) {
                return controllersComponent.createAssignmentController().handle(input);
            } else {
                return APIGatewayV2HTTPResponse.builder().withStatusCode(404).build();
            }
        } catch (SecretSantaException e) {
            return APIGatewayV2HTTPResponse.builder()
                    .withStatusCode(e.getHttpStatusCode())
                    .withBody(e.getMessage())
                    .build();
        }
    }
}
