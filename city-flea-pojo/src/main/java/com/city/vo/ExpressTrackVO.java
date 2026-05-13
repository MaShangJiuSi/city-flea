package com.city.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpressTrackVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime time;
    private String content;
    private String location;
    private String status;
}
