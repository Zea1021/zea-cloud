package com.zea.cloud.basic.controller;

import com.zea.cloud.basic.service.MinIoFileService;
import com.zea.cloud.common.bean.common.Result;
import com.zea.cloud.common.exception.ErrorCode;
import com.zea.cloud.common.utils.ResultUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("minio-file-controller")
public class MinIoFileController {

    @Resource
    private MinIoFileService minIoFileService;

    @PostMapping(value = "/upload")
    public Result<Integer> uploadFile(@RequestBody MultipartFile file) {
        if(null == file){
            return ResultUtil.fail(ErrorCode.BUSINESS_EXCEPTION, "上传文件不能为空!");
        }
        return ResultUtil.success(minIoFileService.uploadFileAndSaveInfoIntoDb(file));
    }

    @GetMapping(value = "/view")
    public Result<String> viewFile(@RequestParam(value = "id") Integer id) {
        return ResultUtil.success(minIoFileService.viewFileById(id));
    }

    @GetMapping(value = "/downLoad")
    public void downLoad(@RequestParam(value = "id") Integer id, HttpServletResponse resp) {
        minIoFileService.downLoadFile(resp, id);
    }

    @GetMapping("/delete")
    public Result<?> deleteFileById(@RequestParam(value = "id") Integer id) {
        minIoFileService.deleteFileById(id);
        return ResultUtil.success();
    }
}
