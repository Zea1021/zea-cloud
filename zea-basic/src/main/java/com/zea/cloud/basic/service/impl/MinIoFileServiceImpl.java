package com.zea.cloud.basic.service.impl;

import com.zea.cloud.basic.bean.entity.FileInfo;
import com.zea.cloud.basic.mapper.MinIoFileMapper;
import com.zea.cloud.basic.service.MinIoFileService;
import com.zea.cloud.basic.util.MinioClientUtil;
import com.zea.cloud.common.exception.ErrorCode;
import com.zea.cloud.common.exception.MyException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class MinIoFileServiceImpl implements MinIoFileService {

    @Resource
    private MinioClient minioClient;
    @Resource
    private MinioClientUtil minioClientUtil;
    @Resource
    private MinIoFileMapper minIoFileMapper;

    @Transactional(rollbackFor = Exception.class)
    public Integer uploadFileAndSaveInfoIntoDb(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            long size = file.getSize();
            String fileName = System.currentTimeMillis() + "-" + originalFilename;
            String bucketName = "zea-test";
            String url = minioClientUtil.uploadFile(bucketName, file, fileName);
            FileInfo fileInfo = FileInfo.builder()
                    .fileName(fileName)
                    .uploadTime(new Date())
                    .bucketName(bucketName)
                    .fileSize(size)
                    .minioUrl(url)
                    .uploadUserId(1)
                    .build();
            minIoFileMapper.insert(fileInfo);
            return fileInfo.getId();
        } catch (Exception e) {
            log.error("----调用上传文件接口发生异常 {} ", e.getMessage(), e);
            throw new MyException(ErrorCode.BUSINESS_EXCEPTION, e.getMessage());
        }
    }

    /**
     * 预览文件
     *
     * @param id 文件id
     * @return minIoUrl
     */
    public String viewFileById(Integer id) {
        FileInfo fileInfo = minIoFileMapper.selectById(id);
        if (Objects.isNull(fileInfo)) {
            throw new MyException(ErrorCode.BUSINESS_EXCEPTION, "当前Id未查询到对应的文件");
        }
        String minIoUrl = fileInfo.getMinioUrl();
        if (!StringUtils.hasLength(minIoUrl)) {
            throw new MyException(ErrorCode.BUSINESS_EXCEPTION, "当前Id查询到文件获取到的url为空");
        }
        return minIoUrl;
    }
    /**
     * 删除文件
     *
     * @param id 文件id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteFileById(Integer id) {
        try {
            FileInfo fileInfo = minIoFileMapper.selectById(id);
            if (Objects.isNull(fileInfo)) {
                throw new MyException(ErrorCode.BUSINESS_EXCEPTION, "当前Id未查询到对应的文件");
            }
            String bucketName = fileInfo.getBucketName();
            String name = fileInfo.getFileName();

            minioClientUtil.deleteFile(bucketName, name);
            minIoFileMapper.deleteById(id);
        } catch (Exception e) {
            log.error("---删除文件{} 发生异常{} ", id, e.getMessage(), e);
            throw new MyException(ErrorCode.BUSINESS_EXCEPTION, e.getMessage());
        }
    }
    /**
     * 文件下载
     *
     * @param response response
     * @param id 文件id
     */
    public void downLoadFile(HttpServletResponse response, Integer id) {
        FileInfo fileInfo = minIoFileMapper.selectById(id);
        if (Objects.isNull(fileInfo)) {
            log.info("当前Id未查询到对应的文件", new RuntimeException());
        }
        String name = fileInfo.getFileName();
        String bucketName = fileInfo.getBucketName();
        try (InputStream ism = new BufferedInputStream(minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(name).build()))) {
            // 调用statObject()来判断对象是否存在。如果不存在, statObject()抛出异常,否则则代表对象存在
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(name).build());
            byte[] buf = new byte[1024];
            int length;
            response.reset();
            // Content-disposition 是 MIME 协议的扩展，MIME 协议指示 MIME 用户代理如何显示附加的文件。
            // Content-disposition其实可以控制用户请求所得的内容存为一个文件的时候提供一个默认的文件名，
            // 文件直接在浏览器上显示或者在访问时弹出文件下载对话框。
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name, StandardCharsets.UTF_8));
            response.setContentType("application/x-msdownload");
            response.setCharacterEncoding("utf-8");
            OutputStream osm = new BufferedOutputStream(response.getOutputStream());
            while ((length = ism.read(buf)) > 0) {
                osm.write(buf, 0, length);
            }
            osm.close();
        } catch (Exception ex) {
            log.info(ex.getMessage(), ex);
        }
    }
}
