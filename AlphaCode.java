/**
 * This is the master file for the first build of the project.
 *
 * Created by Luke on 4/10/2017.
 */
import acm.program.ConsoleProgram;

import java.awt.image.*;
import java.util.Scanner;

public class AlphaCode extends ConsoleProgram {

    public int sortStyle;
    public int sortValueChosen;
    public int sortAttribute1;
    public int sortAttribute2;
    public boolean sortBigToSmall;

    public void userQuestions() {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        println("Enter an image file path to pixel sort:");
        String image = reader.next(); // Scans the next token of the input.
        println("Loading image...");
        println("Loaded!");
        println("Choose your sorting Criteria: ");
        //ask for by which value: RGB, hue, saturation, or brightness
        println("Enter 1 for an RGB value, 2 for hue, 3 for saturation, and 4 for brightness.");
        sortValueChosen = reader.nextInt();
        if (sortValueChosen==1){
            println("enter 5 for Red, 6 for Blue, and 7 for Green.");
            sortValueChosen = reader.nextInt();
        }
        println("Sort Biggest to Smallest value (enter boolean)?");
        sortBigToSmall = reader.nextBoolean();
        println("enter 1 for entire line, 2 for a set size chunks, 3 for random size chunks, 4 for edge detection, 5 for value range");
        sortStyle = reader.nextInt();

        if (sortStyle == 2) {
            println("How size of chunks?"); //check for what size
            sortAttribute1 = reader.nextInt();
        }
        else if (sortStyle == 3) {
            println("What minimum size of chunks?");
            sortAttribute1 = reader.nextInt();
            println("What maximum size of chunks?");
            sortAttribute2 = reader.nextInt();
        }
        else if (sortStyle == 4){
            //ask for what tolerance for the chosen value.
            println("What tolerance (degree of variation) do you allow?");
            sortAttribute1 = reader.nextInt();
        }else if (sortStyle == 5) {
            //ask for what what min and max values for the range for the chosen value.
            println("What minimum value?");
            sortAttribute1 = reader.nextInt();
            println("What minimum value?");
            sortAttribute2 = reader.nextInt();
        }
    }

    public void run(){
        userQuestions();
    }
}
