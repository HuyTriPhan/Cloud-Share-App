package com.example.cloudshare.domain;

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
@Document(collection = "user_credits")
public class UserCredits {
    @Id
    private String id;
    private String clerkId;
    private Integer credits;
    private String plan;
}
