package com.gurkan.aws.awss3.controller;

import com.gurkan.aws.awss3.entity.S3Object;
import com.gurkan.aws.awss3.operation.bucket.ListObjects;
import com.gurkan.aws.awss3.operation.object.DeleteObject;
import com.gurkan.aws.awss3.operation.object.MakeObjectPrivate;
import com.gurkan.aws.awss3.operation.object.MakeObjectPublic;
import com.gurkan.aws.awss3.operation.object.UploadObject;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class ObjectController {

    private final UploadObject uploadObject;
    private final DeleteObject deleteObject;
    private final ListObjects listObjects;
    private final MakeObjectPrivate makeObjectPrivate;
    private final MakeObjectPublic makeObjectPublic;

    @PostMapping("/space/{bucketName}/upload")
    public S3Object upload(@PathVariable("bucketName") String bucketName,
                           @RequestParam("file") MultipartFile file) throws IOException {
        return uploadObject.upload(bucketName, file.getOriginalFilename(), file.getInputStream());
    }

    @DeleteMapping("space/{bucketName}/object/{key}")
    public void deleteObject(@PathVariable("bucketName") String bucketName, @PathVariable String key) {
        deleteObject.delete(bucketName, key);
    }

    @GetMapping("space/{bucketName}/list-objects")
    public List<S3Object> listObjectsInBucket(@PathVariable("bucketName") String bucketName) {
        return listObjects.listObjectsInBucket(bucketName);
    }

    @PutMapping("space/{bucketName}/object/{key}/public")
    public void makePublic(@PathVariable("bucketName") String bucketName, @PathVariable String key) {
        makeObjectPublic.makePublic(bucketName, key);
    }

    @PutMapping("space/{bucketName}/object/{key}/private")
    public void makePrivate(@PathVariable("bucketName") String bucketName, @PathVariable String key) {
        makeObjectPrivate.makePrivate(bucketName, key);
    }
}
