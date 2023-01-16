
public class Player3 extends Player {
	
	private String action = "";
	int actionNum = 0;
	public boolean collidingPlayer = false;
	public boolean collidingBarrierX = false;
	public boolean isHalfHp = false;
	
	public Player3(int centerX, int centerY, String imageFolder) {
		super(centerX, centerY, imageFolder);
		
		//assigning nothing for AI controls
		super.setKeys();
		super.setFacingRight(true);
		
		//assigns whether the player is 'player1' or 'player2' or 'AI' which is 'player3'
		super.setPlayer(3);

	}
	
    public void chooseAction() {
    	
        if (actionNum == 0) {		// Attacking
        	attack();
        } 
        
        else if (actionNum == 1 && collidingPlayer == false) {	// Moving Left
        	chase();
        }
        
        else if (actionNum == 2) {	// Moving Right
        	run();
        }
        
        else if (actionNum == 3) { // Crouching/Blocking
        	crouch();
        }
        
        
        else {
            action = "Idle";
        }
    }
	
	
	// MOVEMENT
	public void chase() {	
		if (super.velocityX>=-100)
		{
			super.velocityX -= 40;
		}
		action = "Left";
		
	}
	
	public void run() {
		if (super.velocityX<=100)
		{
			super.velocityX += 40;
		}
		action = "Right";
		
	}
	
	public void crouch() {
		action = "Crouch";
	}
	
	
	// ATTACK
	public void attack() {
		action = "Attack";
	}
	
	
	
	@Override
	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {

		
		switch (state){ 
		
		case STUN:

			break;
			
		case ATTACK:
			
			break;
		case LOW_ATTACK:
		
			break;
		case DEFEND:
			
			break;
		case LOW_DEFEND:
			
			break;
			
		default:
			if (action == "Attack") { ///Attack
				if (state == State.LOW_IDLE) {
					state = State.LOW_ATTACK;
				}
				else {
					state = State.ATTACK;	
				}
				velocityX = 0;
			    startOfAttackFrame = elapsedFrames;	
			}
			else if (action == "Crouch") { /// Down
				state = State.LOW_IDLE;
			}
			else if (action == "Left") { /// Left
				if (velocityX>= -100) {
				velocityX -= 40; 
				state = State.MOVE;
				}
			} 
			else if (action == "Right") { /// Right
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
		
	super.update(universe, keyboard, actual_delta_time);
	int player3Hp = getHealth();
	
	if (player3Hp < 51) {
		isHalfHp = true;
		System.out.println(isHalfHp);
	}
	
	double deltaX = actual_delta_time * 0.001 * velocityX;
	
	collidingBarrierX = checkCollisionWithBarrier(universe.getSprites(), deltaX, 0);
	collidingPlayer = checkCollisionWithPlayer(universe.getSprites(), deltaX, 0);
	
	if (collidingBarrierX || collidingPlayer) {
		velocityX  = 0;
	}

	}
}