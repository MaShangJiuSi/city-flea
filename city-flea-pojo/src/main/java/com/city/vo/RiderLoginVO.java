package com.city.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiderLoginVO implements Serializable {

    private Long id;
    private String realName;
    private String phone;
    private Integer workStatus;
    private String token;
}
