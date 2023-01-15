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

	private  Image[] frames = new Image[FRAMES];
//	private static boolean framesLoaded = false;
	
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
	private int health = 0;
	
	//these variables detail how many frames each State should take to complete
	//changing these constants will change the length of each state
	private final int ATTACK_FRAMES = 12;
	private double startOfAttackFrame;
	private boolean attackConnected = false;
	private int knockBackVelocity = 0;
	
	private final int DEFEND_FRAMES = 4; 
	private final int HIT_FRAMES = 4;
	private final int DOWN_FRAMES = 4;
	//certain player states like attacking and blocking have to last a certain amount of time. 
	//if a player attacks and misses the other player should be given an opportunity to respond, this is one of the core mechanics within all fighting games
	//an action like crouching does not need to have any time limit, however attacking necessarily does need a limit
	private BoxSprite hurtBox = new BoxSprite(50,50,0);
	private int hurtBoxOffset = 0;
	
	
	private ArrayList<DisplayableSprite> sprites;
	private double endStunFrame; 
	
	// THE ORDER OF THE ANIMATIONS IN THE FOLDER
	// Idle(0); Move(1); Defend(2); LowIdle(3); LowBlock(4); LowAttack(5); Stun(6); Attack(7)
	
	protected State state = State.IDLE;
	
	
	public Player(int centerX, int centerY, String imageFolder) { 
		
		this.centerX = centerX;
		this.centerY = centerY;
		this.health = 5;

//		if (framesLoaded == false) {
			
			frames = new Image[32];
			for (int frame = 0; frame < 32; frame++) {
				String filename = String.format("res/%s/DougTheAdventurer%d.png" , imageFolder,frame+1);
				
				try {
					frames[frame] = ImageIO.read(new File(filename));
				}
				catch (IOException e) {
					System.err.println(e.toString());
				}		
			
			}
//			if (frames[0] != null) {
//				framesLoaded = true;
//			}
//		}		
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
		
		
		//you can add whatever states that don't yet have animations into this if statement
		//TODO! delete these comments 
		
		long period = elapsedTime / PERIOD_LENGTH;
		int frame = (int) (period % FRAMES);
		int index = state.value * FRAMES + frame;
		if(frames[index] != null) {
			return frames[index];
		}
		else {
			return frames[1];
		}
		

		
	}
	
	public void setFacingRight(boolean right) {
		facingRight = right;
		hurtBoxOffset = (facingRight) ? 50: -50;
		knockBackVelocity = (facingRight) ? -500: 500;
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
	public BoxSprite getHurtBox() {
		return hurtBox;
	}
	public int getHurtBoxOffset() {
		return hurtBoxOffset;
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
	public void attack() {
		
	}
	
	public State getState() {
		return state;
	}
	
	public void stun(int length) {
		state = state.STUN;
		endStunFrame = elapsedFrames + 16;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int dmg) {
		health -= dmg;
	}
	
	//not sure what to call this method. 
	//This is used by the universe when the sprite has collided with an enemy attack
	public void hurt(int dmg) {
		stun(10);
		health -= dmg;
		velocityX = knockBackVelocity;
	}
	public boolean getAttackConnected() {
		return attackConnected;
	}
	public void setAttackConnectedTrue() {
		attackConnected = true;
	}
	
	//TODO! start of update function 
	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
		velocityX -= velocityX/8;
												
		switch (state){ 
		case STUN:
			if(elapsedFrames >= endStunFrame) {
				state =  state.IDLE;
			}
			break;
		case ATTACK:
			if(!attackConnected) {
				hurtBox.setCenterX(centerX + hurtBoxOffset);
				hurtBox.setCenterY(this.centerY);
			}
			
			if(elapsedFrames - startOfAttackFrame >= ATTACK_FRAMES ||  attackConnected) {
				stun(3);
				hurtBox.setCenterX(centerX);
				hurtBox.setCenterY(this.centerY - 400);
				attackConnected = false;
			}
			break;
		case DEFEND:
			break;
		
			//these actions can only be performed in the IDLE or MOVE states
		default:
			if (keyboard.keyDown(attackButton)) {
				if (state == State.LOW_IDLE) {
					
				}
				else {
					state = State.ATTACK;
					velocityX = 0;
				    startOfAttackFrame = elapsedFrames;	
				}
				
			}
			else if (keyboard.keyDown(rightButton)) {
				if(velocityX <= 100) {
					velocityX += 40;
					state = State.MOVE;
				}
			}
			else if (keyboard.keyDown(leftButton)) {
				if (velocityX>= -100) {
				velocityX -= 40; 
				state = State.MOVE;
				}
			} 
			else if (keyboard.keyDown(downButton)) {
				state = State.LOW_IDLE;
			} 
			else {
				state = State.IDLE;
			}
			
			break;				
		}
		
		
		
		
		

		double deltaX = actual_delta_time * 0.001 * velocityX;
		
		boolean collidingBarrierX = checkCollisionWithBarrier(universe.getSprites(), deltaX, 0);
		boolean collidingPlayer = checkCollisionWithPlayer(universe.getSprites(), deltaX, 0);
		
		if (collidingBarrierX || collidingPlayer) {
			velocityX  = 0;
		}
		
		this.centerX += actual_delta_time * 0.001 * velocityX;
		
		elapsedTime +=  actual_delta_time;
		this.elapsedFrames ++;
		currentFrame = (int) this.elapsedFrames % FRAMES;																													
		
		//TODO! little bit of debug printing ;)
		
		String attackString = (state == State.ATTACK) ? "attacking": "no";
		if( state == state.STUN) {
			attackString = "stun"; 
		}
		System.out.printf("\n %s",attackString);
	}
	//TODO! end of update function!
}