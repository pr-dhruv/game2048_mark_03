package com.example.mahendraprajapati.gmae2048_mark_03;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameGrid {

    private int[][] grid;
    private int score;

    //Initialise the Grid
    public GameGrid() {

        this.grid = new int[4][4];
        this.score = 0;

        for (int i = 1; i <= 2; i++)
            addRandomTile();

    }

    //Getting the Score
    public int getScore() {
        return this.score;
    }

    //setting the Score
    public void setScore(int score) {
        this.score = score;
    }

    public void loadGameState(String gameState) {
        String[] values = gameState.split(",");

        for (int i = 0; i < values.length; i++)
            grid[i / 4][i % 4] = Integer.valueOf(values[i]);
    }

    private List<Point> getEmptyTiles() {
        List<Point> emptyTiles = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++)
                if (grid[i][j] == 0)
                    emptyTiles.add(new Point(i, j));
        }

        return emptyTiles;
    }

    public String getGameState() {
        StringBuilder gameStates = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++)
                gameStates.append(grid[i][j]);
        }

        //remove last comma
        gameStates.setLength(gameStates.length() - 1);
        return gameStates.toString();
    }

    public boolean gameLostCondition() {
        //if empty tiles are there which implies game is not over
        if (getEmptyTiles().size() != 0)
            return false;

        // checking whether there are two elements in grid
        // next to each other horizontally or vertically
        // if so, return false, cause we can still play
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                //checking for right side
                if (j != 3)
                    if (grid[i][j] == grid[i][j + 1])
                        return false;
                //checking bottom
                if (i != 3)
                    if (grid[i][j] == grid[i + 1][j])
                        return false;

            }
        }

        //Game over
        return true;
    }

    public int addRandomTile() {
        List<Point> emptyTiles = getEmptyTiles();

        int result = 0;

        if (emptyTiles.size() != 0) {
            int randomPosition = new Random().nextInt(emptyTiles.size());
            Point randomPoint = emptyTiles.get(randomPosition);
            grid[randomPoint.x][randomPoint.y] = 2;
            result = randomPoint.x * 4 + randomPoint.y;
        } else
            result = -1;

        return result;
    }

    public int getTileAt(int i) {
        return grid[i / 4][i % 4];
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(/*"====\n"*/);
        stringBuilder.append(String.format("Score : %d\n", score ));

        for (int i =0; i < 4; i++) {
            for (int j =0 ;j < 4; j++)
                stringBuilder.append(grid[i][j]);
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();

    }

    //Movements

    //Left Side
    public void moveLeftSide() {
        moveToLeft();
        sumTilesOfLeft();
        moveToLeft();
        printArray();
    }

    private void moveToLeft() {
        for (int h = 0; h < 4; h++) {
            for (int i = 1; i < 4; i++) {
                int k = i;
                int z = i - 1;
                while (z > -1) {
                    if (grid[h][z] == 0) {
                        grid[h][z] = grid[h][k];
                        grid[h][k] = 0;

                        k--;
                    }
                    z--;
                }
            }
        }
    }

    private void sumTilesOfLeft() {
        for (int h = 0; h < 4; h++) {
            for (int i = 1; i < 4; i++) {
                if (grid[h][i-1] == grid[h][i] ) {
                    grid[h][i-1] = grid[h][i-1] + grid[h][i];
                    grid[h][i] = 0;

                    score += grid[h][i-1];

                    i++;
                }
            }
        }
    }

    //Right Side
    public void moveRightSide() {
        moveToRight();
        sumTilesOfRight();
        moveToRight();
        printArray();
    }

    private void moveToRight() {
        for (int h = 0; h < 4; h++) {
            for (int i = 2; i > -1; i--) {
                int k = i;
                int z = i + 1;
                while (z < 4) {
                    if (grid[h][z] == 0) {
                        grid[h][z] = grid[h][k];
                        grid[h][k] = 0;

                        k++;
                    }
                    z++;
                }
            }
        }
    }

    private void sumTilesOfRight() {
        for (int h = 0; h < 4; h++) {
            for (int i = 3; i > 0; i--) {
                if (grid[h][i-1] == grid[h][i] ) {
                    grid[h][i-1] = grid[h][i-1] + grid[h][i];
                    grid[h][i] = 0;

                    score += grid[h][i-1];

                    i--;
                }
            }
        }
    }

    //Down Side
    public void moveDownSide() {
        moveToDown();
        sumTilesOfDown();
        moveToDown();
        printArray();
    }

    private void moveToDown() {
        for (int h = 0; h < 4; h++) {
            for (int i = 2; i > -1; i--) {
                int k = i;
                int z = i + 1;
                while (z < 4) {
                    if (grid[z][h] == 0) {
                        grid[z][h] = grid[k][h];
                        grid[k][h] = 0;

                        k++;
                    }
                    z++;
                }
            }
        }
    }

    private void sumTilesOfDown() {
        for (int h = 0; h < 4; h++) {
            for (int i = 3; i > 0; i--) {
                if (grid[i-1][h] == grid[i][h] ) {
                    grid[i-1][h] = grid[i-1][h] + grid[i][h];
                    grid[i][h] = 0;

                    score += grid[i-1][h];

                    i--;
                }
            }
        }
    }

    //Top Side
    public void moveTopSide() {
        moveToUp();
        sumTilesOfUp();
        moveToUp();
        printArray();
    }

    private void moveToUp() {
        for (int h = 0; h < 4; h++) {
            for (int i = 1; i < 4; i++) {
                int k = i;
                int z = i - 1;
                while (z > -1) {
                    if (grid[z][h] == 0) {
                        grid[z][h] = grid[k][h];
                        grid[k][h] = 0;

                        k--;
                    }
                    z--;
                }
            }
        }
    }

    private void sumTilesOfUp() {
        for (int h = 0; h < 4; h++) {
            for (int i = 1; i < 4; i++) {
                if (grid[i-1][h] == grid[i][h] ) {
                    grid[i-1][h] = grid[i-1][h] + grid[i][h];
                    grid[i][h] = 0;

                    score += grid[i-1][h];

                    i++;
                }
            }
        }
    }

    public void printArray() {
        String data = "\n";
        for (int[] row : grid) {
            data += Arrays.toString(row) + "\n";
        }
        Log.d("grid", data);
    }
}

