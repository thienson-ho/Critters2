
/**
 * This critter flips a coin to determine if it wants to walk or run during each time step. It keeps track of the total
 * times it has walked and ran and the number of fights it has been in. This critter does not fight it’s own species.
 */
package assignment5;
/* EE422C Project 5 submission by
 * <ThienSon Ho>
 * <tsh848>
 * <15505>
 * <Arjun Singh>
 * <AS78363>
 * <15505>
 * Slip days used: <0>
 * Spring 2018
 */
import javafx.scene.paint.Color;

public class Critter2 extends Critter{

    private int walks;
    private int runs;
    private int fights;

    /**
     * Default constructor
     */
    public Critter2(){
        walks = 0;
        runs = 0;
        fights = 0;

    }

    /**
     * Flips a coin to determine if the critter runs or walks
     */
    private void walkOrRun() {
        int rand = Critter.getRandomInt(2);

        if(rand == 0) {
            run(getRandomInt(8));
            runs++;
        } else {
            walk(getRandomInt(8));
            walks++;
        }
    }

    /**
     * Walks or runs. Reproduces if possible.
     */
    @Override
    public void doTimeStep() {
        walkOrRun();
        if(getEnergy()> 100) {
            Critter2 child = new Critter2();
            reproduce(child, Critter.getRandomInt(8));
        }
    }

    /**
     * Attempts to runaway from encounters with the same species. Fights all other species.
     * @param opponent the toString of the other Critter it encounters
     * @return
     */
    @Override
    public boolean fight(String opponent) {
        fights++;

        //tries to run if it is the same species
        if(opponent.equals("2")) {
            walkOrRun();
            return false;
        } else {
            return true;
        }

    }

    /**
     * Prints the total number of walks, runs, and fights for all Critter2's
     * @param critter2s list of Critter2's from the population
     */
    public static String runStats(java.util.List<Critter> critter2s) {

        int totalWalks =  0;
        int totalRuns = 0;
        int totalFights = 0;

        for (Object obj : critter2s) {
            Critter2 c = (Critter2) obj;
            totalWalks += c.walks;
            totalRuns += c.runs;
            totalFights += c.fights;
        }

        String output = "" + critter2s.size() + " total Critter2's    "
                + "Runs: " + totalRuns + "    "
                +"Walks: " + totalWalks + "    "
                + "Fights: " + totalFights;

        return output;
    }

    @Override
    public CritterShape viewShape() {
        return CritterShape.DIAMOND;
    }

    @Override
    public javafx.scene.paint.Color viewOutlineColor() {return Color.ORANGE;}

    @Override
    public javafx.scene.paint.Color viewFillColor() {return Color.BLUE;}
    @Override
    public String toString() {
        return "2";
    }
}
