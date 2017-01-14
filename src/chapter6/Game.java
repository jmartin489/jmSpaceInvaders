/**
 * 
 */
package chapter6;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * @author jmartin489
 *
 */
public class Game extends JFrame implements GameController {

	private static final long serialVersionUID = 1L;
	
	private BufferStrategy strategy;
	
	private static final int WIDTH = 800, HEIGHT = 600;
	
	// key definitions
	private final byte UP_KEY = 0x08;
	private final byte LEFT_KEY = 0x04;
	private final byte DOWN_KEY = 0x02;
	private final byte RIGHT_KEY = 0x01;
	private final byte SHOOT_KEY = 0x10;
	private byte keys = 0x0;
	
	//timing
	private Timer timer;
	private long lastLoopTime;
	private int fps = 24;
	private long refreshInterval = 0;
	
	
	//flags
	private boolean notRunning = true;
	private boolean dispose;
	private boolean logicRequiredThisLoop;
	private boolean lost, won;
	
	// ship parameters
	private Entity ship;
	private double shipMinSpeed = 50.0;
	private double shipMaxSpeed = 500.0;
	private double shipSpeed = shipMinSpeed;
	
	//shot parameters
	private boolean shotFired = false;
	private int firingInterval = 375;
	private long lastFired = 0L;
	
	// alien parameters
	public int alienStillAlive = 0;
	private Entity alienShip;
	public double alienShipSpeed = 5.0;
	
	// actor lists
	private ArrayList<Entity> entities = new ArrayList<>();
	private ArrayList<Entity> removeList = new ArrayList<>();
	
	//player related parameters
	private boolean userInputMode = true;
	private String playerName = "";
	
	public Player player;
	
	//scoreboard
	public Scoreboard scoreboard;
	
	private int score = 0;
	
	private Canvas canvas;
	
