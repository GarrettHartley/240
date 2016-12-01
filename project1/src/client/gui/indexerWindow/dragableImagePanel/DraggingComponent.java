package client.gui.indexerWindow.dragableImagePanel;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.font.*;
import java.awt.event.*;

import javax.imageio.*;
import javax.swing.*;

import shared.modelClasses.Field;
import client.ClientFacade;
import client.gui.BatchState;
import client.gui.BatchStateListener;
import client.gui.indexerWindow.tableEntry.TableEntryCell;

import java.util.*;
import java.io.*;
import java.net.URL;


@SuppressWarnings("serial")
public class DraggingComponent extends JComponent implements BatchStateListener {

	private static BufferedImage NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

	private int w_translateX;
	private int w_translateY;
	private int wm_translateX;
	private int wm_translateY;
	private double scale;
	boolean imgInverted = false;
	int xOffset;
	int yOffset;
	int currentLocX;
	int currentLocY;
	BufferedImage batch;
	private boolean dragging;
	private int w_dragStartX;
	private int w_dragStartY;
	private int w_dragStartTranslateX;
	private int w_dragStartTranslateY;
	private double highlightLocationX;
	private double highlightLocationY;
	private double highlightWidth;
	private double highlightHeight;
	private AffineTransform dragTransform;
	private ArrayList<DrawingShape> shapes;
	private Font font;
	private BasicStroke stroke;
	private ClientFacade clientFacade;
	private BatchState batchState;
	private ArrayList<DragImageListener> listeners;
	private DragImage batchComponent;
	DraggingComponent self;

	public DraggingComponent(){
		w_translateX = 0;
		w_translateY = 0;
		wm_translateX = 0;
		wm_translateY = 0;
		xOffset=0;
		yOffset=0;
		scale = 1.0;
		initDrag();

		shapes = new ArrayList<DrawingShape>();
		font = new Font("SansSerif", Font.PLAIN, 72);
		stroke = new BasicStroke(5);

		this.setBackground(new Color(229, 255, 204));
		this.setPreferredSize(new Dimension(700, 700));
		this.setMinimumSize(new Dimension(100, 100));
		this.setMaximumSize(new Dimension(1000, 1000));
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addMouseWheelListener(mouseAdapter);

	}

	public DraggingComponent(BatchState batchState, int x, int y) {
		self = this;
		this.batchState = batchState;
		batchState.addUpdateSelectedListener(this);
		batchState.addUpdateImageListener(this);

		xOffset = x;
		yOffset = y;

		w_translateX = 0;
		w_translateY = 0;
		scale = batchState.getZoom();
		initDrag();

		shapes = new ArrayList<DrawingShape>();
		font = new Font("SansSerif", Font.PLAIN, 72);
		stroke = new BasicStroke(5);

		listeners = new ArrayList<DragImageListener>();

		this.setBackground(new Color(229, 255, 204));
		this.setPreferredSize(new Dimension(700, 700));
		this.setMinimumSize(new Dimension(100, 100));
		this.setMaximumSize(new Dimension(1000, 1000));

		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addMouseWheelListener(mouseAdapter);

		batch = loadImage(batchState.getFullImgURL());
		batchComponent = new DragImage(batch, new Rectangle2D.Double(xOffset, yOffset, batch.getWidth(null)*.5 , batch.getHeight(null) *.5 ));
		shapes.add(batchComponent);

		imgInverted = batchState.isImgInverted();

		if(imgInverted==true){
			initialInvert();
		}
	}

	private void initDrag() {
		dragging = false;
		if(batchState!=null){
			w_dragStartX = -batchState.getImageXLoc();
			w_dragStartY =-batchState.getImageYLoc();
			w_dragStartTranslateX = -batchState.getImageYLoc();
			w_dragStartTranslateY = -batchState.getImageXLoc();
		}
		else{
			w_dragStartX = 0;
			w_dragStartY = 0;
			w_dragStartTranslateX = 0;
			w_dragStartTranslateY = 0;
		}
		dragTransform = null;
	}	

	private BufferedImage loadImage(String imageFile) {
		try {
			return ImageIO.read(new URL(imageFile));
		}
		catch (IOException e) {
			return NULL_IMAGE;
		}
	}

	public void setScale(double newScale) {
		scale = newScale;
		this.repaint();
	}


	public void addDragImageListener(DragImageListener listener) {
		listeners.add(listener);
	}

	public void initialInvert(){
		batchState.setImgInverted(imgInverted);
		BufferedImage result = batchComponent.getImage();
		RescaleOp op = new RescaleOp(-1.0f, 255f, null);
		result = op.filter(batchComponent.getImage(), null);
		batchComponent.setImage(result);
		this.repaint();
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		drawBackground(g2);

		g2.translate(getWidth()/2.0, getHeight()/2);
		g2.scale(scale,scale);
		g2.translate(-getWidth()/2+w_translateX, -getHeight()/2+w_translateY);

		drawShapes(g2);
	}

	private void drawBackground(Graphics2D g2) {
		g2.setColor(getBackground());
		g2.fillRect(0,  0, getWidth(), getHeight());
	}

