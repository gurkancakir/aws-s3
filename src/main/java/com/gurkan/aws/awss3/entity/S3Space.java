package com.gurkan.aws.awss3.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class S3Space {
    String name;
    String bucket;
    Integer ttl;
}
