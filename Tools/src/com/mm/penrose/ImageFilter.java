package com.mm.penrose;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * This is a very narrowly focused class. Its purpose is to remove the dark lines from the screen dump of the
 * penrose utility.
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 3/19/14
 * <p>Time: 8:44 PM
 *
 * @author Miguel Mu–oz
 */
public enum ImageFilter {
	;

	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser(System.getProperty("user.home") + "/Desktop");
		chooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				String name = f.getName().toLowerCase();
				return name.endsWith(".jpg") || name.endsWith(".png");
			}

			@Override
			public String getDescription() {
				return "Image files";
			}
		});
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			process(file);
		}
	}

	private static void process(File pFile) {
		try {
			BufferedImage image = ImageIO.read(pFile);
			processImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
1: sat: 0.108   0.108     brt: 0.871   0.871  222-214-198  222-214-198
1: sat: 0.108   0.108     brt: 0.871   0.871  222-214-198  222-214-198
1: sat: 0.108   0.107     brt: 0.871   0.804  222-214-198  205-198-183
1: sat: 0.108   0.091     brt: 0.871   0.086  222-214-198   22- 21- 20
1: sat: 0.108   0.100     brt: 0.871   0.627  222-214-198  160-155-144
1: sat: 0.108   0.105     brt: 0.871   0.937  222-214-198  239-231-214
1: sat: 0.090   0.105     brt: 0.612   0.937  156-152-142  239-231-214
1: sat: 0.081   0.105     brt: 0.145   0.937   37- 36- 34  239-231-214
1: sat: 0.105   0.105     brt: 0.937   0.937  239-231-214  239-231-214
1: sat: 0.105   0.105     brt: 0.937   0.937  239-231-214  239-231-214
1: sat: 0.105   0.105     brt: 0.937   0.937  239-231-214  239-231-214
	
	 */
	private static void processImage(BufferedImage pImage) {
		BufferedImage clone = new BufferedImage(pImage.getWidth(), pImage.getHeight(), pImage.getType());
		WritableRaster cloneRaster = clone.getRaster();
		WritableRaster raster = pImage.getRaster();
		int line1 = 200;
		int line2 = 400;
		int[] pixels = null;
		float[] hsb = new float[3];
		int width = raster.getWidth();
		int height = raster.getHeight();
		for (int ii=0; ii< width; ++ii) {
			pixels = raster.getPixel(ii, line1, pixels);
			int red = pixels[0];
			int grn = pixels[1];
			int blu = pixels[2];
			hsb = Color.RGBtoHSB(red, grn, blu, hsb);
			float sat1 = hsb[1];
			float brt1 = hsb[2];
			pixels = raster.getPixel(ii, line2, pixels);
			hsb = Color.RGBtoHSB(pixels[0], pixels[1], pixels[2], hsb);
			float sat2 = hsb[1];
			float brt2 = hsb[2];
			System.out.printf("1: sat: %5.3f   %5.3f     brt: %5.3f   %5.3f  %3d-%3d-%3d  %3d-%3d-%3d%n", sat1, sat2, brt1, brt2, red, grn, blu, pixels[0], pixels[1], pixels[2]);
		}
		
		int[] clr2 = {239,  231, 214};
		int[] clr1 = {222, 214, 198};
		int[] top=null;
		int[] bot = null;
		int[] pre = null;
		int[] post = null;
		for (int x=0; x< width; ++x) {
			for (int y=0; y< height; ++y) {
				raster.getPixel(x, y, pixels);
				if (!match(clr1, pixels) && !match(clr2, pixels)) {
					top = raster.getPixel(Math.max(0, x-2), y, top);
					bot = raster.getPixel(Math.min(width-1, x+2), y, bot);
					pre = raster.getPixel(x, Math.max(0, y-2), pre);
					post = raster.getPixel(x, Math.min(height-1, y+2), post);
					int highCount = 0;
					int lowCount = 0;
					if (match(top, clr1)) {
						highCount++;
					} else if (match(top, clr2)) {
						lowCount++;
					}
					if (match(bot, clr1)) {
						highCount++;
					} else if (match(bot, clr2)) {
						lowCount++;
					}
					if (match(pre, clr1)) {
						highCount++;
					} else if (match(pre, clr2)) {
						lowCount++;
					}
					if (match(post, clr1)) {
						highCount++;
					} else if (match(post, clr2)) {
						lowCount++;
					}
					if (highCount > lowCount) {
						cloneRaster.setPixel(x, y, clr1);
					} else {
						cloneRaster.setPixel(x, y, clr2);
					}
				} else {
					cloneRaster.setPixel(x, y, pixels);
				}
			}
		}

		try {
			ImageIO.write(clone, "png", new File(System.getProperty("user.home") + "/Desktop/revised.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean match(int[] pClr1, int[] pPixels) {
		for (int ii=0; ii<pClr1.length; ++ii) {
			if (pClr1[ii] != pPixels[ii]) {
				return false;
			}
		}
		return true;
	}
}
