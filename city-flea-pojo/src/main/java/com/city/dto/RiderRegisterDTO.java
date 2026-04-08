package com.city.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RiderRegisterDTO implements Serializable {

    private String realName;
    private String phone;
    private String idCard;
    private String avatar;
    private String password;
    private String idCardFront;
    private String idCardBack;
    private String healthCard;
}
