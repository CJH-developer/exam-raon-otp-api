package com.bandisnc.kobc_raon_otp.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration("ConfigKobcSettings")
@PropertySources({
        @PropertySource("classpath:config/bandisnc.kobc.settings.xml")
})
@ComponentScan({"com.bandisnc"})
public class ConfigKobcSettings {
}
