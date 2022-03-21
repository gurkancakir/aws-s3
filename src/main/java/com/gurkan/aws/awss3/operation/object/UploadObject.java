package com.gurkan.aws.awss3.operation.object;

import com.gurkan.aws.awss3.entity.S3Object;

import java.io.InputStream;

public interface UploadObject {
    S3Object upload(String bucketName, String name, InputStream payload);
}
