package com.oidc.zunsi.service.naver;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.oidc.zunsi.domain.review.Review;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NaverObjectStorageService {
    private AmazonS3 s3;
    private final String endPoint = "https://kr.object.ncloudstorage.com";
    private final String regionName = "kr-standard";
    private final String accessKey = "ACCESS_KEY";
    private final String secretKey = "SECRET_KEY";
    private final String bucketName = "BUCKET_NAME";

    @PostConstruct
    private void initialize() {
        s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    public String uploadProfileImage(User user, MultipartFile imageFile) {
        return uploadImage(imageFile, "profile-image/" + user.getId(), "profile");
    }

    public String uploadPosterImage(Zunsi zunsi, MultipartFile imageFile) {
        return uploadImage(imageFile, "zunsi-image/" + zunsi.getId(), "poster");
    }

    public List<String> uploadDetailImages(Zunsi zunsi, List<MultipartFile> imageFiles) {
        List<String> imageUrlList = new ArrayList<>();
        for (int i = 0; i < imageFiles.size(); i++) {
            imageUrlList.add(uploadImage(imageFiles.get(i), "zunsi-image/" + zunsi.getId(), "detail" + i));
        }
        return imageUrlList;
    }

    public List<String> uploadReviewImages(Review review, List<MultipartFile> imageFiles) {
        List<String> imageUrlList = new ArrayList<>();
        for (int i = 0; i < imageFiles.size(); i++) {
            imageUrlList.add(uploadImage(imageFiles.get(i), "review-image/" + review.getId(), "review" + i));
        }
        return imageUrlList;
    }

    private String uploadImage(MultipartFile file, String folderName, String objectName) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0L);
        objectMetadata.setContentType("application/x-directory");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, new ByteArrayInputStream(new byte[0]), objectMetadata);

        try {
            s3.putObject(putObjectRequest);
            System.out.format("Folder %s has been created.\n", folderName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

        try {
            s3.putObject(bucketName, objectName, file.getResource().getFile());
            System.out.format("Object %s has been created.\n", objectName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s3.getUrl(bucketName, folderName + "/" + objectName).toString();
    }
}
