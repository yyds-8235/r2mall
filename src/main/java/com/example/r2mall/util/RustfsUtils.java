package com.example.r2mall.util;

import com.example.r2mall.config.RustfsConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

/**
 * RustfsUtils
 *
 * @Description
 * @Author hanchenyang
 * @Date 2025/12/12 15:56
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RustfsUtils {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final RustfsConfig rustfsConfig;

    /**
     * 检查存储桶是否存在，不存在则创建
     */
    public void checkAndCreateBucket(String bucketName) {
        try {
            // AWS SDK 没有直接的 bucketExists，通常用 headBucket 探测
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
        } catch (NoSuchBucketException e) {
            // 桶不存在，创建它
            try {
                s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
                log.info("存储桶 [{}] 创建成功", bucketName);
            } catch (Exception createEx) {
                log.error("创建存储桶失败", createEx);
                throw new RuntimeException("S3 初始化失败: 无法创建桶");
            }
        } catch (Exception e) {
            log.error("检查存储桶状态失败", e);
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
        String bucketName = rustfsConfig.getBucketName();
        checkAndCreateBucket(bucketName);

        // 2. 生成唯一文件名 (UUID + 原后缀)，防止文件名冲突
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String objectName = UUID.randomUUID().toString().replace("-", "") + suffix;

        // 3. 执行上传
        try (InputStream inputStream = file.getInputStream()) {
            // AWS SDK v2 上传写法
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .contentType(file.getContentType()) // 设置 Content-Type
                    .build();

            // RequestBody.fromInputStream 需要指定流和长度
            s3Client.putObject(putOb, RequestBody.fromInputStream(inputStream, file.getSize()));

            log.info("文件上传成功: {}", objectName);
            return objectName;
        } catch (IOException e) {
            log.error("文件流读取失败", e);
            throw new RuntimeException("文件上传失败");
        } catch (S3Exception e) {
            log.error("S3服务异常", e);
            throw new RuntimeException("文件存储服务异常");
        }
    }

    /**
     * 获取文件预览/下载链接（带签名，临时有效）
     * 适用于私有桶，生成的链接可以直接在浏览器打开查看
     *
     * @param fileName    文件名
     * @param expiryHours 有效期（单位：小时）
     * @return 完整的 URL 地址
     */
    public String getPreviewUrl(String fileName, int expiryHours) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(rustfsConfig.getBucketName())
                    .key(fileName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofHours(expiryHours)) // 设置有效期
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            return presignedRequest.url().toString();

        } catch (Exception e) {
            log.error("生成预览链接失败", e);
            return null;
        }
    }

    /**
     * 获取文件流 (下载)
     */
    public InputStream getFileStream(String fileName) {
        try {
            return s3Client.getObject(GetObjectRequest.builder()
                    .bucket(rustfsConfig.getBucketName())
                    .key(fileName)
                    .build());
        } catch (Exception e) {
            log.error("获取文件流失败", e);
            throw new RuntimeException("下载文件失败");
        }
    }

    /**
     * 删除文件
     */
    public void remove(String fileName) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(rustfsConfig.getBucketName())
                    .key(fileName)
                    .build());
            log.info("文件删除成功: {}", fileName);
        } catch (Exception e) {
            log.error("删除文件失败", e);
            throw new RuntimeException("删除文件失败");
        }
    }
}