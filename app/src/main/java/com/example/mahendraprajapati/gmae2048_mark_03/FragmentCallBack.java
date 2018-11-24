package com.example.mahendraprajapati.gmae2048_mark_03;

public interface FragmentCallBack {
    public enum Direction {UP, DOWN, LEFT, RIGHT}
    public void onSwap(Direction direction);
    public void onBackButtonPressed();
}
