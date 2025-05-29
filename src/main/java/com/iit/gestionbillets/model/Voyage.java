package com.iit.gestionbillets.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "voyages")
public class Voyage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    @Column(name = "heure_depart", nullable = false)
    private LocalDateTime heureDepart;

    @Column(name = "heure_arrivee", nullable = false)
    private LocalDateTime heureArrivee;

    @Column(nullable = false, precision = 10, scale = 2) 
    private BigDecimal prix;

    @Column(name = "places_disponibles", nullable = false)
    private int placesDisponibles;

    public Voyage() {
    }

    public Voyage(Trajet trajet, LocalDateTime heureDepart, LocalDateTime heureArrivee, BigDecimal prix, int placesDisponibles) {
        this.trajet = trajet;
        this.heureDepart = heureDepart;
        this.heureArrivee = heureArrivee;
        this.prix = prix;
        this.placesDisponibles = placesDisponibles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    public LocalDateTime getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(LocalDateTime heureDepart) {
        this.heureDepart = heureDepart;
    }

    public LocalDateTime getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(LocalDateTime heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public int getPlacesDisponibles() {
        return placesDisponibles;
    }

    public void setPlacesDisponibles(int placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voyage voyage = (Voyage) o;
        return Objects.equals(id, voyage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Voyage{" +
               "id=" + id +
               ", trajet=" + (trajet != null ? trajet.getId() : "null") +
               ", heureDepart=" + heureDepart +
               ", heureArrivee=" + heureArrivee +
               ", prix=" + prix +
               ", placesDisponibles=" + placesDisponibles +
               '}';
    }
}

