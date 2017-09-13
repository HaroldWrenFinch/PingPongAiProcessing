/**
 * Created by jack on 2017.04.04..
 */
package com.haroldwren.machine.pingpong;

import com.haroldwren.machine.PAppletProxy;
import processing.core.PConstants;

import static processing.core.PApplet.constrain;

public class Bat {
    private PAppletProxy pAppletProxy;
    private Float xPoint, yPoint;
    private Float batWidth = 20f, batHeight = 30f;
    private Float yChange = 0f;
    private Integer drawHeight;
    private float red = 76f, green = 255f, blue = 0f;
    private boolean left;
    private boolean showRadius = false;

    /**
     * Create bat
     *
     * @param left
     */
    public Bat(boolean left) {
        if(left) {
            green = 255f;
            blue = 255f;
            red = 127f;
            //batHeight = 70f;
        }
        pAppletProxy = PAppletProxy.getInstance(null);
        setValues(left, pAppletProxy.getWidth(), pAppletProxy.getHeight());
    }

    /**
     * Set the bat values
     *
     * @param left
     * @param drawWidth
     * @param drawHeight
     */
    public void setValues(boolean left, Integer drawWidth, Integer drawHeight) {
        this.drawHeight = drawHeight;
        this.left = left;
        yPoint = (drawHeight / 2f);
        if (left) {
            xPoint = batWidth;
        } else {
            xPoint = drawWidth - batWidth;
        }
    }

    /**
     * Update the bat position
     */
    public void update() {
        yPoint += yChange;
        yPoint = constrain(yPoint, batHeight /2, drawHeight - batHeight /2);
    }

    /**
     * Move the bat
     *
     * @param steps
     */
    public void move(Float steps) {
        yChange = steps;
    }

    /**
     * Show the bat
     */
    public void show() {
        pAppletProxy.fill(red, green, blue, 255f);
        pAppletProxy.rectMode(PConstants.CENTER);
        pAppletProxy.rect(xPoint, yPoint, batWidth, batHeight);
        if(!this.left && this.showRadius) {
            pAppletProxy.fill(255f, 255f, 255f, 50f);
            pAppletProxy.ellipse(xPoint, yPoint, (float) pAppletProxy.getHeight(), (float) pAppletProxy.getHeight());
        }
    }

    public Float getxPoint() {
        return xPoint;
    }
    public Float getyPoint() {
        return yPoint;
    }
    public Float getBatWidth() {
        return batWidth;
    }
    public Float getBatHeight() {
        return batHeight;
    }
    public void setBatHeight(Float batHeight) {
        this.batHeight = batHeight;
    }

    public void toggleShowRadius() {
        this.showRadius = !this.showRadius;
    }

    public float getRed() {
        return red;
    }
    public float getGreen() {
        return green;
    }
    public float getBlue() {
        return blue;
    }
}
