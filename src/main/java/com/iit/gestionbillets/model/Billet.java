package com.iit.gestionbillets.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "billets")
public class Billet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voyage_id", nullable = false)
    private Voyage voyage;

    @Column(name = "date_reservation", nullable = false)
    private LocalDateTime dateReservation;

    @Column(name = "prix_paye", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixPaye; 

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_billet", nullable = false)
    private StatutBillet statut; 

    @Column(name = "reference_billet", unique = true, nullable = false)
    private String referenceBillet; 

    public Billet() {
        this.dateReservation = LocalDateTime.now();
        this.statut = StatutBillet.CONFIRMED; 
    }

    public Billet(User user, Voyage voyage, BigDecimal prixPaye, String referenceBillet) {
        this(); 
        this.user = user;
        this.voyage = voyage;
        this.prixPaye = prixPaye;
        this.referenceBillet = referenceBillet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Voyage getVoyage() {
        return voyage;
    }

    public void setVoyage(Voyage voyage) {
        this.voyage = voyage;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public BigDecimal getPrixPaye() {
        return prixPaye;
    }

    public void setPrixPaye(BigDecimal prixPaye) {
        this.prixPaye = prixPaye;
    }

    public StatutBillet getStatut() {
        return statut;
    }

    public void setStatut(StatutBillet statut) {
        this.statut = statut;
    }
    
    public String getReferenceBillet() {
        return referenceBillet;
    }

    public void setReferenceBillet(String referenceBillet) {
        this.referenceBillet = referenceBillet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Billet billet = (Billet) o;
        return Objects.equals(id, billet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Billet{" +
               "id=" + id +
               ", userId=" + (user != null ? user.getId() : "null") +
               ", voyageId=" + (voyage != null ? voyage.getId() : "null") +
               ", dateReservation=" + dateReservation +
               ", prixPaye=" + prixPaye +
               ", statut=" + statut +
               ", referenceBillet=\"" + referenceBillet + "\"" +
               '}';
    }
}

