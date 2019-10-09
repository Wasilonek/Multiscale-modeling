package model;

import javafx.scene.paint.Color;

public class Grain {
    private int id;
    private int state;
    private int nextState;
    private Color color;

    public Grain() {
        state = nextState = 0;
        id = -1;
        color = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getNextState() {
        return nextState;
    }

    public void setNextState(int nextState) {
        this.nextState = nextState;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
