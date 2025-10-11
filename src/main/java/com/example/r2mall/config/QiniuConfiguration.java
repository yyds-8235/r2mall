package com.example.r2mall.config;

import com.example.r2mall.common.oss.QiniuProperties;
import com.example.r2mall.util.QiniuUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
/**
 * 配置类，用于创建QiniuUtils对象
 */
@Configuration
@Slf4j
public class QiniuConfiguration {
 
    @Bean
    @ConditionalOnMissingBean
    public QiniuUtils qiniuUtils(QiniuProperties qiniuProperties) {
        log.info("开始创建七牛云文件上传工具类对象：{}", qiniuProperties);
 
        return new QiniuUtils(
                qiniuProperties.getAccessKey(),
                qiniuProperties.getSecretKey(),
                qiniuProperties.getBucket(),
                qiniuProperties.getLocalFilePath(),
                qiniuProperties.getDomainOfBucket());
    }
}