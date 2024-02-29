package com.holy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {
 
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("餐饮系统接口文档") // 接口文档标题
                        .description("这是基于Knife4j OpenApi3的餐饮系统接口文档") // 接口文档简介
                        .version("1.0")  // 接口文档版本
                        .contact(new Contact()
                                .name("HolyWizard") //开发者
                                .email("2354818484@outlook.com") // 开发者联系方式
                        )
                );
    }
}