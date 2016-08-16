package com.game.hacking.app.hackinggame2d.grafiks;

import android.opengl.GLES20;

import com.game.hacking.app.hackinggame2d.MainRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by MAGarcia on 8/12/2016.
 */
public class Line {
  private FloatBuffer vertexBuffer;

  private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;                \n"

          + "attribute vec4 vPosition;               \n"
          + "void main() {                           \n"

          + "  gl_Position = uMVPMatrix * vPosition; \n"
          + "}                                         ";

  private final String fragmentShaderCode =
            "precision mediump float;              \n"
          + "uniform vec4 vColor;                  \n"
          + "void main() {                         \n"
          + "  gl_FragColor = vColor;              \n"
          + "}                                       ";

  protected int glProgram;
  protected int positionHandle;
  protected int colorHandle;
  protected int mvpMatrixHandle;

  static final int COORDS_PER_VERTEX = 3;
  static float lineCoords[] = {
          0.0f, 0.0f, 0.0f,
          1.0f, 0.0f, 0.0f,
  };

  private final int vertexCount = lineCoords.length / COORDS_PER_VERTEX;
  private final int vertexStride = COORDS_PER_VERTEX * 4;

  float color[] = { 0.0f, 1.0f, 0.0f, 1.0f };

  public Line() {
    ByteBuffer bb = ByteBuffer.allocateDirect(lineCoords.length * 4);

    bb.order(ByteOrder.nativeOrder());

    vertexBuffer = bb.asFloatBuffer();
    vertexBuffer.put(lineCoords);
    vertexBuffer.position(0);

    int vertexShader = MainRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
    int fragmentShader = MainRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

    glProgram = GLES20.glCreateProgram();
    GLES20.glAttachShader(glProgram, vertexShader);
    GLES20.glAttachShader(glProgram, fragmentShader);
    GLES20.glLinkProgram(glProgram);
  }

  public void setVertices(float v0, float v1, float v2, float v3, float v4, float v5) {
    lineCoords[0] = v0;
    lineCoords[1] = v1;
    lineCoords[2] = v2;
    lineCoords[3] = v3;
    lineCoords[4] = v4;
    lineCoords[5] = v5;

    vertexBuffer.put(lineCoords);
    vertexBuffer.position(0);
  }

  public void setColor(float red, float green, float blue, float alpha) {
    color[0] = red;
    color[1] = green;
    color[2] = blue;
    color[3] = alpha;
  }

  public  void surfaceCreated() {
  }

  public void surfaceChanged() {

  }

  public void draw(float[] mvpMatrix) {
    GLES20.glUseProgram(glProgram);
    positionHandle = GLES20.glGetAttribLocation(glProgram, "vPosition");

    GLES20.glEnableVertexAttribArray(positionHandle);
    GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT,
                                 false, vertexStride, vertexBuffer);

    colorHandle = GLES20.glGetUniformLocation(glProgram, "vColor");

    GLES20.glUniform4fv(colorHandle, 1, color, 0);

    mvpMatrixHandle = GLES20.glGetUniformLocation(glProgram, "uMVPMatrix");

    GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);
    GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexCount);
    GLES20.glDisableVertexAttribArray(positionHandle);
  }
}
