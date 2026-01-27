/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package georgii.proyecto2evaluacion_georgiisytnik.dao;

import georgii.proyecto2evaluacion_georgiisytnik.modelo.Food;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodDaoImpl implements FoodDao {

    private final MongoCollection<Document> foodColl;

    public FoodDaoImpl(MongoDatabase database) {
        this.foodColl = database.getCollection("foods");
    }

    @Override
    public List<Food> findAll() {
        List<Food> result = new ArrayList<>();
        FindIterable<Document> docs = foodColl.find();
        for (Document d : docs) {
            result.add(documentToFood(d));
        }
        return result;
    }

    @Override
    public Food findById(String id) {
        Document d = foodColl.find(new Document("_id", id)).first();
        if (d == null) {
            return null;
        }
        return documentToFood(d);
    }

    @Override
    public List<Food> findByType(String type) {
        List<Food> result = new ArrayList<>();
        FindIterable<Document> docs = foodColl.find(new Document("type", type));
        for (Document d : docs) {
            result.add(documentToFood(d));
        }
        return result;
    }

    @Override
    public Map<String, Integer> countFoodsByType() {
        Map<String, Integer> counts = new HashMap<>();
        List<Document> pipeline = Arrays.asList(
                new Document("$group", new Document()
                        .append("_id", "$type")
                        .append("count", new Document("$sum", 1)))
        );
        AggregateIterable<Document> agg = foodColl.aggregate(pipeline);
        for (Document d : agg) {
            String type = d.getString("_id");
            Integer cnt = d.getInteger("count");
            counts.put(type != null ? type : "(null)", cnt != null ? cnt : 0);
        }
        return counts;
    }

    private Food documentToFood(Document d) {
        try {
            JSONObject obj = new JSONObject(d.toJson());
            return new Food(obj);
        } catch (Exception ex) {
            Food f = new Food();
            System.err.println("Error mapping Document to Food: " + ex.getMessage());
            return f;
        }
    }
}
