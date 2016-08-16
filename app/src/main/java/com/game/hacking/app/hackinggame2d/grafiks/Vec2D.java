package com.game.hacking.app.hackinggame2d.grafiks;

/**
 * Created by MAGarcia on 8/13/2016.
 */
public class Vec2D {
  private float x;
  private float y;

  public Vec2D(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public Vec2D(float x) {
    this(x, 0);
  }

  public Vec2D() {
    this(0,0);
  }

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }
}
