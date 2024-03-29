package model;

import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import java.util.List;

public class Growth {
    public Grain[][] grains;
    private int gridWidth;

    private int gridHeight;

    private int indUp;
    private int indDown;
    private int indLeft;
    private int indRight;

    private boolean isArrayFull = false;
    private int id = 0;
    private int idToAssign = 0;

    private boolean isNeig;

    Random random;

    private Map<Integer, Color> colorMap, colorForEveryId;
    private Map<Integer, Integer> grainMap;

    public List selectedGrainsIds;
    public List selectedPhaseIds;
    public List selectedDualIds;

    //    ******************** Monte Carlo ****************
    public List monteCarloGrainsList;
    public List monteCarloCounters;


    public Growth() {
        colorMap = colorForEveryId = new HashMap<>();
        grainMap = new HashMap<>();
        random = new Random();

        selectedGrainsIds = new ArrayList();
        selectedPhaseIds = new ArrayList();
        selectedDualIds = new ArrayList();
        monteCarloGrainsList = new ArrayList();
        monteCarloCounters = new ArrayList<>();
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

        int counter = 0;
        int x, y;
        int maxAttemptToRandomGrains = 0;

//        clearArray();

        for (int i = 0; i < numberOfGrains; ) {

            x = random.nextInt(gridWidth);
            y = random.nextInt(gridHeight);

            if (grains[x][y].getState() == 1) {
                if (maxAttemptToRandomGrains == 10000) {
                    System.out.println("Rand error");
                    break;
                }

                maxAttemptToRandomGrains++;
                continue;
            }
            counter++;
            grains[x][y].setX(x);
            grains[x][y].setY(y);
            grains[x][y].set_ID(counter);
            grains[x][y].setState(1);
            grains[x][y].setId(i);
            grains[x][y].setColor(colorForEveryId.get(grains[x][y].getId()));
            grains[x][y].setInclusion(false);
            grains[x][y].setFrozen(false);
            grains[x][y].setGrainSelected(false);
            grains[x][y].setOnBorder(false);
            i++;
        }
    }

    public boolean checkLeftUpperNeigbour(int indUp, int indLeft) {
        if (grains[indUp][indLeft].getState() == 1 && grains[indUp][indLeft].isInclusion() == false
                && !grains[indUp][indLeft].isGrainSelected() && !grains[indUp][indLeft].isFrozen() && !grains[indUp][indLeft].isDualPhase()) {

            id = grains[indUp][indLeft].getId();
            fillMap(id, grainMap);
            colorMap.put(id, grains[indUp][indLeft].getColor());
            return true;
        }
        return false;
    }

    public boolean checkMiddleUpperNeigbour(int indUp, int j) {
        if (grains[indUp][j].getState() == 1 && grains[indUp][j].isInclusion() == false
                && !grains[indUp][j].isGrainSelected() && !grains[indUp][j].isFrozen() && !grains[indUp][j].isDualPhase()) {
            id = grains[indUp][j].getId();
            fillMap(id, grainMap);
            colorMap.put(id, grains[indUp][j].getColor());
            return true;
        }
        return false;
    }

    public boolean checkRightUpperNeigbour(int indUp, int indRight) {
        if (grains[indUp][indRight].getState() == 1 && grains[indUp][indRight].isInclusion() == false
                && !grains[indUp][indRight].isGrainSelected() && !grains[indUp][indRight].isFrozen() && !grains[indUp][indRight].isDualPhase()) {
            id = grains[indUp][indRight].getId();
            fillMap(id, grainMap);
            colorMap.put(id, grains[indUp][indRight].getColor());
            return true;
        }
        return false;
    }

    public boolean checkLeftNeigbour(int i, int indLeft) {
        if (grains[i][indLeft].getState() == 1 && grains[i][indLeft].isInclusion() == false
                && !grains[i][indLeft].isGrainSelected() && !grains[i][indLeft].isFrozen() && !grains[i][indLeft].isDualPhase()) {
            id = grains[i][indLeft].getId();
            fillMap(id, grainMap);
            colorMap.put(id, grains[i][indLeft].getColor());
            return true;
        }
        return false;
    }

