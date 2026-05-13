package com.city.service;

import com.city.dto.RiderLoginDTO;
import com.city.dto.RiderRegisterDTO;
import com.city.vo.RiderLoginVO;
import com.city.vo.RiderVO;

@Deprecated
public interface RiderService {

    RiderLoginVO login(RiderLoginDTO riderLoginDTO);

    void register(RiderRegisterDTO riderRegisterDTO);

    void updateWorkStatus(Long riderId, Integer workStatus);

    RiderVO getRiderInfo(Long riderId);
}
