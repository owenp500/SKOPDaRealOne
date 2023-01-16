
public class Player3 extends Player {
	
	private int state = 0;
	
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
		if (super.velocityX>=-100)
		{
			super.velocityX -= 40;
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
	
	
		
	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
		super.update( universe,keyboard,actual_delta_time);
	}
}