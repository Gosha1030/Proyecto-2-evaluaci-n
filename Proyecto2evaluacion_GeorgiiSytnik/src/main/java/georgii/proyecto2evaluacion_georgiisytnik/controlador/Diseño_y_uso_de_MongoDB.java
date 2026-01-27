/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package georgii.proyecto2evaluacion_georgiisytnik.controlador;

import georgii.proyecto2evaluacion_georgiisytnik.dao.ApiClient;
import georgii.proyecto2evaluacion_georgiisytnik.modelo.Food;
import georgii.proyecto2evaluacion_georgiisytnik.modelo.Slime;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.Document;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Diseño_y_uso_de_MongoDB {
    private static final String MONGO_URI = "mongodb://localhost:27017";

    public static void main(String[] args) {
        try {
            ApiClient api = new ApiClient();

            String slimesJson = api.get("slime");
            JSONObject slimesObj = new JSONObject(slimesJson);
            List<Slime> slimes = parseSlimes(slimesObj.get("slimes").toString());
            System.out.println("Slimes obtenidos: " + slimes.size());
            for (Slime s : slimes) {
                System.out.println(s.toString());
            }

            String foodsJson = api.get("food");
            JSONObject foodsObj = new JSONObject(foodsJson);
            List<Food> foods = parseFoods(foodsObj.get("foods").toString());
            System.out.println("Comida obtenida: " + foods.size());
            for (Food f : foods) {
                System.out.println(f.toString());
            }

            insertIntoMongo(slimes, foods);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Slime> parseSlimes(String json) {
        List<Slime> result = new ArrayList<>();
        try {
            Object root = new org.json.JSONTokener(json).nextValue();
            JSONArray arr = (JSONArray) root;
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Slime s = new Slime(obj);
                result.add(s);
            }
        } catch (Exception ex) {
            System.err.println("Error parseando slimes: " + ex.getMessage());
        }
        return result;
    }

    private static List<Food> parseFoods(String json) {
        List<Food> result = new ArrayList<>();
        try {
            Object root = new org.json.JSONTokener(json).nextValue();
            JSONArray arr = (JSONArray) root;
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Food f = new Food(obj);
                result.add(f);
            }
        } catch (Exception ex) {
            System.err.println("Error parseando foods: " + ex.getMessage());
        }
        return result;
    }

    private static void insertIntoMongo(List<Slime> slimes, List<Food> foods) {
        PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(pojoCodecProvider)
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new com.mongodb.ConnectionString(MONGO_URI))
                .codecRegistry(pojoCodecRegistry)
                .build();

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase("SlimeAndFood");
            database.drop();
            MongoCollection<Document> slimeColl = database.getCollection("slimes");
            MongoCollection<Document> foodColl = database.getCollection("foods");

            List<Document> docsSlimes = new ArrayList<>();
            for (Slime s : slimes) {
                Document d = new Document()
                        .append("_id", s.getId())
                        .append("name", s.getName())
                        .append("diet", s.getDiet())
                        .append("favouriteFood", s.getFavouriteFood())
                        .append("type", s.getType());
                docsSlimes.add(d);
            }
            if (!docsSlimes.isEmpty()) {
                slimeColl.insertMany(docsSlimes);
                System.out.println("Inserted " + docsSlimes.size() + " slimes into MongoDB");
            } else {
                System.out.println("No slimes para insertar");
            }

            List<Document> docsFoods = new ArrayList<>();
            for (Food f : foods) {
                Document d = new Document()
                        .append("_id", f.getId())
                        .append("name", f.getName())
                        .append("type", f.getType())
                        .append("slimeId", f.getSlimeId());
                docsFoods.add(d);
            }
            if (!docsFoods.isEmpty()) {
                foodColl.insertMany(docsFoods);
                System.out.println("Inserted " + docsFoods.size() + " foods into MongoDB");
            } else {
                System.out.println("No foods para insertar");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}