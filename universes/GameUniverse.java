import java.util.ArrayList;

public class GameUniverse implements Universe {
	
	private boolean complete = false;
	private double xCenter;
	private double yCenter;
	
	private ArrayList<Background> backgrounds = new ArrayList<Background>();
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	private ArrayList<DisplayableSprite> barriers = new ArrayList<DisplayableSprite>();
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
	private DisplayableSprite boss = null;

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
		
		//ADD BARRIERS
		BarrierSprite barrier1 =  new BarrierSprite(-400,0,400,1,true,0,200);
		BarrierSprite barrier2 = new BarrierSprite(0,0,1,450,true, -400,0);
		BarrierSprite barrier3 = new BarrierSprite(0,0,1,450,true, 400,0);
		barriers.add(barrier1); barriers.add(barrier2); barriers.add(barrier3);
		
		//ADD SPRITES
		player1 = new Player(225,0,100,100);
		camera = new Camera(17,296,100,100);
		sprites.add(new SawSprite(barriers));
		sprites.add(camera);
		sprites.add(player1); sprites.add(barrier1); sprites.add(barrier2); sprites.add(barrier3);
		
	}

	public double getScale() {
		return 1;
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
	
	public DisplayableSprite getBoss() {
		return boss;
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
		


		if (keyboard.keyDownOnce(27)) {
			complete = true;
		}
		
		disposeSprites();

		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, keyboard, actual_delta_time);
    	}
		
//		this.background.setShiftX(player1.getCenterX() * 0.85); 
//		this.background.setShiftY(player1.getCenterY() * 1 + 540);
//		
//		
//		this.middleground.setShiftX(player1.getCenterX() * 0.45);
//		this.middleground.setShiftY(player1.getCenterY() * 1 + 540);
//		
//		this.foreground.setShiftX(player1.getCenterX() * 0.10); 
//		this.foreground.setShiftY(player1.getCenterY() * 1 + 540);

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
