package com.example.saskeskursinis;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class CheckersStarter extends Application {

    private Group tilesGroup = new Group();
    private Group checkersGroup = new Group();

    private boolean playerOneTurn = true;
    private boolean playerTwoTurn = false;

    private boolean playerOneCanMove = true;
    private boolean playerTwoCanMove = false;

    private Checker playingChecker = null;

    private int playerOneCheckers = 12;
    private int playerTwoCheckers = 12;

    private PlaceHolder[][] placeHolder = new PlaceHolder[8][8];


    private Parent createBoard(){
        Pane root = new Pane();
        root.setPrefSize(400, 400);
        root.getChildren().addAll(tilesGroup, checkersGroup);

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Rectangle tile = new Rectangle();
                Checker checker = null;
                tile.setWidth(50);
                tile.setHeight(50);
                tile.relocate(i * 50,  j * 50);
                //Sukuria sviesius langelius
                if((j + i) % 2 == 0){
                    tile.setFill(Color.valueOf("#E1C16E"));
                    placeHolder[j][i] = new PlaceHolder(j, i, false);
                }
                //sukuria tamsius langelius
                else if ((j + i) % 2 != 0){
                    tile.setFill(Color.valueOf("#6E260E"));
                    placeHolder[j][i] = new PlaceHolder(j, i, true);
                }
                //sukuria tamsiasias saskes
                if(i < 3 && (j + i) % 2 != 0){
                    checker = positionChecker(false, j, i);
                }
                //sukuria sviesiasas saskes
                else if(i > 4 && (j + i) % 2 != 0){
                    checker = positionChecker(true, j, i);
                }
                if(checker != null){
                    placeHolder[j][i].setChecker(checker);
                    checkersGroup.getChildren().add(checker);
                }
                tilesGroup.getChildren().add(tile);
            }
        }
        return root;
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(createBoard());
        stage.setTitle("Kursinis");
        stage.setScene(scene);
        stage.show();
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if(playerOneTurn){
                    playerTwoTurn = true;
                    playerOneTurn = false;
                    playerTwoCanMove = true;
                }
                else {
                    playerTwoTurn = false;
                    playerOneTurn = true;
                    playerOneCanMove = true;
                }
                checkIfEnded();
                playingChecker = null;
                System.out.println("Enter key was pressed");
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }

    public Checker positionChecker(boolean isWhite, int x, int y){
        Checker checker = new Checker(isWhite, x, y);

        checker.setOnMouseReleased(event -> {
            int posX = convertPosition(checker.getLayoutX());
            int posY = convertPosition(checker.getLayoutY());

            int checkerPosX = convertPosition(checker.getPosX());
            int checkerPosY = convertPosition(checker.getPosY());

            //System.out.println(posX + " " + posY);
            if(playerOneCheckers != 0 && playerTwoCheckers != 0){
                System.out.println("Light Checkers Left: " +playerOneCheckers + " Dark Checkers Left: " + playerTwoCheckers);
                if (checkerPosX == posX && checkerPosY ==posY) {
                    checker.moveBack();
                } else if (posX >= 0 && posY >= 0 && posX < 8 && posY < 8) {
                    if(placeHolder[posX][posY].isMovableThere() && placeHolder[posX][posY].getChecker() == null){
                        //Pirmas Zaidejas
                        if(checker.isWhite() && playerOneTurn){

                            System.out.println(checkerPosX + " " + posX + " " + checkerPosY + " " + posY);
                            //paprastas ejimas
                            if((((posX - checkerPosX == -1) && (posY - checkerPosY == -1)) || ((posX - checkerPosX == 1) && (posY - checkerPosY == -1))) && playerOneCanMove)
                            {
                                if(playingChecker == null)
                                {
                                    checker.moveChecker(posX, posY);
                                    placeHolder[checkerPosX][checkerPosY].setChecker(null);
                                    placeHolder[posX][posY].setChecker(checker);
                                    System.out.println("You can move here");
                                    System.out.println(checkerPosX + " " + posX + " " + checkerPosY + " " + posY);
                                    playerOneTurn = false;
                                    playerTwoTurn = true;
                                    playerOneCanMove = false;
                                    playerTwoCanMove = true;
                                } else{
                                    System.out.println("You can't move here");
                                    checker.moveBack();
                                }
                            }
                            //kirtimas i prieki kaire
                            else if (((posX - checkerPosX == -2) && (posY - checkerPosY == -2))) {
                                int enemyPosX = posX + 1;
                                int enemyPosY = posY + 1;
                                if(placeHolder[enemyPosX][enemyPosY].hasChecker() && !placeHolder[enemyPosX][enemyPosY].getChecker().isWhite()){
                                    if(playingChecker == null || (convertPosition(playingChecker.getPosX()) == checkerPosX && convertPosition(playingChecker.getPosY())  == checkerPosY))
                                    {
                                        checker.moveChecker(posX, posY);
                                        playingChecker = checker;
                                        placeHolder[checkerPosX][checkerPosY].setChecker(null);
                                        placeHolder[posX][posY].setChecker(checker);
                                        System.out.println("You can move here");
                                        System.out.println(checkerPosX + " " + posX + " " + checkerPosY + " " + posY);
                                        checkersGroup.getChildren().remove(placeHolder[enemyPosX][enemyPosY].getChecker());
                                        placeHolder[enemyPosX][enemyPosY].setChecker(null);
                                        playerTwoCheckers--;
                                        playerOneCanMove = false;
                                    }
                                    else{
                                        System.out.println("You can't move here");
                                        checker.moveBack();
                                    }
                                }
                                else{
                                    System.out.println("You can't move here");
                                    checker.moveBack();
                                }
                            }
                            //kirtimas i prieki desine
                            else if(((posX - checkerPosX == 2) && (posY - checkerPosY == -2))){
                                int enemyPosX = posX - 1;
                                int enemyPosY = posY + 1;
                                if(placeHolder[enemyPosX][enemyPosY].hasChecker() && !placeHolder[enemyPosX][enemyPosY].getChecker().isWhite()){
                                    if(playingChecker == null || (convertPosition(playingChecker.getPosX()) == checkerPosX && convertPosition(playingChecker.getPosY())  == checkerPosY))
                                    {
                                        checker.moveChecker(posX, posY);
                                        playingChecker = checker;
                                        placeHolder[checkerPosX][checkerPosY].setChecker(null);
                                        placeHolder[posX][posY].setChecker(checker);
                                        System.out.println("You can move here");
                                        System.out.println(checkerPosX + " " + posX + " " + checkerPosY + " " + posY);
                                        checkersGroup.getChildren().remove(placeHolder[enemyPosX][enemyPosY].getChecker());
                                        placeHolder[enemyPosX][enemyPosY].setChecker(null);
                                        playerTwoCheckers--;
                                        playerOneCanMove = false;
                                    }
                                    else{
                                        System.out.println("You can't move here");
                                        checker.moveBack();
                                    }
                                }
                                else{
                                    System.out.println("You can't move here");
                                    checker.moveBack();
                                }

                            } else if (((posX - checkerPosX == -2) && (posY - checkerPosY == 2))) {
                                int enemyPosX = posX + 1;
                                int enemyPosY = posY - 1;
                                if(placeHolder[enemyPosX][enemyPosY].hasChecker() && !placeHolder[enemyPosX][enemyPosY].getChecker().isWhite()){
                                    if(playingChecker == null || (convertPosition(playingChecker.getPosX()) == checkerPosX && convertPosition(playingChecker.getPosY())  == checkerPosY))
                                    {
                                        checker.moveChecker(posX, posY);
                                        playingChecker = checker;
                                        placeHolder[checkerPosX][checkerPosY].setChecker(null);
                                        placeHolder[posX][posY].setChecker(checker);
                                        System.out.println("You can move here");
                                        System.out.println(checkerPosX + " " + posX + " " + checkerPosY + " " + posY);
                                        checkersGroup.getChildren().remove(placeHolder[enemyPosX][enemyPosY].getChecker());
                                        placeHolder[enemyPosX][enemyPosY].setChecker(null);
                                        playerTwoCheckers--;
                                        playerOneCanMove = false;
                                    }
                                    else{
                                        System.out.println("You can't move here");
                                        checker.moveBack();
                                    }
                                }
                                else{
                                    System.out.println("You can't move here");
                                    checker.moveBack();
                                }

                            } else if (((posX - checkerPosX == 2) && (posY - checkerPosY == 2))) {
                                int enemyPosX = posX - 1;
                                int enemyPosY = posY - 1;
                                if(placeHolder[enemyPosX][enemyPosY].hasChecker() && !placeHolder[enemyPosX][enemyPosY].getChecker().isWhite()){
                                    if(playingChecker == null || (convertPosition(playingChecker.getPosX()) == checkerPosX && convertPosition(playingChecker.getPosY())  == checkerPosY))
                                    {
                                        checker.moveChecker(posX, posY);
                                        playingChecker = checker;
                                        placeHolder[checkerPosX][checkerPosY].setChecker(null);
                                        placeHolder[posX][posY].setChecker(checker);
                                        System.out.println("You can move here");
                                        System.out.println(checkerPosX + " " + posX + " " + checkerPosY + " " + posY);
                                        checkersGroup.getChildren().remove(placeHolder[enemyPosX][enemyPosY].getChecker());
                                        placeHolder[enemyPosX][enemyPosY].setChecker(null);
                                        playerTwoCheckers--;
                                        playerOneCanMove = false;
                                    }
                                    else{
                                        System.out.println("You can't move here");
                                        checker.moveBack();
                                    }
                                }
                                else{
                                    System.out.println("You can't move here");
                                    checker.moveBack();
                                }
                            } else{
                                System.out.println("You can't move here");
                                checker.moveBack();
                            }
                        }
                        //Antras zaidejas
                        else if (!checker.isWhite() && playerTwoTurn) {
                            System.out.println(checkerPosX + " " + posX + " " + checkerPosY + " " + posY);
                            //paprastas ejimas
                            if((((posX - checkerPosX == -1) && (posY - checkerPosY == 1)) || ((posX - checkerPosX == 1) && (posY - checkerPosY == 1))) && playerTwoCanMove)
                            {
                                if(playingChecker == null)
                                {
                                    checker.moveChecker(posX, posY);
                                    placeHolder[checkerPosX][checkerPosY].setChecker(null);
                                    placeHolder[posX][posY].setChecker(checker);
                                    System.out.println("You can move here");
                                    System.out.println(checkerPosX + " " + posX + " " + checkerPosY + " " + posY);
                                    playerOneTurn = true;
                                    playerTwoTurn = false;
                                    playerOneCanMove = true;
                                    playerTwoTurn = false;
                                } else{
                                    System.out.println("You can't move here");
                                    checker.moveBack();
                                }
                            }
                            //kirtimas i prieki kaire
                            else if (((posX - checkerPosX == -2) && (posY - checkerPosY == -2))) {
                                int enemyPosX = posX + 1;
                                int enemyPosY = posY + 1;
                                if(placeHolder[enemyPosX][enemyPosY].hasChecker() && !placeHolder[enemyPosX][enemyPosY].getChecker().isWhite()){
                                    if(playingChecker == null || (convertPosition(playingChecker.getPosX()) == checkerPosX && convertPosition(playingChecker.getPosY())  == checkerPosY))
                                    {
                                        checker.moveChecker(posX, posY);
                                        playingChecker = checker;
                                        placeHolder[checkerPosX][checkerPosY].setChecker(null);
                                        placeHolder[posX][posY].setChecker(checker);
                                        System.out.println("You can move here");
                                        System.out.println(checkerPosX + " " + posX + " " + checkerPosY + " " + posY);
                                        checkersGroup.getChildren().remove(placeHolder[enemyPosX][enemyPosY].getChecker());
                                        placeHolder[enemyPosX][enemyPosY].setChecker(null);
                                        playerOneCheckers--;
                                        playerTwoCanMove = false;
                                    }
                                    else{
                                        System.out.println("You can't move here");
                                        checker.moveBack();
                                    }
                                }
                                else{
                                    System.out.println("You can't move here");
                                    checker.moveBack();
                                }
                            }
                            //kirtimas i prieki desine
                            else if(((posX - checkerPosX == 2) && (posY - checkerPosY == -2))){
                                int enemyPosX = posX - 1;
                                int enemyPosY = posY + 1;
                                if(placeHolder[enemyPosX][enemyPosY].hasChecker() && placeHolder[enemyPosX][enemyPosY].getChecker().isWhite()){
                                    if(playingChecker == null || (convertPosition(playingChecker.getPosX()) == checkerPosX && convertPosition(playingChecker.getPosY())  == checkerPosY))
                                    {
                                        checker.moveChecker(posX, posY);
                                        playingChecker = checker;
                                        placeHolder[checkerPosX][checkerPosY].setChecker(null);
                                        placeHolder[posX][posY].setChecker(checker);
                                        System.out.println("You can move here");
                                        System.out.println(checkerPosX + " " + posX + " " + checkerPosY + " " + posY);
                                        checkersGroup.getChildren().remove(placeHolder[enemyPosX][enemyPosY].getChecker());
                                        placeHolder[enemyPosX][enemyPosY].setChecker(null);
                                        playerOneCheckers--;
                                        playerOneCanMove = false;
                                    }
                                    else{
                                        System.out.println("You can't move here");
                                        checker.moveBack();
                                    }
                                }
                                else{
                                    System.out.println("You can't move here");
                                    checker.moveBack();
                                }

                            } else if (((posX - checkerPosX == -2) && (posY - checkerPosY == 2))) {
                                int enemyPosX = posX + 1;
                                int enemyPosY = posY - 1;
                                if(placeHolder[enemyPosX][enemyPosY].hasChecker() && placeHolder[enemyPosX][enemyPosY].getChecker().isWhite()){
                                    if(playingChecker == null || (convertPosition(playingChecker.getPosX()) == checkerPosX && convertPosition(playingChecker.getPosY())  == checkerPosY))
                                    {
                                        checker.moveChecker(posX, posY);
                                        playingChecker = checker;
                                        placeHolder[checkerPosX][checkerPosY].setChecker(null);
                                        placeHolder[posX][posY].setChecker(checker);
                                        System.out.println("You can move here");
                                        System.out.println(checkerPosX + " " + posX + " " + checkerPosY + " " + posY);
                                        checkersGroup.getChildren().remove(placeHolder[enemyPosX][enemyPosY].getChecker());
                                        placeHolder[enemyPosX][enemyPosY].setChecker(null);
                                        playerOneCheckers--;
                                        playerOneCanMove = false;
                                    }
                                    else{
                                        System.out.println("You can't move here");
                                        checker.moveBack();
                                    }
                                }
                                else{
                                    System.out.println("You can't move here");
                                    checker.moveBack();
                                }

                            } else if (((posX - checkerPosX == 2) && (posY - checkerPosY == 2))) {
                                int enemyPosX = posX - 1;
                                int enemyPosY = posY - 1;
                                if(placeHolder[enemyPosX][enemyPosY].hasChecker() && placeHolder[enemyPosX][enemyPosY].getChecker().isWhite()){
                                    if(playingChecker == null || (convertPosition(playingChecker.getPosX()) == checkerPosX && convertPosition(playingChecker.getPosY())  == checkerPosY))
                                    {
                                        checker.moveChecker(posX, posY);
                                        playingChecker = checker;
                                        placeHolder[checkerPosX][checkerPosY].setChecker(null);
                                        placeHolder[posX][posY].setChecker(checker);
                                        System.out.println("You can move here");
                                        System.out.println(checkerPosX + " " + posX + " " + checkerPosY + " " + posY);
                                        checkersGroup.getChildren().remove(placeHolder[enemyPosX][enemyPosY].getChecker());
                                        placeHolder[enemyPosX][enemyPosY].setChecker(null);
                                        playerOneCheckers--;

                                        playerOneCanMove = false;
                                    }
                                    else{
                                        System.out.println("You can't move here");
                                        checker.moveBack();
                                    }
                                }
                                else{
                                    System.out.println("You can't move here");
                                    checker.moveBack();
                                }
                            } else{
                                System.out.println("You can't move here");
                                checker.moveBack();
                            }
                        } else{
                            System.out.println("Not your move!");
                            checker.moveBack();
                        }

                    } else if(!placeHolder[posX][posY].isMovableThere() || placeHolder[posX][posY].getChecker() != null){
                        System.out.println("You can't move here");
                        checker.moveBack();
                    }
                }
                else{
                    checker.moveChecker(checkerPosX, checkerPosY);
                }
            }
            else{
                checkIfEnded();
            }
        });
        return checker;
    }

    private void checkIfEnded(){
        if(playerOneCheckers == 0){
            System.out.println("Player Two WON!");
            playerOneTurn = false;
            playerTwoTurn = false;
            playerOneCanMove = false;
            playerTwoCanMove = false;
            playingChecker = null;
        }
        else if(playerTwoCheckers == 0){
            System.out.println("Player One WON!");
            playerOneTurn = false;
            playerTwoTurn = false;
            playerOneCanMove = false;
            playerTwoCanMove = false;
            playingChecker = null;
        }
    }
    private int convertPosition(double position) {
        return (int)(position + 50 / 2) / 50;
    }
}