
public class Player3 extends Player {
	
	public int health = 5;

	public Player3(int centerX, int centerY, String imageFolder) {
		
		super(centerX, centerY, imageFolder);
		//assigning nothing for AI controls
		super.setKeys();
		super.setFacingRight(true);
		
		//assigns whether the player is 'player1' or 'player2' or 'AI' which is 'player3'
		super.setPlayer(3);
		
		class AI {
		    private int state;

		    public AI() {
		        state = 0;
		    }

		    public void chooseDirection() {
		        if (state == 0) {
		            System.out.println("Going Left");
		            state = 1;
		        } else {
		            System.out.println("Going Right");
		            state = 0;
		        }
		    }
		}
}
}