package model;

import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.MediaTracker.*;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.*;
import java.util.List;

public class Growth {
    private Grain[][] grains;
    private int gridWidth;
    private int gridHeight;

    private int indUp;
    private int indDown;
    private int indLeft;
    private int indRight;

    private boolean isArrayFull = true;
    private int id = 0;
    private int idToAssign = 0;

    private boolean isNeig;


    private Map<Integer, Color> colorMap, colorForEveryId;
    private Map<Integer, Integer> grainMap;


    public Growth() {
        colorMap = colorForEveryId = new HashMap<>();
        grainMap = new HashMap<>();
    }

    public void createGrid(int canvasWidth, int canvasHeight) {
        grains = new Grain[canvasWidth][canvasHeight];
        gridWidth = canvasWidth;
        gridHeight = canvasHeight;

        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                grains[i][j] = new Grain();
            }
        }


    }


    public void randColorForEveryId(int numberOfGrains) {
        Random random = new Random();
        for (int i = 0; i < numberOfGrains; i++) {
            colorForEveryId.put(i, (Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble())));
        }
    }

    public void randomGrains(int numberOfGrains) {
        Random random = new Random();

        int x, y;
        int maxAttemptToRandomGrains = 0;

        clearArray();

        for (int i = 0; i < numberOfGrains; ) {

            x = random.nextInt(gridWidth);
            y = random.nextInt(gridHeight);

            if (grains[x][y].getState() == 1) {
                if (maxAttemptToRandomGrains == 10000)
                    break;
                maxAttemptToRandomGrains++;
                continue;
            }
            grains[x][y].setState(1);
            grains[x][y].setId(i);
            grains[x][y].setColor(colorForEveryId.get(grains[x][y].getId()));
            i++;
        }
    }

    public void startGrowth() {

    }

    public void printGrid() {
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                if (grains[i][j].getState() == 1) {
                    System.out.print("*");
                }
                System.out.println();
            }
        }
    }

    public boolean checkLeftUpperNeigbour(int indUp, int indLeft) {
        if (!(indUp == -1) && !(indLeft == -1)) {
            if (grains[indUp][indLeft].getState() == 1) {
                id = grains[indUp][indLeft].getId();
                fillMap(id, grainMap);
                colorMap.put(id, grains[indUp][indLeft].getColor());
                return true;
            }
        }
        return false;
    }

    public boolean checkMiddleUpperNeigbour(int indUp, int j) {
        if (!(indUp == -1)) {
            if (grains[indUp][j].getState() == 1) {
                id = grains[indUp][j].getId();
                fillMap(id, grainMap);
                colorMap.put(id, grains[indUp][j].getColor());
                return true;
            }
        }
        return false;
    }

    public boolean checkRightUpperNeigbour(int indUp, int indRight) {
        if (!(indUp == -1) && !(indRight == -1)) {
            if (grains[indUp][indRight].getState() == 1) {
                id = grains[indUp][indRight].getId();
                fillMap(id, grainMap);
                colorMap.put(id, grains[indUp][indRight].getColor());
                return true;
            }
        }
        return false;
    }

    public boolean checkLeftNeigbour(int i, int indLeft) {
        if (!(indLeft == -1)) {
            if (grains[i][indLeft].getState() == 1) {
                id = grains[i][indLeft].getId();
                fillMap(id, grainMap);
                colorMap.put(id, grains[i][indLeft].getColor());
                return true;
            }
        }
        return false;
    }

    public boolean checkRightNeigbour(int i, int indRight) {
        if (!(indRight == -1)) {
            if (grains[i][indRight].getState() == 1) {
                id = grains[i][indRight].getId();
                fillMap(id, grainMap);
                colorMap.put(id, grains[i][indRight].getColor());
                return true;
            }
        }
        return false;
    }

    public boolean checkLeftBottomNeigbour(int indDown, int indLeft) {
        if (!(indDown == -1) && !(indLeft == -1)) {
            if (grains[indDown][indLeft].getState() == 1) {
                id = grains[indDown][indLeft].getId();
                fillMap(id, grainMap);
                colorMap.put(id, grains[indDown][indLeft].getColor());
                return true;
            }
        }
        return false;
    }

    public boolean checkMiddleBottomNeigbour(int indDown, int j) {
        if (!(indDown == -1)) {
            if (grains[indDown][j].getState() == 1) {
                id = grains[indDown][j].getId();
                fillMap(id, grainMap);
                colorMap.put(id, grains[indDown][j].getColor());
                return true;
            }
        }
        return false;

    }

    public boolean checkRightBottomNeigbour(int indDown, int indRight) {
        if (!(indRight == -1) && !(indDown == -1)) {
            if (grains[indDown][indRight].getState() == 1) {
                id = grains[indDown][indRight].getId();
                fillMap(id, grainMap);
                colorMap.put(id, grains[indDown][indRight].getColor());
                return true;
            }
        }
        return false;
    }

    public void moore() {
        int numberOfGrainNeigbours;


        do {
            isArrayFull = false;
            for (int i = 0; i < gridWidth; i++) {
                for (int j = 0; j < gridHeight; j++) {
                    grainMap.clear();
                    colorMap.clear();
                    numberOfGrainNeigbours = 0;
                    if (grains[i][j].getState() == 0) {
                        isArrayFull = true;
                        setEdge(i, j);


                        if (checkLeftUpperNeigbour(indUp, indLeft)) {
                            numberOfGrainNeigbours++;
                        }

                        if (checkMiddleUpperNeigbour(indUp, j)) {
                            numberOfGrainNeigbours++;
                        }

                        if (checkRightUpperNeigbour(indUp, indRight)) {
                            numberOfGrainNeigbours++;
                        }

                        if (checkLeftNeigbour(i, indLeft)) {
                            numberOfGrainNeigbours++;
                        }

                        if (checkRightNeigbour(i, indRight)) {
                            numberOfGrainNeigbours++;
                        }

                        if (checkLeftBottomNeigbour(indDown, indLeft)) {
                            numberOfGrainNeigbours++;
                        }

                        if (checkMiddleBottomNeigbour(indDown, j)) {
                            numberOfGrainNeigbours++;
                        }

                        if (checkRightBottomNeigbour(indDown, indRight)) {
                            numberOfGrainNeigbours++;
                        }

                        if (numberOfGrainNeigbours > 0) {
                            idToAssign = getIDMaxNeighbour(grainMap);
                            grains[i][j].setNextState(1);
                            grains[i][j].setColor(colorMap.get(idToAssign));
                            grains[i][j].setId(idToAssign);
                        }
                    }
                }

            }

            copyArray();
        } while (isArrayFull);
    }


    public void setEdge(int i, int j) {
        isNeig = false;
        indUp = i - 1;
        indDown = i + 1;
        indLeft = j - 1;
        indRight = j + 1;


        if (i == 0)
            indUp = gridWidth - 1;
        if (i == (gridWidth - 1))
            indDown = 0;
        if (j == 0)
            indLeft = gridHeight - 1;
        if (j == (gridHeight - 1))
            indRight = 0;

    }

    private void fillMap(int id, Map<Integer, Integer> grainMap) {
        if (grainMap.containsKey(id)) {
            grainMap.put(id, grainMap.get(id) + 1);
        } else {
            grainMap.put(id, 1);
        }
    }

    private int getIDMaxNeighbour(Map<Integer, Integer> grainMap) {
        Map.Entry<Integer, Integer> maxEntry = null;

        for (Map.Entry<Integer, Integer> entry : grainMap.entrySet())
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
                maxEntry = entry;

        int id = 0;
        if (maxEntry != null) {
            int max = maxEntry.getValue();

            List<Integer> listMax = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : grainMap.entrySet())
                if (entry.getValue() == max)
                    listMax.add(entry.getKey());

            Random rand = new Random();
            int randWinner = rand.nextInt(listMax.size());
            id = listMax.get(randWinner);
        }

        return id;
    }

    public void copyArray() {
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                if (grains[i][j].getState() == 0) {
                    grains[i][j].setState(grains[i][j].getNextState());
                }
            }
        }
    }

    public void clearArray() {
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                grains[i][j].setState(0);
                grains[i][j].setNextState(0);
                grains[i][j].setColor(Color.WHITE);
                grains[i][j].setId(-1);
            }
        }
    }

    public Grain getGrain(int i, int j) {
        return grains[i][j];
    }

    public int getWidth() {
        return gridWidth;
    }

    public int getHeight() {
        return gridHeight;
    }

    public int getGrainState(int i, int j) {
        return grains[i][j].getState();
    }

    public void saveStructure() throws FileNotFoundException {
        saveToTxt();

    }

    public void saveToTxt() {
        try {
            FileWriter fileReader = new FileWriter("Structure.txt");
            BufferedWriter bufferWritter = new BufferedWriter(fileReader);
            PrintWriter printWriter = new PrintWriter(bufferWritter);
            printWriter.write(gridWidth + " " + gridHeight);
            printWriter.write("\n");
            for (int i = 0; i < gridWidth; i++) {
                for (int j = 0; j < gridHeight; j++) {
                    if (grains[i] != null) {
                        printWriter.write(i + " " + j + " " + grains[i][j].getState() + " " + grains[i][j].getId());
                        printWriter.write("\n");
                    }
                }
            }
            printWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }


}
