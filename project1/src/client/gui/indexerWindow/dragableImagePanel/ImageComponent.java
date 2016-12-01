package client.gui.indexerWindow.dragableImagePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class ImageComponent extends JComponent{
	
	private static Image NULL_IMAGE = new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB);
	Image dragImage;
		
	public ImageComponent(){
		
		this.setBackground(new Color (178, 223,210));
		this.setPreferredSize(new Dimension(700,700));
		this.addMouseListener(mouseAdapter);
		
	}
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			int d_X = e.getX();
			int d_Y = e.getY();
			System.out.println(d_X);
			System.out.println(d_Y);
		}

	};
	
}
