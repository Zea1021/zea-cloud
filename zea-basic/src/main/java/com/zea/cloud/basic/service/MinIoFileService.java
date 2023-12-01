package com.zea.cloud.basic.service;

import com.zea.cloud.common.bean.common.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MinIoFileService {

    Integer uploadFileAndSaveInfoIntoDb(MultipartFile file);

    String viewFileById(Integer id);

    void downLoadFile(HttpServletResponse resp, Integer id);

    void deleteFileById(Integer id);
}
