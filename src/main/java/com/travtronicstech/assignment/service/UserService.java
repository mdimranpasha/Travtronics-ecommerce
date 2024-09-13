package com.travtronicstech.assignment.service;

import com.travtronicstech.assignment.model.Users;
import com.travtronicstech.assignment.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    public Users saveUser(Users user) {
        return userRepo.save(user);
    }

    // Modify this method to return user data and JWT token as JSON
    public Map<String, Object> verify(Users user) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String token = jwtService.generateToken(userDetails);

                // Fetch user data
                Users authenticatedUser = userRepo.findByUserName(user.getUserName());

                // Prepare the response with user data and JWT token
                response.put("userId", authenticatedUser.getId());
                response.put("userName", authenticatedUser.getUserName());
                response.put("email", authenticatedUser.getEmail());
                response.put("role", authenticatedUser.getRole());
                response.put("JWT-token", token);

                return response;
            } else {
                throw new RuntimeException("Invalid login attempt");
            }
        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            response.put("error", "Authentication failed");
            return response;
        }
    }
}
