/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package georgii.proyecto2evaluacion_georgiisytnik.dao;

import georgii.proyecto2evaluacion_georgiisytnik.modelo.Food;
import java.util.List;
import java.util.Map;

public interface FoodDao {

    List<Food> findAll();

    Food findById(String id);

    List<Food> findByType(String type);

    Map<String, Integer> countFoodsByType();
}
