package com.canfuu.templet.ss.common.config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: Gson配置类
 */
@Configuration
public class GsonConfiguration {

	@Bean("gsonBuilder")
	public GsonBuilder gsonBuilder(){
		GsonBuilder gsonBuilder = new GsonBuilder();
		ExclusionStrategy myExclusionStrategy = new ExclusionStrategy() {
			@Override
			public boolean shouldSkipField(FieldAttributes fa) {
				return fa.getName().equals("identify") || fa.getName().equals("datakey");
			}

			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		};
		gsonBuilder.addSerializationExclusionStrategy(myExclusionStrategy);
		gsonBuilder.setDateFormat("yyyy-MM-dd");

		return gsonBuilder;
	}

}
