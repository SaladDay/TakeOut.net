package cn.saladday.rjTakeOut.controller;

import cn.saladday.rjTakeOut.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${rjTakeOut.path}")
    private String imgPath;


    @PostMapping("upload")
    public R<String> uploadImg(MultipartFile file) throws IOException {
        if(file==null)return R.error("no file");
        File dir =  new File(imgPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uuid = String.valueOf(UUID.randomUUID());
        file.transferTo(new File(imgPath+uuid+suffix));
        return R.success(uuid+suffix);
    }

    @GetMapping("download")
    public R download(String name, HttpServletResponse response) throws IOException {
        //文件地址
        String path = imgPath+name;
        FileInputStream is = new FileInputStream(path);
        byte[] bytes = new byte[1024];
        int len;
        ServletOutputStream os = response.getOutputStream();
        while ((len = is.read(bytes))!=-1){
            os.write(bytes,0,len);
            os.flush();
        }
        os.close();
        is.close();
        return R.success("");
    }
}
