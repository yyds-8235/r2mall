package com.example.r2mall.util;

import com.example.r2mall.config.MinioConfig;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * MinIO 文件操作工具类
 * 封装了文件上传、查看（预览）、下载、删除等核心功能
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MinioUtils {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    /**
     * 检查存储桶是否存在，不存在则创建
     */
    public void checkAndCreateBucket(String bucketName) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("存储桶 [{}] 创建成功", bucketName);
            }
        } catch (Exception e) {
            log.error("检查或创建存储桶失败", e);
            throw new RuntimeException("MinIO 初始化失败");
        }
    }

    /**
     * 文件上传
     *
     * @param file 前端上传的文件对象
     * @return 文件的唯一标识（文件名），用于后续访问
     */
    public String upload(MultipartFile file) {
        // 1. 确保桶存在
        String bucketName = minioConfig.getBucketName();
        checkAndCreateBucket(bucketName);

        // 2. 生成唯一文件名 (UUID + 原后缀)，防止文件名冲突
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String objectName = UUID.randomUUID().toString().replace("-", "") + suffix;

        // 3. 执行上传
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType()) // 设置 Content-Type，非常重要，否则预览时会变成下载
                            .build()
            );
            log.info("文件上传成功: {}", objectName);
            return objectName;
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败");
        }
    }

    /**
     * 获取文件预览/下载链接（带签名，临时有效）
     * 适用于私有桶，生成的链接可以直接在浏览器打开查看
     *
     * @param fileName 文件名
     * @param expiry   有效期（单位：小时）
     * @return 完整的 URL 地址
     */
    public String getPreviewUrl(String fileName, int expiry) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioConfig.getBucketName())
                            .object(fileName)
                            .expiry(expiry, TimeUnit.HOURS)
                            .build()
            );
        } catch (Exception e) {
            log.error("获取文件预览链接失败", e);
            return null;
        }
    }

    /**
     * 获取文件流（用于后端处理或自定义下载接口）
     *
     * @param fileName 文件名
     * @return InputStream
     */
    public InputStream getFileStream(String fileName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            log.error("获取文件流失败", e);
            throw new RuntimeException("获取文件流失败");
        }
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     */
    public void remove(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(fileName)
                            .build()
            );
            log.info("文件删除成功: {}", fileName);
        } catch (Exception e) {
            log.error("文件删除失败", e);
            throw new RuntimeException("文件删除失败");
        }
    }
}