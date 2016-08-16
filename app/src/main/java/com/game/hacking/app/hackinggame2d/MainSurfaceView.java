package com.game.hacking.app.hackinggame2d;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.game.hacking.app.hackinggame2d.game.NetworkNode;
import com.game.hacking.app.hackinggame2d.grafiks.Cube;
import com.game.hacking.app.hackinggame2d.grafiks.Vec2D;
import com.game.hacking.app.hackinggame2d.mapsequence.Edge;
import com.game.hacking.app.hackinggame2d.mapsequence.GameGraph;
import com.game.hacking.app.hackinggame2d.mapsequence.Vertex;

import java.util.List;
import java.util.Random;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MainSurfaceView extends GLSurfaceView {
  private Random rand = new Random();
  private GameGraph<NetworkNode> gameGraph;
  private final MainRenderer mRenderer;


  private Cube currentCube;
  private NetworkNode currentNode;

  private float prevMoveX;
  private float prevMoveY;

  private float randomCoord() {
    return (rand.nextFloat() * 100) / 7;
  }

  public MainSurfaceView(Context context) {
    super(context);

    // Create an OpenGL ES 2.0 context.
    setEGLContextClientVersion(2);
    // Set the Renderer for drawing on the GLSurfaceView
    mRenderer = new MainRenderer();

    // Set up the game
    gameGraph = new GameGraph<>();

    gameGraph.addVertexToGraph(new NetworkNode(), new Vec2D(0, 0));
    gameGraph.addVertexToGraph(new NetworkNode(), new Vec2D(2, 2));
    gameGraph.addVertexToGraph(new NetworkNode(), new Vec2D(-2, -2));
    gameGraph.addVertexToGraph(new NetworkNode(), new Vec2D(2, -2));
    gameGraph.addVertexToGraph(new NetworkNode(), new Vec2D(-2, 2));
    gameGraph.addVertexToGraph(new NetworkNode(), new Vec2D(4, 0));
    gameGraph.addVertexToGraph(new NetworkNode(), new Vec2D(0, 4));
    gameGraph.addVertexToGraph(new NetworkNode(), new Vec2D(-4, 0));
    gameGraph.addVertexToGraph(new NetworkNode(), new Vec2D(0, -4));
    gameGraph.addVertexToGraph(new NetworkNode(), new Vec2D(4, 4));
    gameGraph.addVertexToGraph(new NetworkNode(), new Vec2D(-4, -4));
    gameGraph.addVertexToGraph(new NetworkNode(), new Vec2D(4, -4));
    gameGraph.addVertexToGraph(new NetworkNode(), new Vec2D(-4, 4));
    gameGraph.addVertexToGraph(new NetworkNode(), new Vec2D(randomCoord(), 6));
    gameGraph.addVertexToGraph(new NetworkNode(), new Vec2D(0, -randomCoord()));


    mRenderer.addNumberofCubes(gameGraph.getVertices().size());


    List<Vertex<NetworkNode>> list = gameGraph.getVertices();

    for (int i = 1; i < list.size(); ++i) {
      for (int j = i - 1; j > 0; --j) {
        gameGraph.addEdgeBetweenTwoVertices(i, gameGraph.getCoordForVertex(list.get(i)),
                gameGraph.getCoordForVertex(list.get(j)));
      }
    }

//    gameGraph.addEdgeBetweenTwoVertices(1, gameGraph.getCoordForVertex(list.get(0)),
//            gameGraph.getCoordForVertex(list.get(1)));
//
//    gameGraph.addEdgeBetweenTwoVertices(2, gameGraph.getCoordForVertex(list.get(1)),
//            gameGraph.getCoordForVertex(list.get(2)));
//
//    gameGraph.addEdgeBetweenTwoVertices(12, gameGraph.getCoordForVertex(list.get(2)),
//            gameGraph.getCoordForVertex(list.get(3)));
//
//    gameGraph.addEdgeBetweenTwoVertices(11, gameGraph.getCoordForVertex(list.get(5)),
//            gameGraph.getCoordForVertex(list.get(0)));

    mRenderer.addNumberofLines(gameGraph.getEdges().size());

    setRenderer(mRenderer);


    WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);

    prevMoveX = size.x / 2;
    prevMoveY = size.y / 2;
    // Render the view only when there is a change in the drawing data
    setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
  }

  private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
  private float mPreviousX;
  private float mPreviousY;

  @Override
  public boolean onTouchEvent(MotionEvent e) {
    // MotionEvent reports input details from the touch screen
    // and other input controls. In this case, you are only
    // interested in events where the touch position changed.

    float x = e.getX();
    float y = e.getY();

    switch (e.getAction()) {
      case MotionEvent.ACTION_DOWN:
        prevMoveY = y;
        prevMoveX = x;
        break;
      case MotionEvent.ACTION_MOVE:

        float dx = x - mPreviousX;
        float dy = y - mPreviousY;

        float ddx = x - prevMoveX;
        float ddy = y - prevMoveY;

        // reverse direction of rotation above the mid-line
        if (y > getHeight() / 2) {
          dx = dx * -1;
        }

        // reverse direction of rotation to left of the mid-line
        if (x < getWidth() / 2) {
          dy = dy * -1;
        }

//        mRenderer.setAngle(
//                mRenderer.getAngle() +
//                        ((dx + dy) * TOUCH_SCALE_FACTOR));  // = 180.0f / 320

        mRenderer.setCameraX( mRenderer.getCameraX() + -(ddx / 1000));
        mRenderer.setCameraY( mRenderer.getCameraY() + (ddy / 1000));
        requestRender();
    }

    mPreviousX = x;
    mPreviousY = y;
    return true;
  }

  public void initializeCoords() {

    List<Vertex<NetworkNode>> list = gameGraph.getVertices();
    Vec2D c = null;

    for (int i = 0; i < list.size(); ++i) {
      Vertex<NetworkNode> node = list.get(i);
      c = gameGraph.getCoordForVertex(node);
      mRenderer.manageCubeCoordinates(i, c.getX(), c.getY());
    }
//    mRenderer.manageCubeCoordinates(0, c.getX(), c.getY());
//    c = gameGraph.getCoordForVertex(list.get(1));
//    mRenderer.manageCubeCoordinates(1, c.getX(), c.getY());
//    c = gameGraph.getCoordForVertex(list.get(2));
//    mRenderer.manageCubeCoordinates(2, c.getX(), c.getY());
//    c = gameGraph.getCoordForVertex(list.get(3));
//    mRenderer.manageCubeCoordinates(3, c.getX(), c.getY());

    List<Edge<NetworkNode>> edges = gameGraph.getEdges();

    for (int i = 0; i < edges.size(); ++i) {
      Edge<NetworkNode> edge = edges.get(i);
      Vec2D v1 = gameGraph.getCoordForVertex(edge.getSource());
      Vec2D v2 = gameGraph.getCoordForVertex(edge.getDestination());
      mRenderer.manageLineCoordinates(i, v1.getX(), v1.getY(), v2.getX(), v2.getY());
    }

    currentCube = mRenderer.getCube(list.size() - 1);

    for (Vertex<NetworkNode> networkNodeVertex : list) {
      Vec2D vec = gameGraph.getCoordForVertex(networkNodeVertex);
      if (vec.getX() == currentCube.getXCoordinate() && vec.getY() == currentCube.getYCoordinate()) {
        currentNode = networkNodeVertex.getData();
        break;
      }
    }
  }

  public void moveUp() {
    if (currentCube != null) {
      currentCube.moveYUnites(1);
      requestRender();
    }
  }

  public void moveDown() {
    if (currentCube != null) {
      currentCube.moveYUnites(-1);
      requestRender();
    }
  }

  public void moveLeft() {
    if (currentCube != null) {
      currentCube.moveXUnits(-1);
      requestRender();
    }
  }

  public void moveRight() {
    if (currentCube != null) {
      currentCube.moveXUnits(1);
      requestRender();
    }
  }
}