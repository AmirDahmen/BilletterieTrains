package com.iit.gestionbillets.dao.impl;



import com.iit.gestionbillets.config.HibernateUtil;

import com.iit.gestionbillets.dao.Interface.IBilletDAO;

import com.iit.gestionbillets.model.Billet;

import com.iit.gestionbillets.model.StatutBillet;

import org.hibernate.Session;

import org.hibernate.Transaction;

import org.hibernate.query.Query;



import java.time.LocalDate;

import java.time.LocalDateTime;

import java.time.LocalTime;

import java.util.List;

import java.util.Optional;



public class BilletDAOImpl implements IBilletDAO {



    @Override

    public void save(Billet billet) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(billet);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {

                transaction.rollback();

            }

            e.printStackTrace();

        }

    }



    @Override

    public void update(Billet billet) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(billet);

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

            Billet billet = session.get(Billet.class, id);

            if (billet != null) {

                session.remove(billet);

                transaction.commit();

            } else {

                 if (transaction != null) transaction.rollback();

                 System.err.println("Billet with id " + id + " not found for deletion.");

            }

        } catch (Exception e) {

            if (transaction != null) {

                transaction.rollback();

            }

            e.printStackTrace();

        }

    }



    @Override

    public Optional<Billet> findById(Long id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<Billet> query = session.createQuery(

                "SELECT b FROM Billet b " +

                "JOIN FETCH b.user " +

                "JOIN FETCH b.voyage v " +

                "JOIN FETCH v.trajet t " +

                "JOIN FETCH t.gareDepart " +

                "JOIN FETCH t.gareArrivee " +

                "WHERE b.id = :id", Billet.class);

            query.setParameter("id", id);

            return query.uniqueResultOptional();

        } catch (Exception e) {

            e.printStackTrace();

            return Optional.empty();

        }

    }

    

    @Override

    public Optional<Billet> findByReference(String reference) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<Billet> query = session.createQuery(

                "SELECT b FROM Billet b " +

                "JOIN FETCH b.user " +

                "JOIN FETCH b.voyage v " +

                "JOIN FETCH v.trajet t " +

                "JOIN FETCH t.gareDepart " +

                "JOIN FETCH t.gareArrivee " +

                "WHERE b.referenceBillet = :ref", Billet.class);

            query.setParameter("ref", reference);

            return query.uniqueResultOptional();

        } catch (Exception e) {

            e.printStackTrace();

            return Optional.empty();

        }

    }



    @Override

    public List<Billet> findAll() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<Billet> query = session.createQuery(

                "SELECT b FROM Billet b " +

                "JOIN FETCH b.user " +

                "JOIN FETCH b.voyage v " +

                "JOIN FETCH v.trajet t " +

                "JOIN FETCH t.gareDepart " +

                "JOIN FETCH t.gareArrivee " +

                "ORDER BY b.dateReservation DESC", Billet.class);

            return query.list();

        } catch (Exception e) {

            e.printStackTrace();

            return List.of();

        }

    }



    @Override

    public List<Billet> findByUserId(Long userId) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<Billet> query = session.createQuery(

                "SELECT b FROM Billet b " +

                "JOIN FETCH b.user " +

                "JOIN FETCH b.voyage v " +

                "JOIN FETCH v.trajet t " +

                "JOIN FETCH t.gareDepart " +

                "JOIN FETCH t.gareArrivee " +

                "WHERE b.user.id = :userId " +

                "ORDER BY b.dateReservation DESC", Billet.class);

            query.setParameter("userId", userId);

            return query.list();

        } catch (Exception e) {

            e.printStackTrace();

            return List.of();

        }

    }



    @Override

    public List<Billet> findByVoyageId(Long voyageId) {

         try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<Billet> query = session.createQuery(

                "SELECT b FROM Billet b " +

                "JOIN FETCH b.user " +

                "JOIN FETCH b.voyage v " +

                "JOIN FETCH v.trajet t " +

                "JOIN FETCH t.gareDepart " +

                "JOIN FETCH t.gareArrivee " +

                "WHERE b.voyage.id = :voyageId " +

                "ORDER BY b.dateReservation DESC", Billet.class);

            query.setParameter("voyageId", voyageId);

            return query.list();

        } catch (Exception e) {

            e.printStackTrace();

            return List.of();

        }

    }



    @Override

    public List<Billet> findByStatus(StatutBillet statut) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<Billet> query = session.createQuery(

                "SELECT b FROM Billet b " +

                "JOIN FETCH b.user " +

                "JOIN FETCH b.voyage v " +

                "JOIN FETCH v.trajet t " +

                "JOIN FETCH t.gareDepart " +

                "JOIN FETCH t.gareArrivee " +

                "WHERE b.statut = :statut " +

                "ORDER BY b.dateReservation DESC", Billet.class);

            query.setParameter("statut", statut);

            return query.list();

        } catch (Exception e) {

            e.printStackTrace();

            return List.of();

        }

    }

    


    public int countReservationsToday() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            LocalDate today = LocalDate.now();

            LocalDateTime startOfDay = today.atStartOfDay();

            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

            

            Query<Long> query = session.createQuery(

                "SELECT COUNT(b) FROM Billet b " +

                "WHERE b.dateReservation >= :startOfDay " +

                "AND b.dateReservation <= :endOfDay", Long.class);

            query.setParameter("startOfDay", startOfDay);

            query.setParameter("endOfDay", endOfDay);

            

            Long count = query.uniqueResult();

            return count != null ? count.intValue() : 0;

        } catch (Exception e) {

            e.printStackTrace();

            return 0;

        }

    }

}