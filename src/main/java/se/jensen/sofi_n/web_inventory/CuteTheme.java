package se.jensen.sofi_n.web_inventory;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.io.InputStream;

/// CuteTheme class
/// The CuteTheme class contains most of the styling for the JAVAFX application. It contains the colour constants
/// consisting of hexcode and opatity, a function generateBackground which returns a pane with faded circles and elipses
/// of varying sizes, funcitions for button styling, box styling, and fonts(from the ttf files in resources). The purpose
/// of the class is to keep the code relating to the aesthetics of the app separate from other UI logic.

public class CuteTheme {
    /// Backgorund colour variables + opacity for background
    private final Color BG_SKY_BLUE = Color.web("#A0F1FF", 0.3);
    public static final Color BG_PALE_PINK = Color.web("#FFB7DF",  0.6);
    public static final Color BG_LAVENDER = Color.web("#CAB9FF", 0.4);
    public static final Color BG_SAGE = Color.web("#D2E4C4", 0.4);
    public static final Color BG_BUTTER = Color.web("#FFF8B0", 0.4);

    /// Colour variables as hexcode
    public static String LOGO_PINK = "#FF69b4;";

    /*The generateBackground function creates circle objects from the javafx shape package, faded
    * by lower opacity ang guassian blur, and returns a Pane with the shapes added on. The circles and
    * elipses created all have different size and blur to give a blended look*/
    public Pane generateBackground() {
        Pane background = new Pane();
        Circle blueBlob = new Circle(400,300,350, BG_SKY_BLUE);
        blueBlob.setEffect(new GaussianBlur(50));

        Circle pinkBlob = new Circle(800,500,150, BG_PALE_PINK);
        pinkBlob.setEffect(new GaussianBlur(70));

        Circle purpleBlob = new Circle(550,350,150, BG_LAVENDER);
        purpleBlob.setEffect(new GaussianBlur(45));

        Circle yellowBlob = new Circle(200,250,100, BG_SAGE);
        yellowBlob.setEffect(new GaussianBlur(40));

        Circle greenBlob = new Circle(150,100,70, BG_BUTTER);
        greenBlob.setEffect(new GaussianBlur(35));

        Circle blob1 = new Circle(300, 200, 150, BG_SKY_BLUE);
        blob1.setEffect(new GaussianBlur(50));

        Circle blob2 = new Circle(600, 350, 130, BG_PALE_PINK);
        blob2.setEffect(new GaussianBlur(70));

        Circle blob3 = new Circle(200, 400, 80, BG_LAVENDER);
        blob3.setEffect(new GaussianBlur(35));

        Circle blob4 = new Circle(550, 150, 100, BG_SAGE);
        blob4.setEffect(new GaussianBlur(45));

        Circle blob5 = new Circle(400, 500, 120, BG_BUTTER);
        blob5.setEffect(new GaussianBlur(50));

        background.getChildren().addAll(blueBlob, pinkBlob, purpleBlob, yellowBlob, greenBlob,  blob1, blob2, blob3, blob4, blob5);
        return background;
    }

    /*The VBoxHBoxStileString function returns a string to pass as argument the setStyle function for javafx panes
    * */
    public String VBoxHBoxStyleString(){
        return "-fx-background-color: white;" +
                "-fx-background-radius: 16; "+ // round corners
                "-fx-padding: 24;"+ //margin to text/fields etc
                "-fx-effect: dropshadow(gaussian,#CAB9FF,16,0.4,0,5);"; //guassian style drop shadow, lavender, 16 radius, .4 opacity, 5 down
    }
    /*the buttonStyleString function returns a string to be passed as argument in the .setStyle function
    * for a javaFX button object*/
    public String buttonStyleString() {
        return "-fx-background-color: linear-gradient(to bottom, #FFB7DF, #FF69B4);" + //light pink to pink gradience
                        "-fx-background-radius: 12;" + //round corners
                        "-fx-border-radius: 12;" + //round border corners
                        "-fx-background-insets: 0;" + //no space between background and border
                        "-fx-text-fill: white;" + //white text
                        "-fx-font-weight: bold;" + //bold
                        "-fx-effect: dropshadow(gaussian, #b25490, 8, 0.5, 0, 4);" + //dark pink shadow
                        "-fx-border-color: #FF69B4;" + //pink border
                        "-fx-border-width: 2;" + //
                        "-fx-padding: 12 32;"; // top<->bottom, left<->right, space to text
    }

    /*the lowkeyButtonStyleString function returns a string to be passed as argument in the .setStyle function
     *for a javaFX button object*/
    public String lowkeyButtonStyleString() {
        return "-fx-background-color: radial-gradient( radius 135%, #CAB9FF, white 110%);" //circular gradient, lavender to white
                + "-fx-min-width: 44px;"
                + "-fx-min-height: 44px;"
                + "-fx-max-width: 44px;"
                + "-fx-max-height: 44px;"
                + "-fx-effect: dropshadow(three-pass-box, #d4c7ff, 10, 0, 0, 1);"; //3passbox type of shadow, powder blue to very dark purple
    }

    /*the titleFont function takes an integer argument for font size, loads a font from the project
    * resources folder and returns that font in the passed size*/
    public Font titleFont(int size){
        InputStream in = getClass().getResourceAsStream("/fonts/SuperQuench-R9p8V.ttf");//opens input stream
        if(in!=null) {
            try (in) { //try w resources
                Font titleFont = Font.loadFont(in, size);
                return titleFont;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /*The function titleDropShadow takes a label, creates a dropshadow object from javaFX effects and
    * sets it to the passed label*/
    public void titleDropShadow(Label title){
        DropShadow whiteShadow = new DropShadow();
        whiteShadow.setOffsetX(-2);          // -2 left
        whiteShadow.setOffsetY(4);          // 4 pixels down
        whiteShadow.setColor(Color.WHITE);  // White halo
        whiteShadow.setRadius(4);           // 4 thick
        whiteShadow.setSpread(1);         //fully opaque, not gradiant

        title.setEffect(whiteShadow);
    }


    /*the headerFont function takes an integer argument for font size, loads a font from the project
     * resources folder and returns that font in the passed size*/
    public Font headerFont(int size){
        InputStream in = getClass().getResourceAsStream("/fonts/MoonkidsPersonalUseExtbd-gxPZ3.ttf");
        if(in!=null) {
            try(in){
                Font headerFont = Font.loadFont(in, size);
                return headerFont;
            }catch(Exception exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /*The function spacedOut takes a String, adds a space between each character and returns*/
    public static String spacedOut(String text){
        String spacedOut = "";
        for (char character : text.toCharArray()){
            spacedOut += character;
            spacedOut += " ";
        };
        return spacedOut;
    }
}
