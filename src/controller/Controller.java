package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.Growth;
import model.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;


public class Controller {
    private GraphicsContext graphicsContext;
    private Growth growthModel;

    int grainWidth = 1;
    int grainHeight = 1;

    boolean isgridCreated = false;

    @FXML
    Canvas canvas;

    @FXML
    TextField numberOfGrainsField;

    @FXML
    TextField widthField;

    @FXML
    TextField heightField;

    @FXML
    TextField numberOfInclusionsTextField;

    @FXML
    TextField inclusionSizeTextField;

    @FXML
    ChoiceBox inclusionTypeChoiceBox;

    @FXML
    TextField probabilityTextField;

    @FXML
    void initialize() {
        growthModel = new Growth();
        graphicsContext = canvas.getGraphicsContext2D();

        inclusionTypeChoiceBox.setItems(FXCollections.observableArrayList("Square", "Circle"));
        inclusionTypeChoiceBox.setValue("Square");
    }

    public void setGrains() {

        if (!isgridCreated) {
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());

            canvas.setWidth(width);
            canvas.setHeight(height);

            growthModel.createGrid((int) canvas.getWidth() / grainWidth, (int) canvas.getHeight() / grainHeight);
            isgridCreated = true;
        }


        growthModel.randColorForEveryId(Integer.parseInt(numberOfGrainsField.getText()));

