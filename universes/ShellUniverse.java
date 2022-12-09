import java.util.ArrayList;

public class ShellUniverse implements Universe {

	private boolean complete = false;
	private Background backgroundBackground = null;
	private Background middleBackground = null;
	private Background foreBackground = null;
	private DisplayableSprite player1 = null;
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	private ArrayList<Background> backgrounds = new ArrayList<Background>();

	public ShellUniverse () {

		
		middleBackground = new MiddleBackground();
		foreBackground = new ForegroundBackground();
		backgroundBackground = new BackgroundBackground();
		
		
		
		backgrounds = new ArrayList<Background>();
		backgrounds.add(middleBackground);
		backgrounds.add(foreBackground);
		backgrounds.add(backgroundBackground);

		this.setXCenter(0);
		this.setYCenter(0);
		player1 = new SimpleSprite(0,0);
		sprites.add(player1);
			
	}

	public double getScale() {
		return 1;
	}

	public double getXCenter() {
		return 0;
	}

	public double getYCenter() {
		return 0;
	}

	public void setXCenter(double xCenter) {
	}

	public void setYCenter(double yCenter) {
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		complete = true;
	}

	public ArrayList<Background> getBackgrounds() {
		return backgrounds;
	}	

	public DisplayableSprite getPlayer1() {
		return player1;
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
		
		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, keyboard, actual_delta_time);
    	} 
		
		this.backgroundBackground.setShiftX(player1.getCenterX() * 0.85);
		this.backgroundBackground.setShiftY(player1.getCenterY() * 1 + 540);
		
		
		this.middleBackground.setShiftX(player1.getCenterX() * 0.55);
		this.middleBackground.setShiftY(player1.getCenterY() * 1 + 540);
		
		this.foreBackground.setShiftX(player1.getCenterX() * 0.25);
		this.foreBackground.setShiftY(player1.getCenterY() * 1 + 540);
		
		
	}

	public String toString() {
		return "ShellUniverse";
	}

}
