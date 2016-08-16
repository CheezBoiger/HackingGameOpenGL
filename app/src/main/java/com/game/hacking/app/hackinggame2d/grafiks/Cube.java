package com.game.hacking.app.hackinggame2d.grafiks;

import android.opengl.GLES20;

import com.game.hacking.app.hackinggame2d.MainRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by MAGarcia on 8/12/2016.
 */
public class Cube {


  /** Cube vertices */
  private float VERTICES[] = {
          -0.5f, -0.5f, -0.5f, //
          0.5f, -0.5f, -0.5f, //
          0.5f, 0.5f, -0.5f,  //
          -0.5f, 0.5f, -0.5f, //
          -0.5f, -0.5f, 0.5f, //
          0.5f, -0.5f, 0.5f,
          0.5f, 0.5f, 0.5f,
          -0.5f, 0.5f, 0.5f
  };

  /** Vertex colors. */
  private float COLORS[] = {
          0.0f, 1.0f, 1.0f, 1.0f,
          1.0f, 0.0f, 0.0f, 1.0f,
          1.0f, 1.0f, 0.0f, 1.0f,
          0.0f, 1.0f, 0.0f, 1.0f,
          0.0f, 0.0f, 1.0f, 1.0f,
          1.0f, 0.0f, 1.0f, 1.0f,
          1.0f, 1.0f, 1.0f, 1.0f,
          0.0f, 1.0f, 1.0f, 1.0f,
  };


  /** Order to draw vertices as triangles. */
  private byte INDICES[] = {
          0, 1, 3, 3, 1, 2, // Front face.
          0, 1, 4, 4, 5, 1, // Bottom face.
          1, 2, 5, 5, 6, 2, // Right face.
          2, 3, 6, 6, 7, 3, // Top face.
          3, 7, 4, 4, 3, 0, // Left face.
          4, 5, 7, 7, 6, 5, // Rear face.
  };

  private static final int COORDS_PER_VERTEX = 3;
  private static final int VALUES_PER_COLOR = 4;

  /** Vertex size in bytes. */
  private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

  /** Color size in bytes. */
  private final int COLOR_STRIDE = VALUES_PER_COLOR * 4;

  /** Shader code for the vertex. */
  private static final String VERTEX_SHADER_CODE =
          "uniform mat4 uMVPMatrix;" +
                  "attribute vec4 vPosition;" +
                  "attribute vec4 vColor;" +
                  "varying vec4 _vColor;" +
                  "void main() {" +
                  "  _vColor = vColor;" +
                  "  gl_Position = uMVPMatrix * vPosition;" +
                  "}";

  /** Shader code for the fragment. */
  private static final String FRAGMENT_SHADER_CODE =
          "precision mediump float;" +
                  "varying vec4 _vColor;" +
                  "void main() {" +
                  "  gl_FragColor = _vColor;" +
                  "}";


  private final FloatBuffer mVertexBuffer;
  private final FloatBuffer mColorBuffer;
  private final ByteBuffer mIndexBuffer;
  private final int mProgram;
  private int mPositionHandle;
  private int mColorHandle;
  private int mMVPMatrixHandle;

  public Cube() {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(VERTICES.length * 4);

    byteBuffer.order(ByteOrder.nativeOrder());
    mVertexBuffer = byteBuffer.asFloatBuffer();
    mVertexBuffer.put(VERTICES);
    mVertexBuffer.position(0);

    byteBuffer = ByteBuffer.allocateDirect(COLORS.length * 4);
    byteBuffer.order(ByteOrder.nativeOrder());
    mColorBuffer = byteBuffer.asFloatBuffer();
    mColorBuffer.put(COLORS);
    mColorBuffer.position(0);

    mIndexBuffer = ByteBuffer.allocateDirect(INDICES.length);
    mIndexBuffer.put(INDICES);
    mIndexBuffer.position(0);

    mProgram = GLES20.glCreateProgram();
    GLES20.glAttachShader(mProgram, MainRenderer.loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_CODE));
    GLES20.glAttachShader(
            mProgram, MainRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE));
    GLES20.glLinkProgram(mProgram);
  }

  public void changeColor(float[] newColor) {
    for (int i = 0; i < COLORS.length; ++i) {
      COLORS[i] = newColor[i];
    }
  }

  public void changeVertices(float[] vertices) {
    for (int i = 0; i < VERTICES.length; ++i) {
      VERTICES[i] = vertices[i];
    }

    mVertexBuffer.put(VERTICES);
    mVertexBuffer.position(0);
  }

  public void moveXUnits(float x) {
    for (int i = 0; i < VERTICES.length; i += 3) {
      VERTICES[i] += x;
    }

    mVertexBuffer.put(VERTICES);
    mVertexBuffer.position(0);
  }

  public void moveYUnites(float y) {
    for (int i = 1; i < VERTICES.length; i += 3) {
      VERTICES[i] += y;
    }

    mVertexBuffer.put(VERTICES);
    mVertexBuffer.position(0);
  }

  /**
   * Encapsulates the OpenGL ES instructions for drawing this shape.
   *
   * @param mvpMatrix The Model View Project matrix in which to draw this shape
   */
  public void draw(float[] mvpMatrix) {
    // Add program to OpenGL environment.
    GLES20.glUseProgram(mProgram);

    mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
    mColorHandle = GLES20.glGetAttribLocation(mProgram, "vColor");
    mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

    // Prepare the cube coordinate data.
    GLES20.glEnableVertexAttribArray(mPositionHandle);
    GLES20.glVertexAttribPointer(
            mPositionHandle, 3, GLES20.GL_FLOAT, false, VERTEX_STRIDE, mVertexBuffer);

    // Prepare the cube color data.
    GLES20.glEnableVertexAttribArray(mColorHandle);
    GLES20.glVertexAttribPointer(
            mColorHandle, 4, GLES20.GL_FLOAT, false, COLOR_STRIDE, mColorBuffer);

    // Apply the projection and view transformation.
    GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

    // Draw the cube.
    GLES20.glDrawElements(
            GLES20.GL_TRIANGLES, INDICES.length, GLES20.GL_UNSIGNED_BYTE, mIndexBuffer);

    // Disable vertex arrays.
    GLES20.glDisableVertexAttribArray(mPositionHandle);
    GLES20.glDisableVertexAttribArray(mColorHandle);
  }

  public float getXCoordinate() {
    return VERTICES[0];
  }

  public float getYCoordinate() {
    return VERTICES[4];
  }
}
