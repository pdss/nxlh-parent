package com.nxlh.common.utils;


import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;

/**
 * 阿里云 OSS文件存储接口
 */
@Component
@Slf4j
public class OSSUtils {


    private String endpoint = "http://oss-cn-zhangjiakou.aliyuncs.com";
    @Value("${nxlh.oss-accesskeyid}")
    private String accessKeyId;
    @Value("${nxlh.oss-accesskeyseret}")
    private String accessKeySecret;
    private String bucketName = "nxlh-resources";

    public MyResult uploadImage(File file, String name) {
        String key = "nxlh-resources/" + name;
        /*
         * Constructs a client instance with your account for accessing OSS
         */
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {

            /*
             * Determine whether the bucket exists
             */
            if (!ossClient.doesBucketExist(bucketName)) {
                /*
                 * Create a new OSS bucket
                 */
//                System.out.println("Creating bucket " + bucketName + "\n");
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }

            /*
             * Upload an object to your bucket
             */
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, key, file));

        } catch (OSSException oe) {
            log.info("上传图片失败:{}", oe.getMessage());
            return MyResult.build(HttpResponseEnums.InternalServerError, oe);
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            ossClient.shutdown();
        }
        return MyResult.ok(key);
    }

    public MyResult uploadImage(InputStream file, String name) {
        String key = "nxlh-resources/" + name;
        /*
         * Constructs a client instance with your account for accessing OSS
         */
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {

            /*
             * Determine whether the bucket exists
             */
            if (!ossClient.doesBucketExist(bucketName)) {
                /*
                 * Create a new OSS bucket
                 */
//                System.out.println("Creating bucket " + bucketName + "\n");
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }

            /*
             * Upload an object to your bucket
             */
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, key, file));

        } catch (OSSException oe) {
            log.info("上传图片失败:{}", oe.getMessage());
            return MyResult.build(HttpResponseEnums.InternalServerError, oe);
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            ossClient.shutdown();
        }
        return MyResult.ok(key);
    }


}
