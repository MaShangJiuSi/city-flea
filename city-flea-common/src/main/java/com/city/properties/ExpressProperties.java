package com.city.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "city.express")
@Data
public class ExpressProperties {

    private String apiUrl = "https://api.kdniao.com";
    private String ebusinessId;
    private String apiKey;
    private String pushUrl;
}
