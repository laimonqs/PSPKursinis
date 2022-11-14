package com.example.saskeskursinis;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Checker extends StackPane {
    private Ellipse checker;

    private boolean isWhite;
    private int posX;
    private int posY;
    private Double mousePosX;
    private Double mousePosY;

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public Checker(Boolean isWhite, int x, int y){
        this.checker = new Ellipse(50 * 0.3125, 15);
        this.isWhite = isWhite;
        if(isWhite){
            this.checker.setFill(Color.valueOf("#FFFDD0"));
        }
        else{
            this.checker.setFill(Color.valueOf("#36454F"));
        }
        checker.setStroke(Color.BLACK);
        this.posX = x * 50 + 8;
        this.posY = y * 50 + 8;
        relocate(posX, posY);

        getChildren().addAll(checker);


        setOnMousePressed(e -> {
            mousePosX = e.getSceneX();
            mousePosY = e.getSceneY();
        });

        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - mousePosX + posX, e.getSceneY() - mousePosY + posY);
        });
    }

    public void moveChecker(int x, int y){
        this.posX = x * 50 + 8;
        this.posY = y * 50 + 8;
        relocate(this.posX, this.posY);
    }

    public void moveBack() {
        relocate(this.posX, this.posY);
    }

}
