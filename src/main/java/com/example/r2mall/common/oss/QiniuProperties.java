package com.example.r2mall.common.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oss.qiniu")
@Data
public class QiniuProperties {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String localFilePath;
    private String domainOfBucket;
}