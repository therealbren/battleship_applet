// Battleship
// CS 201
// John Bowllan and Brendan Murphy
// fieldBattleshipCanvas class

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

@SuppressWarnings("serial")

class fieldBattleshipCanvas extends Canvas implements MouseListener {
	static final Color lightBlue = new Color(200, 220, 255);
	static final Color tintBlue = new Color(0,191,255);
	static final Color darkRed = new Color(220, 0, 70);

	// instance variables
	int N = 10;
	int[][] fieldBoard = new int[N][N];
	int size = 26;
	int sunkCount;
	boolean boxSelected;
	String[] allDirections = {"North", "South", "East", "West"};
	Random rand1 = new Random();
	Random rand2 = new Random();
	int border = 40;
	boolean canClick; 
	boolean strikeApplicable;

	/*
	 We use integers to specify the various states of the squares on the grid:
	 
	 		0 represents an empty square
		    1 represents an empty square that is highlighted
		    2 represents an empty square that is revealed to be empty
		       to the user (i.e. a miss)
		    3 represents a square that contains a ship
		    4 represents a square that contains a ship that is highlighted
		    5 represents a hit on a ship
	*/
	
	// Constructing a new board with random boat configurations for the CPU
	public void newGame() {
		sunkCount = 0;
		boxSelected = false;
		canClick = false;
		int x = 0;
		
		for (int row = 0; row < N; row++) {
			for (int col = 0; col < N; col++) {
				fieldBoard[row][col] = x;
			}
		}
		setBoats(5);
		setBoats(4);
		setBoats(3);
		setBoats(2);
		// once the 'New Game' button is pressed, all of the ship choices
		// will be re presented to the user.
		BattleshipApplet.shipChoice.removeAll();
		BattleshipApplet.shipChoice.setBackground(Color.white);
		BattleshipApplet.shipChoice.setForeground(Color.black);
		BattleshipApplet.shipChoice.add("Carrier(5)");
		BattleshipApplet.shipChoice.add("Battleship(4)");
		BattleshipApplet.shipChoice.add("Submarine(3)");
		BattleshipApplet.shipChoice.add("Patrol Boat(2)");
		repaint();
	}

	public void setBoats(int shipLength){
		// sets a boat of size 'shipLength' with a random orientation in 
		// a random spot on the grid
		Random randomNum = new Random();
		int direction1 = randomNum.nextInt(4);
		String shipDirection = allDirections[direction1];
			
		int x = rand1.nextInt(10);
		int y = rand2.nextInt(10);
		 
        if (isValid(x, y, shipDirection, shipLength)) {
        	if (shipDirection == "North"){
		 		for (int k = 0; k < shipLength; k++) 
		 			fieldBoard[x][y-k] = 3;
        	} else if (shipDirection == "South") {
		 		for (int k = 0; k < shipLength; k++)
		 			fieldBoard[x][y+k] = 3;
		 	} else if (shipDirection == "East") {
		 		for (int k = 0; k < shipLength; k++)
		 			fieldBoard[x+k][y] = 3;
		 	} else { // (shipDirection == "North") 
		 		for (int k = 0; k < shipLength; k++)
		 			fieldBoard[x-k][y] = 3;
		 	}
        } else 
        	setBoats(shipLength);
	}
			 	 
	public boolean isValid(int x, int y, String direction, int shipLength) {
		// determines whether or not a ship of size 'shipLength' with a given 
		// orientation 'direction' can fit in the alloted squares
		if (y<= 9 && y >=0 && x >= 0 && x <= 9) {
			if (fieldBoard[x][y] == 0 && shipLength == 1)
				return true;
			else if (fieldBoard[x][y] == 3)
		 		return false;
		 	else {
		 		if (direction == "North") 
		 	 		return isValid(x, y-1, direction, shipLength - 1);
		 		else if (direction == "South")
		     		return isValid(x, y+1, direction, shipLength - 1);
		 		else if (direction == "East")
		 			return isValid(x+1, y, direction, shipLength - 1);
		 		else // if (direction == "West") {
		 			return isValid(x-1, y, direction, shipLength - 1);
		 	}
		} else 
			return false;
	}	
	 
	// draw the board
	public void paint(Graphics g) {
		for (int rows = 0; rows < N; rows++) {
			for (int cols = 0; cols < N; cols++) {
				if (fieldBoard[rows][cols] == 0 ||
						fieldBoard[rows][cols] == 3)
					g.setColor(lightBlue);
				else if (fieldBoard[rows][cols] == 1 ||
						fieldBoard[rows][cols] == 4)
					g.setColor(tintBlue);
				else if (fieldBoard[rows][cols] == 2)
					g.setColor(Color.WHITE);
				else	// if (playerBoard[rows][cols] == 5)
					g.setColor(Color.RED);
				int x = (rows * size) + border;
				int y = (cols * size) + border;
	            g.fillRect(x, y, size, size);
	            g.setColor(Color.BLACK);
	            g.drawRect(x, y, size, size);
			}
		}
		if (sunkCount == 14){ // if the user sinks all the ships
			g.setFont(new Font("TimesRoman", Font.BOLD, 48));
			g.drawString("You win!", 100, 100);
		}
	}

	public void set() {
		/* method called when the 'Set my ships' button is pressed.
		 When the user is in the process of setting his/her ships, the 
		 user is not permitted to click or affect this canvas. But once
		 the button is pressed, the game commences and the user is allowed 
		 to click and strike the board.
		  */
		 
		canClick = true;
		repaint();
	}

	public void strike() {
		// method called when the 'Strike' button is pressed
		if (canClick){
		boxSelected = false;
		for (int rows = 0; rows < N; rows++) {
			for (int cols = 0; cols < N; cols++) {
				if (fieldBoard[rows][cols] == 1) fieldBoard[rows][cols] += 1;
	            if (fieldBoard[rows][cols] == 4){
	            	fieldBoard[rows][cols] += 1;
	            	sunkCount += 1;
	            }
			}
		}
		strikeApplicable = false;
		}
		repaint();
	}
	
	// methods handling Mouse events
	
	public void mousePressed(MouseEvent event) { }

	public void mouseReleased(MouseEvent event) { }

	// handles mouse clicking on the playerField; fires at CPU
    public void mouseClicked(MouseEvent event) {
    	if (canClick){ // if the ships have been set and the game has commenced
    	Point p = event.getPoint();

        // check if clicked in box area

        int x = p.x - border;
        int y = p.y - border;

        if (x >= 0 && x < N*size &&
            y >= 0 && y < N*size) {

            int xCoord = x / size;
            int yCoord = y / size;

            if (fieldBoard[xCoord][yCoord] == 0 ||
            		fieldBoard[xCoord][yCoord] == 3) {
            	if (!boxSelected) {
            		fieldBoard[xCoord][yCoord] += 1;
            		boxSelected = true;
            		strikeApplicable = true;
            	}
            } else if (fieldBoard[xCoord][yCoord] == 1 ||
            			fieldBoard[xCoord][yCoord] == 4) {
            	fieldBoard[xCoord][yCoord] -= 1;
            	boxSelected = false;
            }
        }
    	}
        repaint();
    }
    public void mouseEntered(MouseEvent event) { }
    public void mouseExited(MouseEvent event) { }
}