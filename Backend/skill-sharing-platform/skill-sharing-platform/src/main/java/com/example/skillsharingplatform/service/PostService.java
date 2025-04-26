package com.example.skillsharingplatform.service;

import com.example.skillsharingplatform.exception.ResourceNotFoundException;
import com.example.skillsharingplatform.model.Post;
import com.example.skillsharingplatform.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserService userService;
    
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public List<Post> getPostsByUser(String userId) {
        return postRepository.findByUserId(userId);
    }
    
    public Post getPostById(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    }
    
    public Post createPost(String userId, Post post) {
        post.setUserId(userId);
        return postRepository.save(post);
    }
    
    public Post updatePost(String userId, String postId, Post postDetails) {
        Post post = getPostById(postId);
        
        // Verify the user owns the post
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("You are not authorized to update this post");
        }
        
        post.setTitle(postDetails.getTitle());
        post.setDescription(postDetails.getDescription());
        post.setMediaUrls(postDetails.getMediaUrls());
        post.setUpdatedAt(new Date());
        
        return postRepository.save(post);
    }
    
    public void deletePost(String userId, String postId) {
        Post post = getPostById(postId);
        
        // Verify the user owns the post
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this post");
        }
        
        postRepository.delete(post);
    }
}