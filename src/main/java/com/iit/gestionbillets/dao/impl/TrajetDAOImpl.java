package com.iit.gestionbillets.dao.impl;

import com.iit.gestionbillets.config.HibernateUtil;
import com.iit.gestionbillets.dao.Interface.ITrajetDAO;
import com.iit.gestionbillets.model.Trajet;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class TrajetDAOImpl implements ITrajetDAO {

    @Override
    public void save(Trajet trajet) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(trajet);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); 
        }
    }

    @Override
    public void update(Trajet trajet) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(trajet);
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
            Trajet trajet = session.get(Trajet.class, id);
            if (trajet != null) {
                session.remove(trajet);
                transaction.commit();
            } else {
                 if (transaction != null) transaction.rollback();
                 System.err.println("Trajet with id " + id + " not found for deletion.");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); 
            
        }
    }

    @Override
    public Optional<Trajet> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Trajet trajet = session.get(Trajet.class, id);
            return Optional.ofNullable(trajet);
        } catch (Exception e) {
            e.printStackTrace(); 
            return Optional.empty();
        }
    }

    @Override
    public List<Trajet> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
             Query<Trajet> query = session.createQuery("FROM Trajet t ORDER BY t.gareDepart.nom, t.gareArrivee.nom", Trajet.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); 
        }
    }

}

