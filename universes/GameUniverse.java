import java.security.Principal;
import java.util.ArrayList;


public class GameUniverse implements Universe {
	
	private boolean winner = false;
	
	private boolean complete = false;
	private double xCenter;
	private double yCenter;
	private double scale = 1.300;
	
	private ArrayList<Background> backgrounds = new ArrayList<Background>();
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	private ArrayList<DisplayableSprite> disposalList = new ArrayList<DisplayableSprite>();


	/*					*
	 * Backgrounds	 	*
	 *					*/
	private Background background = null;	
	private Background middleground = null;
	private Background foreground = null;
	
	/*					*
	 * Character	 	*
	 *					*/
	private DisplayableSprite camera = null;
	private DisplayableSprite player1 = null;
	private DisplayableSprite hurtBox1 = null;
	
	private DisplayableSprite player2 = null;
	private DisplayableSprite hurtBox2 = null;
	

	/*					*
	 * Barrier blocks 	*
	 *					*/
	private DisplayableSprite floor = null;
	private DisplayableSprite wall = null;


	public GameUniverse () {
		//ADD BACKGROUNDS
		background = new BackgroundBackground();
		middleground = new MiddleBackground();
		foreground = new ForegroundBackground();
		backgrounds.add(middleground);
		backgrounds.add(background);
		backgrounds.add(foreground);
		
		//ADD BARRIER
		BarrierSprite barrier1 =  new BarrierSprite(0,0,5,450,false, -700,360);
		BarrierSprite barrier2 = new BarrierSprite(0,0,5,450,false, 700,360);
			
		//ADD SPRITES
		camera = new Camera(17,216,100,100);
		player1 = new Player1(-351,351, "Doug's Sprites");
		player2 = new Player2(351,351, "Phil's Sprites");
		hurtBox1 = player1.getHurtBox();
		hurtBox2 = player2.getHurtBox();
		
		
		sprites.add(hurtBox1);
		sprites.add(hurtBox2);
		sprites.add(camera); sprites.add(player1); sprites.add(player2); 
		sprites.add(barrier1); 
		sprites.add(barrier2); 

	}

	public double getScale() {
		return scale;
	}
	
	public void getScale(double scale) {
		this.scale = scale;
	}	
	
	public double getXCenter() {
		return this.xCenter;
	}

	public double getYCenter() {
		return this.yCenter;
	}
	
	public void setXCenter(double xCenter) {
		this.xCenter = xCenter;
	}

	public void setYCenter(double yCenter) {
		this.yCenter = yCenter;
	}
	
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		complete = true;
	}

	@Override
	public ArrayList<Background> getBackgrounds() {
		return backgrounds;
	}	
	public DisplayableSprite getPlayer1() {
		return player1;
	}
	public DisplayableSprite getPlayer2() {
		return player2;
	}
	public DisplayableSprite getCamera() {
		return camera;
	}
	
	public DisplayableSprite getBarrier(boolean wall) {
		if (wall) {
			return this.wall;
		}
		else {
			return this.floor;
		}
	}

	public ArrayList<DisplayableSprite> getSprites() {
		return sprites;
	}
		
	public boolean centerOnPlayer() {
		return true;
	}		

	public void update(KeyboardInput keyboard, long actual_delta_time) {
		//casting the players to the player class for readability
		Player player1AsPlayer = (Player) player1;
		Player player2AsPlayer = (Player) player2;
		
		//this is how the universe deals with the players' attacks
		State player1State = player1AsPlayer.getState();
		State player2State = player2AsPlayer.getState();
		
		
		//this block of code checks wether attacks hit, miss, or are blocked and then communicates that to the players
		if((player1State == State.ATTACK || player2State == State.ATTACK)) {	
			if(CollisionDetection.overlaps(player1, hurtBox2) && !player1AsPlayer.getAttackConnected()) {
				if (player1AsPlayer.getDefendingHigh()) {
					player1AsPlayer.setDefendingHighTrue();
					player2AsPlayer.setAttackBlockedTrue();
				}
				else {
					player2AsPlayer.setAttackConnectedTrue();
					player1AsPlayer.setBeingAttackedTrue();
				}
			}
			if(CollisionDetection.overlaps(player2, hurtBox1) && !player2AsPlayer.getAttackConnected()) {
				if (player2AsPlayer.getDefendingHigh()) {
					player2AsPlayer.setDefendingHighTrue();
					player1AsPlayer.setAttackBlockedTrue();
				}
				else {
					player1AsPlayer.setAttackConnectedTrue();
					player2AsPlayer.setBeingAttackedTrue();
				}
			}
		}
		if((player1State == State.LOW_ATTACK || player2State == State.LOW_ATTACK)) {
			if(CollisionDetection.overlaps(player1, hurtBox2) && !player1AsPlayer.getAttackConnected()) {
				if(player1AsPlayer.getDefendingLow()) {
					player1AsPlayer.setDefendingLowTrue();
					player2AsPlayer.setAttackBlockedTrue();
				}
				else {
					player2AsPlayer.setAttackConnectedTrue();
					player1AsPlayer.setBeingAttackedTrue();
				}
			}
			if(CollisionDetection.overlaps(player2, hurtBox1) && !player2AsPlayer.getAttackConnected()) {
				if(player2AsPlayer.getDefendingLow()) {
					player2AsPlayer.setDefendingLowTrue();
					player1AsPlayer.setAttackBlockedTrue();
				}
				else {
					player1AsPlayer.setAttackConnectedTrue();
					player2AsPlayer.setBeingAttackedTrue();
				}
			}
		}
		

		/*					*
		 * Sets Camera POS.	*
		 *					*/
		if (winner == false) {
			double averageX = (player1.getCenterX() + player2.getCenterX() )/ 2;
			((Camera) camera).setCenterX(averageX);
		}
		else {
			if (player1.getDispose()) {
				((Camera) camera).setCenterX(player2.getCenterX());
			}
			else {
				((Camera) camera).setCenterX(player1.getCenterX());
			}
		}
		
		
		/*					*
		 * Dispose Player.	*
		 *					*/
		if (((Player)player2).getHealth() <= 0) {
			player2.setDispose(true);
			winner = true;
		}
		
		if (((Player)player1).getHealth() <= 0) {
			player1.setDispose(true);
			winner = true;
		}
		
		disposeSprites(); 

		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, keyboard, actual_delta_time);
    	}

		this.background.setShiftX(camera.getCenterX() * 0.85); 
		
		this.middleground.setShiftX(camera.getCenterX() * 0.45);

		this.foreground.setShiftX(camera.getCenterX() * 0.10); 
		
	}
	
	 protected void disposeSprites() {
	    	//collect a list of sprites to dispose
	    	//this is done in a temporary list to avoid a concurrent modification exception
			for (int i = 0; i < sprites.size(); i++) {
				DisplayableSprite sprite = sprites.get(i);
	    		if (sprite.getDispose() == true) {
	    			disposalList.add(sprite);
	    		}
	    	}
			
			//go through the list of sprites to dispose
			//note that the sprites are being removed from the original list
			for (int i = 0; i < disposalList.size(); i++) {
				DisplayableSprite sprite = disposalList.get(i);
				sprites.remove(sprite);
				System.out.println("Remove: " + sprite.toString());
	    	}
			
			//clear disposal list if necessary
	    	if (disposalList.size() > 0) {
	    		disposalList.clear();
	    	}
	    }


	public String toString() {
		return String.format("");
	}
}

