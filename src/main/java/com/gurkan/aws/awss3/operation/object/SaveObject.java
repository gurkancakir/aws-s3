package com.gurkan.aws.awss3.operation.object;

import com.gurkan.aws.awss3.entity.S3Object;

import java.io.InputStream;

public interface SaveObject {
    S3Object save(String bucket, String key, String name, InputStream payload);
}
