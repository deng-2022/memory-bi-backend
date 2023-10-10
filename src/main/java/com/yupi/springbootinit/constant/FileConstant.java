package com.yupi.springbootinit.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 文件常量
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface FileConstant {

    /**
     * COS 访问地址
     * todo 需替换配置
     */
    String COS_HOST = "https://yupi.icu";

    /**
     * 允许上传的文件大小
     */
    long ONE_MB = 1024 * 1024L;


    /**
     * 合法的文件后缀
     */
    List<String> VALID_FILE_SUFFIX_LIST = Arrays.asList("xlsx", "xls");
}
