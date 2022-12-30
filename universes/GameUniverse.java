import java.security.Principal;
import java.util.ArrayList;



public class GameUniverse implements Universe {
	
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
	private DisplayableSprite player2 = null;

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
		
		/*					*
		 * Sets Camera POS.	*
		 *					*/
		double averageX = (player1.getCenterX() + player2.getCenterX() )/ 2;
		((Camera) camera).setCenterX(averageX);

		if (keyboard.keyDownOnce(27)) {
			complete = true;
		}
		
		
		disposeSprites(); 

		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, keyboard, actual_delta_time);
    	}

		this.background.setShiftX(camera.getCenterX() * 0.85); 
//		this.background.setShiftY(camera.getCenterY() * 1 + 540);
		
		
		this.middleground.setShiftX(camera.getCenterX() * 0.45);
//		this.middleground.setShiftY(camera.getCenterY() * 1 + 540);
		
		this.foreground.setShiftX(camera.getCenterX() * 0.10); 
//		this.foreground.setShiftY(camera.getCenterY() * 1 + 540);

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

