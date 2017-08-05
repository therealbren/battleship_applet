// Battleship
// CS 201
// John Bowllan and Brendan Murphy
// BattleshipCanvas class

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;


@SuppressWarnings("serial")

public class BattleshipCanvas extends Canvas implements MouseListener {
	static final Color lightBlue = new Color(200, 220, 255);
	static final Color tintBlue = new Color(0,191,255);
	static final Color darkRed = new Color(220, 0, 70);

	// instance variables
	int N = 10;
	int[][] playerBoard = new int[N][N];
	int size = 26;
	int border = 40;
	boolean setup;
	int sunkCount;
	Random rn = new Random();
	String[] allDirections = {"North", "South", "East", "West"};
	int[] boatSizes = {2, 3, 4, 5};
	boolean canClick;
	
	/*
	 We use integers to specify the various states of the squares on the grid:
	 
	 		0 represents an empty square
		    1 represents an empty square that is revealed to be empty
		       to the user (i.e. a miss)
		    2 represents a square that contains a ship
		    3 represents a hit on a ship
	
	*/

	// Constructing a new board
	public void newGame() {
		int x = 0;
		setup = true;
		canClick = true;
		sunkCount = 0;
		for (int row = 0; row < N; row++) {
			for (int col = 0; col < N; col++) {
				playerBoard[row][col] = x;
			}
		}
		repaint();
	}

	// draw the board
	public void paint(Graphics g) {
		for (int rows = 0; rows < N; rows++) {
			for (int cols = 0; cols < N; cols++) {
				if (playerBoard[rows][cols] == 0)
					g.setColor(lightBlue);
				else if (playerBoard[rows][cols] == 1)
					g.setColor(Color.WHITE);
				else if (playerBoard[rows][cols] == 2)
					g.setColor(Color.GRAY);
				else	// if (playerBoard[rows][cols] == 3)
					g.setColor(Color.RED);
				int x = (rows * size) + border;
				int y = (cols * size) + border;
	            g.fillRect(x, y, size, size);
	            g.setColor(Color.BLACK);
	            g.drawRect(x, y, size, size);
			}
		}
		if (sunkCount == 14){ // if the CPU sinks all the ships
			g.setFont(new Font("TimesRoman", Font.BOLD, 48));
			g.drawString("CPU wins!", 100, 100);
			canClick = false;
		}
	}

	public boolean isValid(int x, int y, String direction, int shipLength) {
		// determines whether or not a ship of size 'shipLength' with a given 
		// orientation 'direction' can fit in the alloted squares
		 if (y<= 9 && y >=0 && x >= 0 && x <= 9) {
		 	if (playerBoard[x][y] == 0 && shipLength == 1)
		 		return true;
		 	else if (playerBoard[x][y] == 2)
		 		return false;
		 	else {
		 		if (direction == "North") 
		 	 		return isValid(x, y-1, direction, shipLength - 1);
		 		else if (direction == "South")
		     		return isValid(x, y+1, direction, shipLength - 1);
		 		else if (direction == "East")
		 			return isValid(x+1, y, direction, shipLength - 1);
		 		else // if (direction == "West") 
		 			return isValid(x-1, y, direction, shipLength - 1);
		 	}
		 } else 
			 return false;
	 }
	
	public void set() {
		// method called when the 'Set my ships' button is pressed
		setup = !setup;
		repaint();
	}

	public void strike() {
		// method called when the 'Strike' button is pressed
		if (canClick){
		int x = rn.nextInt(N);
		int y = rn.nextInt(N);
		while (playerBoard[x][y] == 1 || playerBoard[x][y] == 3) {
			x = rn.nextInt(N);
			y = rn.nextInt(N);
		}	
		for (int n = 0; n < 2; n++) { // the CPU will shoot at two adjacent squares
			for (int m = 0; m < 1; m++) {
				int a = (x+n) % N;
				int b = (y+m) % N;
	            if (playerBoard[a][b] == 0) playerBoard[a][b] += 1;
	            if (playerBoard[a][b] == 2){
	            	playerBoard[a][b] += 1;
	            	sunkCount += 1;
	            }
			}
		}
		}
		repaint();
	}


	public int getShipLength(String ship) {
		// returns the lengths of the various ships
    	int shipLength;
    	
    	if (ship == "Carrier(5)") 
    		shipLength = 5;
    	else if (ship == "Battleship(4)")
    		shipLength = 4;
    	else if (ship == "Submarine(3)")
    		shipLength = 3;
    	else // (ship == "Patrol Boat(2)")
    		shipLength = 2;
    	
    	return shipLength;
	}
	
	public String getShipDirection(String direction) {
		// returns the appropriate string representation of a ships orientation
    	String orientation;
    	
    	if (direction == "Up") 
    		orientation = "North";
    	else if (direction == "Down") 
    		orientation = "South";
    	else if (direction == "Left") 
    		orientation = "West";
    	else // (direction == "Right")
    		orientation = "East";
    	
    	return orientation;
	}
	
	
	// methods handling Mouse events
	public void mousePressed(MouseEvent event) { }

	public void mouseReleased(MouseEvent event) { }
	

	// handles mouse clicking on the playerField; fires at CPU
    public void mouseClicked(MouseEvent event) {
    	String ship = BattleshipApplet.shipChoice.getSelectedItem();
    	int shipLength = getShipLength(ship);
    	String direction = BattleshipApplet.directionChoice.getSelectedItem();
    	String shipDirection = getShipDirection(direction);
    	Point p = event.getPoint();

        // check if clicked in box area
    	
    	// the canvas will automatically not allow the user to click on it 
    	// once the user has placed all of his/her ships on the board 
    	if (setup && BattleshipApplet.shipChoice.getItemCount() != 0) {
	        int xCoord = p.x - border;
	        int yCoord = p.y -  border;	        
    	
            int x = xCoord / size;
            int y = yCoord / size;
	     
	        if (isValid(x, y, shipDirection, shipLength)) {
	        	if (shipDirection == "North"){
			 		for (int k = 0; k < shipLength; k++) 
			 			playerBoard[x][y-k] = 2;
	        	} else if (shipDirection == "South") {
			 		for (int k = 0; k < shipLength; k++)
			 			playerBoard[x][y+k] = 2;
			 	} else if (shipDirection == "East") {
			 		for (int k = 0; k < shipLength; k++)
			 			playerBoard[x+k][y] = 2;
			 	} else { // (shipDirection == "North") {
			 		for (int k = 0; k < shipLength; k++)
			 			playerBoard[x-k][y] = 2;
			 	}
	        	// the following conditionals remove a specific ship choice once that
	        	// ship has been placed on the grid
	        	if (BattleshipApplet.shipChoice.getSelectedItem() == "Carrier(5)")
	           	 	BattleshipApplet.shipChoice.remove("Carrier(5)");
	       	 	else if (BattleshipApplet.shipChoice.getSelectedItem() == "Battleship(4)")
	       	 		BattleshipApplet.shipChoice.remove("Battleship(4)");
	       	 	else if (BattleshipApplet.shipChoice.getSelectedItem() == "Submarine(3)")
	       	 		BattleshipApplet.shipChoice.remove("Submarine(3)");
	       	 	else if (BattleshipApplet.shipChoice.getSelectedItem() == "Patrol Boat(2)")
	       	 		BattleshipApplet.shipChoice.remove("Patrol Boat(2)");
	        }
    	}
     repaint();
    }
    
    public void mouseEntered(MouseEvent event) { }
    
    public void mouseExited(MouseEvent event) { }
}

