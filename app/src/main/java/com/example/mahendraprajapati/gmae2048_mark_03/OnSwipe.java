package com.example.mahendraprajapati.gmae2048_mark_03;

public interface OnSwipe {

    public enum Direction {UP, DOWN, LEFT, RIGHT}
    public void onSwipe(Direction direction);

}
