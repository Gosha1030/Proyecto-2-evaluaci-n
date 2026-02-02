/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import modelo.Slime;

public class SlimeDao {

    private final EntityManager em;

    public SlimeDao(EntityManager em) {
        this.em = em;
    }

    public void save(Slime s) {
        em.persist(s);
    }

    public Slime update(Slime s) {
        return em.merge(s);
    }

    public void delete(Slime s) {
        Slime managed = em.contains(s) ? s : em.merge(s);
        em.remove(managed);
    }

    public Slime findById(String id) {
        return em.find(Slime.class, id);
    }

    public List<Slime> findAll() {
        TypedQuery<Slime> q = em.createQuery("SELECT s FROM Slime s", Slime.class);
        return q.getResultList();
    }

    public List<Slime> findByType(String type) {
        TypedQuery<Slime> q = em.createQuery("SELECT s FROM Slime s WHERE s.type = :type", Slime.class);
        q.setParameter("type", type);
        return q.getResultList();
    }
}
