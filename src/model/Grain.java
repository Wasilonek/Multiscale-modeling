package model;

import javafx.scene.paint.Color;

public class Grain {
    private int _ID;
    private int x;
    private int y;
    private int id;
    private int state;
    private int nextState;
    private Color color;
    private boolean onBorder;
    private boolean isInclusion;
    private boolean isGrainSelected;
    private boolean frozen;
    private boolean dualPhase;
    private double energy;

    public Grain() {
        x = -1;
        y = -1;
        _ID = -1;
        state = nextState = 0;
        id = -1;
        color = null;
        onBorder = false;
        isInclusion = false;
        isGrainSelected = false;
        frozen = false;
        dualPhase = false;
        energy = 0;
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

    public boolean isOnBorder() {
        return onBorder;
    }

    public void setOnBorder(boolean onBorder) {
        this.onBorder = onBorder;
    }

    public boolean isInclusion() {
        return isInclusion;
    }

    public void setInclusion(boolean inclusion) {
        isInclusion = inclusion;
    }

    public boolean isGrainSelected() {
        return isGrainSelected;
    }

    public void setGrainSelected(boolean grainSelected) {
        isGrainSelected = grainSelected;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isDualPhase() {
        return dualPhase;
    }

    public void setDualPhase(boolean dualPhase) {
        this.dualPhase = dualPhase;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }
}
