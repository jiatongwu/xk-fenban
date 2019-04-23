package cn.xvkang.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
@Configuration
public class CaptchaConfig {
	@Bean
	public Producer defaultKaptcha() {
		/*
		 * Kaptcha可配置项
配置项 	描述 	默认值 	可选值
kaptcha.border 	是否有边框 	默认为yes 	yes,no
kaptcha.border.color 	边框颜色 	默认为Color.BLACK 	
kaptcha.border.thickness 	边框粗细度 	默认为1 	
kaptcha.producer.impl 	验证码生成器 	默认为DefaultKaptcha 	
kaptcha.textproducer.impl 	验证码文本生成器 	默认为DefaultTextCreator 	
kaptcha.textproducer.char.string 	验证码文本字符内容范围 		
kaptcha.textproducer.char.length 	验证码文本字符长度 	默认为5 	
kaptcha.textproducer.font.names 	验证码文本字体样式 		
kaptcha.textproducer.font.size 	验证码文本字符大小 	默认为40 	
kaptcha.textproducer.font.color 	验证码文本字符颜色 	默认为Color.BLACK 	
kaptcha.textproducer.char.space 	验证码文本字符间距 	默认为2 	
kaptcha.noise.impl 	验证码噪点生成对象 	默认为DefaultNoise 	
kaptcha.noise.color 	验证码噪点颜色 	默认为Color.BLACK 	
kaptcha.obscurificator.impl 	验证码样式引擎 	默认为WaterRipple 	
kaptcha.word.impl 	验证码文本字符渲染 	默认为DefaultWordRenderer 	
kaptcha.background.impl 	验证码背景生成器 	默认为DefaultBackground 	
kaptcha.background.clear.from 	验证码背景颜色渐进 	默认为Color.LIGHT_GRAY 	
kaptcha.background.clear.to 	验证码背景颜色渐进 	默认为Color.WHITE 	
kaptcha.image.width 	验证码图片宽度 	默认为200 	
kaptcha.image.height 	验证码图片高度 	默认为50

图片样式：
水纹 com.google.code.kaptcha.impl.WaterRipple
鱼眼 com.google.code.kaptcha.impl.FishEyeGimpy
阴影 com.google.code.kaptcha.impl.ShadowGimpy

		 */
		DefaultKaptcha defaultKaptcha=new DefaultKaptcha();
		Properties properties=new Properties();
		properties.put("kaptcha.border","yes");
		properties.put("kaptcha.border.color","105,179,90");
		properties.put("kaptcha.textproducer.font.color","blue");
		properties.put("kaptcha.image.width",80);
		properties.put("kaptcha.image.height",30);

		properties.put("kaptcha.textproducer.font.size",27);
		properties.put("kaptcha.textproducer.char.space", "2");
		properties.put("kaptcha.session.key","code");
		properties.put("kaptcha.textproducer.char.length",3);
		properties.put("kaptcha.textproducer.font.names","宋体,楷体,微软雅黑");
		properties.put("kaptcha.textproducer.char.string","abcdefghigklmnopqrstuvwxyz1234567890ABCDEFGHIGKLMNOPQRSTUVWXYZ");
		properties.put("kaptcha.obscurificator.impl","com.google.code.kaptcha.impl.WaterRipple");
		properties.put("kaptcha.noise.color","white");
		//properties.put("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");
		properties.put("kaptcha.background.clear.from","white");
		properties.put("kaptcha.background.clear.to","white");
		Config config=new Config(properties);
		defaultKaptcha.setConfig(config);
		return defaultKaptcha;
	}


}
