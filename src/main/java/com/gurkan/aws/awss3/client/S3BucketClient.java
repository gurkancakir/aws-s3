package com.gurkan.aws.awss3.client;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import com.amazonaws.waiters.WaiterParameters;
import com.gurkan.aws.awss3.entity.S3Object;
import com.gurkan.aws.awss3.operation.bucket.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Component
@AllArgsConstructor
public class S3BucketClient implements CreateBucket, DeleteBucket, AssignVisibility, RemoveVisibility, ListObjects, CreatePresignedUrl {

    private final AmazonS3Client amazonS3Client;

    @Override
    public void create(String bucket) {
        // send bucket creation request
        amazonS3Client.createBucket(bucket);
        System.out.println("Request to create " + bucket + " sent");

        // assure that bucket is available
        amazonS3Client.waiters().bucketExists().run(new WaiterParameters<>(new HeadBucketRequest(bucket)));
        System.out.println("Bucket " + bucket + " is ready");
    }

    @Override
    public void delete(String bucket) {
        // send deletion request
        amazonS3Client.deleteBucket(bucket);
        System.out.println("Request to delete " + bucket + " sent");

        // assure bucket is deleted
        amazonS3Client.waiters().bucketNotExists().run(new WaiterParameters(new HeadBucketRequest(bucket)));
        System.out.println("Bucket " + bucket + " is deleted");
    }

    @Override
    public void setVisibility(String bucket, Integer days) {
        amazonS3Client.setBucketLifecycleConfiguration(
                bucket,
                new BucketLifecycleConfiguration()
                        .withRules(
                                new BucketLifecycleConfiguration.Rule()
                                        .withId(days + "-days-expiration-id")
                                        .withFilter(new LifecycleFilter())
                                        .withStatus(BucketLifecycleConfiguration.ENABLED)
                                        .withExpirationInDays(days)));
    }

    @Override
    public void removeVisibility(String bucket) {
        amazonS3Client.deleteBucketLifecycleConfiguration(bucket);
    }

    @Override
    public List<S3Object> listObjectsInBucket(String bucket) {
        var items =
                amazonS3Client.listObjectsV2(bucket).getObjectSummaries().stream()
                        .parallel()
                        .map(S3ObjectSummary::getKey)
                        .map(key -> mapS3ToObject(bucket, key))
                        .collect(Collectors.toList());

        System.out.println("Found " + items.size() + " objects in the bucket " + bucket);
        return items;
    }

    private S3Object mapS3ToObject(String bucket, String key) {
        return S3Object.builder()
                .name(amazonS3Client.getObjectMetadata(bucket, key).getUserMetaDataOf("name"))
                .key(key)
                .url(amazonS3Client.getUrl(bucket, key))
                .isPublic(
                        amazonS3Client.getObjectAcl(bucket, key).getGrantsAsList().stream()
                                .anyMatch(grant -> grant.equals(this.publicObjectReadGrant())))
                .build();
    }

    private static Grant publicObjectReadGrant() {
        return new Grant(GroupGrantee.parseGroupGrantee(GroupGrantee.AllUsers.getIdentifier()), Permission.Read);
    }

    @Override
    public URL createURL(String bucket, String key, Long duration) {
        var date = new Date(new Date().getTime() + duration * 1000);
        var url = amazonS3Client.generatePresignedUrl(bucket, key, date);
        System.out.println("Generated the signature " + url);
        return url;
    }
}
