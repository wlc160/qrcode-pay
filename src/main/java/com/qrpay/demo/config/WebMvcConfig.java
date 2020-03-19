package com.qrpay.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;



/**
 * Created by PC on 2019/3/29.
 */

/**
 * 说明：虚拟路径映射
 * 工程名:scjw
 * 包名:com.scjw.config
 * 文件名:BaseConfig.java
 *
 * @author yqou
 * @version $Id: BaseConfig.java 2019年6月11日 下午4:51:02 $
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Value("${files.upload-path}")
    private String imagePath;

    @Value("${files.mapping-path}")
    private String image;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(image + "/**").
                addResourceLocations("file:/" + imagePath + "/");

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }
}
