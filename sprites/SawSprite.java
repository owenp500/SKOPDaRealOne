import java.awt.Image; 
import java.io.File;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SawSprite implements DisplayableSprite, MovableSprite, CollidingSprite { 
	private double elapsedFrames = 0;
	private int currentFrame = 0;
	private long elapsedTime = 0;
	private long lastBoostTime = 0;
	private final static int FRAMES = 4;
	private double framesPerSecond = 30;
	private double milliSecondsPerFrame = 1000 / framesPerSecond;
	private static Image[] frames = new Image[FRAMES];
	private static boolean framesLoaded = false;	
	private long lastEnemyHit = -200;
	private long lastEnemyKilled = -3000;
	

	private double centerX = 0;
	private double centerY = 0;
	private double height = 50;
	private double width = 50;
	private boolean dispose = false;
	private boolean isAtExit = false;
	private double proximity;
	private static String proximityMessage;
	
	
	private double velocityX = 0;
	private double velocityY = 0;
	private double revolutions;
	private long score =  0;
	private int health = 5;
	private final int REVOLUTIONS_NEEDED = 500;

	//these two are used to tell which direction the player is colliding on
	private DisplayableSprite xBarrier;
	private DisplayableSprite yBarrier;
	
	private final double ACCELERATION_X = 600;
	//y acceleration affects jump height and how fast you can slam to the ground
	private final double ACCELERATION_Y = 750;
	private final double GRAVITY = 1500;
	
	public SawSprite(ArrayList<DisplayableSprite> barriers) { 
		xBarrier = barriers.get(0);
		yBarrier = barriers.get(0);
		
		if (framesLoaded == false) {
			for (int frame = 0; frame < FRAMES; frame++) {
				String filename = String.format("res/OCP/saw(%d).png" , frame);
				
				try {
					frames[frame] = ImageIO.read(new File(filename));
				}
				catch (IOException e) {
					System.err.println(e.toString());
				}		
			}
			
			if (frames[0] != null) {
				framesLoaded = true;
			}
		}		
	}	
	public Image getImage() {		
		return frames[currentFrame];
	}
	public void setCenterX(double centerX) {		
		this.centerX = centerX;
	}
	
	public void setVelocityY(double pixelsPerSecond) {		
		this.velocityY = pixelsPerSecond;
	}
	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}
	
	public void setVelocityX(double pixelsPerSecond) {		
		this.velocityX = pixelsPerSecond;
	}
	
	public boolean getVisible() {
		return true;
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
	
	public double getWidth() {
		
		return width;
	}
	
	public double getCenterX() {
		
		return centerX;
	}
	
	public double getCenterY() {
		
		return centerY;
	}
	public double getVelocityX() {
		return velocityX;
	}
	public double getVelocityY() {
		return velocityY;
	}
	public double getRevolutions() {
		return revolutions;
	}
	
	public boolean getDispose() {
		
		return dispose;
	}
	public int getHealth() {
		return health;
	}

	
	public void setDispose(boolean dispose) {
		this.dispose = dispose;
	}
						
	private boolean checkCollisionWithBarrier(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {

		//deltaX and deltaY represent the potential change in position
		boolean colliding = false;
		
		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof BarrierSprite) {
				if (CollisionDetection.pixelBasedOverlaps(this,sprite , deltaX, deltaY)) {
					colliding = true;
					if(deltaY == 0) {
						xBarrier = sprite;
					}
					else if(deltaX == 0) {
						yBarrier = sprite;
					}
					break;					
				}
			}
		}		
		return colliding;		
	}
	
	
	private boolean isOnGround() {
		boolean onGround = false;
		if(this.getCenterY() < yBarrier.getCenterY()) { 
			onGround = true;
		}
		
		return onGround;
	}
	private boolean isOnRight() {
		boolean onRight = false;
		if(this.getCenterX() < xBarrier.getCenterX()) {
			onRight = true;
		}
		
		return onRight;
	}
	
	public long getScore() {
		return score;
	}
	public String getProximityMessage() {	
		return proximityMessage;
	}
	
	public boolean getIsAtExit() {
		return isAtExit;
	}
	
	
	//this method is designed so that you have less grip while on the ceiling.
	//rather than calculating the average between the rev and vel I sort of calculate the 'average of the average'.
	// if v = 0 and r = 50
	// on the ground they both average out to 25
	// while on the roof in the same situation v = 12.5 and r= 37.5;
	//(r + v) / 2 = average
	//((r + v) / 2 + v) / 2 = average between average and velocity
	// simplified: (r + 3v) / 4 
	public  void revVelocityCeiling (double revolutions, double velocityX) {
		this.velocityX = (( -revolutions + velocityX) / 2 + velocityX) / 2 ;
		this.revolutions = (revolutions + velocityX) / 4;
	}
	public void revVelocityGround(double revolutions, double velocityX) {
		this.velocityX = (revolutions + velocityX) / 2;
		this.revolutions = (revolutions + velocityX) / 2;
	}
	
	public void revolutionsToVelocityY(double revolutions, double velocityY, boolean onRight) {
		if(onRight) {
			this.velocityY = (-revolutions + velocityY) / 2;
			this.revolutions = (-revolutions + velocityY) / 2;
		}
		else {
			this.velocityY = (revolutions + velocityY) / 2;
			this.revolutions = (revolutions + velocityY) / 2;
		}
	}
	//TODO! start of update function 
	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
		if (health < 0) {
			dispose = true;
		}
		//constant downward y velocity for gravity
		velocityY += actual_delta_time * 0.001 * GRAVITY;
		
		//revolutions += revolutions/20;
		double deltaX = actual_delta_time * 0.001 * velocityX;
		double deltaY = actual_delta_time * 0.001 * velocityY;
		boolean collidingBarrierX = checkCollisionWithBarrier(universe.getSprites(), deltaX, 0);
		boolean collidingBarrierY = checkCollisionWithBarrier(universe.getSprites(), 0, deltaY);	
		
		//q and e give the saw a boost every few seconds regardless of whether its on the floor or not. I decided to do this so the player could get over a ledge after losing they're momentum from hitting a wall
		//this ability gives you more in control midair while not being an extremely powerful movement tool for going super fast.
		
		if (keyboard.keyDown(81)) {
			if (elapsedTime - lastBoostTime > 2500) {
				velocityX -= 75;
				lastBoostTime = elapsedTime;
			}
			
		}
		//e
		if(keyboard.keyDown(69)) {
			if (elapsedTime - lastBoostTime > 1000) {
				velocityX += 75;
				lastBoostTime = elapsedTime;
			}
		}
		
		// DOWN ARROW
		if (keyboard.keyDown(40)) {
			velocityY += actual_delta_time * 0.001 * ACCELERATION_Y;			
		}	
		
		if (collidingBarrierX == false) {
			this.centerX += actual_delta_time * 0.001 * velocityX;
		}
		else {
			revolutionsToVelocityY(revolutions, velocityY, isOnRight());
			velocityX = 0;
		}
		if (collidingBarrierY == false) {
			this.centerY += actual_delta_time * 0.001 * velocityY;
			//left and right arrow have a condition while midair that lets the saw come to a stop faster
			if (keyboard.keyDown(37)) {
				if(revolutions <= 0) {
					revolutions -= actual_delta_time * 0.001 * ACCELERATION_X;
				}
				else {
					revolutions -= actual_delta_time * 0.001 * (ACCELERATION_X / 2) * (ACCELERATION_X / 2) / 100;
				}	
			}
			if (keyboard.keyDown(39)) {
				if(revolutions >= 0) {
					revolutions += actual_delta_time * 0.001 * ACCELERATION_X;
				}
				else {
					revolutions += actual_delta_time * 0.001 * (ACCELERATION_X / 2) * (ACCELERATION_X / 2) / 100;
				}	
			}
		}
		else {
			velocityY = 0;	
			if (isOnGround()) {
			
				//left arrow
				if (keyboard.keyDown(37)) {
					revolutions -=  actual_delta_time * 0.001 * ACCELERATION_X;
				}
				//right arrow
				if (keyboard.keyDown(39)) {
					revolutions +=  actual_delta_time * 0.001 * ACCELERATION_X;
				}
				//up arrow
				revVelocityGround(revolutions,velocityX);
				if (keyboard.keyDown(38)) {
					velocityY -=  ACCELERATION_Y;
				}
			}
			else {
				revVelocityCeiling(revolutions,velocityX);
			}
		}
		
		
		
		
		
		
		elapsedTime += actual_delta_time;
		
		
		
		double frameDifference =  revolutions / 28 * actual_delta_time * 0.001;
		this.elapsedFrames +=  (frameDifference);
		currentFrame = (int) Math.abs(this.elapsedFrames % FRAMES);		
		
	}
	//TODO! end of update function!
}
