/**
 * This is the master file for the first build of the project.
 *
 * Created by Luke on 4/10/2017.
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class AlphaCode {


    public static void main(String args[]){
        AlphaCode ac = new AlphaCode();
        ac.userQuestions();
        ac.paint(img);
    }

    private int sortStyle;
    private int sortValueChosen;
    private int sortAttribute1;
    private int sortAttribute2;
    private boolean sortBigToSmall;
    static BufferedImage img = null;

    public void loadImage(String imageName){
        try {
            img = ImageIO.read(new File(imageName));
        } catch (IOException e) {
        }
    }

    public void paint(BufferedImage pic) {
        Graphics g = pic.getGraphics();
        g.drawImage(img,50,50,null);
    }

    public void userQuestions() {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter an image file path to pixel sort:");
        String imageName = reader.next(); // Scans the next token of the input.
        System.out.println("Loading image...");
        loadImage(imageName);
        System.out.println("Loaded!");
        System.out.println("Choose your sorting Criteria: ");
        //ask for by which value: RGB, hue, saturation, or brightness
        System.out.println("Enter 1 for an RGB value, 2 for hue, 3 for saturation, and 4 for brightness.");
        sortValueChosen = reader.nextInt();
        if (sortValueChosen==1){
            System.out.println("enter 5 for Red, 6 for Blue, and 7 for Green.");
            sortValueChosen = reader.nextInt();
        }
        System.out.println("Sort Biggest to Smallest value (enter boolean)?");
        sortBigToSmall = reader.nextBoolean();
        System.out.println("enter 1 for entire line, 2 for a set size chunks, 3 for random size chunks, 4 for edge detection, 5 for value range");
        sortStyle = reader.nextInt();

        if (sortStyle == 2) {
            System.out.println("How size of chunks?"); //check for what size
            sortAttribute1 = reader.nextInt();
        }
        else if (sortStyle == 3) {
            System.out.println("What minimum size of chunks?");
            sortAttribute1 = reader.nextInt();
            System.out.println("What maximum size of chunks?");
            sortAttribute2 = reader.nextInt();
        }
        else if (sortStyle == 4){
            //ask for what tolerance for the chosen value.
            System.out.println("What tolerance (degree of variation) do you allow?");
            sortAttribute1 = reader.nextInt();
        }else if (sortStyle == 5) {
            //ask for what what min and max values for the range for the chosen value.
            System.out.println("What minimum value?");
            sortAttribute1 = reader.nextInt();
            System.out.println("What minimum value?");
            sortAttribute2 = reader.nextInt();
        }
    }


}