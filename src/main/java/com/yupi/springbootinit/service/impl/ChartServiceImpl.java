package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.common.BiResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.constant.AiConstant;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.manager.AiManager;
import com.yupi.springbootinit.mapper.ChartMapper;
import com.yupi.springbootinit.model.dto.chart.ChartQueryRequest;
import com.yupi.springbootinit.model.dto.chart.GenChartByAiRequest;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.service.ChartService;
import com.yupi.springbootinit.service.UserService;
import com.yupi.springbootinit.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Lenovo
 * @description 针对表【chart(图表信息表)】的数据库操作Service实现
 * @createDate 2023-10-06 21:49:43
 */
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
        implements ChartService {
    @Resource
    private UserService userService;

    @Resource
    private AiManager aiManager;

    /**
     * 图表列表
     *
     * @param chartQueryRequest 图表列表参数
     * @param request           request
     * @return 图表列表
     */
    @Override
    public Page<Chart> listChartByPage(ChartQueryRequest chartQueryRequest, HttpServletRequest request) {
        long current = chartQueryRequest.getCurrent();
        long pageSize = chartQueryRequest.getPageSize();

        Page<Chart> chartPage = new Page<>(current, pageSize);

        QueryWrapper<Chart> cqw = new QueryWrapper<>();

        // todo 封装一个 getChartEqParams, 根据请求参数, 查询对应图表
        String name = chartQueryRequest.getName();
        // 匹配 name
        if (!StringUtils.isAnyBlank(name) && name.length() < 20)
            cqw.like("name", name);

        return page(chartPage, cqw);
    }

    /**
     * 智能分析
     *
     * @param genChartByAiRequest 智能分析参数
     * @param request             request
     * @return AI分析结果
     */
    @Override
    public BiResponse genChartByAi(GenChartByAiRequest genChartByAiRequest, MultipartFile multipartFile, HttpServletRequest request) {
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        // 校验登录
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR, "请先登录后再尝试调用接口");

        // 2.提取图表名信息、分析需求(分析目标 图表类型)，做好参数校验
        StringBuilder userInput = new StringBuilder();
        // 2.1.校验图表名信息
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");
        // 2.2.校验分析目标
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        // 2.2.校验图表类型
        ThrowUtils.throwIf(StringUtils.isBlank(chartType), ErrorCode.PARAMS_ERROR, "目标为空");

        // 3.分析Excel图表，获取原始数据
        String excelToCsv = ExcelUtils.excelToCsv(multipartFile);
        userInput.append("\n")
                .append("分析需求:").append("\n")
                .append(goal).append(", ").append("请生成一张").append(chartType).append("\n")
                .append("原始数据:").append("\n")
                .append(excelToCsv);

        // 4.执行AI接口调用
        String result = aiManager.doChat(AiConstant.BI_MODEL_ID, userInput.toString());

        // 5.处理AI响应的对话信息
        String[] split = result.split("【【【【【");
        String genChart = split[1];
        String genResult = split[2];

        // 6.存储生成的分析结果
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(excelToCsv);
        chart.setChartType(chartType);
        chart.setGenChart(genChart);
        chart.setGenResult(genResult);
        chart.setUserId(loginUser.getId());

        boolean save = save(chart);
        ThrowUtils.throwIf(!save, ErrorCode.SYSTEM_ERROR, "添加图表信息失败");

        // 7.封装分析结果并返回
        BiResponse biResponse = new BiResponse();
        biResponse.setUserId(loginUser.getId());
        biResponse.setGenChart(genChart);
        biResponse.setGenResult(genResult);

        return biResponse;
    }
}




