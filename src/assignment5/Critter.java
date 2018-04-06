package assignment5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import assignment5.Main.scaleCanvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;


public abstract class Critter {
	/* NEW FOR PROJECT 5 */

	public static boolean gridFlag = false;

	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 
	
	public static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	protected final String look(int direction, boolean steps) {
		int numSteps = !steps ? 1 : 2;
		int[] newCoord = move(direction);
		if(isOccupied(newCoord[0],newCoord[1])) {
			for(Critter c: population) {
				if(c.compareLocation(newCoord[0],newCoord[1])) {
					return c.toString();
				}
			}
		}

		return null;

	}
	
	/* rest is unchanged from Project 4 */
	
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }

	private boolean hasMoved = false;
	private boolean hasFought = false;
	
	private int x_coord;
	private int y_coord;

	/**
	 * Deducts walking energy and moves 1 step in the specified direction
	 * @param direction 0 to 7.
	 */
	protected final void walk(int direction) {
		energy -= Params.walk_energy_cost;

		if(!hasMoved && !hasFought) {
			int[] newCoord = move(direction);
			x_coord = newCoord[0];
			y_coord = newCoord[1];
			hasMoved =  true;
		}

		//encounters walk
		else if(!hasMoved) {
			int[] newCoord = move(direction);
			if(!isOccupied(newCoord[0],newCoord[1])) {
				x_coord = newCoord[0];
				y_coord = newCoord[1];
				hasMoved =  true;
			}
		}
	}

	/**
	 * Deducts running energy and moves the critter 2 steps in the specified direction if possible
	 * @param direction 0 to 7
	 */
	protected final void run(int direction) {
		energy -= Params.run_energy_cost;

		if(!hasMoved && !hasFought) {
			int[] newCoord = move(direction);
			x_coord = newCoord[0];
			y_coord = newCoord[1];
			newCoord = move(direction);
			x_coord = newCoord[0];
			y_coord = newCoord[1];
			hasMoved = true;
		}

		else if(!hasMoved && hasFought) {
			int holdX = x_coord;
			int holdY = y_coord;
			int[] newCoord = move(direction);
			x_coord = newCoord[0];
			y_coord = newCoord[1];
			newCoord = move(direction);
			if(!isOccupied(newCoord[0],newCoord[1])) {
				x_coord = newCoord[0];
				y_coord = newCoord[1];
				hasMoved =  true;
			}else{
				x_coord = holdX;
				y_coord = holdY;
			}
		}
	}

	/**
	 * Tests to see if location isOccupied by any creatures
	 * @param x coordinate
	 * @param y coordinate
	 * @return true or false
	 */
	private boolean isOccupied(int x, int y) {
		for(Critter c: population) {
			if(c.compareLocation(x,y)) {
//                System.out.println("OCCUPIED!");

				return true;
			}
		}
		return false;
	}

	/**
	 * Helper function for walk and run to give the new coordinates of the desired move
	 * @param direction 0 to 7
	 * @return and int[] array containing the new x and y coordinates
	 */
	private int[] move(int direction) {
		int[] coords = new int[2];
		//[0] = x
		//[1] = y

		switch (direction) {
			case 0:
				coords[0] = x_coord + 1;
				coords[1] = y_coord;
				break;

			case 1:
				coords[0] = x_coord + 1;
				coords[1] = y_coord - 1;
				break;

			case 2:
				coords[0] = x_coord;
				coords[1] = y_coord - 1;
				break;

			case 3:
				coords[0] = x_coord - 1;
				coords[1] = y_coord - 1;
				break;

			case 4:
				coords[0] = x_coord - 1;
				coords[1] = y_coord;
				break;

			case 5:
				coords[0] = x_coord - 1;
				coords[1] = y_coord + 1;
				break;

			case 6:
				coords[0] = x_coord;
				coords[1] = y_coord + 1;
//                y_coord++;
				break;

			case 7:
				coords[0] = x_coord + 1;
				coords[1] = y_coord + 1;
				break;
		}

		if(coords[0] >= Params.world_width) {
			coords[0] = 0;
		} else if(coords[0] < 0) {
			coords[0] = Params.world_width - 1;
		}

		if(coords[1] >= Params.world_height) {
			coords[1] = 0;
		} else if(coords[1] < 0) {
			coords[1] = Params.world_height - 1;
		}

		return coords;
	}

	/**
	 * Checks to see if the Critter's coordinates are equal to the specified paramters
	 * @param x the x coordinate to be compared to
	 * @param y the y coordinate to be compared to
	 * @return true if the locations are equal, false if not
	 */
	private boolean compareLocation(int x, int y) {
		return x == x_coord && y == y_coord;
	}

	/**
	 * Makes the Critter reproduce a child using half of its energy in an adjacent spot
	 * @param offspring the child to be created
	 * @param direction 0 to 7 to specify the location of the child after birth
	 */
	protected final void reproduce(Critter offspring, int direction) {
//		System.out.println("Reproducing");
		if(this.energy < Params.min_reproduce_energy) {
			return;
		}
		this.energy = (this.energy + 1)/2;
		offspring.energy = (this.energy)/2;
		int[] offspringCoords = move(direction);
		offspring.x_coord = offspringCoords[0];
		offspring.y_coord = offspringCoords[1];
		babies.add(offspring);
	}

	/**
	 * Is called for every Critter during worldTimeStep. This is where each Critter specifies its normal actions
	 */
	public abstract void doTimeStep();

	/**
	 * Determines what a Critter does when it encounters another Critter
	 * @param opponent the toString of the other Critter it encounters
	 * @return true if it wants to fight, false if it doesn't
	 */
	public abstract boolean fight(String opponent);
	
	
	public static void worldTimeStep() {
		//Do time steps
		for(Critter c: population) {
			c.doTimeStep();
		}

		ArrayList<ArrayList<ArrayList<Critter>>> world = new ArrayList<ArrayList<ArrayList<Critter>>>();
		for(int y = 0; y < Params.world_height; y++) {
			world.add(new ArrayList<ArrayList<Critter>>());
			for (int x = 0; x < Params.world_width; x++) {
				world.get(y).add(new ArrayList<Critter>());
			}
		}

//		System.out.println(world.size());
//		System.out.println(world.get(0).size());

		//Find all encounters
		ArrayList<ArrayList<Critter>> encounters = new ArrayList<>();
//        for(int y = 0; y < Params.world_height; y++) {
//	        for (int x = 0; x < Params.world_width; x++) {
//				encounters.add(getCrittersAtLocation(x,y));
//            }
//        }
		for (Critter c: population) {
			world.get(c.y_coord).get(c.x_coord).add(c);
		}

		for(int y = 0; y < Params.world_height; y++) {
			for (int x = 0; x < Params.world_width; x++) {
				encounters.add(world.get(y).get(x));
			}
		}



		//Settle encounters
		for(ArrayList<Critter> cell: encounters) {
			while (cell.size() > 1) { //while multiple critters are on the same spot

				Critter A = cell.get(0);
				Critter B = cell.get(1);

				int fightLocationX = A.x_coord;
				int fightLocationY = B.y_coord;

				if (A.energy <= 0) {
					population.remove(A);
					cell.remove(A);
					continue;
				}

				if (B.energy <= 0) {
					population.remove(B);
					cell.remove(B);
					continue;
				}

//                System.out.println(A.toString() + "'s Energy: " + A.energy);
//                System.out.println(B.toString() + "'s Energy: " + B.energy);
//                System.out.println("Fight between " + A.toString() + " and " + B.toString());

				A.hasFought = true;
				boolean AwantsToFight = A.fight(B.toString());

				B.hasFought = true;
				boolean BwantsToFight = B.fight(A.toString());

				if (!A.compareLocation(fightLocationX, fightLocationY) && !B.compareLocation(fightLocationX, fightLocationY)) {
					cell.remove(A);
					cell.remove(B);
					continue;
				} else if (!A.compareLocation(fightLocationX, fightLocationY) && B.compareLocation(fightLocationX, fightLocationY)) {
					cell.remove(A);
					continue;
				} else if (A.compareLocation(fightLocationX, fightLocationY) && !B.compareLocation(fightLocationX, fightLocationY)) {
					cell.remove(B);
					continue;
				}

				if ((A.x_coord == B.x_coord) && (A.y_coord == B.y_coord)) {

					int A_roll;
					int B_roll;

					if (AwantsToFight && A.energy > 0) {
						A_roll = getRandomInt(A.energy);
					} else {
						A_roll = 0;
					}

					if (BwantsToFight && B.energy > 0) {
						B_roll = getRandomInt(B.energy);
					} else {
						B_roll = 0;
					}

					//A wins
					if (A_roll >= B_roll) {
						A.energy += (B.energy / 2);
						population.remove(B);
						cell.remove(B);
					}

					//B wins
					else {
						B.energy += (A.energy / 2);
						population.remove(A);
						cell.remove(A);
					}
				}
			}
		}

		//update rest energy and flags
		for (Iterator<Critter> iterator = population.iterator(); iterator.hasNext();) {
			Critter c = iterator.next();
			c.hasMoved = false;
			c.hasFought = false;
			c.energy -= Params.rest_energy_cost;

			if (c.energy <= 0) {
				// Remove the current element from the iterator and the list.
				iterator.remove();
			}
		}

		//create new algae
//		genAlgae();

		//add babies
		population.addAll(babies);
		babies.clear();

	}

	private static void genAlgae() {
		for(int i = 0; i < Params.refresh_algae_count; i++) {
			try {
				Critter.makeCritter("Algae");
			} catch (Exception e) {
				System.out.println("error generating Algae");
			}
		}
	}

	public static void displayWorld(Object pane) { 
	/* Alternate displayWorld, where you use Main.<pane> to reach into your
	   display component.
	   // public static void displayWorld() {}
	*/
		int width = (int)((scaleCanvas)pane).width;
		int height = (int)((scaleCanvas)pane).height;

		int totalWidth = width - (width%Params.world_width);
		int totalHeight = height -(height%Params.world_height);

		int cellDimension = (totalWidth/Params.world_width) < (totalHeight/Params.world_height) ?
				(totalWidth/Params.world_width) : (totalHeight/Params.world_height);

		int cellWidth = cellDimension;
		int cellHeight = cellDimension;
		
		int squareLen = cellWidth < cellHeight ? cellWidth : cellHeight;
		squareLen = (squareLen-1)%2==1 ? squareLen-1 : squareLen-2;
		squareLen = squareLen>=0 ? squareLen: 0;
		
		
		GraphicsContext gc = Main.gc;
		gc.clearRect(0, 0, width, height);
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, width, height);
		gc.setStroke(Color.WHITE);
		
		boolean[][] critGrid = new boolean[Params.world_width][Params.world_height];
		for(int i = 0; i < Params.world_width; i++) {
			for(int j = 0; j < Params.world_height; j++) {
				critGrid[i][j] = false;
			}
		}
		
		if(gridFlag) {
			for (int i = 0; i < Params.world_width; i++) {
				for (int j = 0; j < Params.world_height; j++) {
					gc.setStroke(Color.BLACK);
					gc.strokeRect(i * cellWidth, j * cellHeight, cellWidth - 1, cellHeight - 1);
				}
			}
		}

		
		for(Critter c: population) {
			int x = c.x_coord;
			int y = c.y_coord;
			if(critGrid[x][y] == false) {
//				System.out.println("[x: " + x + " y: " + y + " ]  ");
				critGrid[x][y] = true;
				CritterShape cShape = c.viewShape();
				Color cFill = c.viewColor();
				Color cLine = cFill;
				if(!(c.viewFillColor().equals(Color.WHITE)) || !(c.viewOutlineColor().equals(Color.WHITE))) {
					cFill = c.viewFillColor();
					cLine = c.viewOutlineColor();
				}
//				System.out.println(squareLen + " " + cellWidth + " " + cellHeight + " " + totalWidth + " " + totalHeight);
				switch(cShape) {
					case CIRCLE:
						gc.setFill(cFill); 
						gc.fillOval(x*cellWidth, y*cellHeight, squareLen, squareLen);
						gc.setStroke(cLine);
						gc.strokeOval(x*cellWidth, y*cellHeight, squareLen, squareLen);
						break;
					case SQUARE:
						gc.setFill(cFill); 
						gc.fillRect(x*cellWidth, y*cellHeight, squareLen, squareLen);
						gc.setStroke(cLine);
						gc.strokeRect(x*cellWidth, y*cellHeight, squareLen, squareLen);
						break;
					case TRIANGLE:
						gc.setFill(cFill); 
						gc.fillPolygon(new double[]{x*cellWidth,(x*cellWidth+squareLen/2)+1, x*cellWidth+squareLen},
								new double[]{y*cellHeight, y*cellHeight+squareLen, y*cellHeight}, 3);
						gc.setStroke(cLine);
						gc.strokePolygon(new double[]{x*cellWidth,(x*cellWidth+squareLen/2)+1, x*cellWidth+squareLen},
								new double[]{y*cellHeight, y*cellHeight+squareLen, y*cellHeight}, 3);
						break;
					case DIAMOND:
						gc.setFill(cFill); 
						gc.fillPolygon(new double[]{x*cellWidth,(x*cellWidth+squareLen/2)+1, x*cellWidth+squareLen,(x*cellWidth+squareLen/2)+1},
								new double[]{(y*cellHeight+squareLen/2)+1, y*cellHeight, (y*cellHeight+squareLen/2)+1,y*cellHeight+squareLen},
								4);
						gc.setStroke(cLine);
						gc.strokePolygon(new double[]{x*cellWidth,(x*cellWidth+squareLen/2)+1, x*cellWidth+squareLen,(x*cellWidth+squareLen/2)+1},
								new double[]{(y*cellHeight+squareLen/2)+1, y*cellHeight, (y*cellHeight+squareLen/2)+1,y*cellHeight+squareLen},
								4);
						break;
					case STAR:
						int third = squareLen/4;
						gc.setFill(cFill); 
						gc.fillPolygon(new double[]{x*cellWidth,(x*cellWidth+squareLen/2)+1, x*cellWidth+squareLen},
								new double[]{y*cellHeight+third, y*cellHeight+squareLen, y*cellHeight+third}, 3);
						
						gc.fillPolygon(new double[]{x*cellWidth,(x*cellWidth+squareLen/2)+1, x*cellWidth+squareLen},
								new double[]{ y*cellHeight+(3*third),y*cellHeight, y*cellHeight+(3*third)}, 3);
						gc.setStroke(cLine);
						gc.strokePolygon(new double[]{x*cellWidth,(x*cellWidth+squareLen/2)+1, x*cellWidth+squareLen},
								new double[]{y*cellHeight+third, y*cellHeight+squareLen, y*cellHeight+third}, 3);
						gc.strokePolygon(new double[]{x*cellWidth,(x*cellWidth+squareLen/2)+1, x*cellWidth+squareLen},
								new double[]{ y*cellHeight+(3*third),y*cellHeight, y*cellHeight+(3*third)}, 3);
								
						break;
				}
			}
		}
		
		/*
		GraphicsContext gc = Main.gc;
		gc.clearRect(0, 0, width, height);
		gc.setStroke(Color.RED);
		gc.strokeLine(0, 0, width, height);
		gc.strokeLine(0, height, width, 0);
		*/
	
	}

	/**
	 * ASCII representation of the world
	 */
	/*
	public static void displayWorld() {

		//create the top and bottom border and empty world row
		String border = "+";
		String row = "|";
		for(int i = 0; i < Params.world_width; i++) {
			border += "-";
			row += " ";
		}
		border += "+";
		row += "|";

		//Add empty rows to world
		ArrayList<String> world = new ArrayList<>();
		for(int i = 0; i < Params.world_height; i++) {
			world.add(row);
		}

		//Top border
		System.out.println(border);

		//Put the critters in the correct positions
		for(Critter c: population) {
			int x = c.x_coord;
			int y = c.y_coord;

			String currentRow = world.get(y);
			String newRow = currentRow.substring(0, x + 1) + c.toString() + currentRow.substring(x + 2);
			world.set(y,newRow);
		}

		//Print each row of the world
		for(String s: world) {
			System.out.println(s);
		}

		//Bottom border
		System.out.println(border);

//		System.out.println("Number of critters: " + population.size());
//		Critter.runStats(population);
//		for(Critter c: population) {
//			System.out.println("X: " + c.x_coord);
//			System.out.println("Y: " + c.y_coord);
//		}
	}
	*/

	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name name of the Critter class to make
	 * @throws InvalidCritterException critter name is not correct
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try {
			Class critterType = Class.forName(myPackage + "." + critter_class_name); //"assignment4."
			Critter critter = (Critter) critterType.newInstance();
			critter.energy = Params.start_energy;
			critter.x_coord = getRandomInt(Params.world_width);
			critter.y_coord = getRandomInt(Params.world_height);
			population.add(critter);

		} catch (ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		} catch (InstantiationException e) {
			throw new InvalidCritterException(critter_class_name);
		} catch (IllegalAccessException e) {
			throw new InvalidCritterException(critter_class_name);
		}

	}
	
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		try {
			Class critterType = Class.forName(myPackage + "." + critter_class_name);
			for (Critter c: population) {
				if(critterType.isInstance(c)) {
					result.add(c);
				}
			}
		} catch (ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}

		return result;
	}
	
	public static String runStats(List<Critter> critters) {
		if(critters.size() == 0) {
			return "No critters of this type";
		}
		String name = critters.get(0).getClass().getName();
		name = name.substring((name.indexOf(".")+1)) + "s";
		return "There are " + critters.size() + " " + name;
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure thath the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctup update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}
	
	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population.clear();
		babies.clear();
	}
	
	
}
