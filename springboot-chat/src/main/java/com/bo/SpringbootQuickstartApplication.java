package com.bo;

import com.bo.utils.IdWorker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@MapperScan("com.bo.mapper")
@CrossOrigin(origins = "*")
public class SpringbootQuickstartApplication{

    public static void main(String[] args) {
        SpringApplication.run(SpringbootQuickstartApplication.class, args);
    }

    @Bean
    public IdWorker getIdWorker(){
        return new IdWorker();
    }

    @Bean
    public SpringUtil getSpingUtil() {
        return new SpringUtil();
    }

}
