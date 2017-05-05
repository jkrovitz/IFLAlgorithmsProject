/*
Algorithms Final Project
Pixel Sorting
By Luke Brown, Jeremy Krovitz, and Emma Thole
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
     *
     * @param args
     */
    public static void main(String args[]) {
        AlphaCode ac = new AlphaCode();
        ac.userQuestions();
        ac.DoTheSort();
        ac.paint(img);
    }

    //Hey, Look! Variable initializers!!! Aren't they cute...
    private int sortStyle;
    private int sortValueChosen;
    private int sortAttribute1;
    private int sortAttribute2;
    private boolean sortBigToSmall;
    private int sortDirection;
    static BufferedImage img = null;

    /**
     * This cute fellow runs a quick line of code to load up the image.
     * It will load most image formats it seems: jpg, bmp, png, etc.
     *
     * @param imageName
     */
    private void loadImage(String imageName) {
        System.out.println("Loading image...");
        try {
            img = ImageIO.read(new File(imageName));
        }catch (IOException e) {
        }
        System.out.println("Loaded!");
    }

    /**
     * This function saves an output file, in a precise location on the computer.
     * Change the location and file name to whatever you want for a different save file.
     * The path must exist, but the file itself will be created if it does not yet exist,
     * and will be written over if it does exist.
     *
     * @param pic
     */
    public void paint(BufferedImage pic) {
        try {
            File outputfile = new File("/Users/jkrovitz/Desktop/images-17sorted1.jpg"); //Be sure to enter the file path specific to your computer.
            ImageIO.write(pic, "jpg", outputfile);
        } catch (IOException e) {
        }
    }

    /**
     * All of the questions posed to the user to do the sorting.
     */
    private void userQuestions() {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter an image file path to pixel sort:");
        String imageName = reader.next(); // Scans the next token of the input.
        loadImage(imageName);
        System.out.println("Choose your sorting criteria: ");
        //ask for by which value: RGB, hue, saturation, or brightness
        System.out.println("Enter 1 for an RGB value, 2 for hue, 3 for saturation, or 4 for brightness.");
        sortValueChosen = reader.nextInt();
        if (sortValueChosen == 1) {//Ask for clarification if needed.
            System.out.println("Enter 5 for red, 6 for green, or 7 for blue.");
            sortValueChosen = reader.nextInt();
        }
        System.out.println("Enter 1 to sort from biggest to smallest, or 0 to sort from smallest to biggest.");
        int direction = reader.nextInt();
        if (direction==1) sortBigToSmall = true;
        else sortBigToSmall = false;
        System.out.println("Enter 1 for horizontal pixel sorting, or 2 for vertical sorting.");
        sortDirection = reader.nextInt();
        System.out.println("Enter 1 to sort a full row or column, 2 for a set size of chunks, 3 for random size chunks, 4 for edge detection sorts, or 5 to sort within a value range.");
        sortStyle = reader.nextInt();

        if (sortStyle == 2) {
            System.out.println("What size of chunks (in pixels)?"); //check for what size
            sortAttribute1 = reader.nextInt();
        } else if (sortStyle == 3) {
            System.out.println("What minimum size of chunks (in pixels)?");
            sortAttribute1 = reader.nextInt();
            System.out.println("What maximum size of chunks (in pixels)?");
            sortAttribute2 = reader.nextInt();
        } else if (sortStyle == 4) {//ask for what tolerance for the chosen value for edge detection.
            System.out.println("What tolerance (degree of variation) do would you like?");
            System.out.println("(For RGB values, this is a number 1-255, 1-100 for saturation/brightness, and 1-360 for hue.)");
            System.out.println("(The lower the number, the more edges that will be detected and the closer the result will be to the original image.)");
            sortAttribute1 = reader.nextInt();
        } else if (sortStyle == 5) {
            //ask for what what min and max values for the range for the chosen value.
            System.out.println("For the following questions, please follow these guidelines for answers:");
            System.out.println("For RGB values, please answer with a number between 0 and 255.");
            System.out.println("For saturation or brightness, enter a number between 1 and 100");
            System.out.println("For hue, enter a number between 1 and 360");
            System.out.println();
            System.out.println("What is the minimum value?");
            sortAttribute1 = reader.nextInt();
            System.out.println("What is the maximum value?");
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
            if (sortDirection == 1) sortHoriz(imgWidth, imgHeight, 0, 0);
            else sortVert(imgWidth, imgHeight, 0, 0);
        } else if (sortStyle == 2) tileSort();
        else if (sortStyle == 3) randomSort();
        else if (sortStyle == 4 || sortStyle==5) doRangeSort();
    }

    /**
     * Our Custom comparator that makes sure than any array sorting happens by means
     * of whatever value the user chose.
     */
    private Comparator<Color> MyComparator = new Comparator<Color>() {
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
            }else {//Now, we make this work with HSV values
                float[] o1hsb = new float[3];//Building mini-arrays with the values
                Color.RGBtoHSB(o1.getRed(), o1.getGreen(), o1.getBlue(), o1hsb);
                float[] o2hsb = new float[3];
                Color.RGBtoHSB(o2.getRed(), o2.getGreen(), o2.getBlue(), o2hsb);
                float ans;
                if (sortValueChosen == 2) {//Check Hue
                    if (sortBigToSmall) ans = (o1hsb[0] - o2hsb[0]);
                    else ans = (o2hsb[0] - o1hsb[0]);
                    if (ans < 0) return -1;
                    else if (ans > 0) return 1;
                    else return 0;
                }else if (sortValueChosen == 3) {//Check Saturation
                    if (sortBigToSmall) ans = (o1hsb[1] - o2hsb[1]);
                    else ans = (o2hsb[1] - o1hsb[1]);
                    if (ans < 0) return -1;
                    else if (ans > 0) return 1;
                    else return 0;
                }if (sortValueChosen == 4) {//Check Brightness
                    if (sortBigToSmall) ans = (o1hsb[2] - o2hsb[2]);
                    else ans = (o2hsb[2] - o1hsb[2]);
                    if (ans < 0) return -1;
                    else if (ans > 0) return 1;
                    else return 0;
                }return 0; //A catch in case this all us breaks down somehow
            }
        }
    };

    /**
     * This does sorting in a horizontal direction, for a passed in section size of the image.
     *
     * @param wide
     * @param high
     */
    private void sortHoriz(int wide, int high, int TopLeftX, int TopLeftY) {
        for (int i = 0; i < high; i++) {
            int[] arr = new int[wide];
            for (int h = 0; h < wide; h++) {//Iterate over each pixel in the row.
                arr[h] = img.getRGB(h+TopLeftX,i+TopLeftY);
            }Color[] ColorArr = new Color[wide]; //Create an array holding color values for each pixel
            for (int j = 0; j < arr.length; j++) {
                ColorArr[j] = new Color(arr[j]);
            }Arrays.sort(ColorArr, MyComparator); //Sort the sorry suckers, using our custom comparator.
            for (int k = 0; k < arr.length; k++) {
                img.setRGB(k+TopLeftX,i+TopLeftY,ColorArr[k].getRGB());
            }
        }
    }

    /**
     * This does sorting in a vertical direction, for a passed in section size of the image.
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
            }//Now we sort, by using our custom comparator to choose between sort values.
            Arrays.sort(ColorArr, MyComparator);
            //Now, put the sorted Colors back into their columns
            for (int k = 0; k < arr.length; k++) {
                img.setRGB(i+TopLeftX,k+TopLeftY,ColorArr[k].getRGB());
            }
        }
    }

    /**
     * This function handles the options for any kind of tile sort, breaking the image into chunks
     * and calling the previous functions to sort each chunk.
     */
    private void tileSort(){
        //First, check that the user did not put too big of a chunk size (extra chunky).
        if (sortAttribute1>img.getWidth() || sortAttribute1>img.getHeight()) {
            if (sortDirection == 1) sortHoriz(img.getWidth(), img.getHeight(), 0, 0);
            else sortVert(img.getWidth(), img.getHeight(), 0, 0);
        }else{//Now, we get down to the business of subdividing chunks of the image at a time to do.
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

    /**
     * Quick function to generate a random tile size, then call the tile sorting function.
     */
    private void randomSort(){
        sortAttribute1 = (int) (Math.random() * sortAttribute2 + sortAttribute1); //generate a new random size of chunk
        tileSort();//Just use the regular tile sort on this size.
    }

    /**
     * For edge detection or, when a particular range for the values is given or if values
     * are meant to be determined automatically by the algorithm, then this function
     * handles building and replacing rows/columns of the image. It calls a helper function
     * for the reordering of the arrays.
     */
    private void doRangeSort(){
        int high = img.getHeight();
        int wide = img.getWidth();
        if(sortDirection==1) {//Horizontal sorting
            for (int i = 0; i < high; i++) {
                int[] arr = new int[wide];
                for (int h = 0; h < wide; h++) {//Iterate over each pixel in the column.
                    arr[h] = img.getRGB(h, i);
                }Color[] ColorArr = new Color[wide];//new color array
                for (int j = 0; j < arr.length; j++) {
                    ColorArr[j] = new Color(arr[j]);
                }//call a helper function to do the sort of the array, then put the values back in the image.
                Color[] c2 = SortBySubarray(ColorArr);
                for (int k = 0; k < wide; k++) {
                    img.setRGB(k,i,c2[k].getRGB());
                }
            }
        }else{//Veritcal sorting
            for (int i = 0; i < wide; i++) {
                int[] arr = new int[high];
                for (int h = 0; h < high; h++) {//Iterate over each pixel in the column.
                    arr[h] = img.getRGB(i, h);
                }Color[] ColorArr = new Color[high];//new color array
                for (int j = 0; j < arr.length; j++) {
                    ColorArr[j] = new Color(arr[j]);
                }//call a helper function to do the sort of the array, then put the values back in the image.
                Color[] c2 = SortBySubarray(ColorArr);
                for (int k = 0; k < c2.length; k++) {
                    img.setRGB(i,k,c2[k].getRGB());
                }
            }
        }
    }

    /**
     * This takes the array of colors, checks what the sort value is, and appropriately
     * Sorts within the range of attribute1 and attribute 2 if the user chose value range sort.
     * If not it checks the previous pixel's value to see if a major change occurred, implying an edge.
     * The function then sorts the sections necessary and simply copies over pixels that are not sorted,
     * returning that array.
     * @param c
     */
    private Color[] SortBySubarray(Color[] c){
        int start = -1;//variable to keep track of subarray start locations in the whole column/row
        float current_value;
        Color[] sortedc = new Color[c.length];
        Color[] tempArray;//The temporary array to hold subarrays to be sorted.
        for (int i = 0; i < c.length; i++) {
            if (sortValueChosen == 5) current_value = c[i].getRed();
            else if (sortValueChosen == 6) current_value = c[i].getGreen();
            else if (sortValueChosen == 7) current_value = c[i].getBlue();
            else {
                float[] hsbval = Color.RGBtoHSB(c[i].getRed(), c[i].getGreen(), c[i].getBlue(), null);
                if (sortValueChosen == 2) current_value = hsbval[0];
                else if (sortValueChosen == 3) current_value = hsbval[1];
                else current_value = hsbval[3];
            }if (sortValueChosen==3||sortValueChosen==4) current_value = current_value*100;
            if (sortValueChosen==2) current_value = current_value * 360;
            //OK, now we've gotten whatever value we wanted for the current pixel, and it matches the scale given by user input.
            if(sortStyle==5){//Predefined range:
                if (current_value < sortAttribute1 || current_value > sortAttribute2 || i == c.length - 1) {//if the pixel value is outside the range that needs sorted
                    sortedc[i] = c[i];//Copy that pixel over to the returned array in the same spot
                    if (start != -1) {//sort any previous range of pixels that were within bounds.
                        tempArray = Arrays.copyOfRange(c, start, i);
                        Arrays.sort(tempArray, MyComparator);
                        for (int j = 0; j < tempArray.length; j++) {
                            sortedc[start + j] = tempArray[j];
                        }start = -1;
                    }
                }else{//For when the value is in the range of needing sorted
                    if (start == -1) start = i;//keep track of where to start
                }
            }else{//Auto edge detect now
                //Grab previous pixel value for comparison
                sortedc[0] = c[i];
                if (i!=0) {
                    float oldPixVal;
                    if (sortValueChosen == 5) oldPixVal = c[i - 1].getRed();
                    else if (sortValueChosen == 6) oldPixVal = c[i - 1].getGreen();
                    else if (sortValueChosen == 7) oldPixVal = c[i - 1].getBlue();
                    else {
                        float[] hsbval = Color.RGBtoHSB(c[i - 1].getRed(), c[i - 1].getGreen(), c[i - 1].getBlue(), null);
                        if (sortValueChosen == 2) oldPixVal = hsbval[0]*360;
                        else if (sortValueChosen == 3) oldPixVal = hsbval[1]*100;
                        else oldPixVal = hsbval[3]*100;
                    }if (current_value < oldPixVal - sortAttribute1 || current_value > oldPixVal + oldPixVal || i == c.length - 1) {//if the pixel value is outside the range that needs sorted
                        sortedc[i] = c[i];
                        if (start != -1) {//sort previous set that were within this edge
                            tempArray = Arrays.copyOfRange(c, start, i);
                            Arrays.sort(tempArray, MyComparator);
                            for (int j = 0; j < tempArray.length; j++) {
                                sortedc[start + j] = tempArray[j];
                            }start = -1;
                        }
                    }else {
                        if (start == -1) start = i; //Remember the start of current section within edges.
                    }
                }
            }
        }return sortedc;
    }
}
