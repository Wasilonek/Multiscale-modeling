package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import model.Grain;
import model.Growth;
import model.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;


public class Controller {
    private GraphicsContext graphicsContext;
    private Growth growthModel;

    int grainWidth = 1;
    int grainHeight = 1;

    boolean isgridCreated = false;

    boolean isLoadedFromFile = false;

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
    CheckBox setBorderCheckBox;

    @FXML
    Label GBLabel;

    //  ---------------------------
    @FXML
    TextField numberOfStateTextFiled;

    @FXML
    TextField constantTextField;

    @FXML
    TextField iterationsTextField;

    @FXML
    TextField energyOnBorderTextField;

    @FXML
    TextField energyTextField;

    @FXML
    TextField noiseTextField;

    @FXML
    ToggleButton showEnergyToggle;

    @FXML
    void initialize() {
        growthModel = new Growth();
        graphicsContext = canvas.getGraphicsContext2D();

        inclusionTypeChoiceBox.setItems(FXCollections.observableArrayList("Square", "Circle"));
        inclusionTypeChoiceBox.setValue("Square");

        phaseChoiceBox.setItems(FXCollections.observableArrayList("Substructure", "Dual phase"));
        phaseChoiceBox.setValue("Dual phase");
    }

    public void setGrains() {

        if (!isgridCreated) {
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());

            canvas.setWidth(width);
            canvas.setHeight(height);


            if (isLoadedFromFile) {
                isLoadedFromFile = false;
            } else {
                growthModel.createGrid((int) canvas.getWidth() / grainWidth, (int) canvas.getHeight() / grainHeight);
                isgridCreated = true;

            }
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
        fileChooser.setInitialDirectory(new File("./"));

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
        isLoadedFromFile = true;
        showGrid();
    }

