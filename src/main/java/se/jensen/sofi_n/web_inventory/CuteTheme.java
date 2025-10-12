package se.jensen.sofi_n.web_inventory;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class CuteTheme {
    public static final Color SKY_BLUE = Color.web("#A0F1FF", 0.3);
    public static final Color PINK = Color.web("#FFB7DF",  0.6);
    public static final Color LAVENDER = Color.web("#CAB9FF", 0.4);
    public static final Color SAGE = Color.web("#D2E4C4", 0.4);
    public static final Color BUTTER = Color.web("#FFF8B0", 0.4);

    public Pane generateBackground() {
        Pane background = new Pane();
        Circle blueBlob = new Circle(400,300,350, SKY_BLUE);
        blueBlob.setEffect(new GaussianBlur(50));

        Circle pinkBlob = new Circle(800,500,150, PINK);
        pinkBlob.setEffect(new GaussianBlur(70));

        Circle purpleBlob = new Circle(550,350,150, LAVENDER);
        purpleBlob.setEffect(new GaussianBlur(45));

        Circle yellowBlob = new Circle(200,250,100, SAGE);
        yellowBlob.setEffect(new GaussianBlur(40));

        Circle greenBlob = new Circle(150,100,70, BUTTER);
        greenBlob.setEffect(new GaussianBlur(35));

        Circle blob1 = new Circle(300, 200, 150, SKY_BLUE);
        blob1.setEffect(new GaussianBlur(50));

        Circle blob2 = new Circle(600, 350, 130, PINK);
        blob2.setEffect(new GaussianBlur(70));

        Circle blob3 = new Circle(200, 400, 80, LAVENDER);
        blob3.setEffect(new GaussianBlur(35));

        Circle blob4 = new Circle(550, 150, 100, SAGE);
        blob4.setEffect(new GaussianBlur(45));

        Circle blob5 = new Circle(400, 500, 120, BUTTER);
        blob5.setEffect(new GaussianBlur(50));

        background.getChildren().addAll(blueBlob, pinkBlob, purpleBlob, yellowBlob, greenBlob,  blob1, blob2, blob3, blob4, blob5);
        return background;
    }

    public String VBoxHBoxStyleString(){
        return "-fx-background-color: white; -fx-padding: 24; -fx-background-radius: 16; " +
                "-fx-effect: dropshadow(gaussian,#CAB9FF,16,0.4,0,5);";
    }

    public String buttonStyleString() {
        return "-fx-background-color: linear-gradient(to bottom, #FFB7DF, #FF69B4);" + //light pink to pink gradience
                        "-fx-background-radius: 12;" + //round background corners
                        "-fx-border-radius: 12;" + //round border corners
                        "-fx-background-insets: 0;" + //background to border
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-effect: dropshadow(gaussian, #b25490, 8, 0.5, 0, 4);" + //dark pink shadow
                        "-fx-border-color: #FF69B4;" + //pink border
                        "-fx-border-width: 2;" +
                        "-fx-padding: 12 32;"; // top<->bottom, left<->right
    }
    public String lowkeyButtonStyleString() {
        return "-fx-background-color: radial-gradient( radius 135%, #CAB9FF, white 110%);"
                + "-fx-min-width: 44px;"
                + "-fx-min-height: 44px;"
                + "-fx-max-width: 44px;"
                + "-fx-max-height: 44px;"
                + "-fx-effect: dropshadow(three-pass-box, #d4c7ff, 10, 0, 0, 1);";
    }
    public Font titleFont(int size){
        Font titleFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SuperQuench-R9p8V.ttf"),size);
        return titleFont;
    }

    public void titleDropShadow(Label title){
        DropShadow whiteShadow = new DropShadow();
        whiteShadow.setOffsetX(-2);          // -2 left
        whiteShadow.setOffsetY(4);          // 4 pixels down
        whiteShadow.setColor(Color.WHITE);  // White halo
        whiteShadow.setRadius(4);           // 4 thick
        whiteShadow.setSpread(1);         //fully opaque, not gradiant

        title.setEffect(whiteShadow);
    }

    public Font headerFont(int size){
        Font headerFont = Font.loadFont(getClass().getResourceAsStream("/fonts/MoonkidsPersonalUseExtbd-gxPZ3.ttf"),size);
        return headerFont;
    }

    public static String spacedOut(String text){
        String spacedOut = "";
        for (char character : text.toCharArray()){
            spacedOut += character;
            spacedOut += " ";
        };
        return spacedOut;
    }
}
