package com.example.demo1;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Slider;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import java.util.ArrayList;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.control.ComboBox;
import java.util.ArrayList;
import javafx.scene.paint.Paint;
public class HelloController {
    @FXML
    private GridPane gameGridPane;
    @FXML
    private Slider SizeGameBoardSlider;
    @FXML
    private Label SizeGameBoardLabel;
    @FXML
    private Button CreateGameBoardButton;
    @FXML
    private Button StartGameButton;
    @FXML
    private Button StartAutomaticGameButton;
    @FXML
    private Button StopAutomaticGameButton;
    @FXML
    private Button NextStepButton;
    @FXML
    private Button ReturnButton;
    @FXML
    private Button ToNewGameButton;
    @FXML
    private Pane NewGamePane;
    @FXML
    private ComboBox NewGameComboBox;
    @FXML
    private Rectangle Rectangle0;
    @FXML
    private Rectangle Rectangle1;
    @FXML
    private Rectangle Rectangle2;
    @FXML
    private Rectangle Rectangle3;
    @FXML
    private Rectangle Rectangle4;
    @FXML
    private Rectangle Rectangle5;
    @FXML
    private Rectangle Rectangle6;
    @FXML
    private Rectangle Rectangle7;

    Rectangle [] logic_base = new Rectangle[8];



    Rectangle currentCell = new Rectangle();
    ArrayList<Cell> cells = new ArrayList<>();
    int currentRow=-1;
    int currentColumn=-1;


    private GameOfLife game;

    boolean isEditable=true;
    boolean stopGame=false;
    boolean isNewGameMod=false;


