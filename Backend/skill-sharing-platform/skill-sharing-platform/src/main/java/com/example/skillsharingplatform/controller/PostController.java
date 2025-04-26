package com.example.skillsharingplatform.controller;

import com.example.skillsharingplatform.model.Post;
import com.example.skillsharingplatform.payload.request.CreatePostRequest;
import com.example.skillsharingplatform.payload.response.PostResponse;
import com.example.skillsharingplatform.security.UserDetailsImpl;
import com.example.skillsharingplatform.service.FileStorageService;
import com.example.skillsharingplatform.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postResponses);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUser(@PathVariable String userId) {
        List<Post> posts = postService.getPostsByUser(userId);
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable String id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(new PostResponse(post));
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody CreatePostRequest createPostRequest,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Post post = new Post(
                userDetails.getId(),
                createPostRequest.getTitle(),
                createPostRequest.getDescription()
        );
        post.setMediaUrls(createPostRequest.getMediaUrls());

        Post createdPost = postService.createPost(userDetails.getId(), post);
        return ResponseEntity.ok(new PostResponse(createdPost));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable String id,
            @Valid @RequestBody CreatePostRequest updatePostRequest,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Post postDetails = new Post();
        postDetails.setTitle(updatePostRequest.getTitle());
        postDetails.setDescription(updatePostRequest.getDescription());
        postDetails.setMediaUrls(updatePostRequest.getMediaUrls());

        Post updatedPost = postService.updatePost(userDetails.getId(), id, postDetails);
        return ResponseEntity.ok(new PostResponse(updatedPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable String id,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        postService.deletePost(userDetails.getId(), id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            Authentication authentication) throws IOException {
        // Limit to 3 files
        if (files.length > 3) {
            throw new RuntimeException("Maximum 3 files allowed");
        }

        List<String> fileUrls = fileStorageService.storeFiles(files);
        return ResponseEntity.ok(fileUrls);
    }
}