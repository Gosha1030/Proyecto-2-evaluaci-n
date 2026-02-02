/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import modelo.Food;

public class FoodDao {

    private final EntityManager em;

    public FoodDao(EntityManager em) {
        this.em = em;
    }

    public void save(Food f) {
        em.persist(f);
    }

    public Food update(Food f) {
        return em.merge(f);
    }

    public void delete(Food f) {
        Food managed = em.contains(f) ? f : em.merge(f);
        em.remove(managed);
    }

    public Food findById(String id) {
        return em.find(Food.class, id);
    }

    public List<Food> findAll() {
        TypedQuery<Food> q = em.createQuery("SELECT f FROM Food f", Food.class);
        return q.getResultList();
    }

    public List<Food> findBySlimeId(String slimeId) {
        TypedQuery<Food> q = em.createQuery("SELECT f FROM Food f WHERE f.slime.id = :sid", Food.class);
        q.setParameter("sid", slimeId);
        return q.getResultList();
    }
}