    public void initialize() {
        ReturnButton.setVisible(false);
        game = new GameOfLife(10,10);
        this.SizeGameBoardSlider.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            int size=(int) SizeGameBoardSlider.getValue();
            SizeGameBoardLabel.setText("Розмір: "+size+"х"+size);
        });
        game.setCellSize(710.0/10);
        updateGridPane();
        NewGameComboBox.getItems().addAll("AND", "OR", "XOR");
        logic_base[0]=Rectangle0;
        logic_base[1]=Rectangle1;
        logic_base[2]=Rectangle2;
        logic_base[3]=Rectangle3;
        logic_base[4]=Rectangle4;
        logic_base[5]=Rectangle5;
        logic_base[6]=Rectangle6;
        logic_base[7]=Rectangle7;
        for (int i=0; i<8; i++){
            logic_base[i].setOnMouseClicked(event -> handleRectangleClick(event));
        }
    }

    private void handleRectangleClick(MouseEvent event) {
        Rectangle rect = (Rectangle) event.getSource();
        if(rect.getFill().equals(Paint.valueOf("#a9a9a9"))){
            rect.setFill(Color.BLACK);
        } else  rect.setFill(Paint.valueOf("#a9a9a9"));


    }

    public void onToNewGameButtonClick(){
        game = new NewGame(10,10);
        SizeGameBoardSlider.setVisible(true);
        CreateGameBoardButton.setVisible(true);
        StartGameButton.setVisible(true);
        NextStepButton.setVisible(false);
        StartAutomaticGameButton.setVisible(false);
        ReturnButton.setVisible(false);
        gameGridPane.getChildren().clear();
        SizeGameBoardSlider.setValue(10);
        ToNewGameButton.setVisible(false);
        stopGame = false;
        this.SizeGameBoardSlider.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            int size=(int) SizeGameBoardSlider.getValue();
            SizeGameBoardLabel.setText("Розмір: "+size+"х"+size);
        });
        game.setCellSize(710.0/10);
        isEditable=true;
        isNewGameMod = true;
        updateGridPane();

    }
    public void onCreateGameBoardButtonClick()
    {
        int size=(int) SizeGameBoardSlider.getValue();
        game.setWIDTH(size);
        game.setHEIGHT(size);
        game.setCellSize(710.0/size);
        int[][] gameBoard = new int[size][size];

        game.setGameBoard(gameBoard);
        game.setCells(new ArrayList<>());
        gameGridPane.getChildren().clear();
        updateGridPane();

    }

    public void updateGridPane()
    {
        gameGridPane.getChildren().clear();
        if(!isNewGameMod) {
            for (int row = 0; row < game.getHEIGHT(); row++) {
                for (int col = 0; col < game.getWIDTH(); col++) {
                    Rectangle cell = new Rectangle(game.getCELL_SIZE(), game.getCELL_SIZE());
                    cell.setStroke(Color.BLACK);
                    if (game.getGameBoard()[row][col] == 1) {
                        cell.setFill(Color.BLACK);
                    } else {
                        cell.setFill(Color.LIGHTGRAY);
                    }
                    gameGridPane.add(cell, col, row);
                    cell.setOnMouseClicked(event -> handleCellClick(event));
                }
            }
        } else{
            for (int row = 0; row < game.getHEIGHT(); row++) {
                for (int col = 0; col < game.getWIDTH(); col++) {
                    Rectangle cell = new Rectangle(game.getCELL_SIZE(), game.getCELL_SIZE());
                    Text text = new Text();
                    double fontSize = game.getCELL_SIZE() * 0.6;
                    text.setFont(new Font(fontSize));
                    cell.setStroke(Color.BLACK);
                    cell.setFill(Color.LIGHTGRAY);
                    if (game.getGameBoard()[row][col] == 1) {
                        text.setText("1");
                        cell.setFill(Color.DARKGRAY);
                    } else {
                        text.setText("0");
                        cell.setFill(Color.LIGHTGRAY);
                    }
                    ArrayList<Cell>CELLS=game.getCells();
                    int[] dx = {0, 1, 1, 1, 0, -1, -1, -1};
                    int[] dy = {1, 1, 0, -1, -1, -1, 0, 1};
                    for (Cell CELL:CELLS)
                    {
                        int[][] important_neighbours = CELL.current_neighbours(game.gameBoard.length,game.gameBoard.length);
                        for (int[]neigh:important_neighbours) {

                            if(neigh[0]==row && col==neigh[1] && (cell.getFill()==Color.LIGHTGRAY || cell.getFill()==Color.DARKGRAY)){
                                if(CELL.logic_operation==0)cell.setFill(Paint.valueOf("#ff7d7d"));
                                else  if(CELL.logic_operation==1){
                                   cell.setFill(Paint.valueOf("#6adc66"));
                                }
                                else cell.setFill(Paint.valueOf("#86a4ff"));
                            }

                        }
                        if(CELL.row==row&&CELL.column==col){
                            if(cell.getFill()==Color.RED||cell.getFill()==Color.GREEN||cell.getFill()==Color.BLUE) cell.setFill(Color.PURPLE);
                            else{
                                if(CELL.logic_operation==0) cell.setFill(Color.RED);
                                if(CELL.logic_operation==1) cell.setFill(Color.GREEN);
                                if(CELL.logic_operation==2) cell.setFill(Color.BLUE);
                                text.setText(String.valueOf(CELL.status));

                            }


                        }
                    }
                    gameGridPane.add(cell, col, row);
                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().addAll(cell, text);
                    gameGridPane.add(stackPane, col, row);
                    cell.setOnMouseClicked(event -> handleCellClick(event));
                }
            }
        }


    }

    private void handleCellClick(MouseEvent event) {
        Rectangle rect = (Rectangle) event.getSource();
        int column = GridPane.getColumnIndex(rect);
        int row = GridPane.getRowIndex(rect);
        if(isEditable) {
            int[][] Board = game.getGameBoard();
            if (!isNewGameMod){
                if (rect.getFill() == Color.BLACK) {
                    rect.setFill(Color.LIGHTGRAY);
                    Board[row][column] = 0;
                } else if (rect.getFill() == Color.LIGHTGRAY) {
                    rect.setFill(Color.BLACK);
                    Board[row][column] = 1;
                }
            } else{
                if (game.findCell(row,column)!=null)
                {
                    game.removeCell(row,column);
                    updateGridPane();
                    NewGamePane.setVisible(false);

                }
                else{
                    rect.setFill(Color.DARKGREEN);
                    NewGamePane.setVisible(true);
                    if(currentCell.getFill()==Color.DARKGREEN){
                        currentCell.setFill(Color.DARKGRAY);
                    }

                    currentRow=row;
                    currentColumn=column;

                    NewGameComboBox.setValue("AND");
                    currentCell = rect;
                }

            }
        }


    }

    public void onNewGameCellCreateButtonClick(){
        int logic_operation;
        String str = (String) NewGameComboBox.getValue();

        if ("AND".equals(str)) {
            logic_operation = 0;
        } else if ("OR".equals(str)) {
            logic_operation = 1;
        } else {
            logic_operation = 2;
        }

        int status = game.getGameBoard()[currentRow][currentColumn];
        boolean [] neighbours = new boolean[8];

        for (int i=0; i<logic_base.length; i++){
            if(logic_base[i].getFill()==Color.BLACK)
            {
                neighbours[i]=true;
            }
            else neighbours[i]=false;
            logic_base[i].setFill(Paint.valueOf("#a9a9a9"));


        }

        Cell cell = new Cell(currentRow,currentColumn,logic_operation,status, neighbours, 0);
        game.addCell(cell);
        NewGamePane.setVisible(false);
        updateGridPane();
    }

    public void onStartGameButtonClick(){
        SizeGameBoardSlider.setVisible(false);
        StartGameButton.setVisible(false);
        CreateGameBoardButton.setVisible(false);
        StartAutomaticGameButton.setVisible(true);
        NextStepButton.setVisible(true);
        ReturnButton.setVisible(true);
        isEditable=false;
        game.printMatrix();
    }

    public void onStartAutomaticGameButtonClick(){
        StopAutomaticGameButton.setVisible(true);
        StartAutomaticGameButton.setVisible(false);
        NextStepButton.setVisible(false);
        stopGame = false;
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                while (!stopGame){
                    Platform.runLater(() -> onNextStepButtonClick());

                    Thread.sleep(300);

                }

                return null;
            }
        };

        new Thread(task).start();
    }

    public void onStopAutomaticGameButtonClick(){
        StopAutomaticGameButton.setVisible(false);
        StartAutomaticGameButton.setVisible(true);
        NextStepButton.setVisible(true);
        stopGame=true;
    }

    public void onNextStepButtonClick(){
        game.nextStep();
        updateGridPane();

    }

    public void onReturnButtonClick(){
        SizeGameBoardSlider.setVisible(true);
        CreateGameBoardButton.setVisible(true);
        StartGameButton.setVisible(true);
        NextStepButton.setVisible(false);
        StartAutomaticGameButton.setVisible(false);
        ReturnButton.setVisible(false);
        gameGridPane.getChildren().clear();
        game = new GameOfLife(10,10);
        SizeGameBoardSlider.setValue(10);
        stopGame = false;
        this.SizeGameBoardSlider.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            int size=(int) SizeGameBoardSlider.getValue();
            SizeGameBoardLabel.setText("Розмір: "+size+"х"+size);
        });
        game.setCellSize(710.0/10);
        isEditable=true;
        updateGridPane();
    }
}

