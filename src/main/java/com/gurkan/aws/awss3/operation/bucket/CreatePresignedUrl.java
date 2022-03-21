package com.gurkan.aws.awss3.operation.bucket;

import java.net.URL;

public interface CreatePresignedUrl {
    URL createURL(String bucket, String key, Long duration);
}
