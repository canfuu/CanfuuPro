package com.canfuu.templet.ss.common.entity;



import com.canfuu.templet.ss.common.resources.log.CanfuuLogger;
import com.canfuu.templet.ss.common.resources.log.LogLevel;

import java.io.Serializable;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 数据反馈模板
 */
public class Report implements Serializable{

	private Integer status;
	private Object data;
	private Object msg;

	public Report(Integer status, Object object) {
		this.status=status;
		if(status==null){
			this.status=3;
		} else if(status==0){
			data=object;
		}else {
			msg=object;
		}
	}

	public boolean isSuccessful(){
		try{

			return status==0;
		} catch(Exception e){


			CanfuuLogger.log(e,this, LogLevel.ERROR);

		   return false;
		}
	}
	public Integer getStatus() {
		return status;
	}

	public Report setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public Object getData() {
		return data;
	}

	public Report setData(Object data) {
		this.data = data;
		return this;
	}

	public Object getMsg() {
		return msg;
	}

	public Report setMsg(Object msg) {
		this.msg = msg;
		return this;
	}

	@Override
	public String toString() {
		return "Report{" +
				"status=" + status +
				", data=" + data +
				", msg=" + msg +
				'}';
	}
}
