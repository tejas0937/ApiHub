package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService = new UserService();

    // ---------------------------------------
    // Register organization + admin user
    // ---------------------------------------
    @PostMapping("/register")
    public ResponseEntity<?> registerOrganization(@RequestBody Map<String, String> request) {

        try {

            userService.registerOrganizationWithAdmin(
                    request.get("orgName"),
                    request.get("orgEmail"),
                    request.get("adminName"),
                    request.get("adminEmail"),
                    request.get("password")
            );

            Map<String, String> response = new HashMap<>();
            response.put("message", "Organization and admin registered successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Registration failed");
        }
    }

    // ---------------------------------------
    // Login
    // ---------------------------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {

        try {

            User user = userService.login(
                    request.get("email"),
                    request.get("password")
            );

            if (user == null) {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            response.put("organizationId", user.getOrganizationId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Login error");
        }
    }

    // ---------------------------------------
    // Create platform super admin
    // (call only once initially)
    // ---------------------------------------
    @PostMapping("/create-super-admin")
    public ResponseEntity<?> createSuperAdmin(@RequestBody Map<String, String> request) {

        try {

            userService.createSuperAdmin(
                    request.get("name"),
                    request.get("email"),
                    request.get("password")
            );

            return ResponseEntity.ok("Super admin created");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to create super admin");
        }
    }
}
