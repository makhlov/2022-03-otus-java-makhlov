package ru.otus.json.gson;

import com.google.gson.Gson;


public class GsonDemo {
    public static void main(String[] args) {
        var gson = new Gson();
        var obj = new BagOfPrimitives(22, "test", 10); //Класс, который хотим сериализовать в JSON
        System.out.println(obj);

        String json = gson.toJson(obj); //Сериализуем
        System.out.println(json);

        BagOfPrimitives obj2 = gson.fromJson(json, BagOfPrimitives.class); //Де-сериализуем
        System.out.println(obj.equals(obj2));
        System.out.println(obj2);
    }
}
