import java.util.ArrayList;

public class OwensSpecialUniverse implements Universe {

	private boolean complete = false;	
	private Background background = null;
	private Background background2 = null;
	private ArrayList<Background> backgrounds = new ArrayList<Background>();
	private DisplayableSprite player1 = null;
	private DisplayableSprite boss = null;
	private DisplayableSprite boss2 = null;
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	private ArrayList<DisplayableSprite> barriers = new ArrayList<DisplayableSprite>();
	private double xCenter = 0;
	private double yCenter = 0;
	
	private ArrayList<DisplayableSprite> disposalList = new ArrayList<DisplayableSprite>();
	
	private DisplayableSprite floor = null;
	private DisplayableSprite wall = null;
	private DisplayableSprite wall2 = null;
	private DisplayableSprite platform = null;
	private DisplayableSprite platform2 = null;

	public OwensSpecialUniverse () {
		Background background = new OwensSpecialBackground();
		backgrounds.add(background);
		BarrierSprite barrier2 = new BarrierSprite(0,0,1,450,true, -400,0);
		BarrierSprite barrier3 = new BarrierSprite(0,0,1,450,true, 400,0);
		barriers.add(barrier2); barriers.add(barrier3);
		
		sprites.add(new Player1(0,165));
		sprites.add(barrier2);
		sprites.add(barrier3);
		
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
		return false;
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
