package com.nxlh.manager.controller;

import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.FileUtils;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.common.utils.OSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@ApiController("/api/file")
public class FileController extends BaseController {


    @Autowired
    @Lazy
    private OSSUtils ossUtils;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("upload")
    public MyResult upload(@RequestParam() MultipartFile file) throws Exception {
        var fileName = IDUtils.genUUID() + "." + FileUtils.getExtensionName(file.getOriginalFilename());
        var result = this.ossUtils.uploadImage(file.getInputStream(), fileName);
        return result;
    }


    /**
     * 富文本上传图片
     *
     * @return
     */
    @RequestMapping("uploadbyum")
    public MyResult uploadByUMEditor(@RequestParam MultipartFile upfile) throws IOException {
        var fileName = IDUtils.genUUID() + "." + FileUtils.getExtensionName(upfile.getOriginalFilename());
        var result = this.ossUtils.uploadImage(upfile.getInputStream(), fileName);
        var map = new HashMap<String, String>();
        if (result.getStatus() == 200) {
            map.put("url", result.getData().toString());
            map.put("state", "SUCCESS");
            map.put("title", upfile.getOriginalFilename()+".jpg");
            map.put("original", upfile.getOriginalFilename()+".jpg");

        } else {
            map.put("state", "FAIL");
            map.put("error","上传失败");
        }
        return ok(map);

    }

}