	private void drawShapes(Graphics2D g2) {
		for (DrawingShape shape : shapes) {
			shape.draw(g2);
		}
	}

	public TableEntryCell findInTable(int x, int y){

		double minY = (batchState.getFirstYCoord()*.5);
		double maxY = (batchState.getRecordHeight()*batchState.getNumberOfRecords()*.5)+minY;
		double minX = (batchState.getFields().get(0).getXcoord()*.5);
		double maxX = ((batchState.getFields().get((batchState.getNumberOfFields()-1)).getXcoord())*.5+
				(batchState.getFields().get((batchState.getNumberOfFields()-1)).getWidth())*.5);

		if(x>minX && x<maxX && y>minY && y<maxY){				
			int record = findRecord( (int) Math.round(minY),y);
			int col = findCol((int) Math.round(minX),x);
			TableEntryCell result = new TableEntryCell("",record-1,col);
			return result;
		}

		return null;
	}

	public int findRecord(int minY,int y){

		int height = minY;
		int count = 1;
		while(y/height>0){
			height+=batchState.getRecordHeight()*.5;
			count++;
		}

		return count-1;
	}

	public int findCol(int minX,int x){
		int width = minX;
		int count = 0;
		while(x/width>0){
			width+=batchState.getFields().get(count).getWidth()*.5;
			count++;
		}
		return count;
	}

	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {

			int d_X = e.getX();
			int d_Y = e.getY();
			AffineTransform transform = new AffineTransform();
			
			transform.translate(getWidth()/2.0, getHeight()/2);
			transform.scale(scale,scale);
			transform.translate(-getWidth()/2+w_translateX, -getHeight()/2+w_translateY);

			Point2D start_Pt = new Point2D.Double(batchState.getFirstYCoord()*.5,batchState.getFields().get(0).getXcoord()*.5);
			Point2D end_Pt = new Point2D.Double();

			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();

			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
				transform.inverseTransform(start_Pt, end_Pt);
			}
			catch (NoninvertibleTransformException ex) {
				return;
			}

			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
	
			int endR_X = (int) end_Pt.getX();
			int endR_Y = (int) end_Pt.getY();
			boolean hitShape = false;

			Graphics2D g2 = (Graphics2D)getGraphics();
			for (DrawingShape shape : shapes) {
				if (shape.contains(g2, w_X, w_Y)) {
					//Draw the shape		
					TableEntryCell selectedCell= findInTable( w_X-xOffset, w_Y-yOffset);
					batchState.updateSelected(selectedCell);

					repaint();
					hitShape = true;
					break;
				}
			}

