package com.iit.gestionbillets.dao.Interface;

import com.iit.gestionbillets.model.User;
import java.util.List;
import java.util.Optional;

public interface IUserDAO {
    void save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id); // pour edit/delete
    List<User> findAll(); 
    void update(User user); 
    void delete(Long id); 
}

