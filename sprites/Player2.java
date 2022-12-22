import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Player2 extends Player {

	public Player2(int centerX, int centerY) {
		super(centerX, centerY);
		//Player2 does not invoke the setKeys method as the default keys are meant for player 2
		//although if you want to change the keys than feel free to use the method here
		super.setFacingRight(false);
		
	}
	
	
}