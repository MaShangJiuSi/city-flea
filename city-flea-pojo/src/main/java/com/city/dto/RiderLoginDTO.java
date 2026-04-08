package com.city.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RiderLoginDTO implements Serializable {

    private String phone;
    private String password;
}
