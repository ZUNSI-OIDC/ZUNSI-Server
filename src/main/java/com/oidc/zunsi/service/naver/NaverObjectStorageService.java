package com.oidc.zunsi.service.naver;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import com.oidc.zunsi.util.FileManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NaverObjectStorageService {
    private AmazonS3 s3;
    private final String endPoint = "https://kr.object.ncloudstorage.com";
    private final String regionName = "kr-standard";
    private final FileManager fileManager;

    @Value("${spring.naver.cloud.access-key}")
    private String accessKey;

    @Value("${spring.naver.cloud.secret-key}")
    private String secretKey;

    @Value("${spring.naver.cloud.bucket-name}")
    private String bucketName;

    public NaverObjectStorageService(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @PostConstruct
    private void initialize() {
        s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    public String uploadProfileImage(User user, MultipartFile imageFile) throws IOException {
        return uploadImage(imageFile, "profile-image/" + user.getId(), "profile");
    }

    public String uploadPosterImage(Zunsi zunsi, MultipartFile imageFile) throws IOException {
        return uploadImage(imageFile, "zunsi-image/" + zunsi.getId(), "poster");
    }

    public List<String> uploadDetailImages(Zunsi zunsi, MultipartFile[] imageFiles) throws IOException {
        List<String> imageUrlList = new ArrayList<>();
        for (int i = 0; i < imageFiles.length; i++)
            imageUrlList.add(uploadImage(imageFiles[i], "zunsi-image/" + zunsi.getId(), "detail" + i));
        return imageUrlList;
    }

    public List<String> uploadReviewImages(String uuid, MultipartFile[] imageFiles) throws IOException {
        List<String> imageUrlList = new ArrayList<>();
        for (int i = 0; i < imageFiles.length; i++)
            imageUrlList.add(uploadImage(imageFiles[i], "review-image/" + uuid, "review" + i));
        return imageUrlList;
    }

    private String uploadImage(MultipartFile file, String folderName, String objectName) throws IOException {
        String pathName = bucketName + "/" + folderName;
        File imageFile = null;
        try {
            Optional<File> image = fileManager.convertMultipartFileToFile(file);
            if(image.isEmpty()) return null;
            imageFile = image.get();
            s3.putObject(pathName, objectName, imageFile);
            System.out.format("Object %s has been created.\n", objectName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (SdkClientException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        } finally {
            if (imageFile != null) imageFile.delete();
        }

        try {
            AccessControlList accessControlList = s3.getObjectAcl(pathName, objectName);
            accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);
            s3.setObjectAcl(pathName, objectName, accessControlList);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }

        return s3.getUrl(bucketName, folderName + "/" + objectName).toString();
    }
}
