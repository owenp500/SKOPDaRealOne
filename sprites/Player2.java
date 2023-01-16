
public class Player2 extends Player {
	
	public int health = 100;
	
	public Player2(int centerX, int centerY, String imageFolder) {
		
		super(centerX, centerY, imageFolder);
		//Player2 does not invoke the setKeys method as the default keys are meant for player 2
		super.setFacingRight(false);
		
		//assigns whether the player is 'player1' or 'player2'
		super.setPlayer(2);
		
	}
}