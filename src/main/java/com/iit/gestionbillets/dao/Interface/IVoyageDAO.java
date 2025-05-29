package com.iit.gestionbillets.dao.Interface;

import com.iit.gestionbillets.model.Voyage;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IVoyageDAO {
    void save(Voyage voyage);
    void update(Voyage voyage);
    void delete(Long id);
    Optional<Voyage> findById(Long id);
    List<Voyage> findAll();
    List<Voyage> findByCriteria(Long gareDepartId, Long gareArriveeId, LocalDate dateVoyage);
}

