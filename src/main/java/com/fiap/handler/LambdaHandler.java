package com.fiap.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class LambdaHandler implements
        RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context) {
        var logger = context.getLogger();

        logger.log("Request received - " + request.getBody());

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("""
                        {
                            "sucesso": true
                        }
                        """)
                .withIsBase64Encoded(false);
    }

}
