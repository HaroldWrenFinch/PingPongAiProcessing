/**
 * Created by jack on 2017.04.04..
 */
package com.haroldwren.machine.pingpong;


import com.haroldwren.machine.PAppletProxy;
import org.encog.ml.MLRegression;
import processing.core.PApplet;

import java.util.function.Consumer;

public class PongGUIRunner extends PApplet {
    private final static PlayerType LEFT_PLAYER = PlayerType.HUMAN;
    private final static PlayerType RIGHT_PLAYER = PlayerType.AI;
    private Bat left;
    private Bat right;
    private Wall wall;
    private Ball ball;
    private Scores score;
    private NeuralMoveLogic moveLogic;
    private PApplet pApplet;
    private Consumer<MLRegression> funcRight, funcLeft;

    public static void main(String[] args) {
        PApplet.main(PongGUIRunner.class, args);
    }

    @Override
    public void draw() {
        run(null);
        //noLoop();
    }

    @Override
    public void settings() {
        this.pApplet = this;
        pApplet.size(640, 480);
    }

    @Override
    public void setup() {
        PAppletProxy pAppletProxy = PAppletProxy.getInstance(pApplet); // initialize the draw instance.
        pAppletProxy.setGivenHeight(480);
        pAppletProxy.setGivenWidth(640);
        //pApplet.frameRate(10000);
        if(pApplet!=null) {
            pApplet.frameRate(100);
        }
        score = new Scores();
        ball = new Ball(score);
        right = new Bat(false);
        left = new Bat(true);
        wall = new Wall(true);

        moveLogic = new NeuralMoveLogic();
        funcRight = (network) -> (moveLogic).doNeuralLogic(right, ball, network);
        funcLeft = (network) -> (moveLogic).doNeuralLogic(left, ball, network);
    }

    public void run(MLRegression network) {
        if(pApplet!=null) {
            pApplet.background(0);
        }
        if(RIGHT_PLAYER == PlayerType.AI) {
            funcRight.accept(network);
        }
        if(LEFT_PLAYER == PlayerType.AI) {
            funcLeft.accept(network);
        }

        Bat leftBat = LEFT_PLAYER==PlayerType.WALL ? wall : left;
        ball.checkBatRight(right);
        ball.checkBatLeft(leftBat);

        leftBat.show();
        right.show();

        leftBat.update();
        right.update();

        ball.update();
        ball.edges();

        if(pApplet!=null) {
            pApplet.textSize(16);
            pApplet.fill(left.getRed(), left.getGreen(), left.getBlue(), 255f);
            pApplet.text("Human: " + score.getLeftScore(), 32, 40);
            pApplet.fill(right.getRed(), right.getGreen(), right.getBlue(), 255f);
            pApplet.text("AI: " + score.getRightScore(), pApplet.width-64, 40);
        }
        ball.show();
    }

    @Override
    public void keyReleased() {
        left.move(0f);
        right.move(0f);
    }

    @Override
    public void keyPressed() {
        if(pApplet!=null) {
            String pushedStr = "" + pApplet.key;

            if(pushedStr.equals("w") && LEFT_PLAYER==PlayerType.HUMAN) {
                left.move(-10f);
            } else if(pushedStr.equals("s") && LEFT_PLAYER==PlayerType.HUMAN) {
                left.move(10f);
            } else if(pushedStr.equals("i") && RIGHT_PLAYER==PlayerType.HUMAN) {
                right.move(-10f);
            } else if(pushedStr.equals("k") && RIGHT_PLAYER==PlayerType.HUMAN) {
                right.move(10f);
            } else if(pushedStr.equals("p")) {
                right.toggleShowRadius();
            }
        }
    }
}
