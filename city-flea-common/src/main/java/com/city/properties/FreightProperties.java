package com.city.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "city.freight")
@Data
public class FreightProperties {

    private Integer defaultWeight = 500;
    private BigDecimal defaultFirstFee = new BigDecimal("8.00");
    private BigDecimal defaultContinuedFee = new BigDecimal("3.00");
}
