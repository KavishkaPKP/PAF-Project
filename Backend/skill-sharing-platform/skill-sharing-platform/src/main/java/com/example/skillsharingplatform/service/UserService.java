package com.example.skillsharingplatform.service;

import com.example.skillsharingplatform.exception.ResourceNotFoundException;
import com.example.skillsharingplatform.model.User;
import com.example.skillsharingplatform.payload.request.UpdateProfileRequest;
import com.example.skillsharingplatform.repository.UserRepository;
import com.example.skillsharingplatform.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userDetails.getId()));
    }

    public User updateUserProfile(UpdateProfileRequest updateProfileRequest) {
        User user = getCurrentUser();
        
        if (updateProfileRequest.getBio() != null) {
            user.setBio(updateProfileRequest.getBio());
        }
        
        if (updateProfileRequest.getProfilePicture() != null) {
            user.setProfilePicture(updateProfileRequest.getProfilePicture());
        }
        
        if (updateProfileRequest.getSkills() != null) {
            user.setSkills(updateProfileRequest.getSkills());
        }
        
        return userRepository.save(user);
    }

    public User getUserProfile(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    public void followUser(String userIdToFollow) {
        User currentUser = getCurrentUser();
        User userToFollow = userRepository.findById(userIdToFollow)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userIdToFollow));

        currentUser.getFollowing().add(userIdToFollow);
        userToFollow.getFollowers().add(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(userToFollow);
    }

    public void unfollowUser(String userIdToUnfollow) {
        User currentUser = getCurrentUser();
        User userToUnfollow = userRepository.findById(userIdToUnfollow)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userIdToUnfollow));

        currentUser.getFollowing().remove(userIdToUnfollow);
        userToUnfollow.getFollowers().remove(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(userToUnfollow);
    }

    public void deleteUserAccount() {
        User user = getCurrentUser();
        userRepository.delete(user);
    }
}