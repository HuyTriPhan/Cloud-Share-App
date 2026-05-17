package com.example.cloudshare.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "files")
public class FileMetadata {
    @Id
    private String id;
    private String name;
    private String type;
    private Long size;
    private String clerkId;
    private Boolean isPublic;
    private String fileLocation;
    private LocalDateTime uploadedAt;
}
