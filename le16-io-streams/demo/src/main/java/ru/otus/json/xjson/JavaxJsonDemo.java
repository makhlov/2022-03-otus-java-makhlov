package ru.otus.json.xjson;


import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import java.util.Map;

public class JavaxJsonDemo {
    public static void main(String[] args) {
        navigateTree(create());
        readFromFile();
    }

    //Пример ниже для ситуаций, когда мы не знаем точно структуру

    //Создаем JSON
    private static JsonObject create() {
        //Воспользуемся реализаций спецификации - вызовем ObjectBuilder
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

    //Получили JSON неизвестной структуры, но предполагаем, что там есть какие-то данные, которые нам нужны
    private static void navigateTree(JsonValue tree) {
        //Мы делаем обход по дереву. Если у нас есть какие-то узлы, которые могут содержать другие объекты, то мы
        //в них заходим, т.е. обходим дерево вглубь. Если узел не может ветвится, то мы выводим его в консоль
        switch (tree.getValueType()) {
            case OBJECT -> {
                System.out.println("OBJECT");
                var jsonObject = (JsonObject) tree;
                for (Map.Entry<String, JsonValue> entry : jsonObject.entrySet()) {
                    navigateTree(jsonObject.get(entry.getKey()));
                }
            }
            case ARRAY -> {
                System.out.println("ARRAY");
                JsonArray array = (JsonArray) tree;
                for (JsonValue val : array) {
                    navigateTree(val);
                }
            }
            case STRING -> {
                JsonString st = (JsonString) tree;
                System.out.println("STRING " + st.getString());
            }
            case NUMBER -> {
                JsonNumber num = (JsonNumber) tree;
                System.out.println("NUMBER " + num.toString());
            }
            case TRUE, FALSE, NULL -> System.out.println(tree.getValueType().toString());
            default -> throw new IllegalStateException("Unexpected value: " + tree.getValueType());
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
