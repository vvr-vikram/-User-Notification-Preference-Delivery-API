package com.notification.controller;

import com.notification.dto.ApiResponse;
import com.notification.dto.UserDTO;
import com.notification.service.NotificationPreferenceService;
import com.notification.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for User Management
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final NotificationPreferenceService preferenceService;

    /**
     * Create a new user
     * POST /api/users
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        
        // Initialize default preferences (all channels disabled)
        preferenceService.initializeDefaultPreferences(createdUser.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>(true, "User created successfully", createdUser));
    }

    /**
     * Get user by ID
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "User retrieved successfully", user)
        );
    }

    /**
     * Get all users
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Users retrieved successfully", users)
        );
    }

    /**
     * Update user information
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "User updated successfully", updatedUser)
        );
    }

    /**
     * Delete user by ID
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "User deleted successfully", null)
        );
    }
}