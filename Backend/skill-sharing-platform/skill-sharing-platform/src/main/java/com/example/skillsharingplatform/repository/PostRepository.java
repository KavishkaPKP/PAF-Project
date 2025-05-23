package com.example.skillsharingplatform.repository;

import com.example.skillsharingplatform.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUserId(String userId);
    List<Post> findAllByOrderByCreatedAtDesc();
}