    public boolean checkRightNeigbour(int i, int indRight) {
        if (grains[i][indRight].getState() == 1 && grains[i][indRight].isInclusion() == false
                && !grains[i][indRight].isGrainSelected() && !grains[i][indRight].isFrozen() && !grains[i][indRight].isDualPhase()) {
            id = grains[i][indRight].getId();
            fillMap(id, grainMap);
            colorMap.put(id, grains[i][indRight].getColor());
            return true;
        }
        return false;
    }

    public boolean checkLeftBottomNeigbour(int indDown, int indLeft) {
        if (grains[indDown][indLeft].getState() == 1 && grains[indDown][indLeft].isInclusion() == false
                && !grains[indDown][indLeft].isGrainSelected() && !grains[indDown][indLeft].isFrozen() && !grains[indDown][indLeft].isDualPhase()) {
            id = grains[indDown][indLeft].getId();
            fillMap(id, grainMap);
            colorMap.put(id, grains[indDown][indLeft].getColor());
            return true;
        }
        return false;
    }

    public boolean checkMiddleBottomNeigbour(int indDown, int j) {
        if (grains[indDown][j].getState() == 1 && grains[indDown][j].isInclusion() == false
                && !grains[indDown][j].isGrainSelected() && !grains[indDown][j].isFrozen() && !grains[indDown][j].isDualPhase()) {
            id = grains[indDown][j].getId();
            fillMap(id, grainMap);
            colorMap.put(id, grains[indDown][j].getColor());
            return true;
        }
        return false;

    }

    public boolean checkRightBottomNeigbour(int indDown, int indRight) {
        if (grains[indDown][indRight].getState() == 1 && grains[indDown][indRight].isInclusion() == false
                && !grains[indDown][indRight].isGrainSelected() && !grains[indDown][indRight].isFrozen() && !grains[indDown][indRight].isDualPhase()) {
            id = grains[indDown][indRight].getId();
            fillMap(id, grainMap);
            colorMap.put(id, grains[indDown][indRight].getColor());
            return true;
        }
        return false;
    }


