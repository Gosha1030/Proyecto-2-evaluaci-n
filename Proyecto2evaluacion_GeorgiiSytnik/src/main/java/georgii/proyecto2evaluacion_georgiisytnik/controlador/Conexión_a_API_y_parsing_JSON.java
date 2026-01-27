/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package georgii.proyecto2evaluacion_georgiisytnik.controlador;

import georgii.proyecto2evaluacion_georgiisytnik.dao.ApiClient;
import georgii.proyecto2evaluacion_georgiisytnik.modelo.Food;
import georgii.proyecto2evaluacion_georgiisytnik.modelo.Slime;
import org.json.JSONObject;

public class Conexión_a_API_y_parsing_JSON {
    public static void main(String[] args) {
        ApiClient client = new ApiClient();

        try {
            String slimePath = "slime/crystal";
            String slimeJson = client.get(slimePath);
            System.out.println("RECEIVED (slime): " + slimeJson);
            JSONObject slimeObj = new JSONObject(slimeJson);
            Slime slime = new Slime(slimeObj);
            System.out.println("SLIME: " + slime);

            String foodPath = "food/" + slime.getFavouriteFood();
            try {
                String foodJson = client.get(foodPath);
                System.out.println("RECEIVED (food): " + foodJson);
                JSONObject foodObj = new JSONObject(foodJson);
                Food food = new Food(foodObj);
                System.out.println("FOOD: " + food);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}