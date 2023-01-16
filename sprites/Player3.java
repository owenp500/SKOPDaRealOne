
public class Player3 extends Player {

	public Player3(int centerX, int centerY, String imageFolder) {
		
		super(centerX, centerY, imageFolder);
		//assigning nothing for AI controls
		super.setKeys();
		super.setFacingRight(true);
		
		//assigns whether the player is 'player1' or 'player2' or 'AI' which is 'player3'
		super.setPlayer(3);
		
		
		
	}
	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
		super.update( universe,keyboard,actual_delta_time);
	}
}