        growthModel.randomGrains(Integer.parseInt(numberOfGrainsField.getText()));
    }

    @FXML
    public void startGrowthAction() {
        setGrains();

        growthModel.moore();

        growthModel.setBoundaries();

        showGrid();
    }

    @FXML
    public void saveToTextFileAction() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Txt file (.txt)", ".bmp");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(Main.primary);

        System.out.println(file);
        growthModel.saveToTxt(file);
    }

    @FXML
    public void saveToBitMapAction() {
        saveToBMP();
    }

    @FXML
    public void loadFromBitMapAction() {
        growthModel.loadFromBMP();
        showGrid();
    }

    @FXML
    public void loadFromTextFileAction() {
        growthModel.loadFromTextFile();
        showGrid();
    }

    @FXML
    public void clearCanvasAction() {
        clearCanvas();
    }

    @FXML
    public void addInclusionsAction() {


        if (!isgridCreated) {
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());

            canvas.setWidth(width);
            canvas.setHeight(height);

            growthModel.createGrid((int) canvas.getWidth() / grainWidth, (int) canvas.getHeight() / grainHeight);
            isgridCreated = true;
        }

        int numberOfInclusions = Integer.parseInt(numberOfInclusionsTextField.getText());
        String inclusionType = (String) inclusionTypeChoiceBox.getValue();
        int inclusionSize = Integer.parseInt(inclusionSizeTextField.getText());


        Random random = new Random();

        int x, y;
        int maxAttemptToRandomGrains = 0;

        if (growthModel.isArrayFull()) {
            System.out.println("test");
            for (int i = 0; i < numberOfInclusions; ) {

                x = random.nextInt(growthModel.getWidth());
                y = random.nextInt(growthModel.getHeight());

                if (growthModel.getGrain(x, y).isOnBorder() == false) {
                    if (maxAttemptToRandomGrains == 10000)
                        break;
                    maxAttemptToRandomGrains++;
                    continue;
                }
                growthModel.getGrain(x, y).setState(1);
                growthModel.getGrain(x, y).setInclusion(true);
                growthModel.getGrain(x, y).setColor(javafx.scene.paint.Color.BLACK);

//                System.out.println("Initial " + x + " " + y);
                if (inclusionType == "Square") {
                    int step = (int) (inclusionSize / (2 * Math.sqrt(2))) / 2;
                    int stepsX = step / 2;
                    int stepsY = step / 2;
                    System.out.println("step" + step);
                    for (int k = 0; k < step; k++) {
                        int newX = x - stepsX;
                        stepsY = step / 2;
                        for (int j = 0; j < step; j++) {
                            int newY = y - stepsY;
                            if(newX < 0 || newX > growthModel.getWidth()-1
                                    || newY < 0 || newY > growthModel.getHeight()-1){
                                continue;
                            }
                            System.out.println(newX + " " + newY);
                            growthModel.getGrain(newX, newY).setState(1);
                            growthModel.getGrain(newX, newY).setInclusion(true);
                            growthModel.getGrain(newX, newY).setColor(javafx.scene.paint.Color.rgb(0, 0, 0));
                            stepsY--;
                        }
                        stepsX--;
                    }
                } else if (inclusionType == "Circle") {
                    System.out.println("Circle");
                    int step = (int) (inclusionSize / (2 * Math.sqrt(2))) / 2;
                    int stepsX = step / 2;
                    int stepsY = step / 2;
                    System.out.println("step" + step);
                    for (int k = 0; k < step; k++) {
                        int newX = x - stepsX;
                        stepsY = step / 2;
                        for (int j = 0; j < step; j++) {
                            int newY = y - stepsY;
                            if(newX < 0 || newX > growthModel.getWidth()
                                    || newY < 0 || newY > growthModel.getHeight()){
                                continue;
                            }
                            System.out.println(newX + " " + newY);
                            System.out.println(calcLength(newX, newY));
                            if (calcLength( newX, newY) <= inclusionSize) {
                                System.out.println("CIC");
                                growthModel.getGrain(newX, newY).setState(1);
                                growthModel.getGrain(newX, newY).setInclusion(true);
                                growthModel.getGrain(newX, newY).setColor(javafx.scene.paint.Color.BLACK);
                            }
                            stepsY--;
                        }
                        stepsX--;
                    }
                }

                i++;
            }
        } else {
            for (int i = 0; i < numberOfInclusions; ) {

                x = random.nextInt(growthModel.getWidth());
                y = random.nextInt(growthModel.getHeight());

                if (growthModel.getGrain(x, y).getState() == 1) {
                    if (maxAttemptToRandomGrains == 10000)
                        break;
                    maxAttemptToRandomGrains++;
                    continue;
                }
                growthModel.getGrain(x, y).setState(1);
                growthModel.getGrain(x, y).setInclusion(true);
//                growthModel.getGrain(x, y).setColor(javafx.scene.paint.Color.BLACK);

//                System.out.println("Initial " + x + " " + y);
                if (inclusionType == "Square") {
                    int step = (int) (inclusionSize / (2 * Math.sqrt(2))) / 2;
                    int stepsX = step / 2;
                    int stepsY = step / 2;
                    System.out.println("step" + step);
                    for (int k = 0; k < step; k++) {
                        int newX = x - stepsX;
                        stepsY = step / 2;
                        for (int j = 0; j < step; j++) {
                            int newY = y - stepsY;
                            if(newX < 0 || newX > growthModel.getWidth()-1
                                    || newY < 0 || newY > growthModel.getHeight()-1){
                                continue;
                            }
                            System.out.println(newX + " " + newY);
                            growthModel.getGrain(newX, newY).setState(1);
                            growthModel.getGrain(newX, newY).setInclusion(true);
//                            growthModel.getGrain(newX, newY).setColor(javafx.scene.paint.Color.BLACK);
                            stepsY--;
                        }
                        stepsX--;
                    }
                } else if (inclusionType == "Circle") {
//                    System.out.println("Circle");
//                    int r = inclusionSize;
//                    for (int k = y-r; k < y+r; k++) {
//                        for (int j = x; Math.pow((j-x),2) + Math.pow((k-y),2) <= r^2; j--) {
//                            //in the circle
//                        }
//                        for (int j = x+1; (j-x)*(j-x) + (k-y)*(k-y) <= r*r; j++) {
//                            //in the circle
//                        }
//                    }
//                    int step = (int) (inclusionSize / (2 * Math.sqrt(2))) / 2;
//                    int stepsX = step / 2;
//                    int stepsY = step / 2;
////                    System.out.println("step " + step);
//                    for (int k = 0; k < step; k++) {
//                        int newX = x - stepsX;
//                        stepsY = step / 2;
//                        for (int j = 0; j < step; j++) {
//                            int newY = y - stepsY;
//                            if(newX < 0 || newX > growthModel.getWidth()-1
//                                    || newY < 0 || newY > growthModel.getHeight()-1){
//                                continue;
//                            }
////                            System.out.println(newX + " " + newY);
//                            System.out.println("length: " + calcLength(newX, newY));
//                            System.out.println("Length init" + calcLength(x, y));
//                            System.out.println("Roznica " + (calcLength(newX, newY) - calcLength(x, y)));
//                            if (calcLength(newX, newY) - calcLength(x, y) <= step) {
//                                System.out.println("CIC");
//                                growthModel.getGrain(newX, newY).setState(1);
//                                growthModel.getGrain(newX, newY).setInclusion(true);
//                                growthModel.getGrain(newX, newY).setColor(javafx.scene.paint.Color.BLACK);
//                            }
//                            stepsY--;
//                        }
//                        stepsX--;
//                    }
                }

                i++;
            }
        }

        showGrid();

    }

    public double calcLength(int x2, int y2) {
        return Math.sqrt(Math.pow((x2 - 0), 2) + Math.pow((y2 - 0), 2));
    }

    public void saveToBMP() {

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("BMP file (.bmp)", ".bmp");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(Main.primary);

        System.out.println(file);

        BufferedImage bi = new BufferedImage((int) canvas.getWidth(), (int) canvas.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics graphic = bi.getGraphics();


        for (int i = 0; i < growthModel.getWidth(); i++) {
            for (int j = 0; j < growthModel.getHeight(); j++) {
                if (growthModel.getGrainState(i, j) == 1) {

                    javafx.scene.paint.Color fxColor = growthModel.getGrain(i, j).getColor();

                    java.awt.Color awtColor = new java.awt.Color((float) fxColor.getRed(),
                            (float) fxColor.getGreen(),
                            (float) fxColor.getBlue(),
                            (float) fxColor.getOpacity());

                    graphic.setColor(awtColor);
                    graphic.fillRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
                }
            }
        }
        try {
            ImageIO.write(bi, "BMP", file);
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }
    }

    public void clearCanvas() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        growthModel.clearArray();
        isgridCreated = false;
    }

    public void showGrid() {
//        clearCanvas();
        for (int i = 0; i < growthModel.getWidth(); i++) {
            for (int j = 0; j < growthModel.getHeight(); j++) {
                if (growthModel.getGrainState(i, j) == 1) {
                    if (growthModel.getGrain(i, j).isInclusion()) {
                        graphicsContext.setFill(javafx.scene.paint.Color.BLACK);
                    } else {
                        graphicsContext.setFill(growthModel.getGrain(i, j).getColor());
                    }
                    graphicsContext.fillRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
                }
            }
        }
    }

    @FXML
    public void furtherMooreAction() {
        setGrains();
        growthModel.furtherMoore(Integer.parseInt(probabilityTextField.getText()));
        showGrid();
    }
}

