package com.zea.cloud.basic.service;

import com.zea.cloud.basic.util.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MinIoFileService {

    Result uploadFileAndSaveInfoIntoDb(MultipartFile file);

    Result viewFileById(Integer id);

    void downLoadFile(HttpServletResponse resp, Integer id);

    Result removeFileById(Integer id);
}
