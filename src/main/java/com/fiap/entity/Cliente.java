package com.fiap.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Cliente {

    private final String cpf;
    private final String id;

    public Cliente(String cpf, String id) {
        this.cpf = cpf;
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public String getId() {
        return id;
    }

    public static Cliente fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Cliente.class);
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    
}
