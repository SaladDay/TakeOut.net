package cn.saladday.rjTakeOut;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@SpringBootApplication
@ServletComponentScan
public class rjTakeOutApplication {
    public static void main(String[] args) {
        SpringApplication.run(rjTakeOutApplication.class,args);
        log.info("::application starting...");
    }
}
