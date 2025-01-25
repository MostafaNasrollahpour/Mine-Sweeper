package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MineSweeper {

    private class MineTile extends JButton{
        int r;
        int c;
        public MineTile(int r, int c){
            this.r = r;
            this.c = c;
        }
    }
    int tileSize = 70;
    int numRows = 8;
    int numCols = numRows;
    int boardWidth = numCols * tileSize;
    int boardHeight = numRows * tileSize;

    JFrame frame = new JFrame("Minesweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    int mineCount = 10;
    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;
    Random random = new Random();

    int tilesClicked = 0; //goal is to click all tiles except the ones containing mines
    boolean gameOver = false;

    MineSweeper(){

        frame.setSize(boardWidth, boardHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numCols));
        frame.add(boardPanel);

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
                tile.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {
                        if(gameOver)
                            return;
                        MineTile tile = (MineTile) mouseEvent.getSource();

                        // Left click
                        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                            if (Objects.equals(tile.getText(), "")) {
                                if (mineList.contains(tile)) {
                                    reviewMines();
                                } else {
                                    checkMine(tile.r, tile.c);
                                }
                            }
                        }
                        // Right click
                        else if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                            if (Objects.equals(tile.getText(), "") && tile.isEnabled()) {
                                tile.setText("q"); // Use flag emoji
                            } else if (Objects.equals(tile.getText(), "q")) {
                                tile.setText("");
                            }
                        }
                    }
                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseExited(MouseEvent mouseEvent) {

                    }
                });

                boardPanel.add(tile);
            }
        }

        frame.setVisible(true);

        setMines();
    }

    public void setMines(){
        mineList = new ArrayList<>();

        int mineLeft = mineCount;

        while(mineLeft > 0){
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);

            MineTile tile = board[r][c];

            if(!mineList.contains(tile)){
                mineList.add(tile);
                mineLeft--;
            }

        }
    }

    public void reviewMines(){
        for (MineTile tile : mineList) {
            tile.setText("\uD83D\uDCA3");
        }

        gameOver = true;
        textLabel.setText("Game Over!");
    }

    public void checkMine(int r, int c){
        if (r < 0 || r >= numRows || c < 0 || c >= numCols){
            return;
        }

        MineTile tile = board[r][c];
        if (!tile.isEnabled()){
            return;
        }

        tile.setEnabled(false);
        tilesClicked++;

        int minesFound = 0;

        //top 3
        minesFound += countMine(r - 1, c - 1);
        minesFound += countMine(r - 1, c);
        minesFound += countMine(r - 1, c + 1);

        //left and right
        minesFound += countMine(r, c - 1);
        minesFound += countMine(r, c + 1);

        //bottom 3
        minesFound += countMine(r + 1, c - 1);
        minesFound += countMine(r + 1, c);
        minesFound += countMine(r + 1, c + 1);

        if (minesFound > 0){
            tile.setText(Integer.toString(minesFound));
        }else{
            tile.setText("");

            // top 3
            checkMine(r - 1, c - 1);
            checkMine(r - 1, c);
            checkMine(r - 1, c + 1);

            // left and right
            checkMine(r, c - 1);
            checkMine(r, c + 1);

            //bottom 3
            checkMine(r + 1, c - 1);
            checkMine(r + 1, c);
            checkMine(r + 1, c + 1);
        }
        if (tilesClicked == numRows * numCols - mineList.size()){
            gameOver = true;
            textLabel.setText("Mines Cleared");
        }
    }

    public int countMine(int r, int c){
        if (r < 0 || r >= numRows || c < 0 || c >= numCols){
            return 0;
        }
        if (mineList.contains(board[r][c])){
            return 1;
        }
        return 0;
    }
}
