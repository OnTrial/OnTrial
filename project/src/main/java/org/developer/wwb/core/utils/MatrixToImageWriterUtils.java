package org.developer.wwb.core.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

/**
 * 生成 二维码的工具类
 * 
 * @author Administrator
 *
 */

public final class MatrixToImageWriterUtils {

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	private MatrixToImageWriterUtils() {
	}

	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format " + format + " to " + file);
		}
	}

	public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format " + format);
		}
	}

	/**
	 * 
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param fileName
	 *            要生成的图片名
	 * @param filePath
	 *            生成图片的存放的地址
	 * @param suffix
	 *            后缀名 如（ png）
	 * @param content
	 *            要生成的地址
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void GenerationImg(int width, int height, String fileName, String filePath, String suffix,
			String content) {
		try {
			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
			Map hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			File file1 = new File(filePath, fileName + "." + suffix);
			if (!file1.exists()) {
				try {
					file1.mkdirs();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			MatrixToImageWriterUtils.writeToFile(bitMatrix, suffix, file1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		try {

			String content = "120605181003;http://www.cnblogs.com/jtmjx";
			String path = "d:/";

			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

			Map hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400, hints);
			File file1 = new File(path, "餐巾纸.jpg");
			MatrixToImageWriterUtils.writeToFile(bitMatrix, "jpg", file1);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}