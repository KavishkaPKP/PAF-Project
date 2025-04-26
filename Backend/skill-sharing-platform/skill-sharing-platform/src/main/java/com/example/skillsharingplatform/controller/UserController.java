package com.example.skillsharingplatform.controller;


import com.example.skillsharingplatform.model.User;
import com.example.skillsharingplatform.payload.request.UpdateProfileRequest;
import com.example.skillsharingplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> getCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> updateProfile(@RequestBody UpdateProfileRequest updateProfileRequest) {
        User updatedUser = userService.updateUserProfile(updateProfileRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable String userId) {
        User user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/follow/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> followUser(@PathVariable String userId) {
        userService.followUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unfollow/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> unfollowUser(@PathVariable String userId) {
        userService.unfollowUser(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAccount() {
        userService.deleteUserAccount();
        return ResponseEntity.noContent().build();
    }
}