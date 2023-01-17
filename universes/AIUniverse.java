import java.security.Principal;
import java.util.ArrayList;
import java.util.Random;


public class AIUniverse implements Universe {
	
	private boolean winner = false;
	
	private boolean complete = false;
	private double xCenter;
	private double yCenter;
	private double scale = 1.300;
	
	private ArrayList<Background> backgrounds = new ArrayList<Background>();
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	private ArrayList<DisplayableSprite> disposalList = new ArrayList<DisplayableSprite>();
	
	private Random rand = new Random();


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
	
	private DisplayableSprite player3 = null;
	private DisplayableSprite hurtBox2 = null;
	

	/*					*
	 * Barrier blocks 	*
	 *					*/
	private DisplayableSprite floor = null;
	private DisplayableSprite wall = null;


	public AIUniverse () {
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
		player3 = new Player3(351,351, "Phil's Sprites");
		hurtBox1 = player1.getHurtBox();
		hurtBox2 = player3.getHurtBox();
		
		
		sprites.add(hurtBox1);
		sprites.add(hurtBox2);
		sprites.add(camera); sprites.add(player1); sprites.add(player3); 
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
		return player3;
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
	public Double abs(double x) {
		if(x<0) {
			return -x;
		}
		else { return x;}
	}
	public void update(KeyboardInput keyboard, long actual_delta_time) {
				
		//casting the players to the player class for readability
		Player player1AsPlayer = (Player) player1;
		Player player3AsPlayer = (Player) player3;
		Player3 playerAsAI = (Player3) player3;
		
		//this is how the universe deals with the players' attacks
		State player1State = player1AsPlayer.getState();
		State player3State = player3AsPlayer.getState();
		
		if((player1State == State.ATTACK || player3State == State.ATTACK)) {	
			if(CollisionDetection.overlaps(player1, hurtBox2) && !player1AsPlayer.getAttackConnected()) {
				if (player1AsPlayer.getDefendingHigh()) {
					player1AsPlayer.setDefendingHighTrue();
					player3AsPlayer.setAttackBlockedTrue();
				}
				else {
					player3AsPlayer.setAttackConnectedTrue();
					player1AsPlayer.setBeingAttackedTrue();
				}
			}
			if(CollisionDetection.overlaps(player3, hurtBox1) && !player3AsPlayer.getAttackConnected()) {
				if (player3AsPlayer.getDefendingHigh() || player3AsPlayer.getBuffer()) {
					player3AsPlayer.setDefendingHighTrue();
					player1AsPlayer.setAttackBlockedTrue();
				}
				else {
					player1AsPlayer.setAttackConnectedTrue();
					player3AsPlayer.setBeingAttackedTrue();
				}
			}
		}
		if((player1State == State.LOW_ATTACK || player3State == State.LOW_ATTACK)) {
			if(CollisionDetection.overlaps(player1, hurtBox2) && !player1AsPlayer.getAttackConnected()) {
				if(player1AsPlayer.getDefendingLow()) {
					player1AsPlayer.setDefendingLowTrue();
					player3AsPlayer.setAttackBlockedTrue();
				}
				else {
					player3AsPlayer.setAttackConnectedTrue();
					player1AsPlayer.setBeingAttackedTrue();
				}
			}
			if(CollisionDetection.overlaps(player3, hurtBox1) && !player3AsPlayer.getAttackConnected()) {
				if(player3AsPlayer.getDefendingLow()) {
					player3AsPlayer.setDefendingLowTrue();
					player1AsPlayer.setAttackBlockedTrue();
				}
				else {
					player1AsPlayer.setAttackConnectedTrue();
					player3AsPlayer.setBeingAttackedTrue();
				}
			}
		}
		
		/*					*
		 * AI State Handler.*
		 *					*/
		//distance between players
		double distance = abs(player3.getCenterX() - player1.getCenterX());
		
		//Decision Tree while player1 is crouching...
		if (player1State == State.LOW_IDLE || player1State == State.LOW_ATTACK) {
			
			if (playerAsAI.state == State.LOW_IDLE && player1State == State.LOW_ATTACK) {
				playerAsAI.actionNum = 3;
			}
			else if (playerAsAI.state == State.LOW_IDLE) {
				playerAsAI.actionNum = 0;
			}
			else if (distance <= 230 && playerAsAI.state != State.LOW_IDLE) {
				playerAsAI.actionNum = 1;
			}
	
			
		}
		// Decision Tree while player1 is standing up...
		else {
			if (player1State == State.ATTACK && distance <= 260) {
				playerAsAI.actionNum = 3;
			}
			else if (playerAsAI.collidingPlayer || player1State == State.STUN) {
				if (distance <= 260) {
					playerAsAI.actionNum = 0;
				}
				
			}
			else if (distance >= 201) {
				playerAsAI.actionNum = 2;
			}
			else {
				playerAsAI.actionNum = 5;
			}
		}
		

		/*					*
		 * Sets Camera POS.	*
		 *					*/
		if (winner == false) {
			double averageX = (player1.getCenterX() + player3.getCenterX() )/ 2;
			((Camera) camera).setCenterX(averageX);
		}
		else {
			if (player1.getDispose()) {
				((Camera) camera).setCenterX(player3.getCenterX());
			}
			else {
				((Camera) camera).setCenterX(player1.getCenterX());
			}
		}
		
		
		/*					*
		 * Dispose Player.	*
		 *					*/
		if (((Player)player3).getHealth() <= 0) {
			player3.setDispose(true);
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

