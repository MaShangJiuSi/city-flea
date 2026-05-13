package com.city.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ExpressCompanyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String name;
    private String logo;
    private String contactPhone;
    private Integer sort;
    private Integer enabled;
    private Integer isDefault;
}
