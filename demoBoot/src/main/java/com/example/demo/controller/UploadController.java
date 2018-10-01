package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/file")
public class UploadController {
    @Value("${dharma.upload.path}")
    String uploadPath;

    @RequestMapping("/upload")
    public  String upload(@RequestParam("dharmaFile")MultipartFile file, ModelMap map){
        if (file.isEmpty()){
            map.put("error","file empty");
            return "upload";
        }

        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID() + suffixName;

        File dest = new File(uploadPath + fileName);
        if (!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest);
            map.put("file" ,fileName);
            return "upload";

        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("error","upload failed");
        return  "upload";
    }


}
