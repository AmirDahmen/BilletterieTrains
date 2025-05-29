package com.iit.gestionbillets.dao.Interface;



import com.iit.gestionbillets.model.Billet;

import com.iit.gestionbillets.model.StatutBillet;



import java.util.List;

import java.util.Optional;



public interface IBilletDAO {

    void save(Billet billet);

    void update(Billet billet);

    void delete(Long id);

    Optional<Billet> findById(Long id);

    Optional<Billet> findByReference(String reference);

    List<Billet> findAll();

    List<Billet> findByUserId(Long userId);

    List<Billet> findByVoyageId(Long voyageId);

    List<Billet> findByStatus(StatutBillet statut);

    


    int countReservationsToday();

}