
public class Player2 extends Player {
	
	public int health = 5;
	
	public Player2(int centerX, int centerY, String imageFolder) {
		
		super(centerX, centerY, imageFolder);
		//Player2 does not invoke the setKeys method as the default keys are meant for player 2
		//although if you want to change the keys than feel free to use the method here
		super.setFacingRight(false);
		
		//assigns whether the player is 'player1' or 'player2'
		super.setPlayer(2);
		
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int dmg) {
		health -= dmg;
	}

}