package com.iit.gestionbillets.dao.Interface;

import com.iit.gestionbillets.model.Trajet;
import java.util.List;
import java.util.Optional;

public interface ITrajetDAO {
    void save(Trajet trajet);
    void update(Trajet trajet);
    void delete(Long id);
    Optional<Trajet> findById(Long id);
    List<Trajet> findAll();
}

