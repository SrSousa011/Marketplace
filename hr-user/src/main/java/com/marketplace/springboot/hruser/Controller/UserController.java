package com.marketplace.springboot.hruser.Controller;

import com.marketplace.springboot.Model.UserModel;
import com.marketplace.springboot.hruser.Dto.UserRecordDto;
import com.marketplace.springboot.hruser.Exception.Impl.DeletedException;
import com.marketplace.springboot.hruser.Exception.Impl.NotFoundException;
import com.marketplace.springboot.hruser.Service.Impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "User API REST", description = "Endpoints for managing user resources.")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user with the provided details.")
    public ResponseEntity<Object> updateUser(
            @PathVariable(value = "id") UUID id,
            @RequestBody @Valid UserRecordDto userRecordDto
    ) {
        try {
            UserModel updatedUser = userService.updateUser(id, userRecordDto);
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Get a list of all users.")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserModel> usersList = userService.getAllUsers();
            return ResponseEntity.ok(usersList);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get details of a specific user.")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "id") UUID id) {
        try {
            Optional<UserModel> user = userService.getUserById(id);

            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for ID: " + id);
            }
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Save a new user with the provided details.")
    public ResponseEntity<UserModel> saveUser(@RequestBody @Valid UserRecordDto userRecordDto) {
        try {
            var userModel = getUserModel(userRecordDto);
            UserModel savedUser = userService.saveUser(userModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private static UserModel getUserModel(UserRecordDto userRecordDto) {
        return new UserModel(/* Set relevant properties */);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user with the specified ID.")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") UUID id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).body("User with ID " + id + " successfully deleted.");
        } catch (NotFoundException | DeletedException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}