package com.example.imageserver.requests;

public class JsonRequest {
    private String path;

    public JsonRequest(){

    }

    public JsonRequest(final String path)
    {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
