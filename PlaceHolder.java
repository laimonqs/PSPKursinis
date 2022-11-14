package com.example.saskeskursinis;

import javafx.scene.shape.Rectangle;

public class PlaceHolder extends Rectangle {
    private Checker checker;
    private boolean isMovable;

    public Checker getChecker() {
        return checker;
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
    }

    public boolean isMovableThere(){
        return isMovable;
    }

    public boolean hasChecker() {
        if(checker == null)
        {
            return false;
        }
        else{
            return true;
        }
    }

    public PlaceHolder(int x, int y, boolean isMovable) {
        this.setWidth(50);
        this.setHeight(50);
        this.isMovable = isMovable;
        relocate(x * 50, y * 50);
    }
}
