package com.iit.gestionbillets.service.Interface;

import com.iit.gestionbillets.model.User;
import java.util.Optional;

public interface IAuthService {
    Optional<User> login(String email, String password);
    boolean register(String email, String password, String firstName, String lastName);
    // Add logout functionality later if needed
}

