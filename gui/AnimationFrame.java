import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseMotionAdapter;


public class AnimationFrame extends JFrame {

	final public static int FRAMES_PER_SECOND = 30;
	final public static int SCREEN_HEIGHT = 768;
	final public static int SCREEN_WIDTH = 1360;

	private int screenCenterX = SCREEN_WIDTH / 2;
	private int screenCenterY = SCREEN_HEIGHT / 2;

	private double scale = 0;
	//point in universe on which the screen will center
	private double logicalCenterX = 0;		
	private double logicalCenterY = 0;

	private JPanel panel = null;
	private JButton buttonPauseRun;
	private JButton buttonStartRun;
	private JButton buttonAIRun;
	private JLabel lableHealth;
	private JLabel lableHealth2;
	private JLabel lableBotom;

	private static boolean stop = false;

	private long current_time = 0;								//MILLISECONDS
	private long next_refresh_time = 0;							//MILLISECONDS
	private long last_refresh_time = 0;
	private long minimum_delta_time = 1000 / FRAMES_PER_SECOND;	//MILLISECONDS
	private long actual_delta_time = 0;							//MILLISECONDS
	private long elapsed_time = 0;
	private boolean isPaused = false;
	private boolean start = false;

	private KeyboardInput keyboard = new KeyboardInput();
	private Universe universe = null;

	//local (and direct references to various objects in universe ... should reduce lag by avoiding dynamic lookup
	private Animation animation = null;
	private DisplayableSprite player1 = null;
	private DisplayableSprite player2 = null;
	private DisplayableSprite camera = null;
	private ArrayList<DisplayableSprite> sprites = null;
	private ArrayList<Background> backgrounds = null;
	private Background background = null;
	boolean centreOnPlayer = false;
	int universeLevel = 0;
	
