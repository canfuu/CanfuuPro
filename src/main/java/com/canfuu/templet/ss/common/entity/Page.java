package com.canfuu.templet.ss.common.entity;


import com.canfuu.templet.ss.common.resources.log.CanfuuLogger;
import com.canfuu.templet.ss.common.resources.log.LogLevel;
import com.canfuu.templet.ss.common.utils.StringUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: Page类，作为Controller类的所有参数的父类及返回Report对象中data的夫类
 */
public class Page extends Entity implements Serializable{
	protected String SESSIONID;
	protected String identify;
	protected String datakey;

	public String getDatakey() {
		return datakey;
	}

	public Page setDatakey(String datakey) {
		this.datakey = datakey;
		return this;
	}

	public String getIdentify() {
		return identify;
	}

	public Page setIdentify(String identify) {
		this.identify = identify;
		return this;
	}

	public String getSESSIONID() {
		return SESSIONID;
	}

	public Page setSESSIONID(String SESSIONID) {
		this.SESSIONID = SESSIONID;
		return this;
	}

	public void copyTo(Page page){
		page.setDatakey(this.datakey);
		page.setIdentify(this.identify);
		page.setSESSIONID(this.SESSIONID);
	}
	/**
	 * 本方法仅转换Page类与POJO类名称相同的数据。
	 */
	public POJO newPOJO(Class pojoClass) {
		POJO pojo = null;
		try {
			pojo = (POJO) pojoClass.newInstance();
			HashMap<String, String> pageGetMethod = new HashMap<>(16);
			for (Field field : this.getClass().getDeclaredFields()) {
				pageGetMethod.put(field.getName(), "get" + StringUtil.captureName(field.getName()));
			}
			Field[] pojoFields = pojo.getClass().getDeclaredFields();
            for (int i = 0; i < pojoFields.length; i++) {
                Field f = pojoFields[i];
                f.setAccessible(true);
				String pojoFieldName = pojoFields[i].getName();
				String pageFieldNameTransed = transPageFieldName(pojoFieldName, pojo.getClass());
                if (pageGetMethod.containsKey(pageFieldNameTransed)) {
                    Method setPojoField;
                    setPojoField = pojo.getClass().getDeclaredMethod("set" + StringUtil.captureName(pojoFieldName),f.getType());
					Method getThisField = this.getClass().getDeclaredMethod(pageGetMethod.get(pageFieldNameTransed));
					setPojoField.invoke(pojo, getThisField.invoke(this));
                }
			}
		} catch (Exception e) {
			e.printStackTrace();
			CanfuuLogger.log(e, this, LogLevel.ERROR);
		}
		return pojo;
	}

	private String transPageFieldName(String pojoFieldName, Class POJOClass){
		String profix = StringUtil.upCaptureName(POJOClass.getSimpleName());
		String body = StringUtil.captureName(pojoFieldName);
		return profix+body;
	}

	@Override
	public String toString() {
		return "Page{" +
				"SESSIONID='" + SESSIONID + '\'' +
				", identify='" + identify + '\'' +
				", datakey='" + datakey + '\'' +
				'}';
	}
}
