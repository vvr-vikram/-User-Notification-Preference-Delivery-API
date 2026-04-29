package com.notification.service;

import com.notification.dto.UserDTO;
import com.notification.entity.User;
import com.notification.exception.ResourceNotFoundException;
import com.notification.mapper.EntityMapper;
import com.notification.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for User management
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final EntityMapper entityMapper;

    /**
     * Create a new user
     */
    public UserDTO createUser(UserDTO userDTO) {
        // Validate unique email
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + userDTO.getEmail());
        }

        // Validate unique phone
        if (userRepository.findByPhone(userDTO.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Phone number already exists: " + userDTO.getPhone());
        }

        User user = entityMapper.toUserEntity(userDTO);
        User savedUser = userRepository.save(user);
        return entityMapper.toUserDTO(savedUser);
    }

    /**
     * Get user by ID
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return entityMapper.toUserDTO(user);
    }

    /**
     * Get all users
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(entityMapper::toUserDTO)
            .collect(Collectors.toList());
    }

    /**
     * Update user information
     */
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Validate unique email if changed
        if (!user.getEmail().equals(userDTO.getEmail()) && 
            userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + userDTO.getEmail());
        }

        // Validate unique phone if changed
        if (!user.getPhone().equals(userDTO.getPhone()) && 
            userRepository.findByPhone(userDTO.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Phone number already exists: " + userDTO.getPhone());
        }

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());

        User updatedUser = userRepository.save(user);
        return entityMapper.toUserDTO(updatedUser);
    }

    /**
     * Delete user by ID
     */
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        userRepository.delete(user);
    }

    /**
     * Internal method to verify user exists
     */
    @Transactional(readOnly = true)
    public User getUserEntityById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }
}