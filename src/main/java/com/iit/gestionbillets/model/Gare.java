package com.iit.gestionbillets.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "gares")
public class Gare implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false)
    private String ville;

    public Gare() {
    }

    public Gare(String nom, String ville) {
        this.nom = nom;
        this.ville = ville;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gare gare = (Gare) o;
        return Objects.equals(id, gare.id) && Objects.equals(nom, gare.nom) && Objects.equals(ville, gare.ville);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, ville);
    }

    @Override
    public String toString() {
        return "Gare{" +
               "id=" + id +
               ", nom=\'" + nom + "\'" +
               ", ville=\'" + ville + "\'" +
               '}';
    }
}