			if (hitShape) {
				dragging = true;		
				w_dragStartX = w_X;
				w_dragStartY = w_Y;		
				w_dragStartTranslateX = w_translateX;
				w_dragStartTranslateY = w_translateY;
				dragTransform = transform;
			}

		}

		@Override
		public void mouseDragged(MouseEvent e) {		
			if (dragging) {
				int d_X = e.getX();
				int d_Y = e.getY();

				Point2D d_Pt = new Point2D.Double(d_X, d_Y);
				Point2D w_Pt = new Point2D.Double();
				try
				{
					dragTransform.inverseTransform(d_Pt, w_Pt);
				}
				catch (NoninvertibleTransformException ex) {
					return;
				}
				int w_X = (int)w_Pt.getX();
				int w_Y = (int)w_Pt.getY();

				int w_deltaX = w_X - w_dragStartX;
				int w_deltaY = w_Y - w_dragStartY;

				w_translateX = w_dragStartTranslateX + w_deltaX;
				w_translateY = w_dragStartTranslateY + w_deltaY;

				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {

			if(dragTransform!=null){
				int d_X = e.getX();
				int d_Y = e.getY();

				Point2D d_Pt = new Point2D.Double(d_X, d_Y);
				Point2D w_Pt = new Point2D.Double();
				try
				{
					dragTransform.inverseTransform(d_Pt, w_Pt);
				}
				catch (NoninvertibleTransformException ex) {
					return;
				}
				int w_X = (int)w_Pt.getX();
				int w_Y = (int)w_Pt.getY();

				int w_deltaX = w_X - w_dragStartX;
				int w_deltaY = w_Y - w_dragStartY;

				w_translateX = w_dragStartTranslateX + w_deltaX;
				w_translateY = w_dragStartTranslateY + w_deltaY;
			}

			batchState.setImageYLoc(w_translateX+xOffset);
			batchState.setImageXLoc(w_translateY+yOffset);

			System.out.println("@@@@X: "+batchState.getImageXLoc());
			System.out.println("@@@@Y: "+batchState.getImageYLoc());

			initDrag();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			int scrollAmount = 0;
			if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
				scrollAmount = e.getUnitsToScroll();
			} else {
				scrollAmount = e.getWheelRotation();
			}

			if(scrollAmount>0 && scale < 5){
				scale += .5;
			}
			else if(scrollAmount<0 && scale >.5){
				scale -=.5;
			}
			repaint();
		}	
	};

	/////////////////
	// Drawing Shape
	/////////////////

	interface DrawingShape {
		boolean contains(Graphics2D g2, double x, double y);
		void draw(Graphics2D g2);
		Rectangle2D getBounds(Graphics2D g2);
	}

	class DrawingRect implements DrawingShape {

		private Rectangle2D rect;
		private Color color;

		public Rectangle2D getRectangle(){
			return rect;
		}

		public void setRectangle(){
			System.out.println("highlited? "+batchState.isHighlighted());
			if(!batchState.isHighlighted()){
				System.out.println("bringing it back!");
				rect.setRect(highlightLocationX, highlightLocationY, highlightWidth, highlightHeight);
				batchState.setHighlighted(true);
			}
			else{
				System.out.println("set the size of rectangle to 0");
				rect.setRect(highlightLocationX, highlightLocationY, 0, 0);
				batchState.setHighlighted(false);
			}
		}

		public DrawingRect(Rectangle2D rect, Color color) {
			this.rect = rect;
			this.color = color;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.setColor(color);
			g2.fill(rect);
		}

		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}
	}


	class DragImage extends JComponent implements DrawingShape {

		private BufferedImage image;
		private Rectangle2D rect;
		int currentX;
		int currentY;

		public DragImage(BufferedImage image, Rectangle2D rect) {
			this.image = image;
			this.rect = rect;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.drawImage(image, (int)rect.getMinX(), (int)rect.getMinY(), (int)rect.getMaxX(), (int)rect.getMaxY(),
					0, 0, image.getWidth(null), image.getHeight(null), null);
		}	

		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}

		public Rectangle2D getRectangle(){
			return this.rect;
		}

		public BufferedImage getImage(){
			return image;
		}
		public void setImage(BufferedImage image){
			System.out.println("set the image");
			this.image = image;
		}

	}


	@Override
	public void downloadBatch() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectField() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSelected(TableEntryCell selectedCell) {
		//Do bunch of stuffffff	
		if(selectedCell!=null){		
			if(shapes.size()>1){
				shapes.remove(1);
			}

			Field temp = new Field();
			temp = batchState.getFields().get(selectedCell.getColumn()-1);
			highlightLocationX = (temp.getXcoord()*.5)+xOffset;
			highlightLocationY = ((batchState.getFirstYCoord()+batchState.getRecordHeight()*selectedCell.getRow())*.5)+yOffset;
			highlightWidth = temp.getWidth()*.5;
			highlightHeight =batchState.getRecordHeight()*.5;

			if(batchState.isHighlighted()){
				shapes.add(new DrawingRect(new Rectangle2D.Double(highlightLocationX,
						highlightLocationY, highlightWidth, highlightHeight), new Color(153, 255, 255, 192)));
			}
			else{
				shapes.add(new DrawingRect(new Rectangle2D.Double(highlightLocationX,
						highlightLocationY, 0, 0), new Color(153, 255, 255, 192)));	
			}
			this.repaint();
			//		shapes.add(new DrawingRect(new Rectangle2D.Double(batchState.getFields().get(0).getXcoord()*.5,batchState.getFirstYCoord()*.5, batchState.getCellWidth()*.5, batchState.getRecordHeight()*.5), new Color(153, 255, 255, 192)));
		}
	}

	@Override
	public void performLogout(String function) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateImage(String function) {
		switch(function){
		case "Invert Image":
			if(imgInverted == false){
				imgInverted = true;
			}else{
				imgInverted = false;
			}
			batchState.setImgInverted(imgInverted);
			BufferedImage result = batchComponent.getImage();
			RescaleOp op = new RescaleOp(-1.0f, 255f, null);
			result = op.filter(batchComponent.getImage(), null);
			batchComponent.setImage(result);
			this.repaint();
			break;
		case "Zoom In":
			System.out.println("this is inside Zoom in");
			if(scale == .5){
				scale +=.5;
			}
			else if(scale < 5){
				scale += 1;
			}
			repaint();
			break;
		case "Zoom Out":
			if(scale == 1){
				scale -=.5;
			}
			else if(scale > 0 && scale != .5){
				scale -= 1;
			}
			repaint();
			break;
		case "Toggle Highlights":
			if(shapes.size()>1){
				((DrawingRect) shapes.get(1)).setRectangle();
			}
			repaint();
			this.repaint();
			break;
		default:	
		}
	}

	public double getScale() {
		return scale;
	}

	public int getW_dragStartTranslateX() {
		return w_dragStartTranslateX;
	}

	public void setW_dragStartTranslateX(int w_dragStartTranslateX) {
		this.w_dragStartTranslateX = w_dragStartTranslateX;
	}

	public int getW_dragStartTranslateY() {
		return w_dragStartTranslateY;
	}

	public void setW_dragStartTranslateY(int w_dragStartTranslateY) {
		this.w_dragStartTranslateY = w_dragStartTranslateY;
	}

	public int getCurrentLocX() {
		return currentLocX;
	}

	public void setCurrentLocX(int currentLocX) {
		this.currentLocX = currentLocX;
	}

	public int getCurrentLocY() {
		return currentLocY;
	}

	public void setCurrentLocY(int currentLocY) {
		this.currentLocY = currentLocY;
	}

}