package com.zea.cloud.basic.controller;

import com.zea.cloud.basic.util.Result;
import com.zea.cloud.basic.service.MinIoFileService;
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
    public Result uploadFile(@RequestBody MultipartFile file) {
        if(null == file){
            return Result.errorWithOutData("上传文件不能为空");
        }
        return minIoFileService.uploadFileAndSaveInfoIntoDb(file);
    }

    @GetMapping(value = "/view")
    public Result viewFile(@RequestParam(value = "id") Integer id) {
        return minIoFileService.viewFileById(id);
    }

    @GetMapping(value = "/downLoad")
    public void downLoad(@RequestParam(value = "id") Integer id, HttpServletResponse resp) {
        minIoFileService.downLoadFile(resp, id);
    }

    @GetMapping("/delete")
    public Result removeFileById(@RequestParam(value = "id") Integer id) {
        return minIoFileService.removeFileById(id);
    }
}
