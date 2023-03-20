package com.document.documentstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


@SpringBootApplication
public class DocumentStoreApplication {

    private AmazonS3Service amazonS3Service;

    public static void main(String[] args) {
        AmazonS3Service s3Service = new AmazonS3Service();
        try {
            File file = new File("/Users/ShethShlok/Downloads/trial.pdf");
            FileInputStream input = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("trial.pdf", input);
            String guid = s3Service.uploadFileToS3AndGetGuid(multipartFile);
            System.out.println("File uploaded successfully. GUID: " + guid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public void run(String... args) throws Exception {
//        // Load the file
//        File file = new File("/Users/ShethShlok/Downloads/logfile_1 (5).lgf");
//        FileInputStream input = new FileInputStream(file);
//        MultipartFile multipartFile = new MockMultipartFile("/Users/ShethShlok/Downloads/logfile_1 (5).lgf", input);
//        // Upload the file to S3
//        String guid = amazonS3Service.uploadFileToS3AndGetGuid(multipartFile);
//        // Print a message to confirm the upload was successful
//        System.out.println("File uploaded to S3!");
//        System.out.println(guid);
//    }


}
