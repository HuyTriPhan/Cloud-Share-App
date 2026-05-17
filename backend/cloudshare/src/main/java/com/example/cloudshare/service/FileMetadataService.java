package com.example.cloudshare.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.cloudshare.domain.FileMetadata;
import com.example.cloudshare.domain.Profile;
import com.example.cloudshare.dto.FileMetadataDTO;
import com.example.cloudshare.repository.FileMetadataRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileMetadataService {
    private final ProfileService profileService;
    private final UserCreditsService userCreditsService;
    private final FileMetadataRepository fileMetadataRepository;

    public List<FileMetadataDTO> uploadFiles(MultipartFile files[]) {
        Profile currentProfile = profileService.getCurrentProfile();
        List<FileMetadata> savedFiles = new ArrayList<>();

        if (!userCreditsService.hasEnoughCredits(files.length)) {
            throw new RuntimeException("Not enough credits to upload files. Please purchase more credits");
        }

        try {
            Path uploadPath = Paths.get("upload").toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            for (MultipartFile file : files) {
                String originalName = file.getOriginalFilename();
                String fileName = UUID.randomUUID() + "."
                        + StringUtils.getFilenameExtension(file.getOriginalFilename());

                Path targetLocation = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                FileMetadata fileMetadata = FileMetadata.builder()
                        .fileLocation(targetLocation.toString())
                        .name(originalName)
                        .size(file.getSize())
                        .type(file.getContentType())
                        .clerkId(currentProfile.getClerkId())
                        .isPublic(false)
                        .uploadedAt(LocalDateTime.now())
                        .build();

                userCreditsService.consumeCredit();
                savedFiles.add(fileMetadataRepository.save(fileMetadata));
            }

        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }

        return savedFiles.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private FileMetadataDTO mapToDTO(FileMetadata fileMetadata) {
        return FileMetadataDTO.builder()
                .id(fileMetadata.getId())
                .fileLocation(fileMetadata.getFileLocation())
                .name(fileMetadata.getName())
                .size(fileMetadata.getSize())
                .type(fileMetadata.getType())
                .clerkId(fileMetadata.getClerkId())
                .isPublic(fileMetadata.getIsPublic())
                .uploadedAt(fileMetadata.getUploadedAt())
                .build();
    }

    public List<FileMetadataDTO> getFiles() {
        Profile currentProfile = profileService.getCurrentProfile();
        List<FileMetadata> files = fileMetadataRepository.findByClerkId(currentProfile.getClerkId());
        return files.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public FileMetadataDTO getPublicFile(String id) {
        Optional<FileMetadata> fileOptional = fileMetadataRepository.findById(id);
        if (fileOptional.isEmpty() || !fileOptional.get().getIsPublic()) {
            throw new RuntimeException("Unable to get the file");
        }

        FileMetadata document = fileOptional.get();
        return mapToDTO(document);
    }

    public FileMetadataDTO getDownloadableFile(String id) {
        FileMetadata file = fileMetadataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        return mapToDTO(file);
    }

    public void deleteFile(String id) {
        try {
            Profile currentProfile = profileService.getCurrentProfile();
            FileMetadata file = fileMetadataRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("File not found"));

            if (!file.getClerkId().equals(currentProfile.getClerkId())) {
                throw new RuntimeException("File does not belong to current user");
            }

            Path filePath = Paths.get(file.getFileLocation());
            Files.deleteIfExists(filePath);

            fileMetadataRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting the file");
        }
    }

    public FileMetadataDTO tooglePublic(String id) {
        FileMetadata file = fileMetadataRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("File not found"));

            file.setIsPublic(!file.getIsPublic());
            fileMetadataRepository.save(file);
            return mapToDTO(file);
    }

}
