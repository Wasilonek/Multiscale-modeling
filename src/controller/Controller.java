package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    TextField boundarySizeTextField;

    @FXML
    ChoiceBox phaseChoiceBox;

    @FXML
    void initialize() {
        growthModel = new Growth();
        graphicsContext = canvas.getGraphicsContext2D();

        inclusionTypeChoiceBox.setItems(FXCollections.observableArrayList("Square", "Circle"));
        inclusionTypeChoiceBox.setValue("Square");

        phaseChoiceBox.setItems(FXCollections.observableArrayList("Substructure", "Dual phase"));
        phaseChoiceBox.setValue("Substructure");
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
                            if (newX < 0 || newX > growthModel.getWidth() - 1
                                    || newY < 0 || newY > growthModel.getHeight() - 1) {
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

                    int step = (int) (inclusionSize / (2 * Math.sqrt(2))) / 2;
                    int stepsX = step / 2;
                    int stepsY = step / 2;

                    for (int k = 0; k < step; k++) {
                        int newX = x - stepsX;
                        stepsY = step / 2;
                        for (int j = 0; j < step; j++) {
                            int newY = y - stepsY;
                            if (newX < 0 || newX > growthModel.getWidth()
                                    || newY < 0 || newY > growthModel.getHeight()) {
                                continue;
                            }

                            System.out.println(Math.abs(x - newX));
                            System.out.println(Math.abs(y - newY));
                            System.out.println(Math.pow(Math.abs(x - newX), 2) + Math.pow(Math.abs(y - newY), 2));
                            System.out.println(step);
                            System.out.println();

                            if (Math.pow(Math.abs(x - newX), 2) + Math.pow(Math.abs(y - newY), 2) < step) {
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
                            if (newX < 0 || newX > growthModel.getWidth() - 1
                                    || newY < 0 || newY > growthModel.getHeight() - 1) {
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
                    int step = (int) (inclusionSize / (2 * Math.sqrt(2))) / 2;
                    int stepsX = step / 2;
                    int stepsY;
                    for (int k = 0; k < step; k++) {
                        int newX = x - stepsX;
                        stepsY = step / 2;
                        for (int j = 0; j < step; j++) {
                            int newY = y - stepsY;
                            if (newX < 0 || newX > growthModel.getWidth() - 1
                                    || newY < 0 || newY > growthModel.getHeight() - 1) {
                                continue;
                            }

                            if (Math.pow(Math.abs(x - newX), 2) + Math.pow(Math.abs(y - newY), 2) < step) {
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
        }

        showGrid();

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

    @FXML
    public void clearUnselectedGrainsAction() {
        System.out.println("Clear unselected");
        growthModel.clearUnselectedGrains();
        showGrid();
    }

//    --------------------------------------------

    @FXML
    public void showBoundariesAction() {
        int size = Integer.parseInt(boundarySizeTextField.getText());
        graphicsContext.setFill(javafx.scene.paint.Color.BLACK);
        for (int i = 0; i < growthModel.getWidth(); i++) {
            for (int j = 0; j < growthModel.getHeight(); j++) {
                if (growthModel.getGrain(i, j).isOnBorder()) {
                    graphicsContext.fillRect(i * grainWidth, j * grainHeight, size, size);
                    growthModel.getGrain(i, j).setInclusion(true);
                }
            }
        }
    }

    @FXML
    public void clearWithBoundariesAction() {
        graphicsContext.setFill(javafx.scene.paint.Color.WHITE);
        for (int i = 0; i < growthModel.getWidth(); i++) {
            for (int j = 0; j < growthModel.getHeight(); j++) {
                if (!growthModel.getGrain(i, j).isOnBorder()) {
                    graphicsContext.fillRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
                    growthModel.getGrain(i, j).setInclusion(true);
                }
            }
        }
    }

    @FXML
    public void canvasClickedAction(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getSceneX();
        int y = (int) mouseEvent.getSceneY() - 25;
        String phase = phaseChoiceBox.getValue().toString();
        growthModel.selectGrain(x, y,phase);
    }

    @FXML
    public void showSelectedBoundaryAction() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.setFill(javafx.scene.paint.Color.BLACK);
        for (int i = 0; i < growthModel.getWidth(); i++) {
            for (int j = 0; j < growthModel.getHeight(); j++) {
                if (growthModel.getGrain(i, j).isOnBorder()) {
                    for (int k = 0; k < growthModel.selectedGrainsIds.size(); k++) {
                        if (growthModel.getGrain(i, j).getId() == (int) growthModel.selectedGrainsIds.get(k)) {
                            graphicsContext.fillRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
                            growthModel.getGrain(i, j).setFrozen(true);
                        }
                    }
                } else {
                    growthModel.clearGrain(i, j);
                }
            }
        }
    }


    @FXML
    public void clearPhaseAction() {
        growthModel.clearPhase();
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i < growthModel.getWidth(); i++) {
            for (int j = 0; j < growthModel.getHeight(); j++) {
                if (growthModel.getGrain(i, j).isGrainSelected()) {
                    for (int k = 0; k < growthModel.selectedPhaseIds.size(); k++) {
                        if (growthModel.getGrain(i, j).getId() == (int) growthModel.selectedPhaseIds.get(k)) {
                            graphicsContext.setFill(growthModel.getGrain(i, j).getColor());
                            graphicsContext.fillRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
                        }
                    }
                    for (int k = 0; k < growthModel.selectedDualIds.size(); k++) {
                        if (growthModel.getGrain(i, j).getId() == (int) growthModel.selectedDualIds.get(k)) {
                            graphicsContext.setFill(javafx.scene.paint.Color.RED);
                            graphicsContext.fillRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
                        }
                    }
                }
            }
        }

    }
}

