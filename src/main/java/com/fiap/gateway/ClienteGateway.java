package com.fiap.gateway;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.internal.IteratorSupport;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import com.fiap.entity.Cliente;

import java.util.Optional;

public class ClienteGateway {

    private static final String DYNAMODB_CUSTOMER_TABLE = "tf-clients-table";
    private static final String REGION = "us-east-1";
    private static final String INDEX_NAME = "cpf";

    public Optional<Cliente> getClientePorCPF(String cpf) {
        AmazonDynamoDB dbClient = AmazonDynamoDBClientBuilder.standard()
                .withRegion(REGION)
                .build();

        DynamoDB dynamoDb = new DynamoDB(dbClient);
        Table table = dynamoDb.getTable(DYNAMODB_CUSTOMER_TABLE);
        Index index = table.getIndex(INDEX_NAME);
        System.out.println("Realizando consulta no DynamoDB para o CPF: " + cpf);


        QuerySpec querySpec = new QuerySpec().withHashKey("cpf", cpf);
        IteratorSupport<Item, QueryOutcome> iterator = index.query(querySpec).iterator();
        System.out.println("Iterator: " + cpf);

        if (iterator.hasNext()) {
            Item item = iterator.next();
            String json = item.toJSON();
            System.out.println("Item encontrado no DynamoDB para o CPF: " + cpf);
            return Optional.of(Cliente.fromJson(json));
        }

        return Optional.empty();
    }
}