    public void moore() {
        int numberOfGrainNeigbours;
        boolean isGrainChange;

        do {
            isArrayFull = false;
            isGrainChange = false;


            for (int i = 0; i < gridWidth; i++) {
                for (int j = 0; j < gridHeight; j++) {

                    grainMap.clear();
                    colorMap.clear();
                    numberOfGrainNeigbours = 0;
                    if (grains[i][j].getState() == 0 && !grains[i][j].isInclusion() && !grains[i][j].isGrainSelected() && !grains[i][j].isDualPhase()) {
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
                            isGrainChange = true;

                            idToAssign = getIDMaxNeighbour(grainMap);
                            grains[i][j].setNextState(1);
                            grains[i][j].setColor(colorMap.get(idToAssign));
                            grains[i][j].setId(idToAssign);
                            grains[i][j].setInclusion(false);
                        }
                    }
                }

            }

            copyArray();
        } while (isArrayFull && isGrainChange);
        isArrayFull = true;
        setBoundaries();
    }

    public void regrowth() {
        int numberOfGrainNeigbours;
        boolean isGrainChange;

        do {
            isArrayFull = false;
            isGrainChange = false;
            for (int i = 0; i < gridWidth; i++) {
                for (int j = 0; j < gridHeight; j++) {
                    grainMap.clear();
                    colorMap.clear();
                    numberOfGrainNeigbours = 0;
                    if (!grains[i][j].isInclusion() && grains[i][j].getState() == 0) {
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
                            isGrainChange = true;
                            idToAssign = getIDMaxNeighbour(grainMap);
                            grains[i][j].setNextState(1);
                            grains[i][j].setColor(colorMap.get(idToAssign));
                            grains[i][j].setId(idToAssign);
                            grains[i][j].setInclusion(false);
                        }
                    }
                }
            }
            copyArray();
        } while (isArrayFull && isGrainChange);
        isArrayFull = true;
        setBoundaries();
    }

    public void furtherMoore(int probability) {
        boolean isGrainChange = false;
        do {
            isArrayFull = false;
            isGrainChange = false;
            for (int i = 0; i < gridWidth; i++) {
                for (int j = 0; j < gridHeight; j++) {
                    if (grains[i][j].getState() == 0 && grains[i][j].isInclusion() == false) {
                        setEdge(i, j);
                        isArrayFull = true;
                        if (firstRule(i, j)) {
                            isGrainChange = true;
                        } else if (secondRule(i, j)) {
                            isGrainChange = true;

                        } else if (thirdRule(i, j)) {
                            isGrainChange = true;

                        } else if (fourthRule(i, j, probability)) {
                            isGrainChange = true;

                        }
                    }

                }
            }
            copyArray();
        } while (isArrayFull && isGrainChange);
        isArrayFull = true;
        setBoundaries();
    }

    private boolean firstRule(int i, int j) {
        grainMap.clear();
        colorMap.clear();
        int numberOfGrainNeigbours = 0;

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
            if (grainMap.get(id) >= 5) {
                grains[i][j].setNextState(1);
                grains[i][j].setColor(colorMap.get(idToAssign));
                grains[i][j].setId(idToAssign);
                grains[i][j].setInclusion(false);
                return true;
            }
        }
        return false;
    }

    private boolean secondRule(int i, int j) {
        grainMap.clear();
        colorMap.clear();
        int numberOfGrainNeigbours = 0;
        if (checkMiddleUpperNeigbour(indUp, j)) {
            numberOfGrainNeigbours++;
        }

        if (checkLeftNeigbour(i, indLeft)) {
            numberOfGrainNeigbours++;
        }

        if (checkRightNeigbour(i, indRight)) {
            numberOfGrainNeigbours++;
        }

        if (checkMiddleBottomNeigbour(indDown, j)) {
            numberOfGrainNeigbours++;
        }

        if (numberOfGrainNeigbours > 0) {
            idToAssign = getIDMaxNeighbour(grainMap);
            if (grainMap.get(id) >= 3) {
                grains[i][j].setNextState(1);
                grains[i][j].setColor(colorMap.get(idToAssign));
                grains[i][j].setId(idToAssign);
                grains[i][j].setInclusion(false);
                return true;
            }
        }
        return false;
    }

    private boolean thirdRule(int i, int j) {
        grainMap.clear();
        colorMap.clear();
        int numberOfGrainNeigbours = 0;

        if (checkLeftUpperNeigbour(indUp, indLeft)) {
            numberOfGrainNeigbours++;
        }

        if (checkRightUpperNeigbour(indUp, indRight)) {
            numberOfGrainNeigbours++;
        }

        if (checkLeftBottomNeigbour(indDown, indLeft)) {
            numberOfGrainNeigbours++;
        }

        if (checkRightBottomNeigbour(indDown, indRight)) {
            numberOfGrainNeigbours++;
        }

        if (numberOfGrainNeigbours > 0) {
            idToAssign = getIDMaxNeighbour(grainMap);
            if (grainMap.get(id) >= 3) {
                grains[i][j].setNextState(1);
                grains[i][j].setColor(colorMap.get(idToAssign));
                grains[i][j].setId(idToAssign);
                grains[i][j].setInclusion(false);
                return true;
            }
        }
        return false;
    }

    private boolean fourthRule(int i, int j, int probability) {
        grainMap.clear();
        colorMap.clear();
        int numberOfGrainNeigbours = 0;

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
            int randomNumber = random.nextInt(100) + 1;
            if (randomNumber <= probability) {
                idToAssign = getIDMaxNeighbour(grainMap);
                setGrain(i, j, idToAssign);
                return false;
            } else {
                return true;
            }

        }
        return false;
    }

    private void setGrain(int i, int j, int idToAssign) {
        grains[i][j].setNextState(1);
        grains[i][j].setColor(colorMap.get(idToAssign));
        grains[i][j].setId(idToAssign);
        grains[i][j].setInclusion(false);
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

    public void setBoundaries() {
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                Set<Integer> tempIds = new HashSet<>();
                setEdge(i, j);

                if (grains[indUp][indLeft].getState() == 1) {
                    tempIds.add(grains[indUp][indLeft].getId());
                }

                if (grains[indUp][j].getState() == 1) {
                    tempIds.add(grains[indUp][j].getId());
                }

                if (grains[indUp][indRight].getState() == 1) {
                    tempIds.add(grains[indUp][indRight].getId());
                }

                if (grains[i][indLeft].getState() == 1) {
                    tempIds.add(grains[i][indLeft].getId());
                }

                if (grains[i][indRight].getState() == 1) {
                    tempIds.add(grains[i][indRight].getId());
                }

                if (grains[indDown][indLeft].getState() == 1) {
                    tempIds.add(grains[indDown][indLeft].getId());
                }

                if (grains[indDown][j].getState() == 1) {
                    tempIds.add(grains[indDown][j].getId());
                }

                if (grains[indDown][indRight].getState() == 1) {
                    tempIds.add(grains[indDown][indRight].getId());
                }

                if (tempIds.size() > 1) {
                    grains[i][j].setOnBorder(true);
                }
            }
        }
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
                grains[i][j].setX(-1);
                grains[i][j].setY(-1);
                grains[i][j].set_ID(-1);
                grains[i][j].setState(0);
                grains[i][j].setNextState(0);
                grains[i][j].setColor(Color.WHITE);
                grains[i][j].setId(-1);
                grains[i][j].setOnBorder(false);
                grains[i][j].setInclusion(false);
                grains[i][j].setGrainSelected(false);
                grains[i][j].setFrozen(false);

            }
        }
        isArrayFull = false;
        selectedGrainsIds.clear();
        selectedPhaseIds.clear();
        selectedDualIds.clear();
    }

    public void clearGrain(int i, int j) {
        grains[i][j].setX(-1);
        grains[i][j].setY(-1);
        grains[i][j].set_ID(-1);
        grains[i][j].setState(0);
        grains[i][j].setNextState(0);
        grains[i][j].setColor(Color.WHITE);
        grains[i][j].setId(-1);
        grains[i][j].setOnBorder(false);
        grains[i][j].setInclusion(false);
        grains[i][j].setGrainSelected(false);
        grains[i][j].setFrozen(false);
        grains[i][j].setDualPhase(false);
    }

    public void saveToTxt(File filePath) {
        try {
            FileWriter fileReader = new FileWriter(new File(String.valueOf(filePath)));
            BufferedWriter bufferWritter = new BufferedWriter(fileReader);
            PrintWriter printWriter = new PrintWriter(bufferWritter);
            printWriter.write(gridWidth + " " + gridHeight);
            printWriter.write("\n");
            for (int i = 0; i < gridWidth; i++) {
                for (int j = 0; j < gridHeight; j++) {
                    if (grains[i] != null) {
                        printWriter.write(
                                i +
                                        " " + j +
                                        " " + grains[i][j].getState() +
                                        " " + grains[i][j].getId() +
                                        " " + grains[i][j].getColor().getRed() +
                                        " " + grains[i][j].getColor().getGreen() +
                                        " " + grains[i][j].getColor().getBlue() +
                                        " " + grains[i][j].isInclusion() +
                                        " " + grains[i][j].isOnBorder());
                        printWriter.write("\n");
                    }
                }
            }
            printWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void loadFromBMP() {
        BufferedImage img = null;

        try {
            img = ImageIO.read(new File("C:\\Users\\Kamil\\Desktop\\Multiscale\\img.bmp"));
        } catch (IOException e) {
            System.out.println(e);
        }

        createGrid(img.getWidth(), img.getHeight());

        Map<Integer, Color> loadedColors = new HashMap<>();

        int index = 0;
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                int p = img.getRGB(i, j);

                //get alpha
                int a = (p >> 24) & 0xff;

                //get red
                int r = (p >> 16) & 0xff;

                //get green
                int g = (p >> 8) & 0xff;

                //get blue
                int b = p & 0xff;
//                System.out.println(r + "   " + b + "   " + g);

                Color newColor = Color.rgb(r, g, b);

                if (!loadedColors.containsValue(newColor)) {
                    loadedColors.put(index, newColor);
                    index++;
                }


                Grain grain = grains[i][j];

                grain.setState(1);
                grain.setColor(newColor);

                if (r == 0 & b == 0 && g == 0) {
                    grain.setInclusion(true);
                }

                if (r == 255 & b == 255 && g == 255) {
                    grain.setState(0);
                }


                for (Map.Entry<Integer, Color> entry : loadedColors.entrySet()) {
                    if (Objects.equals(newColor, entry.getValue())) {
                        grain.setId(entry.getKey());
                    }
                }

            }
        }
        setBoundaries();
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                System.out.println(grains[i][j].getState());
            }
        }
    }

    public void loadFromTextFile() {
        try (Scanner scanner = new Scanner(new FileInputStream("Structure.txt"));) {
            String[] sizes = scanner.nextLine().split(" ");

            createGrid(Integer.parseInt(sizes[0]), Integer.parseInt(sizes[0]));

            String[] grainDetails;
            while (scanner.hasNextLine()) {
                grainDetails = scanner.nextLine().split((" "));
                int x = Integer.parseInt(grainDetails[0]);
                int y = Integer.parseInt(grainDetails[1]);
                int state = Integer.parseInt(grainDetails[2]);
                int id = Integer.parseInt(grainDetails[3]);
                double red = Double.parseDouble(grainDetails[4]);
                double green = Double.parseDouble(grainDetails[5]);
                double blue = Double.parseDouble(grainDetails[6]);
                String inclusion = grainDetails[7];
                String boundary = grainDetails[7];

                grains[x][y].setState(state);
                grains[x][y].setId(id);
                grains[x][y].setColor(Color.color(red, green, blue));

                if (inclusion == "true") {
                    grains[x][y].setInclusion(true);
                }

                if (boundary == "true") {
                    grains[x][y].setOnBorder(true);
                }


            }
        } catch (IOException e) {
            System.out.println("File not found");
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

    public boolean isArrayFull() {
        return isArrayFull;
    }


//    ---------------------------------------------

    public void selectGrain(int x, int y, String phase) {
        boolean unselect = false;
        int selectedGrainId = grains[x][y].getId();

        if (selectedPhaseIds.indexOf(selectedGrainId) != -1) {
            selectedPhaseIds.remove(selectedPhaseIds.indexOf(selectedGrainId));
            unselect = true;
        }

        if (selectedDualIds.indexOf(selectedGrainId) != -1) {
            selectedDualIds.remove(selectedDualIds.indexOf(selectedGrainId));
            unselect = true;
        }

        if (selectedGrainsIds.indexOf(selectedGrainId) != -1) {
            selectedGrainsIds.remove(selectedGrainsIds.indexOf(selectedGrainId));
            unselect = true;
        }

        if (!unselect) {
            if (phase == "Substructure") {
                selectedPhaseIds.add(selectedGrainId);
                System.out.println(phase);
            } else {
                selectedDualIds.add(selectedGrainId);
                System.out.println(phase);
            }
            selectedGrainsIds.add(selectedGrainId);

            selectGrains(selectedGrainId);
        } else {
            unselectGrains(selectedGrainId);
        }

    }

    private void selectGrains(int id) {
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                if (grains[i][j].getId() == id) {
                    grains[i][j].setGrainSelected(true);
                    grains[i][j].setFrozen(true);
                }
            }
        }
    }

    public void unselectGrains(int id) {
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                if (grains[i][j].getId() == id) {
                    grains[i][j].setGrainSelected(false);
                    grains[i][j].setFrozen(false);
                }
            }
        }
    }

