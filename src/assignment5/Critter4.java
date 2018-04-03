/* CRITTERS Critter4.java
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
 * 
 * The Critter4 class describes a vicious Critter that multiplies and eats all the Algae it can (an army). It is like a microbe
 * that only feeds on algae, it doesn't  eat any other organism. It will rapidly teraform the map with this and potentially
 * feed many predators, a bloom. The species doesn't move, it acts like a weed. If there no immediately adjacent algae it dies out
 *  
 */
package assignment5;

import java.util.ArrayList;

public class Critter4 extends Critter{
	
	// stores the random direction "seed"
	int rand;
	// defines whether a Critter4 can reproduce based on value
	int proCreate;
	// determines if the Critter4 can reproduce again 
	int reload;
	//The number of algae fought by this Critter4
	int algaeKill;
	
	/**
	 * Default constructor
	 */
	public Critter4() {
		rand = Critter.getRandomInt(8);
		proCreate = -5;
		reload = 5;
		algaeKill = 0;
	}
	
	/**
	 * Constructor that redefines the initial limit to proCreating
	 * @param limit the new proCreate and reload values
	 */
	public Critter4(int limit) {
		rand = Critter.getRandomInt(8);
		proCreate = limit;
		reload = limit;
	}

	
	/**
	 * Solves if Critter4 should multiply
	 */
	@Override
	public void doTimeStep() {
		//if the proCreate is on cool down, decrement the timer
		if(proCreate >= 3) {
			reload--;
		}
		//if the timer hits 0, reset the timer and allow the organism to start reproducing again 
		if(reload == 0) {
			proCreate = 0;
			reload = 5;
		}
		//procreate up to 2 times in a single time step
		for(int i = 0; i < 2; i++) {
			if(proCreate < 3) {
				rand = Critter.getRandomInt(8);
				proCreate++;
				Critter4 aCritter = new Critter4(proCreate);
				
				reproduce(aCritter, (i+rand)%8);
			}
		}
	}
	
	/**
	 * @param opponent the toString of the critter to fight
	 */
	public boolean fight(String opponent) {
		//Always fight the algae and increment algaeKill
		if(opponent.equals("@")) {
			algaeKill++;
			return true;
		}
		//If the weed is large enough, put up a fight
		if(this.getEnergy() > 50) {
			proCreate--;
			return true;
		}
		return false;
	}

	@Override
	public CritterShape viewShape() {
		return null;
	}

	/**
	 * returns the string representation of Critter4
	 */
	public String toString() {
		return("4");
	}
	
	/**
	 * Displays Statistics on the army of weeds via print statements
	 * @param critter4s
	 */
	public static String runStats(java.util.List<Critter> critter4s) {
		//the total number of algae killed by the living members of the army
        int algaeFought = 0;
        //the size of the army
        int army = 0;

        //Increments the counters
        for (Object obj : critter4s) {
            Critter4 c = (Critter4) obj;
            algaeFought += c.algaeKill;
            army++;
        }
        
        //the String output
        if(army > 1) {
        	System.out.println(army + " Critter4s threw hands with " + algaeFought + " @");
        }else if(army < 1) {
        	System.out.println("R.I.P. Army");
        }else if(army == 1 && critter4s.size() != 1){
        	System.out.println(army + " Critter4 threw hands with " + algaeFought + " @");
        }else {
        	System.out.println("A Critter4 threw hands with " + algaeFought + " @");
        }
        return "";
    }
}
