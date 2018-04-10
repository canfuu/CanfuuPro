package com.canfuu.templet.ss.common.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 图像处理接口
 * @see ImageHandleUtilDemo
 */

public final class ImageHandleUtil {
	private static final String path = "";


	private static final ImageHandle FAST;
	private static final ImageHandle SMOOTH;
	private static final ImageHandle DEFAULT;
	private static final ExecutorService ES;
	private static final Lock L = new ReentrantLock();
	static{
		ES = Executors.newCachedThreadPool();

		DEFAULT = new ImageHandle(Image.SCALE_DEFAULT,true) {
			@Override
			public void handle(String from,String to,double p,int imageType,boolean doCompressed) throws IOException {
				ImageHandleUtil.handle(from, to, p, imageType, doCompressed);
			}
		};
		FAST = new ImageHandle(Image.SCALE_FAST,true) {
			@Override
			public void handle(String from, String to, double p, int imageType, boolean doCompressed) throws IOException {
				ImageHandleUtil.handle(from, to, p, Image.SCALE_FAST,true);
			}
		};
		SMOOTH = new ImageHandle(Image.SCALE_SMOOTH,true) {
			@Override
			public void handle(String from,String to,double p, int imageType, boolean doCompressed) throws IOException {
				ImageHandleUtil.handle(from, to, p, Image.SCALE_SMOOTH,true);
			}
		};
	}
	/**
	 * 平滑处理
	 */
	public static void smooth(String from,String to,double p) throws IOException {
		SMOOTH.handle(from,to,p);
	}

	/**
	 * 快速处理
	 */
	public static void fast(String from,String to,double p) throws IOException {
		FAST.handle(from,to,p);
	}

	/**
	 * Web大礼包处理
	 */
	public static void auto(String from) throws IOException {
        System.out.println(path+from+"="+new File(from).length());
        String suffix = from.substring(from.indexOf("."));
		String prefix = from.replace(suffix,"");

		//原图压缩处理
		FAST.handle(from,prefix+"_YT"+suffix,-1.0);

		//普通图片处理
		SMOOTH.handle(from,prefix+"_PT"+suffix,720.0);

		//缩略图片处理
		SMOOTH.handle(from,prefix+"_ST"+suffix,180.0);
	}

	/**
	 * 自定义处理
	 */
	public static ImageHandle newImageHandle(ImageHandle imageHandle) {
		return imageHandle;
	}

	public abstract static class ImageHandle {
		private int imageType;
		private boolean doCompressed;

		public void handle(String from,String to,double p) throws IOException{
			handle(from, to, p,this.imageType,this.doCompressed);
		}

		/**
		 *
		 * @param from 原图片相对路径
		 * @param to 输出图片相对路径
		 * @param p 图片分辨率尺寸
		 * @param imageType 图片类型，见Image中的int类型常量
		 * @see Image
		 *
		 * @param doCompressed 是否压缩
		 * @throws IOException
		 */
		public abstract void handle(String from,String to,double p,int imageType,boolean doCompressed) throws IOException;
		public ImageHandle(int imageType,boolean doCompressed){
			this.imageType=imageType;
			this.doCompressed=doCompressed;
		}
	}

	private static void handle(String from,String to,double p,int imageType,boolean doCompressed) throws FileNotFoundException {
		String toImage = path + to;
		String fromImage = path + from;
        System.out.println(toImage);
        System.out.println(fromImage);
        if(!new File(fromImage).exists()) {
			throw new FileNotFoundException(fromImage+"不存在");
		}
		new File(to).deleteOnExit();

		ES.submit(() -> {
			try {
				Image img = null;


				Path pp = Paths.get(fromImage);

				L.lock();
				try {
					img = ImageIO.read(Files.newInputStream(pp,CREATE_NEW));
				} catch (IOException ignored) {
				} finally {
					L.unlock();
				}

				int tempH = img.getHeight(null);
				int tempW = img.getWidth(null);

				Double s = (tempH > tempW ? tempW : tempH) / p;
				if (s < 1) {
					s = 1.0;
				}
				int w = ((Double) (img.getWidth(null) / s)).intValue();
				int h = ((Double) (img.getHeight(null) / s)).intValue();
				BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				if (doCompressed) {
					image.getGraphics().drawImage(img.getScaledInstance(w, h, imageType), 0, 0, null);
				}
				String suffix = toImage.substring(toImage.indexOf('.') + 1);
				ImageIO.write(image,suffix,Files.newOutputStream(Paths.get(toImage),CREATE_NEW));
			}catch (IOException ignored){}
		});
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		ES.shutdown();
	}

}
