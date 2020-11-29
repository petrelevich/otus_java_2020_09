package ru.otus.json.xjson;


import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import java.util.Map;

public class JavaxJsonDemo {
    public static void main(String[] args) {
        navigateTree(create());
        readFromFile();
    }

    private static JsonObject create() {
        var jsonObject = Json.createObjectBuilder()
                .add("firstName", "Duke")
                .add("age", 28)
                .add("streetAddress", "100 Internet Dr")
                .add("phoneNumbers",
                        Json.createArrayBuilder()
                                .add(Json.createObjectBuilder()
                                        .add("type", "home")
                                        .add("number", "222-222-2222")))
                .build();

        System.out.println("jsonObject:" + jsonObject + "\n");
        return jsonObject;
    }

    private static void navigateTree(JsonValue tree) {
        switch (tree.getValueType()) {
            case OBJECT:
                System.out.println("OBJECT");
                var jsonObject = (JsonObject) tree;
                for (Map.Entry<String, JsonValue> entry : jsonObject.entrySet()) {
                    navigateTree(jsonObject.get(entry.getKey()));
                }
                break;
            case ARRAY:
                System.out.println("ARRAY");
                JsonArray array = (JsonArray) tree;
                for (JsonValue val : array) {
                    navigateTree(val);
                }
                break;
            case STRING:
                JsonString st = (JsonString) tree;
                System.out.println("STRING " + st.getString());
                break;
            case NUMBER:
                JsonNumber num = (JsonNumber) tree;
                System.out.println("NUMBER " + num.toString());
                break;
            case TRUE:
            case FALSE:
            case NULL:
                System.out.println(tree.getValueType().toString());
                break;
        }
    }

    private static void readFromFile() {
        try (var jsonReader = Json.createReader(JavaxJsonDemo.class.getClassLoader().getResourceAsStream("jsondata.txt"))) {
            JsonStructure jsonFromTheFile = jsonReader.read();
            System.out.println("\n json from the file:");
            System.out.println(jsonFromTheFile);
            System.out.println("property:" + jsonFromTheFile.getValue("/firstName"));
        }
    }
}
