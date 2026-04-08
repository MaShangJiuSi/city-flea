package com.city.controller.admin;

import com.city.result.Result;
import com.city.service.WorkspaceService;
import com.city.vo.BusinessDataVO;
import com.city.vo.GoodsOverViewVO;
import com.city.vo.OrderOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 工作台
 */
@RestController
@RequestMapping("/admin/workspace")
public class WorkSpaceController {

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 工作台今日数据查询
     * 
     * @return
     */
    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessData() {
        // 获得当天的开始时间
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        // 获得当天的结束时间
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);

        BusinessDataVO businessDataVO = workspaceService.getBusinessData(begin, end);
        return Result.success(businessDataVO);
    }

    /**
     * 查询订单管理数据
     * 
     * @return
     */
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> orderOverView() {
        return Result.success(workspaceService.getOrderOverView());
    }

    /**
     * 查询物品总览
     * 
     * @return
     */
    @GetMapping("/overviewGoodses")
    public Result<GoodsOverViewVO> goodsOverView() {
        return Result.success(workspaceService.getGoodsOverView());
    }
}
