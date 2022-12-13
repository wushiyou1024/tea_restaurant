package com.xmut.tearestaurant.controller;

import com.xmut.tearestaurant.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Bless_Wu
 * @Description 文件上传保存到本地
 * @create 2022-10-29 20:19
 */
@RestController
@RequestMapping("/common")
@Slf4j
@CrossOrigin
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {

        log.info(file.toString());

        //原始文件名
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));

        File dir = new File(basePath);
        //不存在目录需要创建目录
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //使用uuid重新生成文件名
        filename = UUID.randomUUID().toString() + suffix;//suffix为.jpg 拼接uuid
        //file是一个临时文件，需要转存到指定位置
        try {
            file.transferTo(new File(basePath + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(filename);
    }


    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        //输入流 读取文件
        ServletOutputStream outputStream = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流 把文件写回到浏览器
            outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
