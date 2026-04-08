package com.city.service;

import com.city.vo.BusinessDataVO;
import com.city.vo.GoodsOverViewVO;
import com.city.vo.OrderOverViewVO;

import java.time.LocalDateTime;

public interface WorkspaceService {

    /**
     * 根据时间段统计营业数据
     * 
     * @param begin
     * @param end
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 查询订单管理数据
     * 
     * @return
     */
    OrderOverViewVO getOrderOverView();

    /**
     * 查询物品总览
     * 
     * @return
     */
    GoodsOverViewVO getGoodsOverView();


}
