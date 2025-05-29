package com.iit.gestionbillets.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "trajets")
public class Trajet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "gare_depart_id", nullable = false)
    private Gare gareDepart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gare_arrivee_id", nullable = false)
    private Gare gareArrivee;

    @Column(nullable = true)
    private String description;

    public Trajet() {
    }

    public Trajet(Gare gareDepart, Gare gareArrivee, String description) {
        this.gareDepart = gareDepart;
        this.gareArrivee = gareArrivee;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gare getGareDepart() {
        return gareDepart;
    }

    public void setGareDepart(Gare gareDepart) {
        this.gareDepart = gareDepart;
    }

    public Gare getGareArrivee() {
        return gareArrivee;
    }

    public void setGareArrivee(Gare gareArrivee) {
        this.gareArrivee = gareArrivee;
    }

     public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trajet trajet = (Trajet) o;
        return Objects.equals(id, trajet.id); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); 
    }

    @Override
    public String toString() {
        return "Trajet{" +
               "id=" + id +
               ", gareDepart=" + (gareDepart != null ? gareDepart.getNom() : "null") +
               ", gareArrivee=" + (gareArrivee != null ? gareArrivee.getNom() : "null") +
               ", description=\'" + description + "\'" +
               '}';
    }
}