	public AnimationFrame(Animation animation)
	{
		super("");
		
		this.universe = new StartUniverse();
		
		this.animation = animation;
		this.setVisible(true);		
		this.setFocusable(true);
		this.setSize(SCREEN_WIDTH + 20, SCREEN_HEIGHT + 36);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
		});

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				keyboard.keyPressed(arg0);
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				keyboard.keyReleased(arg0);
			}
		});
		getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				contentPane_mouseMoved(e);
			}
		});
		
		Container cp = getContentPane();
		cp.setBackground(Color.GRAY);
		cp.setLayout(null);

		panel = new DrawPanel();
		panel.setLayout(null);
		panel.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		getContentPane().add(panel, BorderLayout.CENTER);

		buttonPauseRun = new JButton("||");
		buttonPauseRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnPauseRun_mouseClicked(arg0);
			}
		});
		
		buttonStartRun = new JButton("2 PLAYER");
		buttonStartRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnStartRun_mouseClicked(arg0);
			}
		});
		
		buttonAIRun = new JButton("AI PLAYER");
		buttonAIRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnAIRun_mouseClicked(arg0);
			}
		});
		
		
		buttonPauseRun.setFont(new Font("SansSerif", Font.BOLD, 12));
		buttonPauseRun.setBounds(SCREEN_WIDTH - 64, 20, 48, 32);
		buttonPauseRun.setFocusable(false);
		getContentPane().add(buttonPauseRun);
		getContentPane().setComponentZOrder(buttonPauseRun, 0);

		buttonStartRun.setFont(new Font("SansSerif", Font.BOLD, 50));
		buttonStartRun.setBounds(SCREEN_WIDTH/2 - 300, SCREEN_HEIGHT /2 - 50, 300, 100);
		buttonStartRun.setFocusable(false);
		getContentPane().add(buttonStartRun);
		getContentPane().setComponentZOrder(buttonStartRun, 0);
		
		buttonAIRun.setFont(new Font("SansSerif", Font.BOLD, 50));
		buttonAIRun.setBounds(SCREEN_WIDTH/2 , SCREEN_HEIGHT /2 - 50, 300, 100);
		buttonAIRun.setFocusable(false);
		getContentPane().add(buttonAIRun);
		getContentPane().setComponentZOrder(buttonAIRun, 0);

		lableHealth = new JLabel("");
		lableHealth.setForeground(Color.BLUE);
		lableHealth.setFont(new Font("Monospaced", Font.BOLD, 20));
		lableHealth.setBounds(400, 45, SCREEN_WIDTH, 60);
		getContentPane().add(lableHealth);
		getContentPane().setComponentZOrder(lableHealth, 0);

		lableHealth2 = new JLabel("");
		lableHealth2.setForeground(Color.RED);
		lableHealth2.setFont(new Font("Monospaced", Font.BOLD, 20));
		lableHealth2.setBounds(800, 45, SCREEN_WIDTH, 60);
		getContentPane().add(lableHealth2);
		getContentPane().setComponentZOrder(lableHealth2, 0);
		
		lableBotom = new JLabel("", SwingConstants.CENTER);
		lableBotom.setForeground(Color.PINK);
		lableBotom.setFont(new Font("SansSerif", Font.BOLD, 40));
		lableBotom.setBounds(SCREEN_WIDTH/2 -200, 45, 400, 1200);
		getContentPane().add(lableBotom);
		getContentPane().setComponentZOrder(lableBotom, 0);

	}
	
	
	public double limit(double value) {
	    return Math.max(0, Math.min(value, 100));
	}

	public void start()
	{
		Thread thread = new Thread()
		{
			public void run()
			{
				animationLoop();
				System.out.println("run() complete");
			}
		};

		thread.start();
		System.out.println("main() complete");

	}	
	private void animationLoop() {

		universe = animation.getNextUniverse();
		universeLevel++;

		while (stop == false && universe != null) {

			sprites = universe.getSprites();
			player1 = universe.getPlayer1();
			player2 = universe.getPlayer2();
			camera = universe.getCamera();
			backgrounds = universe.getBackgrounds();
			centreOnPlayer = universe.centerOnPlayer();
			this.scale = universe.getScale();

			// main game loop
			while (stop == false && universe.isComplete() == false) {

				//adapted from http://www.java-gaming.org/index.php?topic=24220.0
				last_refresh_time = System.currentTimeMillis();
				next_refresh_time = current_time + minimum_delta_time;

				//sleep until the next refresh time
				while (current_time < next_refresh_time)
				{
					//allow other threads (i.e. the Swing thread) to do its work
					Thread.yield();

					try {
						Thread.sleep(1);
					}
					catch(Exception e) {    					
					} 

					//track current time
					current_time = System.currentTimeMillis();
				}

				//read input
				keyboard.poll();
				handleKeyboardInput();

				//UPDATE STATE
				updateTime();
				
				universe.update(keyboard, actual_delta_time);

				//REFRESH
				if (camera != null && centreOnPlayer) {
					logicalCenterX = camera.getCenterX();
					logicalCenterY = camera.getCenterY();     
				}
				else {
					this.logicalCenterX = universe.getXCenter();
					this.logicalCenterY = universe.getYCenter();
				}
				
				updateControls();
				this.repaint();
			}

			universe = animation.getNextUniverse();
			keyboard.poll();

		}

		System.out.println("animation complete");
		AudioPlayer.setStopAll(true);
		dispose();	

	}
	
	private void updateControls() {
		
		double player1Hp = (((Player) player1).getHealth());
		double player2Hp = (((Player) player2).getHealth());
		
		if (start == true) {
			this.lableHealth.setText(String.format("PLAYER ONE: %d%%", (int) limit(player1Hp)));
			this.lableHealth2.setText(String.format("PLAYER TWO: %d%%", (int) limit(player2Hp)));
		}
		
		
		if (player2Hp <= 0) {
			this.lableBotom.setText(String.format("PLAYER ONE WINS"));
		}
		else if (player1Hp <= 0) {
			this.lableBotom.setText(String.format("PLAYER TWO WINS"));
		}

	}

	private void updateTime() {

		current_time = System.currentTimeMillis();
		actual_delta_time = (isPaused ? 0 : current_time - last_refresh_time);
		last_refresh_time = current_time;
		elapsed_time += actual_delta_time;

	}

	protected void btnPauseRun_mouseClicked(MouseEvent arg0) {
		if (isPaused) {
			isPaused = false;
			this.buttonPauseRun.setText("||");
		}
		else {
			isPaused = true;
			this.buttonPauseRun.setText(">");
		}
	}
	
	protected void btnStartRun_mouseClicked(MouseEvent arg0) {
		if (!start) {
			start = true;
			universe.setComplete(true);
			universeLevel++;
			buttonAIRun.setVisible(false);
			buttonStartRun.setVisible(false);

		}
	}
	
	protected void btnAIRun_mouseClicked(MouseEvent arg0) {
		if (!start) {	
			start = true;
			animation.getNextUniverse();
			universe.setComplete(true);
			universeLevel += 2;
			buttonAIRun.setVisible(false);
			buttonStartRun.setVisible(false);
		}
	}

	private void handleKeyboardInput() {
		
		
		
		if (keyboard.keyDown(80) && ! isPaused) {
			btnPauseRun_mouseClicked(null);	
		}
		if (keyboard.keyDown(79) && isPaused ) {
			btnPauseRun_mouseClicked(null);
		}
		if (keyboard.keyDown(112)) {
			scale *= 1.01;
		}
		if (keyboard.keyDown(113)) {
			scale /= 1.01;
		}
		
		if (keyboard.keyDown(65)) {
			screenCenterX -= 1;
		}
		if (keyboard.keyDown(68)) {
			screenCenterX += 1;
		}
		if (keyboard.keyDown(83)) {
			screenCenterY -= 1;
		}
		if (keyboard.keyDown(88)) {
			screenCenterY += 1;
		}
		
	}

	class DrawPanel extends JPanel {

		public void paintComponent(Graphics g)
		{	
			if (universe == null) {
				return;
			}
			
			if (backgrounds != null) {
				for (Background background: backgrounds) {
					paintBackground(g, background);
				}
			}

			if (sprites != null) {
				for (DisplayableSprite activeSprite : sprites) {
					DisplayableSprite sprite = activeSprite;
					if (sprite.getVisible()) {
						if (sprite.getImage() != null) {
							g.drawImage(sprite.getImage(), translateToScreenX(sprite.getMinX()), translateToScreenY(sprite.getMinY()), scaleLogicalX(sprite.getWidth()), scaleLogicalY(sprite.getHeight()), null);
						}
						else {
							g.setColor(Color.RED);
							g.fillRect(translateToScreenX(sprite.getMinX()), translateToScreenY(sprite.getMinY()), scaleLogicalX(sprite.getWidth()), scaleLogicalY(sprite.getHeight()));
						}
					}
				}				
			}
		}
		
		private void paintBackground(Graphics g, Background background) {
			
			if ((g == null) || (background == null)) {
				return;
			}
			
			//what tile covers the top-left corner?
			double logicalLeft = (logicalCenterX  - (screenCenterX / scale) - background.getShiftX());
			double logicalTop =  (logicalCenterY - (screenCenterY / scale) - background.getShiftY()) ;
						
			int row = background.getRow((int)(logicalTop - background.getShiftY() ));
			int col = background.getCol((int)(logicalLeft - background.getShiftX()  ));
			Tile tile = background.getTile(col, row);
			
			boolean rowDrawn = false;
			boolean screenDrawn = false;
			while (screenDrawn == false) {
				while (rowDrawn == false) {
					tile = background.getTile(col, row);
					if (tile.getWidth() <= 0 || tile.getHeight() <= 0) {
						//no increase in width; will cause an infinite loop, so consider this screen to be done
						g.setColor(Color.GRAY);
						g.fillRect(0,0, SCREEN_WIDTH, SCREEN_HEIGHT);					
						rowDrawn = true;
						screenDrawn = true;						
					}
					else {
						Tile nextTile = background.getTile(col+1, row+1);
						int width = translateToScreenX(nextTile.getMinX()) - translateToScreenX(tile.getMinX());
						int height = translateToScreenY(nextTile.getMinY()) - translateToScreenY(tile.getMinY());
						g.drawImage(tile.getImage(), translateToScreenX(tile.getMinX() + background.getShiftX()), translateToScreenY(tile.getMinY() + background.getShiftY()), width, height, null);
					}					
					//does the RHE of this tile extend past the RHE of the visible area?
					if (translateToScreenX(tile.getMinX() + background.getShiftX() + tile.getWidth()) > SCREEN_WIDTH || tile.isOutOfBounds()) {
						rowDrawn = true;
					}
					else {
						col++;
					}
				}
				//does the bottom edge of this tile extend past the bottom edge of the visible area?
				if (translateToScreenY(tile.getMinY() + background.getShiftY() + tile.getHeight()) > SCREEN_HEIGHT || tile.isOutOfBounds()) {
					screenDrawn = true;
				}
				else {
					col = background.getCol(logicalLeft);
					row++;
					rowDrawn = false;
				}
			}
		}				
	}

	private int translateToScreenX(double logicalX) {
		return screenCenterX + scaleLogicalX(logicalX - logicalCenterX);
	}		
	private int scaleLogicalX(double logicalX) {
		return (int) Math.round(scale * logicalX);
	}
	private int translateToScreenY(double logicalY) {
		return screenCenterY + scaleLogicalY(logicalY - logicalCenterY);
	}		
	private int scaleLogicalY(double logicalY) {
		return (int) Math.round(scale * logicalY);
	}

	private double translateToLogicalX(int screenX) {
		int offset = screenX - screenCenterX;
		return offset / scale;
	}
	private double translateToLogicalY(int screenY) {
		int offset = screenY - screenCenterY;
		return offset / scale;			
	}
	
	protected void contentPane_mouseMoved(MouseEvent e) {
		MouseInput.screenX = e.getX();
		MouseInput.screenY = e.getY();
		MouseInput.logicalX = translateToLogicalX(MouseInput.screenX);
		MouseInput.logicalY = translateToLogicalY(MouseInput.screenY);
	}

	protected void this_windowClosing(WindowEvent e) {
		System.out.println("windowClosing()");
		stop = true;
		dispose();	
	}
}
