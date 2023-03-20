package com.document.documentstore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

@Service
public class AmazonS3Service {

    @Value("${aws.access_key_id}")
    private String awsAccessKeyId;

    @Value("${aws.secret_access_key}")
    private String awsSecretAccessKey;

    @Value("${aws.s3.region}")
    private String awsS3Region;

    @Value("${aws.s3.bucket_name}")
    private String awsS3BucketName;

    private final AmazonS3 amazonS3Client;

    public AmazonS3Service() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
        this.amazonS3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(awsS3Region)
                .build();
    }

    public String uploadFileToS3AndGetGuid(MultipartFile multipartFile) throws IOException {
        String guid = UUID.randomUUID().toString();
        String fileName = guid + "_" + multipartFile.getOriginalFilename();
        File file = new File(fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
        PutObjectRequest putObjectRequest = new PutObjectRequest(awsS3BucketName, fileName, file);
        amazonS3Client.putObject(putObjectRequest);
        file.delete();
        return guid;
    }

    public File downloadFileFromS3ByGuid(String guid) {
        String fileName = guid + "_";
        S3Object s3Object = amazonS3Client.getObject(awsS3BucketName, guid);
        GetObjectRequest getObjectRequest = new GetObjectRequest(awsS3BucketName, s3Object.getKey());
        File file = new File(s3Object.getKey());
        amazonS3Client.getObject(getObjectRequest, file);
        return file;
    }

}

