package com.canfuu.templet.ss.common.utils;

import java.awt.*;
import java.io.IOException;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 图像处理工具类使用放啊
 */
public class ImageHandleUtilDemo {
	public static void main(String[] args) throws IOException, InterruptedException {
		/**
		 * auto方法是web环境下一键生成的工具方法（推荐）
		 * 本方法将在原目录下会生成三个图片
		 * 1. TestImage_YT.jpg 原图片清晰度 与原图分辨率相同 使用无损压缩处理
		 * 2. TestImage_PT.jpg 普通图清晰度 大小在90-120k之间的图片 （若原图尺寸小于比例缩小后尺寸则此图片分辨率与原图相同）
		 * 3. TestImage_ST.jpg 缩略图清晰度 大小在10k左右 模糊缩略图可用作小图展示
		 */

		ImageHandleUtil.auto("image/TestImage.jpg");

		/**
		 * fast方法可以快速处理一张图片
		 * smooth方法可以平滑处理一张图片
		 * 第一个参数为目标文件相对路径
		 * 第二个参数为存储文件相对路径
		 * 第三个参数为分辨率系数，它总等于图片处理完成后长宽中最小的数值（当参数为负数或小于原图长宽中最小的数值时，输出图片为原尺寸）
		 * 分辨率系数与生成文件匹配表
		 * 30p -> 1kb
		 * 80p -> 3kb
		 * 180p -> 9kb
		 * 720p -> 70kb
		 * 900p -> 100kb
		 * 1080p -> 140kb
		 * 1280p -> 180kb
		 * 1800p -> 300kb
		 */
		ImageHandleUtil.fast("image/TestImage.jpg","image/TestImage1.jpg",720);
		ImageHandleUtil.smooth("image/TestImage.jpg","image/TestImage1.jpg",720);

		/**
		 * 此方法可以自定义图片处理过程
		 * 需注意new ImageHandleUtil.ImageHandle构造方法的两个参数的初始化(第一个为压缩算法，见Image类中的常量，第二个参数是是否压缩)
		 */
		ImageHandleUtil.newImageHandle(new ImageHandleUtil.ImageHandle(Image.SCALE_FAST,true) {
			/**
			 *
			 * @param from 原图片相对路径
			 * @param to 输出图片相对路径
			 * @param p 图片分辨率尺寸
			 * @param imageType 图片类型，见Image中的int类型常量
			 * @see Image
			 * @param doCompressed 是否压缩
			 * @throws IOException
			 */
			@Override
			public void handle(String from, String to, double p, int imageType, boolean doCompressed) throws IOException {

			}
		});
	}
}
