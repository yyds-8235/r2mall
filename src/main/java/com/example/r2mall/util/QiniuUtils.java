package com.example.r2mall.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@AllArgsConstructor
@Data
@Slf4j
public class QiniuUtils {

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String localFilePath;
    private String domainOfBucket;
    private static final String HTTP_PREFIX = "http://";

    /**
     * 文件上传
     */
    public String upload(byte[] bytes, String fileName) throws RuntimeException, UnsupportedEncodingException {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = Configuration.create(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);

        Auth auth = Auth.create(accessKey, secretKey);

        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(bytes, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            log.error("上传异常: {}", ex.getMessage(), ex);
            if (ex.response != null) {
                System.err.println(ex.response);
                try {
                    String body = ex.response.toString();
                    System.err.println(body);
                } catch (Exception ignored) {
                }
            }
        }


        // 获取文件下载路径

        String downloadUrl = getPrivateDownloadUrl(fileName);
        log.info("文件上传到:{}", downloadUrl);

        return downloadUrl;
    }

    /**
     * 获取文件下载路径
     */
    public String getPrivateDownloadUrl(String fileName) {
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);

        Auth auth = Auth.create(accessKey, secretKey);
        long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
        return HTTP_PREFIX + auth.privateDownloadUrl(publicUrl, expireInSeconds);
    }
}