//    ------------------------------------------------

    public void clearPhase() {
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                if (!grains[i][j].isGrainSelected()) {
                    grains[i][j].setX(-1);
                    grains[i][j].setY(-1);
                    grains[i][j].set_ID(-1);
                    grains[i][j].setState(0);
                    grains[i][j].setNextState(0);
                    grains[i][j].setColor(Color.WHITE);
                    grains[i][j].setId(-1);
                    grains[i][j].setOnBorder(false);
                    grains[i][j].setInclusion(false);
                    grains[i][j].setGrainSelected(false);
                    grains[i][j].setFrozen(false);
                }

            }
        }
        isArrayFull = false;
//        selectedGrainsIds.clear();
    }


    public void fillSpaceWithGrains(int numberOfStates) {
        monteCarloCounters.clear();
        monteCarloGrainsList.clear();
        int counter = 0;
//
//        for (int i = 0; i < gridWidth; i++) {
//            for (int j = 0; j < gridHeight; j++) {
//                if (grains[i][j].isFrozen() || grains[i][j].isGrainSelected() || selectedDualIds.contains(grains[i][j].getId())) {
//                    System.out.println(counter++);
//                }
//            }
//        }

//        int dualPhaseNum = 0;
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                int id = random.nextInt(numberOfStates);
//                if (grains[i][j].isDualPhase()) {
//                    dualPhaseNum++;
//                    continue;
//                }

                grains[i][j].setX(i);
                grains[i][j].setY(j);
                grains[i][j].setState(1);
                grains[i][j].setId(id);
                grains[i][j].setColor(colorForEveryId.get(grains[i][j].getId()));
                grains[i][j].set_ID(counter);
                monteCarloGrainsList.add(grains[i][j]);
                monteCarloCounters.add(counter++);
            }
        }
