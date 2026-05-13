package com.city.service;

import com.city.dto.OrderShipDTO;
import com.city.vo.ExpressTrackVO;
import com.city.vo.ExpressVO;
import java.util.List;

public interface ExpressService {

    void ship(Long orderId, OrderShipDTO shipDTO);

    ExpressVO getExpressInfo(Long orderId);

    List<ExpressTrackVO> getExpressTrack(Long orderId);

    void syncExpressTrack(Long orderId);

    void subscribeTraces(Long orderId);
}
