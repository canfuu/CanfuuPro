package com.canfuu.templet.ss.common.resources.exceptions.common;


import com.canfuu.templet.ss.common.utils.ExceptionUtil;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 通用Exception
 */
public class PageIsNullException extends Exception{
	public PageIsNullException(){
		super(ExceptionUtil.explainError("Page对象为空","请检查参数"));
	}
}
