package com.example.cloudshare.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.cloudshare.domain.Profile;

public interface ProfileRepository extends MongoRepository<Profile, String>{
    Optional<Profile> findByEmail(String email);

    Profile findByClerkId(String cleckId);

    boolean existsByClerkId(String cleckId);
}