//        System.out.println("Dual Phase Num: " + dualPhaseNum);
//        System.out.println("Num of grains: " + monteCarloGrainsList.size());
//        System.out.println("Sum of grains: " + (monteCarloGrainsList.size() + dualPhaseNum));
    }


    private List switchItem(List list, int from, int to) {
        int temp = (int) list.get(from);
        list.set(from, list.get(to));
        list.set(to, temp);
        return list;
    }

    private void getRandomUniqueValues() {
        for (var i = 0; i < monteCarloCounters.size(); i++) {
            int randomPosition = (int) Math.floor(Math.random() * Math.floor(monteCarloCounters.size() - i - 1));
            monteCarloCounters = switchItem(monteCarloCounters, randomPosition, monteCarloCounters.size() - 1 - i);
        }
    }

    public void MonteCarloGrowth(int numberOfIterations, double constant) {
        List copy = new ArrayList(monteCarloCounters);
        List localNeighboursIds = new ArrayList();

        System.out.println("Copy: " + copy.size());
        System.out.println("Grains: " + monteCarloGrainsList.size());
        System.out.println("Counter: " + monteCarloCounters.size());


        int x = -1;
        int y = -1;


        for (int k = 0; k < numberOfIterations; k++) {
//            System.out.println(k);
            monteCarloCounters = new ArrayList(copy);
            getRandomUniqueValues();

            for (int g = 0; g < monteCarloCounters.size(); g++) {

                localNeighboursIds.clear();


                Grain grain = (Grain) monteCarloGrainsList.get((Integer) monteCarloCounters.get(g));
                if (grain.isDualPhase()) {
                    continue;
                }

                x = grain.getX();
                y = grain.getY();

                setEdge(x, y);


                if (grains[indUp][indLeft].getId() != grains[x][y].getId()) {
                    if (!grains[indUp][indLeft].isDualPhase()) {
                        localNeighboursIds.add(grains[indUp][indLeft].getId());
                    }
                }

                if (grains[indUp][y].getId() != grains[x][y].getId()) {
                    if (!grains[indUp][y].isDualPhase()) {
                        localNeighboursIds.add(grains[indUp][y].getId());
                    }
                }

                if (grains[indUp][indRight].getId() != grains[x][y].getId()) {
                    if (!grains[indUp][indRight].isDualPhase()) {
                        localNeighboursIds.add(grains[indUp][indRight].getId());
                    }
                }

                if (grains[x][indLeft].getId() != grains[x][y].getId()) {
                    if (!grains[x][indLeft].isDualPhase()) {
                        localNeighboursIds.add(grains[x][indLeft].getId());
                    }
                }

                if (grains[x][indRight].getId() != grains[x][y].getId()) {
                    if (!grains[x][indRight].isDualPhase()) {
                        localNeighboursIds.add(grains[x][indRight].getId());
                    }
                }

                if (grains[indDown][indLeft].getId() != grains[x][y].getId()) {
                    if (!grains[indDown][indLeft].isDualPhase()) {
                        localNeighboursIds.add(grains[indDown][indLeft].getId());
                    }
                }

                if (grains[indDown][y].getId() != grains[x][y].getId()) {
                    if (!grains[indDown][y].isDualPhase()) {
                        localNeighboursIds.add(grains[indDown][y].getId());
                    }
                }

                if (grains[indDown][indRight].getId() != grains[x][y].getId()) {
                    if (!grains[indDown][indRight].isDualPhase()) {
                        localNeighboursIds.add(grains[indDown][indRight].getId());
                    }
                }


                double oldEnergy = constant * localNeighboursIds.size();

                if (localNeighboursIds.size() != 0) {

                    int newId = (int) localNeighboursIds.get(random.nextInt(localNeighboursIds.size()));


                    localNeighboursIds.clear();
                    if (grains[indUp][indLeft].getId() != newId) {
                        if (!grains[indUp][indLeft].isDualPhase()) {
                            localNeighboursIds.add(grains[indUp][indLeft].getId());
                        }
                    }

                    if (grains[indUp][y].getId() != newId) {
                        if (!grains[indUp][y].isDualPhase()) {
                            localNeighboursIds.add(grains[indUp][y].getId());
                        }
                    }

                    if (grains[indUp][indRight].getId() != newId) {
                        if (!grains[indUp][indRight].isDualPhase()) {
                            localNeighboursIds.add(grains[indUp][indRight].getId());
                        }
                    }

                    if (grains[x][indLeft].getId() != newId) {
                        if (!grains[x][indLeft].isDualPhase()) {
                            localNeighboursIds.add(grains[x][indLeft].getId());
                        }
                    }

                    if (grains[x][indRight].getId() != newId) {
                        if (!grains[x][indRight].isDualPhase()) {
                            localNeighboursIds.add(grains[x][indRight].getId());
                        }
                    }

                    if (grains[indDown][indLeft].getId() != newId) {
                        if (!grains[indDown][indLeft].isDualPhase()) {
                            localNeighboursIds.add(grains[indDown][indLeft].getId());
                        }
                    }

                    if (grains[indDown][y].getId() != newId) {
                        if (!grains[indDown][y].isDualPhase()) {
                            localNeighboursIds.add(grains[indDown][y].getId());
                        }
                    }

                    if (grains[indDown][indRight].getId() != newId) {
                        if (!grains[indDown][indRight].isDualPhase()) {
                            localNeighboursIds.add(grains[indDown][indRight].getId());
                        }
                    }

                    double newEnergy = constant * localNeighboursIds.size();
                    double delta = newEnergy - oldEnergy;

                    if (delta <= 0) {
                        grains[x][y].setId(newId);
                        grains[x][y].setColor(colorForEveryId.get(grains[x][y].getId()));
                    }

                }

            }
        }
        setBoundaries();
    }

    public void setGrainsToDualPhase(int x, int y) {
        int id = grains[x][y].getId();
        boolean state;
        if (grains[x][y].isDualPhase()) {
            state = false;
        } else {
            state = true;
        }
        grains[x][y].setDualPhase(state);
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                if (grains[i][j].getId() == id) {
                    grains[i][j].setDualPhase(state);
                }
            }
        }
    }

    public void setEnergyEvenly(double energy, double noise) {
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                double randNoise = random.nextInt() - noise;
                grains[i][j].setEnergy(energy + randNoise);
            }
        }
    }

    public void setEnergyWithBorders(double energy, double noise, double energyOnBorder) {
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                Grain grain = grains[i][j];

                if (grain.isOnBorder()) {
                    grain.setEnergy(energyOnBorder);
                } else {
                    double randNoise;
                    if(random.nextBoolean() == false) {
                        randNoise = -noise;
                    } else {
                        randNoise = noise;
                    }
//                    double randNoise = random.ne() - noise;
                    grain.setEnergy(energy + randNoise);
                }
            }
        }
    }

}


