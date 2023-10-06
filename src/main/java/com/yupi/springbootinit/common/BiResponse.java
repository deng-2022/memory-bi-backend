package com.yupi.springbootinit.common;

import lombok.Data;

/**
 * @author 邓哈哈
 * 2023/10/6 21:42
 * Function: 封装 AI 生成的图表分析结果
 * Version 1.0
 */

@Data
public class BiResponse {
    /**
     * 调用用户id
     */
    private long userId;

    /**
     * 生成图表代码
     */
    private String genChart;

    /**
     * 生成分析结论
     */
    private String genResult;
}
