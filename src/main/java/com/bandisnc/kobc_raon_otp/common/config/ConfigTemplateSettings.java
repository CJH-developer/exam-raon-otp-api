package com.bandisnc.kobc_raon_otp.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigTemplateSettings {
    @Bean
    public RestTemplate restTemplate() {return new RestTemplate();}
}
