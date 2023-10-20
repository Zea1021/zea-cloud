package com.zea.cloud.basic.bean.entity;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 文件信息
 * @author: jfzou
 * @date: 2023/9/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "file_info")
public class FileInfo implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件名称
     */
    @NotNull(message = "文件名称不能为空")
    @NotEmpty(message = "文件名称不能为空")
    @TableField("file_name")
    private String fileName;

    /**
     * MinIo中访问Url
     */
    @NotNull(message = "MinIo中访问Url不能为空")
    @NotEmpty(message = "MinIo中访问Url不能为空")
    @TableField("minio_url")
    private String minioUrl;
    /**
     * 桶名称
     */
    @NotNull(message = "桶名称不能为空")
    @NotEmpty(message = "桶名称不能为空")
    @TableField("bucket_name")
    private String bucketName;
    /**
     * 文件大小
     */
    @TableField("file_size")
    private Long fileSize;
    /**
     * 上传时间
     */
    @TableField("upload_time")
    private Date uploadTime;
    /**
     * 上传人id
     */
    @NotNull(message = "上传人id不能为空")
    @NotEmpty(message = "上传人id不能为空")
    @TableField("upload_user")
    private Integer uploadUserId;
}
