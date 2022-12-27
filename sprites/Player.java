import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;


public class Player implements DisplayableSprite , MovableSprite, CollidingSprite {
	
	private static final int PERIOD_LENGTH = 200;			

	private long elapsedTime = 0;
	private double elapsedFrames = 0;
	private int currentFrame = 0;
		
	private boolean facingRight = true;

	private final static int FRAMES = 4;

	private static Image[] frames = new Image[FRAMES];
	private static boolean framesLoaded = false;
	
	//attack defaults to the up arrow key
	private int attackButton = 38;
	//left, right, and down buttons default to their respective arrow keys
	private int leftButton = 37;
	private int rightButton = 39;
	private int downButton = 40;
	
	private double centerX = 0;
	private double centerY = 0;
	private double height = 200;
	private double width = 200;
	private boolean dispose = false;
	private boolean isAtExit = false;
	private static String proximityMessage;
	private int player;
	
//	protected String imageFolder = null;
	
	private double velocityX = 0;
	private double velocityY = 0;
	private double revolutions;
	private long score =  0;
	private int health = 5;
	
	//these variables detail how many frames each State should take to complete
	//changing these constants will change the length of each state
	private final int ATTACK_FRAMES = 4;
	private final int DEFEND_FRAMES = 4; 
	private final int HIT_FRAMES = 4;
	private final int DOWN_FRAMES = 4;
	//certain player states like attacking and blocking have to last a certain amount of time. 
	//if a player attacks and misses the other player should be given an opportunity to respond, this is one of the core mechanics within all fighting games
	//an action like crouching does not need to have any time limit, however attacking necessarily does need a limit
	
	
	
	
	protected State state = State.IDLE;
	protected enum State { IDLE(0), MOVE(1), DEFFEND(2), ATTACK(3);
		private int value = 0;
		private State(int value) {
			this.value = value;
		}
	};
	
	
	public Player(int centerX, int centerY, String imageFolder) { 
		
		this.centerX = centerX;
		this.centerY = centerY;
		
		if (framesLoaded == false) {
			
			frames = new Image[12];
			for (int frame = 0; frame < 12; frame++) {
				String filename = String.format("res/%s/DougTheAdventurer%d.png" , imageFolder,frame+1);
				
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
	
	public void setPlayer(int player) {
		this.player = player;
	}
	
	public Image getImage() {	
		long period = elapsedTime / PERIOD_LENGTH;
		int frame = (int) (period % FRAMES);
		int index = state.value * FRAMES + frame;
		return frames[index];
		
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
	
	protected boolean checkCollisionWithPlayer(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {

		//deltaX and deltaY represent the potential change in position
		boolean colliding = false;
		
		if (player == 1) {
			for (DisplayableSprite sprite : sprites) {
				if (sprite instanceof Player2) {
					if (CollisionDetection.pixelBasedOverlaps(this, sprite,deltaX,deltaY)) {
						colliding = true;
						break;					
					}
				}
			}
			return colliding;
		}
		if (player == 2) {
			for (DisplayableSprite sprite : sprites) {
				if (sprite instanceof Player1) {
					if (CollisionDetection.pixelBasedOverlaps(this, sprite,deltaX,deltaY)) {
						colliding = true;
						break;					
					}
				}
			}
			return colliding;	
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
	
	
	//TODO! start of update function 
	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
		velocityX -= velocityX/8;

		if (keyboard.keyDown(leftButton)) {
			if (velocityX>= -100) {
			velocityX -= 40; 
			state = State.MOVE;
			}
		}
		else if (keyboard.keyDown(rightButton)) {
			if(velocityX <= 100) {
				velocityX += 40;
				state = State.MOVE;
			}
		}
		else if (keyboard.keyDown(downButton)) { //temporary placement key for simple defend
			state = State.DEFFEND;
		}
		else if (keyboard.keyDown(attackButton)) {
		//	state = State.ATTACK;
		}
		else {
			state = State.IDLE;
		}
		

		double deltaX = actual_delta_time * 0.001 * velocityX;
		
		boolean collidingBarrierX = checkCollisionWithBarrier(universe.getSprites(), deltaX, 0);
		boolean collidingPlayer = checkCollisionWithPlayer(universe.getSprites(), deltaX, 0);
		
		if (collidingBarrierX || collidingPlayer) {
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