/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package georgii.proyecto2evaluacion_georgiisytnik.controlador;

import georgii.proyecto2evaluacion_georgiisytnik.modelo.Slime;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import georgii.proyecto2evaluacion_georgiisytnik.dao.FoodDao;
import georgii.proyecto2evaluacion_georgiisytnik.dao.FoodDaoImpl;
import georgii.proyecto2evaluacion_georgiisytnik.dao.SlimeDao;
import georgii.proyecto2evaluacion_georgiisytnik.dao.SlimeDaoImpl;

import java.util.List;
import java.util.Map;

public class DAOs_y_agregaciones {

    public static void main(String[] args) {
        final String MONGO_URI = "mongodb://localhost:27017";
        Diseño_y_uso_de_MongoDB.main(args);

        try (MongoClient client = MongoClients.create(MONGO_URI)) {
            MongoDatabase database = client.getDatabase("SlimeAndFood");
            SlimeDao slimeDao = new SlimeDaoImpl(database);
            FoodDao foodDao = new FoodDaoImpl(database);
            System.out.println("\nSlimes de tipo 'special'");
            List<Slime> special = slimeDao.findByType("special");
            special.forEach(System.out::println);

            System.out.println("\nSlimes con toda comida de su dieta");
            List<Document> joined = slimeDao.findSlimesWithFoods();
            for (Document d : joined) {
                System.out.println(d.toJson());
            }
            
            System.out.println("\nSlime por tipo");
            Map<String, Integer> countsSlime = slimeDao.countSlimesByType();
            countsSlime.forEach((k, v) -> System.out.println(k + " -> " + v));

            System.out.println("\nComida por tipo");
            Map<String, Integer> countsFood = foodDao.countFoodsByType();
            countsFood.forEach((k, v) -> System.out.println(k + " -> " + v));

            List<Slime> slimes = slimeDao.findAll();
            if (!slimes.isEmpty()) {
                String id = slimeDao.findByType("hostile").get(0).getId();
                System.out.println("\nInfo slime con id = " + id);
                Slime s = slimeDao.findById(id);
                System.out.println(s);

                System.out.println("\nComida favorita de " + s.getName());
                System.out.println(foodDao.findById(s.getFavouriteFood()).getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}