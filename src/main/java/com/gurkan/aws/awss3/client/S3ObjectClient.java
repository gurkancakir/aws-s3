package com.gurkan.aws.awss3.client;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.gurkan.aws.awss3.entity.S3Object;
import com.gurkan.aws.awss3.operation.object.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Component
@AllArgsConstructor
public class S3ObjectClient implements SaveObject, DeleteObject, MakeObjectPrivate, MakeObjectPublic, UploadObject {

    private final AmazonS3Client amazonS3Client;

    @Override
    public S3Object save(String bucket, String key, String name, InputStream payload) {
        var metadata = new ObjectMetadata();
        metadata.addUserMetadata("name", name);
        amazonS3Client.putObject(bucket, key, payload, metadata);
        System.out.println("Sent the request");
        return S3Object.builder()
                .name(name)
                .key(key)
                .url(amazonS3Client.getUrl(bucket, key))
                .build();
    }

    @Override
    public void delete(String bucket, String key) {
        amazonS3Client.deleteObject(bucket, key);
        System.out.println("Sent request to delete file with key " + key + " in bucket " + bucket);
    }

    @Override
    public void makePrivate(String bucket, String key) {
        amazonS3Client.setObjectAcl(bucket, key, CannedAccessControlList.BucketOwnerFullControl);
        System.out.println("Sent request to make object in bucket " + bucket + " with key " + key + " private");
    }

    @Override
    public void makePublic(String bucket, String key) {
        amazonS3Client.setObjectAcl(bucket, key, CannedAccessControlList.PublicRead);
        System.out.println("Sent request to make object in bucket " + bucket + " with key " + key + " public");
    }

    @Override
    public S3Object upload(String bucketName, String name, InputStream payload) {

        // save
        System.out.println(
                "Going to upload the file into to "
                        + bucketName
                        + "/"
                        + name
                        + " with metadata name of "
                        + name);

        return this.save(bucketName, name, name, payload);
    }
}
