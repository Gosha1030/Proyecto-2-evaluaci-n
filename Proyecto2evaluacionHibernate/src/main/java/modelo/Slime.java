/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "slime")
public class Slime {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "diet")
    private String diet;

    @OneToOne()
    @JoinColumn(name = "favourite_Food")
    private Food food;

    @Column(name = "type")
    private String type;


    public Slime() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

     public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
