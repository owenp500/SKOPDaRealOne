import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;


public class Player implements DisplayableSprite , MovableSprite, CollidingSprite {
	
	private static final int PERIOD_LENGTH = 200;			

	private long elapsedTime = 0;
	protected double elapsedFrames = 0;
		
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
	private boolean isAI = false;
	
	protected double velocityX = 0;
	protected double velocityY = 0;
	private long score =  0;
	protected int health = 100;
	
	protected final int ATTACK_DAMAGE = 15;
	protected final int BLOCK_DAMAGE = 3;
	protected final int ATTACK_FRAMES = 12;
	private final int DEFEND_FRAMES = 4;
	
	protected double startOfAttackFrame;
	private boolean attackConnected = false;
	private boolean attackBlocked = false;
	protected int knockBackVelocity = 0;
	
	protected boolean defendingHigh = false;
	protected boolean defendingLow = false;
	protected double startOfDefendingFrame = 0;
	
	protected boolean beingAttacked = false;
	
	protected final int ATTACK_DOWN_FRAMES = 4;
	
	private BoxSprite hurtBox = new BoxSprite(50,125,0);
	private int hurtBoxOffset = 0;

	
	private double endStunFrame; 
	
	protected State state = State.IDLE;
	
	
	public Player(int centerX, int centerY, String imageFolder) { 
		
		this.centerX = centerX;
		this.centerY = centerY;
			
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
	}
	
	public void setKeys() {
		isAI  = true;
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
		//TODO! remove this later
		if(frames[index] != null) {
			return frames[index];
		}
		else {
			return frames[1];
		}
	}
	
	public void setFacingRight(boolean right) {
		facingRight = right;
		hurtBoxOffset = (facingRight) ? 100: -100;
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
	//this is technically a temporary solution to collision detection.
	//it would be more appropriate and less costly for the universe to check for the player
	protected boolean checkCollisionWithPlayer(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {

		//deltaX and deltaY represent the potential change in position
		boolean colliding = false;
		
		if (player == 1) {
			for (DisplayableSprite sprite : sprites) {
				if (sprite instanceof Player2 || sprite instanceof Player3) {
					if (CollisionDetection.overlaps(this, sprite,deltaX,deltaY)) {
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
					if (CollisionDetection.overlaps(this, sprite,deltaX,deltaY)) {
						colliding = true;
						break;					
					}
				}
			}
			return colliding;	
		}
		if (player == 3) {
			for (DisplayableSprite sprite : sprites) {
				if (sprite instanceof Player1) {
					if (CollisionDetection.overlaps(this, sprite,deltaX,deltaY)) {
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
		state = State.STUN;
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
	public boolean getDefendingLow() {
		return (state == State.LOW_IDLE);
	}
	public boolean getDefendingHigh() {
		boolean movingBackwards = (facingRight) ? velocityX < 0: velocityX > 0;
		return (state == State.MOVE && movingBackwards);
	}
	public void setAttackConnectedTrue() {
		attackConnected = true;
	}
	public void setAttackBlockedTrue() {
		attackBlocked = true;
	}
	public void setBeingAttackedTrue() {
		beingAttacked = true;
	}
	public void setDefendingHighTrue() {
		defendingHigh = true;
	}
	public void setDefendingLowTrue() {
		defendingLow = true;
	}

	
	
	
	//TODO! start of update function 
	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
		velocityX -= velocityX/8;

		
			
		if(beingAttacked) {
			stun(10);
			health -= ATTACK_DAMAGE;
			velocityX = knockBackVelocity;
			beingAttacked = false;
		}
		else if(defendingHigh) {
			health -= BLOCK_DAMAGE;
			state = State.DEFEND;
			startOfDefendingFrame = elapsedFrames;
			defendingHigh = false;
		}
		else if(defendingLow) {
			health -= BLOCK_DAMAGE;
			state = State.LOW_DEFEND;
			startOfDefendingFrame = elapsedFrames;
			defendingLow = false;
		}
												
		switch (state){ 
		case STUN:
			if(elapsedFrames >= endStunFrame) {
				state = State.IDLE;
			}
			break;
			
		case ATTACK:
			if(!attackConnected) {
				hurtBox.setCenterX(centerX + hurtBoxOffset);
				hurtBox.setCenterY(this.centerY);
			}
			if(elapsedFrames - startOfAttackFrame >= ATTACK_FRAMES ||  attackConnected || attackBlocked) {
				if(attackBlocked) {
					stun(ATTACK_DOWN_FRAMES * 10);
					attackBlocked = false;
				}
				else {
					stun(ATTACK_DOWN_FRAMES);
				}
				hurtBox.setCenterX(centerX);
				hurtBox.setCenterY(this.centerY - 400);
				attackConnected = false;
			}
			break;
		case LOW_ATTACK:
			if(!attackConnected) {
				hurtBox.setCenterX(centerX + hurtBoxOffset);
				hurtBox.setCenterY(this.centerY);
			}
			if(elapsedFrames - startOfAttackFrame >= ATTACK_FRAMES ||  attackConnected || attackBlocked) {
				if(attackBlocked) {
					stun(ATTACK_DOWN_FRAMES * 10);
					attackBlocked = false;
				}
				else {
					stun(ATTACK_DOWN_FRAMES);
				}
				hurtBox.setCenterX(centerX);
				hurtBox.setCenterY(this.centerY - 400);
				attackConnected = false;
			}
			break;
		case DEFEND:
			if(elapsedFrames - startOfDefendingFrame >= DEFEND_FRAMES) {
				state = State.IDLE;
			}
			break;
		case LOW_DEFEND:
			if(elapsedFrames - startOfDefendingFrame >= DEFEND_FRAMES) {
				state = State.IDLE;
			}
			break;
		default:
			if (!isAI) {
				if (keyboard.keyDown(attackButton)) {
					if (state == State.LOW_IDLE) {
						state = State.LOW_ATTACK;
					}
					else {
						state = State.ATTACK;	
					}
					velocityX = 0;
				    startOfAttackFrame = elapsedFrames;	
				}
				else if (keyboard.keyDown(downButton)) {
					state = State.LOW_IDLE;
				}
				else if (keyboard.keyDown(leftButton)) {
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
				else {
					state = State.IDLE;
				}
				break;				
			}
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
	//	currentFrame = (int) this.elapsedFrames % FRAMES;																													
		
	}
	//TODO! end of update function!
	
}