package com.zea.cloud.basic.util;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MinioClientUtil {

    @Autowired
    private MinioClient minioClient;

    /**
     * 判断MinIO中桶是否存在
     *
     * @param bucketName 桶名
     * @return 是否存在
     */
    public boolean bucketExist(String bucketName) throws Exception{
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error("---判断桶{}在MinIo服务器是否存在发生异常", bucketName, e);
            throw new Exception(e);
        }
    }

    /**
     * 创建桶
     *
     * @param bucketName 桶名
     */
    public void createBucket(String bucketName) throws Exception {
        try {
            if (!bucketExist(bucketName)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                return;
            }
            log.error("----MinIo上当前桶{}存在", bucketName);
        } catch (Exception e) {
            log.error("---在MinIo上创建桶{}发生异常", bucketName, e);
            throw new Exception(e);
        }
    }

    /**
     * 获取全部bucket
     */
    @SneakyThrows
    public List<Bucket> getAllBuckets() {
        return minioClient.listBuckets();
    }

    /**
     * 根据bucketName获取信息
     *
     * @param bucketName bucket名称
     */
    @SneakyThrows
    public Optional<Bucket> getBucket(String bucketName) {
        return minioClient.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * 指定桶中上传文件
     *
     * @param bucketName 桶名
     * @param file MultipartFile上传文件对象
     * @param fileName 文件名
     * @return 上传文件的访问URL
     */
    public String uploadFile(String bucketName, MultipartFile file, String fileName) throws Exception {
        try {
            if (!bucketExist(bucketName)) {
                createBucket(bucketName);
            }
            InputStream inputStream = file.getInputStream();
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .contentType(file.getContentType())
                    .stream(inputStream, inputStream.available(), -1)
                    .object(fileName)
                    .build();
            minioClient.putObject(putObjectArgs);
            return getFileUrl(bucketName, fileName);
        } catch (Exception e) {
            log.error("---在往MinIo的桶{}上传文件{}时发生错误", bucketName, file.getName(), e);
            throw new Exception(e);
        }
    }

    /**
     * 获取上传文件的访问URL
     *
     * @param bucketName 桶名
     * @param objectFile 文件名
     * @return 上传文件的访问URL
     */
    public String getFileUrl(String bucketName, String objectFile) throws Exception {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectFile)
                    .build()
            );
        } catch (Exception e) {
            log.error("---获取上传文件的url发生异常:{}", e.getMessage(), e);
            throw new Exception(e);
        }
    }
    /**
     * 删除指定文件
     *
     * @param bucketName 桶名
     * @param fileName 文件名
     * @return 删除成功
     */
    public boolean deleteFile(String bucketName, String fileName) throws Exception {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("---删除文件{}发生异常:{}", fileName, e.getMessage(), e);
            throw new Exception(e);
        }
    }
    /**
     * 下载文件
     *
     * @param bucketName 桶名
     * @param fileName 文件名
     * @param toPath 文件的访问URL
     * @return
     */
    public boolean downloadFile(String bucketName, String fileName, String toPath) throws Exception {
        try {
            minioClient.downloadObject(DownloadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .filename(toPath)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("---下载桶{}的文件{}发生异常:{}", bucketName, fileName, e.getMessage(), e);
            throw new Exception(e);
        }
    }

    /**
     * 设置浏览器下载响应头
     *
     * @param request request
     * @param response response
     * @param fileName 文件名
     */
    public void setResponseHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
        try {
            fileName = new String(fileName.getBytes(), "ISO8859-1");
            response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
            response.setContentType("application/octet-stream");
            // 这后面可以设置导出Excel的名称
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
