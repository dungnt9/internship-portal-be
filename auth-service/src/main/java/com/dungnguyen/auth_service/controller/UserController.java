package com.dungnguyen.auth_service.controller;

import com.dungnguyen.auth_service.dto.UserResponseDTO;
import com.dungnguyen.auth_service.entity.User;
import com.dungnguyen.auth_service.exception.UserNotFoundException;
import com.dungnguyen.auth_service.response.ApiResponse;
import com.dungnguyen.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthService authService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Integer id) {
        try {
            User user = authService.findById(id);

            if (user == null) {
                throw new UserNotFoundException("User not found with ID: " + id);
            }

            UserResponseDTO userResponse = new UserResponseDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getRole().getName(),
                    user.getIsActive()
            );

            return ResponseEntity.ok(ApiResponse.success(userResponse, "User found successfully"));
        } catch (UserNotFoundException e) {
            log.error("User not found with ID: {}", id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<UserResponseDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving user with ID {}: {}", id, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<UserResponseDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving user information")
                            .data(null)
                            .build());
        }
    }
}