/* CRITTERS Critter3.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <ThienSon Ho>
 * <tsh848>
 * <15505>
 * <Arjun Singh>
 * <AS78363>
 * <15505>
 * Slip days used: <0>
 * Spring 2018
 * 
 * The Critter3 class Establishes the first Critter3 as the queen. The queen documents her position relative to where
 * she started. The queen creates Drone Critter3s that go fight and collect energy for her to consume. All The Critter3s
 * work together to ensure the queens survival by providing her with energy; however the queen isn't the only critter that 
 * reproduces. Drones reproduce to ensure the survival of the colony and to collect more energy to give the queen.
 */
package assignment5;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Critter3 extends Critter{
	//Keep track of the number of Critter3s created
	static int count = 0;
	//An ArrayList for the relative position and energy of every Critter3 
	static ArrayList<int[]> position = new ArrayList<int[]>();
	//The indx in the ArrayList that holds the Critter3's data
	int index;
	//The number of times left that the Critter3 can reproduce
	int life;
	
	/**
	 * Default constructor for a Critter3
	 */
	public Critter3() {
		count++;
		this.index = count - 1;
		int[] newVal = {0,0,this.getEnergy()};
		position.add(newVal);
		life = 1000;
	}
	// The directions and vals
	//          2
	//       3  ^  1
	//        \ | /
	//    4 < - * - > 0
	//        / | \
	//       5  V  7
	//          6
	
	/**
	 * A Critter3 drone that responds to the direction it is created
	 * @param direction the direction the parent moved in its time step
	 * @param index the index of the parent
	 * @param life the number of times the new Critter3 can reproduce
	 */
	public Critter3(int direction, int index, int life) {
		//Gets the parent relative position
		int x = position.get(index)[0];
		int y = position.get(index)[1];
		this.life = life;
		
		//Determines this relative position based on parents move direction
		switch (direction) {
        case 0:
        	int[] newVal0 = {x - 1, y, this.getEnergy()};
        	position.add(newVal0);
            break;

        case 1:
        	int[] newVal1 = {x - 1, y + 1, this.getEnergy()};
        	position.add(newVal1);
            break;

        case 2:
        	int[] newVal2 = {x, y + 1, this.getEnergy()};
        	position.add(newVal2);
            break;

        case 3:
        	int[] newVal3 = {x + 1, y + 1, this.getEnergy()};
        	position.add(newVal3);
            break;

        case 4:
        	int[] newVal4 = {x + 1, y, this.getEnergy()};
        	position.add(newVal4);
            break;

        case 5:
        	int[] newVal5 = {x + 1, y - 1, this.getEnergy()};
        	position.add(newVal5);
            break;

        case 6:
        	int[] newVal6 = {x, y - 1, this.getEnergy()};
        	position.add(newVal6);
            break;

        case 7:
        	int[] newVal7 = {x - 1, y - 1, this.getEnergy()};
        	position.add(newVal7);
            break;
		}
	}
	
	/**
	 * Handles if and how the Critter3 moves
	 */
	@Override
	public void doTimeStep() {
		// If the critter can't breed or is weak, return to the queen
		if(life <= 0 || this.getEnergy() < 20) {
			//gets info regarding this and queen position
			int x = position.get(this.index)[0];
			int y = position.get(this.index)[1];
			int kingX = position.get(0)[0];
			int kingY = position.get(0)[1];
			
			//Solve for move direction
			if(x < kingX) {
				if(y < kingY) {
					walk(7);
					int[] newVal0 = {x + 1, y + 1, this.getEnergy()};
		        	position.set(this.index, newVal0);
				}else if(y > kingY) {
					walk(1);
					int[] newVal0 = {x + 1, y - 1, this.getEnergy()};
		        	position.set(this.index, newVal0);
				}else {
					walk(0);
					int[] newVal0 = {x + 1, y, this.getEnergy()};
		        	position.set(this.index, newVal0);
				}
			}else if(x > kingX) {
				if(y < kingY) {
					walk(5);
					int[] newVal0 = {x - 1, y + 1, this.getEnergy()};
		        	position.set(this.index, newVal0);
				}else if(y > kingY) {
					walk(3);
					int[] newVal0 = {x - 1, y - 1, this.getEnergy()};
		        	position.set(this.index, newVal0);
				}else {
					walk(4);
					int[] newVal0 = {x - 1, y, this.getEnergy()};
		        	position.set(this.index, newVal0);
				}
			}else {
				if(y < kingY) {
					walk(6);
					int[] newVal0 = {x, y + 1, this.getEnergy()};
		        	position.set(this.index, newVal0);
				}else if(y > kingY) {
					walk(2);
					int[] newVal0 = {x, y - 1, this.getEnergy()};
		        	position.set(this.index, newVal0);
				}
			}
			
		//If the ant is healthy, move randomly and spawn
		}else {
			
			int rand = getRandomInt(8);
			int x = position.get(index)[0];
			int y = position.get(index)[1];
			
			for(int i = 0; i < 8; i++) {
				switch ((i + rand) % 8) {
		        case 0:
		        	int[] newVal0 = {x + 1, y, this.getEnergy()};
		        	position.set(this.index, newVal0);
		            break;

		        case 1:
		        	int[] newVal1 = {x + 1, y - 1, this.getEnergy()};
		        	position.set(this.index, newVal1);
		            break;

		        case 2:
		        	int[] newVal2 = {x, y - 1, this.getEnergy()};
		        	position.set(this.index, newVal2);
		            break;

		        case 3:
		        	int[] newVal3 = {x - 1, y - 1, this.getEnergy()};
		        	position.set(this.index, newVal3);
		            break;

		        case 4:
		        	int[] newVal4 = {x - 1, y, this.getEnergy()};
		        	position.set(this.index, newVal4);
		            break;

		        case 5:
		        	int[] newVal5 = {x - 1, y + 1, this.getEnergy()};
		        	position.set(this.index, newVal5);
		            break;

		        case 6:
		        	int[] newVal6 = {x, y + 1, this.getEnergy()};
		        	position.set(this.index, newVal6);
		            break;

		        case 7:
		        	int[] newVal7 = {x + 1, y + 1, this.getEnergy()};
		        	position.set(this.index, newVal7);
		            break;
				}
				
				// makes sure the Critter3 doesn't encounter another Critter3
				boolean isUnique = true;
				for(int j = 0; j < position.size(); j++) {
					if(j != index && position.get(j)[0] == x && position.get(j)[1] == y) {
						j = position.size();
						isUnique = false;
					}
				}
				
				// If there exists a position to move, spawn a critter in the previous location
				if(isUnique && life > 0) {
					life--;
					walk((i + rand) % 8);
					Critter3 newCritter = new Critter3((i+rand) %  8,this.index, 5);
					reproduce(newCritter, (i + rand + 4) % 8);
				}
			}
		}
	}
	
	/**
	 * Determines how the Critter3 does encounters
	 * @param opponent the Critter to fight
	 */
	public boolean fight(String opponent) {
		//if on the tile of the queen, kill itself, otherwise fight
		if(position.get(this.index)[0] == position.get(0)[0] && position.get(this.index)[1] == position.get(0)[1]) {
			if(this.index != 0) {
				return false;
			}
		}
		life++;
		return true;
	}

	@Override
	public CritterShape viewShape() {
		return CritterShape.STAR;
	}

	@Override
	public javafx.scene.paint.Color viewOutlineColor() {return Color.PURPLE;}

	@Override
	public javafx.scene.paint.Color viewFillColor() {return Color.RED;}@Override


	/**
	 * @return the char of 3 to represent on the grid
	 */
	public String toString() {
		return("3");
	}
	
	/**
	 * Prints statistics regarding the champion in the colony
	 * @param critter3s the List of all Critter3s
	 */
	public static String runStats(java.util.List<Critter> critter3s) {
		//Info of the queen
        int pivotX = position.get(0)[0];
        int pivotY = position.get(0)[1];
        
        int colony = 0;
        int maxEnergy = 0;
        int maxIndex = 0;
        
        //Find the most powerful ant
        for(Object obj : critter3s) {
            Critter3 c = (Critter3) obj;
            colony++;
            int energy = position.get(c.index)[2];
            
            //if this ant has more energy, udate the champion ant
            if(energy > maxEnergy) {
            	maxEnergy = energy;
            	maxIndex = c.index;
            }
        }

        String output = "";

        //The print statements
        if(colony > 1) {
        	output = "The greatest among a colony of " + colony + " has an energy of " + maxEnergy
					+ "\nIt resides at [" + position.get(maxIndex)[0] + "," + position.get(maxIndex)[1] + "], and pivots about [" + position.get(0)[0] + "," + position.get(0)[1] + "]";
        }else if(colony < 1) {
        	output = "R.I.P. Colony";
        }else {
        	output = "A lone ant resides at [" + position.get(maxIndex)[0] + "," + position.get(maxIndex)[1]
					+ "], and pivots about [" + position.get(0)[0] + "," + position.get(0)[1] + "]";
        }

        return output;
	}
}

