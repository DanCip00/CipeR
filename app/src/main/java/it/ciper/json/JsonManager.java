package it.ciper.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JsonManager {

    private static ObjectMapper objectMapper = getObjectMapper();

    private static ObjectMapper getObjectMapper(){
        ObjectMapper objectMapperBuilder = new ObjectMapper();
        //Settings
        return objectMapperBuilder;
    }

    public static JsonNode parseJson(String jsonFile){
        JsonNode node = null;
        try {
            node = objectMapper.readTree(jsonFile);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return node;
    }

    public static JsonNode parseJson(InputStream jsonFile){
        JsonNode node = null;
        try {
            node = objectMapper.readTree(jsonFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return node;
    }

    public static <A> A parseJsonClass(InputStream jsonFile, Class<A> classe){

        try {
            return objectMapper.readValue(jsonFile, classe);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <A> A parseJsonClass(String jsonFile, Class<A> classe){

        try {
            return objectMapper.readValue(jsonFile, classe);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
