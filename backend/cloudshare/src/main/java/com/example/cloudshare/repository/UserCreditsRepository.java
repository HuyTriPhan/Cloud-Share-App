package com.example.cloudshare.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.cloudshare.domain.UserCredits;

public interface UserCreditsRepository extends MongoRepository<UserCredits, String>{
    Optional<UserCredits> findByClerkId(String clerkId);

    Long countByClerkId(String clerkId);
}
