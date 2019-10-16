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


public class Controller {
    private GraphicsContext graphicsContext;
    private Growth growthModel;

    int grainWidth = 1;
    int grainHeight = 1;

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
    void initialize() {
        growthModel = new Growth();
        graphicsContext = canvas.getGraphicsContext2D();

        inclusionTypeChoiceBox.setItems(FXCollections.observableArrayList("Square", "Circle"));
        inclusionTypeChoiceBox.setValue("Square");
    }

    public void setGrains() {

        int width = Integer.parseInt(widthField.getText());
        int height = Integer.parseInt(heightField.getText());

        canvas.setWidth(width);
        canvas.setHeight(height);

        growthModel.createGrid((int) canvas.getWidth() / grainWidth, (int) canvas.getHeight() / grainHeight);

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
        growthModel.saveToTxt();
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

    }

    public void saveToBMP() {

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("BMP file (.bmp)", ".bmp");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(Main.primary);

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
    }

    public void showGrid() {
        clearCanvas();
        for (int i = 0; i < growthModel.getWidth(); i++) {
            for (int j = 0; j < growthModel.getHeight(); j++) {
                if (growthModel.getGrainState(i, j) == 1) {
                    graphicsContext.setFill(growthModel.getGrain(i, j).getColor());
                    graphicsContext.fillRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
                }
            }
        }
    }
}

