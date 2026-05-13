package com.city.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "city.delivery")
@Data
public class DeliveryProperties {

    private String mode = "express";
    private Boolean riderEnabled = false;
}
