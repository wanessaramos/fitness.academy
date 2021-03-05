package br.fitness.academy.service;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class ImageForFile {
	public static BufferedImage toBufferedImage(Image i) {
	    if (i instanceof BufferedImage) {
	        return (BufferedImage)i;
	    }
	    Image img;
	    img = new ImageIcon(i).getImage();
	    BufferedImage b;
	    b = new BufferedImage(img.getWidth(null),img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	    Graphics g = b.createGraphics();
	    g.drawImage(img, 0, 0, null);
	    g.dispose();
	    return b;
	}

}
