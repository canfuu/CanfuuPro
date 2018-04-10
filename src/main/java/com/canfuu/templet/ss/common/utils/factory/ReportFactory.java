package com.canfuu.templet.ss.common.utils.factory;


import com.canfuu.templet.ss.common.entity.Report;
import com.canfuu.templet.ss.common.utils.CommonUtil;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: ReportFactory
 */
public final class ReportFactory {
	public static Report getReport(Integer i){
		Report report;
		try{
			report = (Report) CommonUtil.WAC.getBean("report_"+i);
			if(report==null){
				return (Report) CommonUtil.WAC.getBean("report_"+2);
			} else {
				return report;
			}
		} catch(Exception e){
			return (Report) CommonUtil.WAC.getBean("report_"+2);
		}
	}

}
