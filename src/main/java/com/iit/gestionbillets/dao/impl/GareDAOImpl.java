package com.iit.gestionbillets.dao.impl;

import com.iit.gestionbillets.config.HibernateUtil;
import com.iit.gestionbillets.dao.Interface.IGareDAO;
import com.iit.gestionbillets.model.Gare;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class GareDAOImpl implements IGareDAO {

    @Override
    public void save(Gare gare) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(gare);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); 
        }
    }

    @Override
    public void update(Gare gare) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(gare); // merge on l utilise pour les update
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); 
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Gare gare = session.get(Gare.class, id);
            if (gare != null) {
                session.remove(gare); // on utilise  remove pour la  deletion
                transaction.commit();
            } else {
                 if (transaction != null) transaction.rollback(); 
                 System.err.println("Gare with id " + id + " not found for deletion.");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); 
        }
    }

    @Override
    public Optional<Gare> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Gare gare = session.get(Gare.class, id);
            return Optional.ofNullable(gare);
        } catch (Exception e) {
            e.printStackTrace(); 
            return Optional.empty();
        }
    }

    @Override
    public List<Gare> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Gare> query = session.createQuery("FROM Gare ORDER BY nom", Gare.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace(); 
            return List.of(); 
        }
    }

    @Override
    public Optional<Gare> findByNom(String nom) {
         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Gare> query = session.createQuery("FROM Gare WHERE nom = :nomParam", Gare.class);
            query.setParameter("nomParam", nom);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            e.printStackTrace(); 
            return Optional.empty();
        }
    }
}

