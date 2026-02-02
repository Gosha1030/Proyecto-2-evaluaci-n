/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import dao.FoodDao;
import dao.SlimeDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import modelo.Food;
import modelo.Slime;
import vista.Vista;

public class Controller {

    Vista v;

    public Controller(Vista v) {
        this.v = v;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadDePersistencia");
        EntityManager em = emf.createEntityManager();

        SlimeDao slimeDao = new SlimeDao(em);
        FoodDao foodDao = new FoodDao(em);

        try {
            em.getTransaction().begin();

            Slime s = new Slime();
            s.setId("test");
            s.setName("Slime Test");
            s.setDiet("fruit");
            s.setType("basic");

            slimeDao.save(s);

            em.getTransaction().commit();


            System.out.println("SLIMES");
            List<Slime> slimes = slimeDao.findAll();
            for (Slime slime : slimes) {
                System.out.println("Slime: " + slime.getName() + " id=" + slime.getId() + " favouriteFood=" + (slime.getFood() != null ? slime.getFood().getId() : "null"));
            }

            System.out.println("FOODS");
            List<Food> foods = foodDao.findAll();
            for (Food food : foods) {
                System.out.println("Food: " + food.getName() + " id=" + food.getId() + " slimeId=" + (food.getSlime() != null ? food.getSlime().getId() : "null"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
            emf.close();
        }
    }
}