/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package georgii.proyecto2evaluacion_georgiisytnik.dao;

import georgii.proyecto2evaluacion_georgiisytnik.modelo.Slime;
import org.bson.Document;
import java.util.List;
import java.util.Map;

public interface SlimeDao {

    List<Slime> findAll();

    Slime findById(String id);

    List<Slime> findByType(String type);
    
    Map<String, Integer> countSlimesByType();

    /**
     * Realiza un $lookup con la colección "foods" y devuelve los documentos
     * resultantes donde cada documento contiene el slime y un array "foods" con
     * sus comidas.
     */
    List<Document> findSlimesWithFoods();
}
