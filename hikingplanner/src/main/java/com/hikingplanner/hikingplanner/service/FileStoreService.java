package com.hikingplanner.hikingplanner.service;


import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;


import java.io.IOException;

@Service
public class FileStoreService {
    
    private final AmazonS3 s3;
    
    public FileStoreService(AmazonS3 s3){
        this.s3 = s3;
    }

    public String uploadFile(String bucketName, MultipartFile file) {
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            s3.putObject(new PutObjectRequest(bucketName, filename, file.getInputStream(), metadata));
        } catch (IOException e) {
            throw new IllegalStateException("파일 업로드 실패", e);
        }
        return s3.getUrl(bucketName, filename).toString();
    }
}

