package com.iit.gestionbillets.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.iit.gestionbillets.model.User;
import com.iit.gestionbillets.model.Gare;
import com.iit.gestionbillets.model.Trajet;
import com.iit.gestionbillets.model.Voyage;
import com.iit.gestionbillets.model.Billet; 

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                configuration.configure("hibernate.cfg.xml");

                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Gare.class);
                configuration.addAnnotatedClass(Trajet.class);
                configuration.addAnnotatedClass(Voyage.class);
                configuration.addAnnotatedClass(Billet.class); 

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                System.out.println("Hibernate Java Config serviceRegistry created");

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                System.out.println("SessionFactory created.");

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Initial SessionFactory creation failed." + e);
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            getSessionFactory().close();
        }
    }
}

