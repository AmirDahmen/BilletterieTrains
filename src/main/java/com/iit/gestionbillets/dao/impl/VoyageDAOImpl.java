package com.iit.gestionbillets.dao.impl;

import com.iit.gestionbillets.config.HibernateUtil;
import com.iit.gestionbillets.dao.Interface.IVoyageDAO;
import com.iit.gestionbillets.model.Voyage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class VoyageDAOImpl implements IVoyageDAO {

    @Override
    public void save(Voyage voyage) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(voyage);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); 
        }
    }

    @Override
    public void update(Voyage voyage) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(voyage);
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
            Voyage voyage = session.get(Voyage.class, id);
            if (voyage != null) {
                session.remove(voyage);
                transaction.commit();
            } else {
                 if (transaction != null) transaction.rollback();
                 System.err.println("Voyage with id " + id + " not found for deletion.");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); 
        }
    }

    @Override
    public Optional<Voyage> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
           
            Voyage voyage = session.get(Voyage.class, id);
        
            return Optional.ofNullable(voyage);
        } catch (Exception e) {
            e.printStackTrace(); 
            return Optional.empty();
        }
    }

    @Override
    public List<Voyage> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
           
            Query<Voyage> query = session.createQuery("FROM Voyage v ORDER BY v.heureDepart DESC", Voyage.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); 
        }
    }

    @Override
    public List<Voyage> findByCriteria(Long gareDepartId, Long gareArriveeId, LocalDate dateVoyage) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
         
            LocalDateTime startOfDay = dateVoyage.atStartOfDay(); 
            LocalDateTime endOfDay = dateVoyage.atTime(LocalTime.MAX); 

            String hql = "FROM Voyage v WHERE v.trajet.gareDepart.id = :gareDepartId " +
                         "AND v.trajet.gareArrivee.id = :gareArriveeId " +
                         "AND v.heureDepart >= :startOfDay AND v.heureDepart <= :endOfDay " +
                         "AND v.placesDisponibles > 0 " + 
                         "ORDER BY v.heureDepart ASC";

            Query<Voyage> query = session.createQuery(hql, Voyage.class);
            query.setParameter("gareDepartId", gareDepartId);
            query.setParameter("gareArriveeId", gareArriveeId);
            query.setParameter("startOfDay", startOfDay);
            query.setParameter("endOfDay", endOfDay);

            return query.list();
        } catch (Exception e) {
            e.printStackTrace(); 
            return List.of(); 
        }
    }

}

