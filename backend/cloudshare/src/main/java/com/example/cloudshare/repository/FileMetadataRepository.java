package com.example.cloudshare.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.cloudshare.domain.FileMetadata;

public interface FileMetadataRepository extends MongoRepository<FileMetadata, String> {
    List<FileMetadata> findByClerkId(String clerkId);

    Long countByClerkId(String clerkId);
}
