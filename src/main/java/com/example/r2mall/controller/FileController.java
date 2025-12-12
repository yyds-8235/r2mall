package com.example.r2mall.controller;

import com.example.r2mall.common.Result;
import com.example.r2mall.util.MinioUtils;
import com.example.r2mall.util.QiniuUtils;
import com.example.r2mall.util.RustfsUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@Tag(name = "文件管理", description = "用户图片文件管理接口")
@Slf4j
@RequiredArgsConstructor
public class FileController {

    //    private final QiniuUtils qiniuUtils;
//    private final MinioUtils minioUtils;
    private final RustfsUtils rustfsUtils;

//    @PostMapping("/upload")
//    @Operation(summary = "文件上传")
//    public Result<String> upload(MultipartFile file) {
//        log.info("文件上传：{}", file);
//
//        try {
//            //原始文件名
//            String originalFilename = file.getOriginalFilename();
//            //截取原始文件名的后缀   a.png ==> png
//            String extension = null;
//            if (originalFilename != null) {
//                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            }
//            //构造新文件名称
//            String objectName = UUID.randomUUID().toString() + extension;
//
//            //文件的请求路径
//            String filePath = qiniuUtils.upload(file.getBytes(), objectName);
//            return Result.success("操作成功", filePath);
//        } catch (IOException e) {
//            log.error("文件上传失败：{}", e.getMessage());
//        }
//
//        return Result.error("操作失败...");
//    }

//    /**
//     * 上传接口
//     */
//    @PostMapping("/upload")
//    public Result<String> upload(@RequestParam("file") MultipartFile file) {
//        // 上传文件并获取唯一文件名
//        String fileName = minioUtils.upload(file);
//
//        // 获取一个有效期 24 小时的预览链接
//        String previewUrl = minioUtils.getPreviewUrl(fileName, 24);
//
//        return Result.success("操作成功", previewUrl);
//    }
//
//    /**
//     * 删除接口
//     */
//    @DeleteMapping("/{fileName}")
//    public Result<String> delete(@PathVariable String fileName) {
//        minioUtils.remove(fileName);
//        return Result.success();
//    }
//
//    /**
//     * 预览接口 (如果之前存的 URL 过期了，可以调这个接口重新获取)
//     */
//    @GetMapping("/preview/{fileName}")
//    public Result<String> getUrl(@PathVariable String fileName) {
//        String previewUrl = minioUtils.getPreviewUrl(fileName, 2);
//        log.info("获取文件预览链接：{}", previewUrl);
//        return Result.success("操作成功", previewUrl);
//    }

    /**
     * 上传接口
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        // 上传文件并获取唯一文件名
        String fileName = rustfsUtils.upload(file);

        // 获取一个有效期 24 小时的预览链接
        String previewUrl = rustfsUtils.getPreviewUrl(fileName, 24);

        return Result.success("操作成功", previewUrl);
    }

    /**
     * 删除接口
     */
    @DeleteMapping("/{fileName}")
    public Result<String> delete(@PathVariable String fileName) {
        rustfsUtils.remove(fileName);
        return Result.success();
    }

    /**
     * 预览接口 (如果之前存的 URL 过期了，可以调这个接口重新获取)
     */
    @GetMapping("/preview/{fileName}")
    public Result<String> getUrl(@PathVariable String fileName) {
        String previewUrl = rustfsUtils.getPreviewUrl(fileName, 2);
        log.info("获取文件预览链接：{}", previewUrl);
        return Result.success("操作成功", previewUrl);
    }

    /**
     * 下载接口
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        try {
            InputStream inputStream = rustfsUtils.getFileStream(fileName);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            IOUtils.copy(inputStream, response.getOutputStream());
            inputStream.close();
            response.flushBuffer();
            log.info("文件下载成功：{}", fileName);
        } catch (Exception e) {
            log.error("获取文件流失败", e);
        }
    }
}