    @FXML
    public void loadFromTextFileAction() {
        growthModel.loadFromTextFile();
        isLoadedFromFile = true;
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
        fileChooser.setInitialDirectory(new File("./"));

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
                } else {
                    System.out.println("BLACK");
                    javafx.scene.paint.Color fxColor = growthModel.getGrain(i, j).getColor();


                    java.awt.Color awtColor = new java.awt.Color(1, 1, 1, 1);
                    graphic.setColor(new java.awt.Color(255, 255, 255));
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

                    if (growthModel.getGrain(i, j).isDualPhase()) {
                        graphicsContext.setFill(javafx.scene.paint.Color.RED);
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

    //    --------------------------------------------

    @FXML
    public void canvasClickedAction(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getSceneX();
        int y = (int) mouseEvent.getSceneY() - 25;
        String phase = phaseChoiceBox.getValue().toString();
        growthModel.selectGrain(x, y, phase);
        growthModel.setGrainsToDualPhase(x, y);
    }

    @FXML
    public void showBoundariesAction() {
        int size = Integer.parseInt(boundarySizeTextField.getText());
        graphicsContext.setFill(javafx.scene.paint.Color.BLACK);
        for (int i = 0; i < growthModel.getWidth(); i++) {
            for (int j = 0; j < growthModel.getHeight(); j++) {
                if (growthModel.getGrain(i, j).isOnBorder()) {
                    graphicsContext.fillRect(i * grainWidth, j * grainHeight, size, size);
                }
            }
        }
        growthModel.clearArray();
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        WritableImage snapshot = canvas.snapshot(new SnapshotParameters(), writableImage);
        PixelReader pixelReader = snapshot.getPixelReader();
        for (int i = 0; i < growthModel.getWidth(); i++) {
            for (int j = 0; j < growthModel.getHeight(); j++) {
                int color = pixelReader.getArgb(i, j);
                int red = (color >> 16) & 0xff;
                int green = (color >> 8) & 0xff;
                int blue = color & 0xff;
                if (red == 0 && green == 0 && blue == 0) {
                    growthModel.getGrain(i, j).setInclusion(true);
                    growthModel.getGrain(i, j).setState(1);
                    growthModel.getGrain(i, j).setNextState(0);
                    growthModel.getGrain(i, j).setColor(javafx.scene.paint.Color.BLACK);
                    growthModel.getGrain(i, j).setId(-1);
                    growthModel.getGrain(i, j).setOnBorder(false);
                    growthModel.getGrain(i, j).setGrainSelected(false);
                    growthModel.getGrain(i, j).setFrozen(false);
                }
            }
        }
    }

    @FXML
    public void clearWithBoundariesAction() {
        double holeSize = growthModel.getWidth() * growthModel.getHeight();
        double boundarySize = 0;
        graphicsContext.setFill(javafx.scene.paint.Color.WHITE);
        for (int i = 0; i < growthModel.getWidth(); i++) {
            for (int j = 0; j < growthModel.getHeight(); j++) {
                if (!growthModel.getGrain(i, j).isInclusion()) {
                    graphicsContext.fillRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
                } else {
                    boundarySize++;
                }
            }
        }
        DecimalFormat df2 = new DecimalFormat("#.##");
        GBLabel.setText("" + df2.format(boundarySize / holeSize));
    }

    @FXML
    public void showSelectedBoundaryAction() {
        int size = Integer.parseInt(boundarySizeTextField.getText());
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.setFill(javafx.scene.paint.Color.BLACK);
        for (int i = 0; i < growthModel.getWidth(); i++) {
            for (int j = 0; j < growthModel.getHeight(); j++) {
                if (growthModel.getGrain(i, j).isOnBorder()) {
                    for (int k = 0; k < growthModel.selectedGrainsIds.size(); k++) {
                        if (growthModel.getGrain(i, j).getId() == (int) growthModel.selectedGrainsIds.get(k)) {
                            graphicsContext.fillRect(i * grainWidth, j * grainHeight, size, size);
                            growthModel.getGrain(i, j).setFrozen(true);
                        }
                    }
                }
            }
        }
        readInclusions();
    }

    public void readInclusions() {
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        WritableImage snapshot = canvas.snapshot(new SnapshotParameters(), writableImage);
        PixelReader pixelReader = snapshot.getPixelReader();
        for (int i = 0; i < growthModel.getWidth(); i++) {
            for (int j = 0; j < growthModel.getHeight(); j++) {
                int color = pixelReader.getArgb(i, j);
                int red = (color >> 16) & 0xff;
                int green = (color >> 8) & 0xff;
                int blue = color & 0xff;
                growthModel.clearGrain(i, j);
                if (red == 0 && green == 0 && blue == 0) {
                    growthModel.getGrain(i, j).setInclusion(true);
                    growthModel.getGrain(i, j).setState(1);
                }
            }
        }
    }

    @FXML
    public void regrowthAction() {
        setGrains();

        growthModel.regrowth();

        growthModel.setBoundaries();

        showGrid();

    }


//    ----------------------------------------------------------------

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
                            growthModel.getGrain(i, j).setFrozen(true);

                        }
                    }
                    for (int k = 0; k < growthModel.selectedDualIds.size(); k++) {
                        if (growthModel.getGrain(i, j).getId() == (int) growthModel.selectedDualIds.get(k)) {
                            graphicsContext.setFill(javafx.scene.paint.Color.RED);
                            graphicsContext.fillRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
                            growthModel.getGrain(i, j).setFrozen(true);
                            growthModel.getGrain(i, j).setColor(javafx.scene.paint.Color.RED);
                        }
                    }
                }
            }
        }

    }


    // ************* Monte Carlo *****************

    @FXML
    public void fillMonteCarloGrains() {
        int width = Integer.parseInt(widthField.getText());
        int height = Integer.parseInt(heightField.getText());

        canvas.setWidth(width);
        canvas.setHeight(height);

        growthModel.createGrid((int) canvas.getWidth() / grainWidth, (int) canvas.getHeight() / grainHeight);

        growthModel.randColorForEveryId(Integer.parseInt(numberOfStateTextFiled.getText()));

        growthModel.fillSpaceWithGrains(Integer.parseInt(numberOfStateTextFiled.getText()));

        showGrid();
    }

    @FXML
    public void monteCarloGrowthAction() {

        growthModel.fillSpaceWithGrains(Integer.parseInt(numberOfStateTextFiled.getText()));

        growthModel.MonteCarloGrowth(
                Integer.parseInt(iterationsTextField.getText()),
                Double.parseDouble(constantTextField.getText()));
        isgridCreated = true;
        showGrid();
    }

    @FXML
    public void showMonteCarloDualPhase() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i < growthModel.getWidth(); i++) {
            for (int j = 0; j < growthModel.getHeight(); j++) {
                if (growthModel.getGrain(i, j).isDualPhase()) {
                    graphicsContext.setFill(javafx.scene.paint.Color.RED);
                    graphicsContext.fillRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
                } else {
                    growthModel.clearGrain(i, j);
                }
            }
        }
    }

    @FXML
    public void showEnergyAction() {
        double energy = Double.parseDouble(energyTextField.getText());
        double noise = Double.parseDouble(noiseTextField.getText());
        double energyOnBorder = Double.parseDouble(energyOnBorderTextField.getText());
        double minEnergy = energy - noise;
        double maxEnergy = energy + noise;

        growthModel.setEnergyWithBorders(energy, noise, energyOnBorder);


        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (showEnergyToggle.isSelected()) {
            for (int i = 0; i < growthModel.getWidth(); i++) {
                for (int j = 0; j < growthModel.getHeight(); j++) {
                    Grain grain = growthModel.getGrain(i, j);

                    if (grain.isOnBorder()) {
                        graphicsContext.setFill(javafx.scene.paint.Color.YELLOW);
                        graphicsContext.fillRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
                        continue;
                    }


                    int color = (int) (  ((grain.getEnergy() - minEnergy) * (255 - 200) ) / (maxEnergy - minEnergy)) + 200;
                    System.out.println(color);
                    graphicsContext.setFill(javafx.scene.paint.Color.rgb(0, 0, color));
                    graphicsContext.fillRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
                }
            }
        } else {
            showGrid();
        }

    }
}

