/* CRITTERS Critter1.java
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
 */
/**
 * This critter constantly moves in a circle (four square box). It keeps track of how many circles it has completed.
 * It never wants to fight but instead tries to walk away in the current direction it's supposed to move in.
 */

package assignment5;

public class Critter1 extends Critter {
    private int babies;

    private int direction;
    private int circles;

    /**
     * Default constructor. Critters always start moving in the Up direction
     */
    public Critter1() {
        babies = 0;
        direction = 2;
        circles = 0;
    }

    /**
     * Determines the next direction the critter is supposed to move in
     * @param currentDirection the direction the Critter just moved
     * @return the new direction
     */
    private int nextDirection(int currentDirection) {
        switch (currentDirection){
            case 0: return 6;
            case 2: return 0;
            case 4: return 2;
            case 6: return 4;
        }

        return 2;
    }

    /**
     * Moves the critter in a circle. Attempts to reproduce if possible.
     */
    @Override
    public void doTimeStep() {
        walk(direction);
        direction = nextDirection(direction);
        if(direction == 2) {
            circles++;
        }
        if(getEnergy() >= 120) {
            Critter1 child = new Critter1();
            reproduce(child, Critter.getRandomInt(8));
            babies++;
        }
    }

    /**
     * Attempts to continue moving in the direction it's supposed to go
     * @param opponent the toString of the other Critter it encounters
     * @return true if the opponent is an Algae. false for all other critter types
     */
    @Override
    public boolean fight(String opponent) {
        walk(direction);
        direction = nextDirection(direction);
        if(direction == 2) {
            circles++;
        }

        if(opponent.equals("@")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Prints the total number of circles all Critter1's have made and the total number of babies
     * produced
     * @param critter1s List of Critter1's from the population
     */
    public static String runStats(java.util.List<Critter> critter1s) {
        int totalBabies = 0;
        int totalCircles = 0;

        for (Object obj : critter1s) {
            Critter1 c = (Critter1) obj;
            totalBabies += c.babies;
            totalCircles += c.circles;

        }

        System.out.print("" + critter1s.size() + " total Critter1's    ");
        System.out.print("" + totalCircles + " total circles completed    ");
        System.out.println("" + totalBabies + " total babies made");

        return "";
    }

    @Override
    public CritterShape viewShape() {
        return null;
    }

    @Override
    public String toString() {
        return "1";
    }
}
