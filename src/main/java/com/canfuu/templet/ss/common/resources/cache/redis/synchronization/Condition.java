package com.canfuu.templet.ss.common.resources.cache.redis.synchronization;

import com.canfuu.templet.ss.common.entity.Page;

import java.io.Serializable;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 查询条件缓存类
 */
public class Condition implements Serializable{
	private Page page;

	public Condition(Page page) {
		this.page = page;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}


	@Override
	public String toString() {
		return "Condition{" +
				"page=" + page +
				'}';
	}
}
