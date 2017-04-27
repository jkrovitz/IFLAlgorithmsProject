/**
 * This is the master file for the first build of the project.
 * May the Gods be merciful on us.
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
            System.out.println("enter 5 for Red, 6 for Green, and 7 for Blue.");
            sortValueChosen = reader.nextInt();
        }
        System.out.println("Sort Biggest to Smallest value (enter boolean)?");
        sortBigToSmall = reader.nextBoolean();
        System.out.println("enter 1 for entire line, 2 for a set size chunks, 3 for random size chunks, 4 for edge detection, 5 for value range");
        sortStyle = reader.nextInt();
        System.out.println("enter 1 for horizontal pixel sorting, 2 for vertical sorts ");
        sortDirection = reader.nextInt();

        if (sortStyle == 2) {
            System.out.println("What size of chunks (as squares)?"); //check for what size
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
     * This is the main sort function, which chooses which sort function to use, calling that helper function
     */
    private void DoTheSort() {
        if (sortStyle == 1) {
            int imgWidth = img.getWidth();
            int imgHeight = img.getHeight();
            if (sortDirection == 1) sortHoriz(imgWidth, imgHeight,0,0);
            else sortVert(imgWidth, imgHeight,0,0);
        }else if (sortStyle==2) tileSort();

    }

    /**
     * This does sorting in a horizontal direction.
     *
     * @param wide
     * @param high
     */
    private void sortHoriz(int wide, int high, int TopLeftX, int TopLeftY) {
            for (int i = 0; i < high; i++) {
                int[] arr = new int[wide];
                for (int h = 0; h < wide; h++) {//Iterate over each pixel in the column.
                    arr[h] = img.getRGB(h+TopLeftX,i+TopLeftY);
                }
                Color[] ColorArr = new Color[wide];
                for (int j = 0; j < arr.length; j++) {
                    ColorArr[j] = new Color(arr[j]);
                }Arrays.sort(ColorArr, new Comparator<Color>() {
                    @Override
                    public int compare(Color o1, Color o2) {
                        if (sortValueChosen == 5) {
                            if (sortBigToSmall) return o1.getRed() - o2.getRed();
                            else return o2.getRed() - o1.getRed();
                        }else if (sortValueChosen == 6) {
                            if (sortBigToSmall) return o1.getGreen() - o2.getGreen();
                            else return o2.getGreen() - o1.getGreen();
                        }else if (sortValueChosen==7) {
                            if(sortBigToSmall) return o1.getBlue() - o2.getBlue();
                            else return o2.getBlue() - o1.getBlue();
                        }else{//Now, we make this work with HSV values
                            float[] o1hsb = new float[3];//Building mini-arrays with the values
                            Color.RGBtoHSB(o1.getRed(),o1.getGreen(), o1.getBlue(), o1hsb);
                            float[] o2hsb = new float[3];
                            Color.RGBtoHSB(o2.getRed(),o2.getGreen(), o2.getBlue(), o2hsb);
                            if (sortValueChosen==2){//Check Hue
                                if (sortBigToSmall) {
                                    float ans = (o1hsb[0]-o2hsb[0]);
                                    System.out.println(ans);
                                    if (ans<0) return -1;
                                    else if (ans>0) return 1;
                                    else return 0;
                                }else {
                                    float ans = (o2hsb[0] - o1hsb[0]);
                                    if (ans < 0) return -1;
                                    else if (ans>0) return 1;
                                    else return 0;
                                }
                            }else if (sortValueChosen==3){//Check Saturation
                                if (sortBigToSmall) {
                                    float ans = (o1hsb[1]-o2hsb[1]);
                                    if (ans<0) return -1;
                                    else if (ans>0) return 1;
                                    else return 0;
                                }else {
                                    float ans = (o2hsb[1] - o1hsb[1]);
                                    if (ans < 0) return -1;
                                    else if (ans>0) return 1;
                                    else return 0;
                                }
                            }if (sortValueChosen==4){//Check Brightness
                                if (sortBigToSmall) {
                                    float ans = (o1hsb[2]-o2hsb[2]);
                                    if (ans<0) return -1;
                                    else if (ans>0) return 1;
                                    else return 0;
                                }else {
                                    float ans = (o2hsb[2] - o1hsb[2]);
                                    if (ans < 0) return -1;
                                    else if (ans>0) return 1;
                                    else return 0;
                                }
                            }return 0; //A catch if all us breaks down somehow
                        }
                    }
                });
                for (int k = 0; k < arr.length; k++) {
                    img.setRGB(k+TopLeftX,i+TopLeftY,ColorArr[k].getRGB());
                }
            }
        }

    /**
     * This does sorting in a vertical direction.
     * @param wide
     * @param high
     */
    private void sortVert(int wide, int high, int TopLeftX, int TopLeftY) {
            for (int i = 0; i < wide; i++) {//iterate over each column, building an array of pixel colors in ints
                int[] arr = new int[high];
                for (int h = 0; h < high; h++) {//Iterate over each pixel in the column.
                    arr[h] = img.getRGB(i+TopLeftX,h+TopLeftY);
                }//create a new array of Color objects, for each pixel
                Color[] ColorArr = new Color[high];
                for (int j = 0; j < ColorArr.length; j++) {
                    ColorArr[j] = new Color(arr[j]);
                }//Now we sort, by writing new comparators to choose between sort values.
                Arrays.sort(ColorArr, new Comparator<Color>() {
                        @Override
                        public int compare(Color o1, Color o2) {
                            if (sortValueChosen == 5) {
                                if (sortBigToSmall) return o1.getRed() - o2.getRed();
                                else return o2.getRed() - o1.getRed();
                            }else if (sortValueChosen == 6) {
                                if (sortBigToSmall) return o1.getGreen() - o2.getGreen();
                                else return o2.getGreen() - o1.getGreen();
                            }else if (sortValueChosen == 7) {
                                if (sortBigToSmall) return o1.getBlue() - o2.getBlue();
                                else return o2.getBlue() - o1.getBlue();
                            }else{//Now, we make this work with HSV values
                                float[] o1hsb = new float[3];//Building mini-arrays with the values
                                Color.RGBtoHSB(o1.getRed(),o1.getGreen(), o1.getBlue(), o1hsb);
                                float[] o2hsb = new float[3];
                                Color.RGBtoHSB(o2.getRed(),o2.getGreen(), o2.getBlue(), o2hsb);
                                if (sortValueChosen==2){//Check Hue
                                    if (sortBigToSmall) {
                                        float ans = (o1hsb[0]-o2hsb[0]);
                                        if (ans<0) return -1;
                                        else if (ans>0) return 1;
                                        else return 0;
                                    }else {
                                        float ans = (o2hsb[0] - o1hsb[0]);
                                        if (ans < 0) return -1;
                                        else if (ans>0) return 1;
                                        else return 0;
                                    }
                                }else if (sortValueChosen==3){//Check Saturation
                                    if (sortBigToSmall) {
                                        float ans = (o1hsb[1]-o2hsb[1]);
                                        if (ans<0) return -1;
                                        else if (ans>0) return 1;
                                        else return 0;
                                    }else {
                                        float ans = (o2hsb[1] - o1hsb[1]);
                                        if (ans < 0) return -1;
                                        else if (ans>0) return 1;
                                        else return 0;
                                    }
                                }if (sortValueChosen==4){//Check Brightness
                                    if (sortBigToSmall) {
                                        float ans = (o1hsb[2]-o2hsb[2]);
                                        if (ans<0) return -1;
                                        else if (ans>0) return 1;
                                        else return 0;
                                    }else {
                                        float ans = (o2hsb[2] - o1hsb[2]);
                                        if (ans < 0) return -1;
                                        else if (ans>0) return 1;
                                        else return 0;
                                    }
                                }return 0; //A catch if all us breaks down somehow
                            }
                        }
                    });//Now, put the sorted Colors back into their columns
                for (int k = 0; k < arr.length; k++) {
                    img.setRGB(i+TopLeftX,k+TopLeftY,ColorArr[k].getRGB());
                }
            }
        }

    /**
     * This function handles the options for any kind of tile sort.
     */
    private void tileSort(){
        //First, check that the user did not put too big of a chunk size (extra chunky).
        if (sortAttribute1>img.getWidth() || sortAttribute1>img.getHeight()) {
            if (sortValueChosen >= 5) {
                if (sortDirection == 1) sortHoriz(img.getWidth(), img.getHeight(), 0, 0);
                else sortVert(img.getWidth(), img.getHeight(), 0, 0);
            }//Now, we get down to the business of subdividing chunks of the image at a time to do.
        }else{
            if(sortValueChosen>=5){
                for (int i = 0; i < img.getHeight()/sortAttribute1; i++) {//For each row of tiles
                    for (int j = 0; j < img.getWidth()/sortAttribute1; j++) {//For each tile in that row
                        if (sortDirection == 1) sortHoriz(sortAttribute1, sortAttribute1,j*sortAttribute1,i*sortAttribute1);
                        else sortVert(sortAttribute1, sortAttribute1,j*sortAttribute1,i*sortAttribute1);
                    }//Now, cover the left edge partial tiles
                    if (sortDirection == 1) sortHoriz(img.getWidth()%sortAttribute1, sortAttribute1, img.getWidth() - (img.getWidth()%sortAttribute1),i*sortAttribute1);
                    else sortVert(img.getWidth()%sortAttribute1, sortAttribute1, img.getWidth() - (img.getWidth()%sortAttribute1),i*sortAttribute1);
                }//Now, cover the bottom edge partial tiles
                for (int j = 0; j < img.getWidth()/sortAttribute1; j++) {
                    if (sortDirection == 1) sortHoriz(sortAttribute1, img.getHeight()%sortAttribute1,j*sortAttribute1,img.getHeight() - (img.getHeight()%sortAttribute1));
                    else sortVert(sortAttribute1, img.getHeight()%sortAttribute1,j*sortAttribute1,img.getHeight() - (img.getHeight()%sortAttribute1));
                }//Finally, the complicated mess of a line or two that gives us the bottom right corner tile
                if (sortDirection == 1) sortHoriz(img.getWidth()%sortAttribute1, img.getHeight()%sortAttribute1,img.getWidth() - (img.getWidth()%sortAttribute1),img.getHeight() - (img.getHeight()%sortAttribute1));
                else sortVert(img.getWidth()%sortAttribute1, img.getHeight()%sortAttribute1,img.getWidth() - (img.getWidth()%sortAttribute1),img.getHeight() - (img.getHeight()%sortAttribute1));
            }
        }
    }
}