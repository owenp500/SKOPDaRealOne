
public class Player3 extends Player {
	
	
	boolean collidingPlayer = false;
	boolean collidingBarrierX = false;
	
	public Player3(int centerX, int centerY, String imageFolder) {
		super(centerX, centerY, imageFolder);
		
		state = 0;
		//assigning nothing for AI controls
		super.setKeys();
		super.setFacingRight(true);
		
		//assigns whether the player is 'player1' or 'player2' or 'AI' which is 'player3'
		super.setPlayer(3);

	}
	
    public void chooseAction() {
        if (state == 0) {
            System.out.println("Going Left");
            state = 1;
        } 
        
        else if (state == 1) {
        	System.out.println("Going Right");
        	state = 2;
        }
        
        else {
            System.out.println("Up");
            state = 0;
        }
    }
	
	
	// MOVEMENT
	public void chase() {
		while (!collidingPlayer) {
			if (super.velocityX>=-100)
			{
				super.velocityX -= 40;
			}
		}
		
	}
	
	public void run() {
		if (super.velocityX<=100)
		{
			super.velocityX += 40;
		}
	}
	
	
	// ATTACK
	public void attack( ) {
		
	}
	
	@Override
	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
		System.out.println(collidingPlayer);
		switch (state){ 
		case STUN:

			break;
			
		case ATTACK:
			
			}
			break;
		case LOW_ATTACK:
		
			break;
		case DEFEND:
			
			break;
		case LOW_DEFEND:
			
			break;
		default:
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
		super.update(universe, keyboard, actual_delta_time);
		
		double deltaX = actual_delta_time * 0.001 * velocityX;
		
		 collidingBarrierX = checkCollisionWithBarrier(universe.getSprites(), deltaX, 0);
		 collidingPlayer = checkCollisionWithPlayer(universe.getSprites(), deltaX, 0);
	}

}