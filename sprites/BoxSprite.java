import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class BoxSprite implements DisplayableSprite{
	private static Image image;
	private boolean visible = true;
	private double centerX = 0;
	private double centerY = 0;
	private double width = 50;
	private double height = 50;
	private boolean dispose = false;	
	private DisplayableSprite sprite = null;
	
	public BoxSprite(double height, double width, double centerX, boolean visible) {
		this.height = height;
		this.width = width;
		this .centerX = centerX;
		this.visible = visible;
		
		if (image == null) {
			try {
				image = ImageIO.read(new File("res/hitBox.png"));
			}
			catch (IOException e) {
				System.err.println(e.toString());
			}		
		}
	}
	public Image getImage() { 
		return image;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean getVisible() {
		return this.visible;
	}
	
	public double getMinX() {
		return centerX - (width / 2);
	}

	public double getMaxX() {
		return centerX + (width / 2);
	}

	public double getMinY() {
		return centerY - (height / 2);
	}

	public double getMaxY() {
		return centerY + (height / 2);
	}

	public double getHeight() {
		return height;
	}
	public BoxSprite getHurtBox() {
		return null;
	}
	public double getWidth() {
		return width;
	}

	public double getCenterX() {
		return centerX;
	}

	public double getCenterY() {
		return centerY;
	}
	public void setCenterX(double x) {
		centerX = x;
	}
	public void setCenterY(double y) {
		centerY = y;
	}
	public boolean getDispose() {
		return dispose;
	}

	public void setDispose(boolean dispose) {
		this.dispose = dispose;
	}

	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
		
	}
	
	public boolean isHit(boolean b) {
		return false;
	}
	public void setDontMove(boolean DontMove) {
	}
	public int getHurtBoxOffset() {
		return 0;
	}
	
	
}
