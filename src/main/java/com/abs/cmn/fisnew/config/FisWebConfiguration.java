<<<<<<<< HEAD:src/main/java/com/abs/cmn/fisnew/config/FisWebConfiguration.java
package com.abs.cmn.fisnew.config;
========
package com.abs.cmn.fis.config;
>>>>>>>> b6a4ceb96f00e2cd665c4d81fd741ba739051eb0:src/main/java/com/abs/cmn/fis/config/FISWebConfiguration.java

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FisWebConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET,PUT,POST,DELETE").allowedHeaders("*");
    }
}
