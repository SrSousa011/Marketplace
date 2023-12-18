package com.marketplace.springboot.hruser.Controller;

import com.marketplace.springboot.Model.UserModel;
import com.marketplace.springboot.hruser.Dto.UserRecordDto;
import com.marketplace.springboot.hruser.Exception.Impl.DeletedException;
import com.marketplace.springboot.hruser.Exception.Impl.NotFoundException;
import com.marketplace.springboot.hruser.Service.Impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@Tag(name = "User API REST", description = "Endpoints for managing user resources.")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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
            logger.info("Updating user with ID: {}", id);
            UserModel updatedUser = userService.updateUser(id, userRecordDto);
            logger.info("User with ID {} updated successfully", id);
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            logger.error("User update failed. Reason: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Get a list of all users.")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserModel> usersList = userService.getAllUsers();
            List<EntityModel<UserModel>> usersWithLinks = new ArrayList<>();

            for (UserModel user : usersList) {
                UUID userId = user.getUserId();
                EntityModel<UserModel> userWithLink = EntityModel.of(user);
                userWithLink.add(linkTo(methodOn(UserController.class).getOneUser(userId)).withSelfRel());
                usersWithLinks.add(userWithLink);
            }

            CollectionModel<EntityModel<UserModel>> collectionModel = CollectionModel.of(usersWithLinks);
            collectionModel.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());

            logger.info("Retrieved a list of all users.");
            return ResponseEntity.status(HttpStatus.OK).body(collectionModel);

        } catch (NotFoundException e) {
            logger.error("Error retrieving users. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get details of a specific user.")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "id") UUID id) {
        try {
            Optional<UserModel> user = userService.getUserById(id);

            if (user.isPresent()) {
                logger.info("Retrieved details of user with ID {}", id);
                EntityModel<Optional<UserModel>> resource = EntityModel.of(user);
                resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("Users List"));
                return ResponseEntity.ok(resource);
            } else {
                logger.warn("User not found for ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for ID: " + id);
            }
        } catch (NotFoundException e) {
            logger.error("Error retrieving user details. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Save a new user with the provided details.")
    public ResponseEntity<UserModel> saveUser(@RequestBody @Valid UserRecordDto userRecordDto) {
        try {
            logger.info("Saving a new user");

            var userModel = getUserModel(userRecordDto);
            UserModel savedUser = userService.saveUser(userRecordDto);

            EntityModel<UserModel> userWithLink = EntityModel.of(savedUser);
            userWithLink.add(linkTo(methodOn(UserController.class).getOneUser(savedUser.getUserId())).withSelfRel());

            logger.info("User saved successfully with ID: {}", savedUser.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(userWithLink.getContent());
        } catch (Exception e) {
            logger.error("Failed to save user. Reason: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user with the specified ID.")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") UUID id) {
        try {
            logger.info("Deleting user with ID: {}", id);
            userService.deleteUser(id);
            logger.info("User with ID {} deleted successfully", id);
            return ResponseEntity.status(HttpStatus.OK).body("User with ID " + id + " successfully deleted.");
        } catch (NotFoundException | DeletedException e) {
            logger.error("Failed to delete user. Reason: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private static UserModel getUserModel(UserRecordDto userRecordDto) {
        return new UserModel(/* Set relevant properties */);
    }
}
