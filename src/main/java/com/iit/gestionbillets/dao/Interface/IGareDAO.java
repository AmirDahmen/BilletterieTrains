package com.iit.gestionbillets.dao.Interface;

import com.iit.gestionbillets.model.Gare;
import java.util.List;
import java.util.Optional;

public interface IGareDAO {
    void save(Gare gare);
    void update(Gare gare);
    void delete(Long id);
    Optional<Gare> findById(Long id);
    List<Gare> findAll();
    Optional<Gare> findByNom(String nom); // pour la  validation
}

