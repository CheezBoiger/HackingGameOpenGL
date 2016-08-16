package com.game.hacking.app.hackinggame2d;

import android.graphics.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.game.hacking.app.hackinggame2d.grafiks.Cube;
import com.game.hacking.app.hackinggame2d.grafiks.Line;
import com.game.hacking.app.hackinggame2d.grafiks.Triangle;
import com.game.hacking.app.hackinggame2d.grafiks.Vec2D;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by MAGarcia on 8/11/2016.
 */
public class MainRenderer implements GLSurfaceView.Renderer {
  private Random rand = new Random();
  private static final float PI = 3.14159265359f;

  private float angle = 0;

  private float cameraX = 0f;
  private float cameraY = 0f;
  private float cameraZ = 16.0f;

  private float targetX = 0.0f;
  private float targetY = 0.0f;
  private float targetZ = 0.0f;

  private float upX = 0f;
  private float upY = 1.0f;
  private float upZ = 0f;

  private Line[] lines;
  private List<Vec2D> coordsForCubes;
  private Cube[] cubes;
  int programHandle;

  private final int mBytesPerFloat = 4;
  private float[] mViewMatrix = new float[16];
  private float[] mRotationMatrix = new float[16];

  private float mAngle;


  public MainRenderer() {

  }


  public void addNumberofCubes(int num) {
    if (num >= 0) {
      cubes = new Cube[num];
    }
  }


  public void addNumberofLines(int num) {
    if (num >= 0) {
      lines = new Line[num];
    }
  }


  public void manageCubeCoordinates(int index, float x, float y) {
    if (index < cubes.length) {
      cubes[index].moveXUnits(x);
      cubes[index].moveYUnites(y);
    }
  }


  public void manageLineCoordinates(int index, float x1, float y1, float x2, float y2) {
    if (index < lines.length) {
      lines[index].setVertices(x1, y1, 0, x2, y2, 0);
    }
  }

  public float getXFromCube(int index) {
    if (index < cubes.length) {
      return cubes[index].getXCoordinate();
    }

    return 0;
  }


  public float getYFromCube(int index) {
    if (index < cubes.length) {
      return cubes[index].getYCoordinate();
    }

    return 0;
  }


  @Override
  public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

    Matrix.setLookAtM(mViewMatrix, 0, cameraX, cameraY, cameraZ, targetX, targetY, targetZ, upX, upY, upZ);
    /////////////////////////////////////////////////////////////////////////////////


    for (int i = 0; i < cubes.length; ++i) {
      cubes[i] = new Cube();

      float[] colors = new float[32];

      for (int j = 0; j < colors.length; ++j) {
        if ((i+1) % 4 != 0) {
          colors[i] = 1.0f;//rand.nextFloat() + 0.5f;
        } else {
          colors[i] = 1.0f;
        }
      }

      cubes[i].changeColor(colors);

    }

    for (int i = 0; i < lines.length; ++i) {
      lines[i] = new Line();

      lines[i].setColor(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1.0f);
    }
  }


  private float[] mProjectionMatrix = new float[16];


  @Override
  public void onSurfaceChanged(GL10 gl10, int width, int height) {
    // Adjust the viewport based on geometry changes,
    // such as screen rotation
    GLES20.glViewport(0, 0, width, height);

    float ratio = (float) width / height;

    // this projection matrix is applied to object coordinates
    // in the onDrawFrame() method
    Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 30);
  }


  private float[] mModelMatrix = new float[16];
  private float[] rotationMatrix = new float[16];


  @Override
  public void onDrawFrame(GL10 gl10) {
    float[] scratch = new float[16];

    // Draw background color
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

    // Set the camera position (View matrix)
    Matrix.setLookAtM(mViewMatrix, 0, cameraX, cameraY, cameraZ, targetX, targetY, targetZ, upX, upY, upZ);

    // Calculate the projection and view transformation
    Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

    // Create a rotation for the triangle

    // Use the following code to generate constant rotation.
    // Leave this code out when using TouchEvents.
    // long time = SystemClock.uptimeMillis() % 4000L;
    // float angle = 0.090f * ((int) time);

    Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);


    // Combine the rotation matrix with the projection and camera view
    // Note that the mMVPMatrix factor *must be first* in order
    // for the matrix multiplication product to be correct.
    Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

    // Draw triangle
    //triangles.get(0).draw(scratch);

    for (Line line : lines) {
      line.draw(scratch);
    }

    for (Cube cube : cubes) {
      cube.draw(scratch);
    }
  }


  private float[] mMVPMatrix = new float[16];


  /**
   * Load the shader to the Renderer.
   * @param shaderType
   * @param shaderCode
   * @return
   */
  public static int loadShader(int shaderType, String shaderCode) {
    int shader = GLES20.glCreateShader(shaderType);

    if (shader != 0) {
      GLES20.glShaderSource(shader, shaderCode);

      GLES20.glCompileShader(shader);

      final int[] compileStatus = new int[1];
      GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

      if (compileStatus[0] == 0) {
        GLES20.glDeleteShader(shader);
        shader = 0;
      }
    }

    return shader;
  }

  public void rotateCamera() {
    targetX -= .1f;
    targetY -= .1f;

  }


  /**
   * Returns the rotation angle of the triangle shape (mTriangle).
   *
   * @return - A float representing the rotation angle.
   */
  public float getAngle() {
    return mAngle;
  }


  /**
   * Sets the rotation angle of the triangle shape (mTriangle).
   */
  public void setAngle(float angle) {
    mAngle = angle;
  }

  public void setCameraX(float x) {
    cameraX = x;
    targetX = x;
  }

  public void setCameraY(float y) {
    cameraY = y;
    targetY = y;
  }


  public float getCameraX() {
    return cameraX;
  }


  public float getCameraY() {
    return cameraY;
  }


  public void zoomOut() {

  }

  public Cube getCube(int index) {
    if (index < cubes.length) {
      return cubes[index];
    }

    return null;
  }
}
