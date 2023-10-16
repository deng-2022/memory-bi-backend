package com.yupi.springbootinit.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.BiResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.constant.AiConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.manager.AiManager;
import com.yupi.springbootinit.model.dto.chart.ChartQueryRequest;
import com.yupi.springbootinit.model.dto.chart.GenChartByAiRequest;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.model.enums.FileUploadBizEnum;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.service.ChartService;
import com.yupi.springbootinit.service.UserService;
import com.yupi.springbootinit.utils.ExcelUtils;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 文件接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {
    @Resource
    private ChartService chartService;

    /**
     * 智能分析 (同步)
     *
     * @param multipartFile       Excel文件
     * @param genChartByAiRequest 指定图表信息
     * @param request             request
     * @return 生成的图表信息
     */
    @PostMapping("/generate")
    public com.yupi.springbootinit.common.BaseResponse<BiResponse> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                                                                GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        // 1.controller层对请求参数的校验
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();

        // 打上注释
        ThrowUtils.throwIf(StringUtils.isAllBlank(name, goal, chartType),
                ErrorCode.PARAMS_ERROR, "请求参数不能为空");

        BiResponse biResponse = chartService.genChartByAi(genChartByAiRequest, multipartFile, request);

        return ResultUtils.success(biResponse);
    }

    /**
     * 智能分析 (异步)
     *
     * @param multipartFile       Excel文件
     * @param genChartByAiRequest 指定图表信息
     * @param request             request
     * @return 生成的图表信息
     */
    @PostMapping("/generate/async")
    public com.yupi.springbootinit.common.BaseResponse<BiResponse> genChartByAiAsync(@RequestPart("file") MultipartFile multipartFile,
                                                                                     GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        // 1.controller层对请求参数的校验
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();

        // 打上注释
        ThrowUtils.throwIf(StringUtils.isAllBlank(name, goal, chartType),
                ErrorCode.PARAMS_ERROR, "请求参数不能为空");

        BiResponse biResponse = chartService.genChartByAiAsync(genChartByAiRequest, multipartFile, request);

        return ResultUtils.success(biResponse);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/page")
    public BaseResponse<Page<Chart>> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
                                                     HttpServletRequest request) {
        // 1.controller层对请求参数的校验
        ThrowUtils.throwIf(chartQueryRequest == null, ErrorCode.PARAMS_ERROR, "请求参数为空");

        long pageSize = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);

        Page<Chart> chartPage = chartService.listChartByPage(chartQueryRequest, request);
        return ResultUtils.success(chartPage);
    }
}
