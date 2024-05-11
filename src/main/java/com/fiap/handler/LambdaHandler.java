package com.fiap.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fiap.gateway.ClienteGateway;
import com.fiap.util.JwtTokenUtil;
import com.google.gson.Gson;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Gson gson = new Gson();
    ClienteGateway clienteGateway = new ClienteGateway();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        var logger = context.getLogger();

        if (request.getBody() == null || request.getBody().isEmpty()) {
            logger.log("Invalid request - Empty body");
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("{ \"error\": \"Empty body\" }");
        }

        try {
            AuthDto authDto = gson.fromJson(request.getBody(), AuthDto.class);
            if (authDto == null || authDto.getCpf() == null || authDto.getCpf().isEmpty()) {
                logger.log("Invalid request - Missing or empty 'cpf' field");
                return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("{ \"error\": \"Missing or empty 'cpf' field\" }");
            }

            String cpf = authDto.getCpf().replaceAll("\\D", "");
            logger.log("Received CPF: " + cpf);

            if (cpf.length() != 11) {
                logger.log("Invalid CPF - Must have 11 digits");
                return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("{ \"error\": \"Invalid CPF - Must have 11 digits\" }");
            }

            var response = clienteGateway.getClientePorCPF(cpf);
            if (response.isPresent()) {
                var token = JwtTokenUtil.generateToken(cpf);
                var responseBody = "{ \"token\": \"" + token + "\" }";
                return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(responseBody);
            } else {
                return new APIGatewayProxyResponseEvent().withStatusCode(403).withBody("{ \"token\": false }");
            }
        } catch (Exception e) {
            logger.log("Error processing request: " + e.getMessage());
            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("{ \"error\": \"Internal Server Error\" }");
        }
    }

    private static class AuthDto {
        private String cpf;

        public String getCpf() {
            return cpf;
        }

        public void setCpf(String cpf) {
            this.cpf = cpf;
        }
    }
}
