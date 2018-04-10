package com.canfuu.templet.ss.common.resources.exceptions.common;


import com.canfuu.templet.ss.common.utils.ExceptionUtil;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 通用Exception
 */
public class BeanNotFoundException extends Exception {
	public BeanNotFoundException(Class c){
		super(ExceptionUtil.explainError("bean未找到","请去resources.canfuu.spring包中配置类"+c.getName()+"的bean"));
	}
	public BeanNotFoundException(String c){
		super(ExceptionUtil.explainError("bean未找到","情趣resources.canfuu.spring包中配置名为"+c+"的bean"));
	}
}
