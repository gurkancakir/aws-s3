package com.gurkan.aws.awss3.controller;

import com.gurkan.aws.awss3.operation.bucket.CreateBucket;
import com.gurkan.aws.awss3.operation.bucket.DeleteBucket;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class BucketController {

    private final CreateBucket createBucket;
    private final DeleteBucket deleteBucket;

    @PostMapping("/space/{bucketName}/create")
    public void create(@PathVariable("bucketName") String bucketName) {
        createBucket.create(bucketName);
    }

    @DeleteMapping("/space/{bucketName}/delete")
    public void delete(@PathVariable("bucketName") String bucketName) {
        deleteBucket.delete(bucketName);
    }
}
