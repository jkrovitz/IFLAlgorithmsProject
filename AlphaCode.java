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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class AlphaCode {


    /**
     * This is the part that actually runs, but mostly it just calls the DoTheSort fxn.
     * @param args
     */
    public static void main(String args[]) {
        AlphaCode ac = new AlphaCode();
        ac.userQuestions();
        ac.DoTheSort();
        ac.paint(img);
    }

    private int sortStyle;
    private int sortValueChosen;
    private int sortAttribute1;
    private int sortAttribute2;
    private boolean sortBigToSmall;
    private int sortDirection;
    static BufferedImage img = null;

    /**
     * This cute fellow runs a quick line of code to load up the image.
     * @param imageName
     */
    private void loadImage(String imageName) {
        System.out.println("Loading image...");
        try {
            img = ImageIO.read(new File(imageName));
        } catch (IOException e) {
        }
        System.out.println("Loaded!");
    }

    /**
     * For now this function saves an output file, in a precise location on my machine.
     * Change the location and file name to whatever you want.
     * @param pic
     */
    public void paint(BufferedImage pic) {
        try {
            File outputfile = new File("C:\\Users\\Luke\\Pictures\\savedAndSorted.jpg");
            ImageIO.write(pic, "jpg", outputfile);
        } catch (IOException e) {
        }
    }

    /**
     * Temporary Giant ball of Yuck function, which asks impertinent questions on what the
     * user wants to happen. Mostly demands ints. Needs fed and watered regularly.
     */
    private void userQuestions() {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter an image file path to pixel sort:");
        String imageName = reader.next(); // Scans the next token of the input.
        loadImage(imageName);
        System.out.println("Choose your sorting Criteria: ");
        //ask for by which value: RGB, hue, saturation, or brightness
        System.out.println("Enter 1 for an RGB value, 2 for hue, 3 for saturation, and 4 for brightness.");
        sortValueChosen = reader.nextInt();
        if (sortValueChosen == 1) {
            System.out.println("enter 5 for Red, 6 for Blue, and 7 for Green.");
            sortValueChosen = reader.nextInt();
        }
        System.out.println("Sort Biggest to Smallest value (enter boolean)?");
        sortBigToSmall = reader.nextBoolean();
        System.out.println("enter 1 for entire line, 2 for a set size chunks, 3 for random size chunks, 4 for edge detection, 5 for value range");
        sortStyle = reader.nextInt();
        System.out.println("enter 1 for horizontal pixel sorting, 2 for vertical sorts ");
        sortDirection = reader.nextInt();

        if (sortStyle == 2) {
            System.out.println("How size of chunks?"); //check for what size
            sortAttribute1 = reader.nextInt();
        } else if (sortStyle == 3) {
            System.out.println("What minimum size of chunks?");
            sortAttribute1 = reader.nextInt();
            System.out.println("What maximum size of chunks?");
            sortAttribute2 = reader.nextInt();
        } else if (sortStyle == 4) {
            //ask for what tolerance for the chosen value.
            System.out.println("What tolerance (degree of variation) do you allow?");
            sortAttribute1 = reader.nextInt();
        } else if (sortStyle == 5) {
            //ask for what what min and max values for the range for the chosen value.
            System.out.println("What minimum value?");
            sortAttribute1 = reader.nextInt();
            System.out.println("What minimum value?");
            sortAttribute2 = reader.nextInt();
        }
    }

    /**
     * This is the main sort, which chooses which sort function to use, calling that helper function
     */
    private void DoTheSort() {
        if (sortStyle == 1) {
            int imgWidth = img.getWidth();
            int imgHeight = img.getHeight();
            if (sortValueChosen >= 5) {
                sortRGB(imgWidth, imgHeight);
            }
        }
    }

    private void sortRGB(int wide, int high) {
        if (sortDirection == 1) {
            for (int i = 0; i < high; i++) {
                int arr[] = img.getRGB(0, i, wide, 1, null, 0, wide);
                Color[] ColorArr = new Color[wide];
                for (int j = 0; j < arr.length; j++) {
                    ColorArr[j] = new Color(arr[j]);
                }
                if (sortValueChosen == 5) {
                    Arrays.sort(ColorArr, new Comparator<Color>() {
                        @Override
                        public int compare(Color o1, Color o2) {
                            return o1.getRed() - o2.getRed();
                        }
                    });
                } else if (sortValueChosen == 6) {
                    Arrays.sort(ColorArr, new Comparator<Color>() {
                        @Override
                        public int compare(Color o1, Color o2) {
                            return o1.getBlue() - o2.getBlue();
                        }
                    });
                } else {
                    Arrays.sort(ColorArr, new Comparator<Color>() {
                        @Override
                        public int compare(Color o1, Color o2) {
                            return o1.getBlue() - o2.getBlue();
                        }
                    });
                }
                int[] sortedArr = new int[wide];
                for (int k = 0; k < arr.length; k++) {
                    sortedArr[k] = ColorArr[k].getRGB();
                }
                img.setRGB(0, i, wide, 1, sortedArr, 0, wide);
            }
        } else {//Do the whole thing 1 column at a time
            for (int i = 0; i < wide; i++) {//iterate over each column, building an array of pixel colors in ints
                int[] arr = new int[high];
                for (int h = 0; h < high; h++) {
                    arr[h] = img.getRGB(i,h);
                }//create a new array of Color objects, for each pixel
                Color[] ColorArr = new Color[high];
                for (int j = 0; j < ColorArr.length; j++) {
                    ColorArr[j] = new Color(arr[j]);
                }//Now we sort, by writing new comparators to choose between RBG.
                if (sortValueChosen == 5) {
                    Arrays.sort(ColorArr, new Comparator<Color>() {
                        @Override
                        public int compare(Color o1, Color o2) {
                            return o1.getRed() - o2.getRed();
                        }
                    });
                } else if (sortValueChosen == 6) {
                    Arrays.sort(ColorArr, new Comparator<Color>() {
                        @Override
                        public int compare(Color o1, Color o2) {
                            return o1.getBlue() - o2.getBlue();
                        }
                    });
                } else {
                    Arrays.sort(ColorArr, new Comparator<Color>() {
                        @Override
                        public int compare(Color o1, Color o2) {
                            return o1.getBlue() - o2.getBlue();
                        }
                    });
                }//Now, put the sorted Colors back into their columns
                for (int k = 0; k < arr.length; k++) {
                    img.setRGB(i,k,ColorArr[k].getRGB());
                }

            }
        }
    }
}