	public Game(){
		super("The Final Wave");
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		add(canvas);
		
		setSize(WIDTH, HEIGHT);
		
		canvas.setIgnoreRepaint(true);
		canvas.setFocusable(false);
		
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		addKeyListener(new KeyboardHandler());
		
		canvas.createBufferStrategy(2);
		strategy = canvas.getBufferStrategy();
		
		initEntities();
		
		timer = new Timer( (int) (1000.0/fps), new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				render();	
			}	
		});
		
		lastLoopTime = System.currentTimeMillis();	
		scoreboard = new Scoreboard();
		scoreboard.addPlayer(player);
			
	}
	
	private void render() {
		if (dispose)
			dispose();
		
		Graphics2D gc = (Graphics2D) strategy.getDrawGraphics();
		gc.setColor(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		
		displayMessage(playerName.toUpperCase()+" Score: " +score, gc, Color.white, -275);
		
		if (notRunning)
		{
			if(lost)
			{
				displayMessage("INVASION SUCCESSFUL GAME OVER", gc, Color.red, 0);
				try 
				{ // 2 second delay
					Thread.sleep(2000);
				}
				catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				score = 0;
			}
			else if (won)
			{
				
				displayMessage("*** YOU ARE WINNER! ***", gc, Color.green, 0);
			} 
			else if(userInputMode)
			{
				
				displayMessage("Enter your name: " + playerName, gc, Color.yellow, 40);
				
				
			} 
			else 
			{
					
				displayMessage("*** Space Invaders: Engage Harder ***", gc, Color.magenta, 0);
				displayMessage("Press any key " + playerName +"!", gc, Color.white, 40);

			}
			
		} else
		{
			//process the keys
			processKeysAndRelocateActors();
			// check for collisions
			processCollision();
		}
		
		for (Entity entity : entities)
			entity.draw(gc);
		
		entities.removeAll(removeList);
		removeList.clear();
		
		if (!won && alienStillAlive == 0 && removeList.isEmpty())
			notifyWin();
		
		
		if (logicRequiredThisLoop){
			for (Entity entity : entities)
				entity.doLogic();
			logicRequiredThisLoop = false;
		}
		
		if(shotFired){
			attemptToFire();
			shotFired = false;
		}
		
		gc.dispose();
		strategy.show();
		
		lastLoopTime = System.currentTimeMillis();

	}
	


	private void displayMessage(String message, Graphics2D gc, Color color, int voffset) {
		Color oldColor = gc.getColor();
		Font oldFont = gc.getFont();
		
		Font font = new Font("Courier New", Font.PLAIN, 16);
		gc.setFont(font);
		gc.setColor(color);
		
		int stringWidth = gc.getFontMetrics().stringWidth(message);
		gc.drawString(message, (WIDTH - stringWidth)/2, HEIGHT/2 + voffset);
		
		gc.setColor(oldColor);
		gc.setFont(oldFont);
	}

	private void processCollision() {
		for(int i = 0; i < entities.size(); i++){
			for(int j = i+1; j < entities.size(); j++){
				
				Entity thisActor = (Entity) entities.get(i);
				Entity thatActor = (Entity) entities.get(j);
				
				if(thatActor instanceof Alien)
					if(thatActor.locY + thatActor.getHeight() > canvas.getHeight())
						notifyLost();
				
				if(thisActor.detectCollision(thatActor)){
					if(thisActor instanceof SpaceShip)
						notifyLost();
					else if (thatActor instanceof Missile){
						SoundFX.COLLIDE.play();
					}
					removeList.add(thisActor);
					removeList.add(thatActor);
					if(thisActor instanceof AlienShip){
						alienShip();
						score +=1000;
						removeList.add(alienShip);
					} else {
						alienStillAlive--;
						speedUpAliens();
						score +=100;
					}
				}
			}
		}
		
	}
	
	private void notifyWin() {
		SoundFX.WIN.play();
		initFlags();
		lost = false;
		won = true;
		for (Entity e : entities)
			e.activate(false);
		
	}
	private void notifyLost() {
		SoundFX.LOSE.play();
		initFlags();
		lost = true;
		won = false;
		for (Entity e : entities)
			e.activate(false);
	}

	private void attemptToFire() {
		// gun jam
		if(System.currentTimeMillis() - lastFired < firingInterval)
			return;
		
		// okay to fire
		lastFired = System.currentTimeMillis();
		
		int locx = (int)ship.getLocX() + 10;
		int locy = (int)ship.getLocY() - 20;
		Missile shot = new Missile("sprites/projectileSpriteSheet.png", 
				3, locx, locy, this);
		entities.add(shot);
		
		SoundFX.SHOOT.play();
		
	}

	private void processKeysAndRelocateActors() {
		refreshInterval = System.currentTimeMillis() - lastLoopTime;
		
		// Actor processing
		setShipDirection();
		
		// move other actors
		for (Entity entity : entities)
			entity.updateLocation(refreshInterval);
	}


	private void setShipDirection() {
		ship.setHorizontalSpeed(0);
		
		// A
		if ((keys & LEFT_KEY) != 0x0)
			ship.setHorizontalSpeed(-shipSpeed);
		
		// D
		if ((keys & RIGHT_KEY) != 0x0)
			ship.setHorizontalSpeed(shipSpeed);
		
		if ( (keys & (LEFT_KEY | RIGHT_KEY) ) != 0){
			if (shipSpeed < shipMaxSpeed)
				shipSpeed += 10;
		} else {
			shipSpeed = shipMinSpeed;
		}
		
		if( (keys & SHOOT_KEY) != 0x0)
			shotFired = true;
		
	}


	private class KeyboardHandler extends KeyAdapter{
		
		private boolean processStartKey = true;
		
		@Override
		public void keyReleased(KeyEvent e){
			
			if(notRunning)
				return;
			
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				keys &= ~UP_KEY;
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				keys &= ~LEFT_KEY;
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				keys &= ~DOWN_KEY;
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				keys &= ~RIGHT_KEY;
				break;
			case KeyEvent.VK_SPACE:
				keys &= ~SHOOT_KEY;
				break;
			}
		}
		
		@Override
		public void keyPressed(KeyEvent e){
			
			if(notRunning)
				return;
			
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				keys |= UP_KEY;
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				keys |= LEFT_KEY;
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				keys |= DOWN_KEY;
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				keys |= RIGHT_KEY;
				break;
			case KeyEvent.VK_SPACE:
				keys |= SHOOT_KEY;
				break;
			case KeyEvent.VK_ESCAPE:
				dispose = true;
			}
		}
		
		@Override
		public void keyTyped(KeyEvent e){
			
			if(notRunning){
				if (userInputMode){
					char ch = e.getKeyChar();
					switch(ch){
					case KeyEvent.VK_ENTER:
						userInputMode = false;
						playerName = playerName.toUpperCase();
						break;
					case KeyEvent.VK_BACK_SPACE:
						if(playerName.length() > 0){
							String tmp = playerName.substring(0, playerName.length()-1);
							playerName = tmp;
							player = new Player(playerName);
						}
						break;
					default: 
						if(playerName.length() >=5){
							userInputMode = false;
							playerName = playerName.toUpperCase();
						} else
							playerName += ch;
						
						
					}
					
				
					
				}else if(processStartKey){
					startGame();
					notRunning = false;
					processStartKey = false;
				
			} else {
				processStartKey = true;
			}
		}
			
			if(e.getKeyChar() == KeyEvent.VK_ESCAPE)
				dispose = true;
		}
		
	}
	
	private void speedUpAliens(){
		double factor = 3.0;
		
		for (Entity entity : entities){
			
			if (entity instanceof Alien)
				entity.setHorizontalSpeed(entity.getHorizontalSpeed() * (1.0 + factor/100));
		}
	}
	
	private void startGame() {
		entities.clear();
		initEntities();
		for(Entity entity: entities)
			entity.activate(true);
		
	}
	
	private void initFlags(){
		notRunning = true;
		shipSpeed = 0;
		shotFired = false;
		logicRequiredThisLoop = false;
	
	}
	
	private void initEntities() {
		ship = new SpaceShip("sprites/shipSpriteSheet.png", 3, WIDTH/2 - 30, HEIGHT - 75, this);
		shipSpeed = 0;
		entities.add(ship);
		
		alienShip();
		
		//create an array of aliens
		alienStillAlive = 0;
		
		// Array of 5 x 12 aliens
		int maxRows = 5;
		int maxColumns = 12;
		
		for(int row = 0; row < maxRows; row++){
			for(int col = 0; col < maxColumns; col++){
				Entity alien = new Alien("sprites/alienSpriteSheet.png", 3, // imageCount
						HEIGHT/6 +(col *50), // x location 
						50 + row * 30, // y location
						this); // controller
				alienStillAlive++;
				entities.add(alien);
			}
		}
		
		for (Entity entity : entities)
			entity.activate(false);
		
	}
	
	
	public void start(){
		timer.start();
	}
	
	public void dispose(){
		super.setVisible(false);
		super.dispose();
		timer.stop();
		scoreboard.trim(0);
		scoreboard.backup();
		System.exit(0);
	}
	
	@Override
	public void onGameEvent(GameEvent e) {
		switch(e.getEvent()){
		case REMOVE_ENTITY:
			removeList.add(e.getEntity());
			break;
		case LOGIC_REQUIRED:
			logicRequiredThisLoop = true;
			break;
		default:
			break;
		}

	}

	public void alienShip(){
		alienShip = new AlienShip("sprites/alienShip.png", 3, WIDTH/30 - 200, HEIGHT - 600, this);
		alienShipSpeed = 0;
		entities.add(alienShip);
	}
	
	@Override
	public Rectangle getBounds() {
		return super.getBounds();
	}
	
	public void showScoreBoard()
	{
		scoreboard.addPlayer(player);
		scoreboard.backup();
		scoreboard.toString();
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Game g = new Game();
		g.start();

	}

}