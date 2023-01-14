import java.security.Principal;
import java.util.ArrayList;


public class StartUniverse implements Universe {
	
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


	public StartUniverse () {
			
		//ADD SPRITES
		player1 = new Player1(-351,351, "Doug's Sprites");
		player2 = new Player2(351,351, "Phil's Sprites");

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
		this.complete = true;
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
	public Double abs(double x) {
		if(x<0) {
			return -x;
		}
		else { return x;}
	}
	
	
	public void update(KeyboardInput keyboard, long actual_delta_time) {
		
		if (keyboard.keyDownOnce(27)) {
			complete = true;
		}
		disposeSprites(); 

		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, keyboard, actual_delta_time);
    	}
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

