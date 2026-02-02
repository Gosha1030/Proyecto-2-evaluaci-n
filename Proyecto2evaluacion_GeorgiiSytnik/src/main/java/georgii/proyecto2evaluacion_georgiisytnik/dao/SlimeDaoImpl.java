/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package georgii.proyecto2evaluacion_georgiisytnik.dao;

import georgii.proyecto2evaluacion_georgiisytnik.modelo.Slime;
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

public class SlimeDaoImpl implements SlimeDao {

    private final MongoCollection<Document> slimeColl;
    private final MongoCollection<Document> foodColl;

    public SlimeDaoImpl(MongoDatabase database) {
        this.slimeColl = database.getCollection("slimes");
        this.foodColl = database.getCollection("foods");
    }

    @Override
    public List<Slime> findAll() {
        List<Slime> result = new ArrayList<>();
        FindIterable<Document> docs = slimeColl.find();
        for (Document d : docs) {
            result.add(documentToSlime(d));
        }
        return result;
    }

    @Override
    public Slime findById(String id) {
        Document d = slimeColl.find(new Document("_id", id)).first();
        if (d == null) {
            return null;
        }
        return documentToSlime(d);
    }

    @Override
    public List<Slime> findByType(String type) {
        List<Slime> result = new ArrayList<>();
        FindIterable<Document> docs = slimeColl.find(new Document("type", type));
        for (Document d : docs) {
            result.add(documentToSlime(d));
        }
        return result;
    }
    
    @Override
    public Map<String, Integer> countSlimesByType() {
        Map<String, Integer> counts = new HashMap<>();
        List<Document> pipeline = Arrays.asList(
                new Document("$group", new Document()
                        .append("_id", "$type")
                        .append("count", new Document("$sum", 1)))
        );
        AggregateIterable<Document> agg = slimeColl.aggregate(pipeline);
        for (Document d : agg) {
            String type = d.getString("_id");
            Integer cnt = d.getInteger("count");
            counts.put(type != null ? type : "(null)", cnt != null ? cnt : 0);
        }
        return counts;
    }

    @Override
    public List<Document> findSlimesWithFoods() {
        List<Document> out = new ArrayList<>();
        List<Document> pipeline = Arrays.asList(
                new Document("$lookup", new Document()
                        .append("from", "foods")
                        .append("localField", "diet")
                        .append("foreignField", "type")
                        .append("as", "foods"))
        );
        AggregateIterable<Document> agg = slimeColl.aggregate(pipeline);
        for (Document d : agg) {
            out.add(d);
        }
        return out;
    }

    private Slime documentToSlime(Document d) {
        try {
            JSONObject obj = new JSONObject(d.toJson());
            return new Slime(obj);
        } catch (Exception ex) {
            Slime s = new Slime();
            System.err.println("Error mapping Document to Slime: " + ex.getMessage());
            return s;
        }
    }
}
