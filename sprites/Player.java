import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Player implements DisplayableSprite , MovableSprite, CollidingSprite {
	
	private static final int PERIOD_LENGTH = 200;			
	private static final int IMAGES_IN_CYCLE = 4;
	
	private long elapsedTime = 0;
	protected double elapsedFrames = 0;
	protected int currentFrame = 0;
		
	private boolean facingRight = true;

	protected final static int FRAMES = 4;

	private static Image[] frames = new Image[FRAMES];
	private static boolean framesLoaded = false;
	
	//attack defaults to the up arrow key
	protected int attackButton = 38;
	//left, right, and down buttons default to their respective arrow keys
	protected int leftButton = 37;
	protected int rightButton = 39;
	protected int downButton = 40;
	
	protected double centerX = 0;
	protected double centerY = 0;
	protected double height = 200;
	protected double width = 200;
	protected boolean dispose = false;
	protected boolean isAtExit = false;
	protected static String proximityMessage;
	
	
	protected double velocityX = 0;
	protected double velocityY = 0;
	protected double revolutions;
	protected long score =  0;
	protected int health = 5;
	
	//y acceleration affects jump height and how fast you can slam to the ground
	private final double ACCELERATION_Y = 750;
	private final double GRAVITY = 1500;
	
	public Player(int centerX, int centerY) { 
		this.centerX = centerX;
		this.centerY = centerY;
		
		if (framesLoaded == false) {
			for (int frame = 0; frame < FRAMES; frame++) {
				String filename = String.format("res/Doug's Sprites/DougTheAdventurer%d.png" , frame+1);
				
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
	public void setKeys(int attack, int left, int right, int down) {
		attackButton = attack;
		leftButton = left;
		rightButton = right;
		downButton = down;
	}
	public Image getImage() {	
		long period = elapsedTime / PERIOD_LENGTH;
		int frame = (int) (period % IMAGES_IN_CYCLE);
		System.out.printf("%d\n", frame);
	//	frame = IMAGES_IN_CYCLE + frame;
		return frames[frame];
		
	}
	public void setFacingRight(boolean right) {
		facingRight = right;
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
						
	protected boolean checkCollisionWithBarrier(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {

		//deltaX and deltaY represent the potential change in position
		boolean colliding = false;
		
		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof BarrierSprite) {
				if (CollisionDetection.overlaps(this, sprite,deltaX,deltaY)) {
					colliding = true;
					break;					
				}
			}
		}		
		return colliding;		
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
	
	
	
	
	//TODO! start of update function 
	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
		velocityX -= velocityX/8;
		//constant downward y velocity for gravity
		if (keyboard.keyDown(leftButton)) {
			if (velocityX>= -100) {
			velocityX -= 40; 
			}
		}
		//
		if (keyboard.keyDown(rightButton)) {
			if(velocityX <= 100) {
				velocityX += 40;
			}
		}

		double deltaX = actual_delta_time * 0.001 * velocityX;
		
		boolean collidingBarrierX = checkCollisionWithBarrier(universe.getSprites(), deltaX, 0);		
		
		if (collidingBarrierX) {
			velocityX  = 0;
		}
		if (collidingBarrierX == false) {
			this.centerX += actual_delta_time * 0.001 * velocityX;
		}
					
		
		
		elapsedTime +=  actual_delta_time;
		this.elapsedFrames ++;
		currentFrame = (int) Math.abs(this.elapsedFrames % FRAMES);		
		
	}
	//TODO! end of update function!
}