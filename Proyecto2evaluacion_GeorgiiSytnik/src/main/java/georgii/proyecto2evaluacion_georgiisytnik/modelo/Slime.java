/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package georgii.proyecto2evaluacion_georgiisytnik.modelo;

import org.json.JSONObject;

public class Slime {
    private String id;
    private String name;
    /**
     * Slime puede comer comida(food) solo de ese tipo(type)
     */
    private String diet;
    /**
     * Comida(food) favorita de slime
     */
    private String favouriteFood;
    /**
     * Slime pueden ser de diferentes tipos
     */
    private String type;

    public Slime() {}

    public Slime(JSONObject o) {
        JSONObject src = o;
        if (o.has("slime")) {
            src = o.getJSONObject("slime");
        }
        this.id = src.has("_id")
            ? src.get("_id").toString()
            : src.optString("id");
        this.name = src.optString("name");
        this.diet = src.optString("diet", "other");
        this.favouriteFood = src.optString("favouriteFood", "no");
        this.type = src.optString("type", "special");
    }

    @Override
    public String toString() {
        return "Slime{name=" + name +
               ", diet=" + diet +
               ", favouriteFood=" + favouriteFood +
               ", type=" + type + 
               '}';
    }

    public String getFavouriteFood() {
        return favouriteFood;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiet() {
        return diet;
    }

    public String getType() {
        return type;
    }
    
}
