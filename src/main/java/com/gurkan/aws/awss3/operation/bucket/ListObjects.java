package com.gurkan.aws.awss3.operation.bucket;

import com.gurkan.aws.awss3.entity.S3Object;

import java.util.List;

public interface ListObjects {
    List<S3Object> listObjectsInBucket(String bucket);

}
