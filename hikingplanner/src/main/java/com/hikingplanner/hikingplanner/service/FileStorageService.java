// package com.hikingplanner.hikingplanner.service;

// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import com.amazonaws.services.s3.AmazonS3;
// import com.amazonaws.services.s3.AmazonS3ClientBuilder;
// import com.amazonaws.services.s3.model.ObjectMetadata;
// import com.amazonaws.services.s3.model.PutObjectRequest;

// import java.io.IOException;
// import java.util.UUID;

// @Service
// public class FileStorageService {
    
//     private AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
//     private String bucketName = "hikingplanner";

//     public String storeFile(MultipartFile file) {
//         String fileName = generateFileName(file);
//         try {
//             ObjectMetadata metadata = new ObjectMetadata();
//             metadata.setContentLength(file.getSize());
//             metadata.setContentType(file.getContentType());
//             s3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(),metadata));
//             return s3Client.getUrl(bucketName, fileName).toString();
            
//         } catch (IOException e) {
//             throw new RuntimeException("Failed to upload file",e);
//         }

//     }

//     private String generateFileName(MultipartFile file) {
//         return UUID.randomUUID().toString() + "-" +file.getOriginalFilename().replace(" ","_");
//     }
// }
