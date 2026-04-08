package com.city.service;

import java.time.LocalDate;

import com.city.vo.OrderReportVO;
import com.city.vo.SalesTop10ReportVO;
import com.city.vo.TurnoverReportVO;
import com.city.vo.UserReportVO;

import jakarta.servlet.http.HttpServletResponse;

public interface ReportService {

    /**
     * 营业额统计
     * 
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverReport(LocalDate begin, LocalDate end);

    /**
     * 根据时间区间统计用户数量
     * 
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 根据时间区间统计订单数量
     * 
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * 查询指定时间区间内的销量排名top10
     * 
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    /**
     * 导出excel
     * 
     * @param resp
     */
    void exportExcel(HttpServletResponse resp);

}
