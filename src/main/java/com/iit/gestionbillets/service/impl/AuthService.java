package com.iit.gestionbillets.service.impl;

import com.iit.gestionbillets.dao.Interface.IUserDAO;
import com.iit.gestionbillets.dao.impl.UserDAOImpl; // Assuming direct instantiation, consider Dependency Injection later
import com.iit.gestionbillets.model.Role;
import com.iit.gestionbillets.model.User;
import com.iit.gestionbillets.service.Interface.IAuthService;

import java.util.Optional;

public class AuthService implements IAuthService {

    private IUserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAOImpl();
    }

    public AuthService(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> login(String email, String password) {
        Optional<User> userOptional = userDAO.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            } else {
                 System.out.println("Password mismatch for user: " + email); 
            }
        } else {
             System.out.println("User not found: " + email); 
        }
        return Optional.empty();
    }

    @Override
    public boolean register(String email, String password, String firstName, String lastName) {
        if (userDAO.findByEmail(email).isPresent()) {
            System.out.println("Registration failed: Email already exists - " + email); 
            return false; 
        }

    
        User newUser = new User(email, password, firstName, lastName, Role.CLIENT); // Default role is CLIENT

        try {
            userDAO.save(newUser);
            System.out.println("User registered successfully: " + email); 
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error during user registration for email: " + email);
            return false;
        }
    }


}

