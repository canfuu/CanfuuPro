package com.canfuu.templet.ss.common.config;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu JiangYuan
 * @version v1.0: 常量类
 */
public final class Constant {
    public static final String BaseURL = "/data/img";


    public static final String OfficialImageUrl = "/var/www/official/";  //正式服务器图片地址


    /**
     * 短信模板
     */
    public static final String SMS_RED_PACKET = "SMS_127152165";  //大愚加盟红包短信
    public static final String SMS_TO_ZHUANYE = "SMS_127162160";  //大愚科技官网留言提醒模板ID SMS_127162160 -- 庄叶提醒
    public static final String SMS_TO_XIAOAN = "SMS_127162152";  //大愚科技官网留言提醒模板ID SMS_127162152 -- 刘小安提醒

    /**
     * 缓存设置
     */
    public static final Integer SESSION_REFLUSH_TIMES = 60*60*24*3;//Session刷新时间
    public static final Integer CACHE_TIMES = 60*60*24*3;//缓存数据刷新时间
    public static final String MYBATIS_MAPPING_PATH = "/resources/canfuu/mapping";
    public static boolean CACHE = true; //缓存是否开启
    public static boolean SESSION = true; //Session是否开启
    public static boolean FLUSH_CACHE_DB = true; //重启项目时缓存是否刷新

    public static final String CONDITION_RESOURCE_CACHE = "CONDITION_RESOURCE_CACHE"; //查询条件缓存库名称
    public static final String PAGE_RESOURCE_CACHE = "PAGE_RESOURCE_CACHE"; //Web页面对象缓存库名称
    public static final String SESSION_RESOURCE_CACHE = "SESSION_RESOURCE_CACHE"; //session
    public static final String TABLE_RESOURCE_CACHE = "TABLE_RESOURCE_CACHE";
    public static final String DEFAULT_PAGE = "DEFAULT";



//    public static final Integer TEMPLATE_ID = 67083;  //短信模板ID

    public static final String RESUME_MOBILE = "15652922740";  //短信模板ID

    public static final String REQUIREMENT_MOBILE = "18811503605";
}
