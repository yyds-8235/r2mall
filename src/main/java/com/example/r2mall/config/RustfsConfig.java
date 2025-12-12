package com.example.r2mall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

/**
 * RustfsConfig
 *
 * @Description
 * @Author hanchenyang
 * @Date 2025/12/12 15:46
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rustfs")
public class RustfsConfig {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    /**
     * 基础鉴权凭证
     */
    private StaticCredentialsProvider credentialsProvider() {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.CN_NORTH_1)
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(credentialsProvider())
                .forcePathStyle(true) // 关键配置！RustFS 需启用 Path-Style
                .build();
    }

    /**
     * 预签名客户端：专门负责生成临时 URL
     */
    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.CN_NORTH_1)
                .credentialsProvider(credentialsProvider())
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }
}
