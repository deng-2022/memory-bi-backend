package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.common.BiResponse;
import com.yupi.springbootinit.model.dto.chart.ChartQueryRequest;
import com.yupi.springbootinit.model.dto.chart.GenChartByAiRequest;
import com.yupi.springbootinit.model.entity.Chart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lenovo
 * @description 针对表【chart(图表信息表)】的数据库操作Service
 * @createDate 2023-10-06 21:49:43
 */
public interface ChartService extends IService<Chart> {
    /**
     * 图表列表
     *
     * @param chartQueryRequest 图表列表参数
     * @param request           request
     * @return 图表列表
     */
    Page<Chart> listChartByPage(ChartQueryRequest chartQueryRequest, HttpServletRequest request);

    /**
     * 智能分析 (同步)
     *
     * @param genChartByAiRequest 智能分析参数
     * @param request             request
     * @return AI分析结果
     */
    BiResponse genChartByAi(GenChartByAiRequest genChartByAiRequest, MultipartFile multipartFile, HttpServletRequest request);

    /**
     * 智能分析 (异步)
     *
     * @param genChartByAiRequest 智能分析参数
     * @param request             request
     * @return AI分析结果
     */
    BiResponse genChartByAiAsync(GenChartByAiRequest genChartByAiRequest, MultipartFile multipartFile, HttpServletRequest request);
}
