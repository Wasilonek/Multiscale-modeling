package controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import model.Growth;
import model.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Controller {
    private GraphicsContext graphicsContext;
    private Growth growthModel;

    int grainWidth = 2;
    int grainHeight = 2;

    @FXML
    Canvas canvas;

    @FXML
    TextField numberOfGrainsField;

    @FXML
    void initialize() {
        growthModel = new Growth();
        graphicsContext = canvas.getGraphicsContext2D();
    }

    public void setGrainsAction() {
        growthModel.createGrid((int) canvas.getWidth() / grainWidth, (int) canvas.getHeight() / grainHeight);
        growthModel.randColorForEveryId(Integer.parseInt(numberOfGrainsField.getText()));
        growthModel.randomGrains(Integer.parseInt(numberOfGrainsField.getText()));
        showGrid();
    }

    @FXML
    public void startGrowthAction() {
        growthModel.moore();
        showGrid();
        System.out.println("END");
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

    public void clearCanvas() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    @FXML
    public void saveAction() {
        try {
            growthModel.saveStructure();
            saveToBMP();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveToBMP() {

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (.bmp)", ".bmp");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(Main.primary);

//        if (file != null) {
//            try {
//                WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
//                canvas.snapshot(null, writableImage);
//                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
//                ImageIO.write(renderedImage, "BMP", file);
//            } catch (IOException ex) {
//            }
//        }

        BufferedImage bi = new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
        Graphics gd = bi.getGraphics();
//        gd.drawRect(0, 0, 10, 10);
        for (int i = 0; i < growthModel.getWidth(); i++) {
            for (int j = 0; j < growthModel.getHeight(); j++) {
                if (growthModel.getGrainState(i, j) == 1) {

                    javafx.scene.paint.Color fx = growthModel.getGrain(i, j).getColor();


                    java.awt.Color awtColor = new java.awt.Color((float) fx.getRed(),
                            (float) fx.getGreen(),
                            (float) fx.getBlue(),
                            (float) fx.getOpacity());

                    gd.setColor(awtColor);

                    gd.drawRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
                    gd.fillRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
                }
            }
        }
        try {
            ImageIO.write(bi, "BMP", file);
//            ImageIO.write(bi, "PNG",file);
        } catch (IOException e) {
            System.out.println("error "+e.getMessage());
        }
    }


    @FXML
    public void loadAction() {
//        FileChooser fileChooser = new FileChooser();
        BufferedImage img = null;
//        File file = fileChooser.showSaveDialog(Main.primary);

        //read image
        try{

            img = ImageIO.read(new File("C:\\Users\\Kamil\\Desktop\\Multiscaling\\Code\\dd.bmp"));
        }catch(IOException e){
            System.out.println(e);
        }
        System.out.println(img);
        System.out.println(img.getRGB(0,0));
        int p = img.getRGB(0,0);

        //get alpha
        int a = (p>>24) & 0xff;

        //get red
        int r = (p>>16) & 0xff;

        //get green
        int g = (p>>8) & 0xff;

        //get blue
        int b = p & 0xff;

        System.out.println(r + " " + g +  " " + b);


//        for (int i = 0; i < growthModel.getWidth(); i++) {
//            for (int j = 0; j < growthModel.getHeight(); j++) {
//                if (growthModel.getGrainState(i, j) == 1) {
//
//                    javafx.scene.paint.Color fx = growthModel.getGrain(i, j).getColor();
//
//
//                    java.awt.Color awtColor = new java.awt.Color((float) fx.getRed(),
//                            (float) fx.getGreen(),
//                            (float) fx.getBlue(),
//                            (float) fx.getOpacity());
//
//                    gd.setColor(awtColor);
//
//                    gd.drawRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
//                    gd.fillRect(i * grainWidth, j * grainHeight, grainWidth, grainHeight);
//                }
//            }
//        }
    }

}