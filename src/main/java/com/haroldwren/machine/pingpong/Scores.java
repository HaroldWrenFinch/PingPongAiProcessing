package com.haroldwren.machine.pingpong;

/**
 * Created by jack on 2017.04.04..
 */
public class Scores {
    private Integer leftScore = 0; // score of the left side player
    private Integer rightScore = 0; // score of the right side player

    public void increaseLeftScore() {
        leftScore++;
    }
    public void increaseRighScore() {
        rightScore++;
    }
    public Integer getLeftScore() {
        return leftScore;
    }
    public Integer getRightScore() {
        return rightScore;
    }
}
