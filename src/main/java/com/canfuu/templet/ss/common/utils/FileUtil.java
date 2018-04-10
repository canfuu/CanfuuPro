package com.canfuu.templet.ss.common.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 文件工具类用于处理文件操作
 */
public class FileUtil {
    HttpServletRequest servletRequest;

    public static String createFileName(String name){
        if (name.indexOf('.') == -1)
            return null;
        return DateUtil.getSystemTimeInMM()
                + CodeUtil.getRandom(4)
                + name.substring(name.indexOf('.'),name.length());
    }
}
