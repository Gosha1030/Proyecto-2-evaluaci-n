/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package georgii.proyecto2evaluacion_georgiisytnik.modelo;

import org.json.JSONObject;

public class Food {
    private String id;
    private String name;
    /**
     * Tipo de comida
     */
    private String type;
    /**
     * Esa comida es favorita de slime con ese id
     */
    private String slimeId;

    public Food() {}

    public Food(JSONObject o) {
        JSONObject src = o;
        if (o.has("food")) {
            src = o.getJSONObject("food");
        }
        this.id = src.optString("id");
        this.name = src.optString("name");
        this.type = src.optString("type");
        this.slimeId = src.optString("slimeId", "no");
    }

    @Override
    public String toString() {
        return "Food{name=" + name +
               ", type=" + type +
               ", slimeId=" + slimeId +
               '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSlimeId() {
        return slimeId;
    }
    
    
}