package com.game.hacking.app.hackinggame2d;

import android.app.Activity;
//import android.app.ActivityManager;
//import android.content.Context;
//import android.content.pm.ConfigurationInfo;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;

public class MainActivity extends Activity {
  private Button hackButton;
  private Button uploadButton;
  private Button moveUpButton;
  private Button moveDownButton;
  private Button moveLeftButton;
  private Button moveRightButton;

  private MainSurfaceView glSurfaceView;
  private MainRenderer renderer;
  private Intent intent;
  private View view;




  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
//    BitmapFactory.Options options = new BitmapFactory.Options();
//    options.inScaled = false;
//
//    drawableImageView = (ImageView) this.findViewById(R.id.drawable_image);
//    bitmap = Bitmap.createBitmap(getWindowManager()
//      .getDefaultDisplay().getWidth(), getWindowManager()
//      .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
//
//    canvas = new Canvas(bitmap);
//    drawableImageView.setImageBitmap(bitmap);
//    view = findViewById(R.id.drawable_image);
//
//    view.setDrawingCacheEnabled(true);
//    view.buildDrawingCache(true);
//    paint = new Paint();
//    paint.setColor(Color.MAGENTA);
//    paint.setStrokeWidth(10f);
//
//    lastX = 0;
//    lastY = 0;
//    int startx = 150;
//    int starty = 400;
//    int endx = 150;
//    int endy = 1000;
//
//    canvas.drawLine(startx, starty, endx, endy, paint);
//
//    canvas.drawCircle(endx, endy, 20f, paint);
    glSurfaceView = new MainSurfaceView(this);

    final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
    final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

    Button button = (Button) findViewById(R.id.button);

    hackButton = (Button) findViewById(R.id.button_hack);
    uploadButton = (Button) findViewById(R.id.button_upload);

    TextView xcoord = (TextView) findViewById(R.id.xcoord);
    TextView ycoord = (TextView) findViewById(R.id.ycoord);
    moveDownButton = (Button) findViewById(R.id.moveDown_button);
    moveLeftButton = (Button) findViewById(R.id.moveLeft_Button);
    moveRightButton = (Button) findViewById(R.id.moveRight_Button);
    moveUpButton = (Button) findViewById(R.id.moveUp_button);


    moveUpButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        requestMoveUp();
      }
    });


    moveDownButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        requestMoveDown();
      }
    });

    moveLeftButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        requestMoveLeft();
      }
    });


    moveRightButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        requestMoveRight();
      }
    });

    // On click hack.
    hackButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

      }
    });


    // On upload.
    uploadButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        requestReset();
      }

    });

    // Options?
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
      doIt();
      }
    });

    RelativeLayout gui = (RelativeLayout) findViewById(R.id.surface_view);

    RelativeLayout.LayoutParams layoutParams =
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

    gui.addView(glSurfaceView, layoutParams);

//    intent = new Intent(this, MusicService.class);
//    intent.setAction("com.example.action.PLAY");
//    startService(intent);
  }

  public void doIt() {
    glSurfaceView.initializeCoords();
    glSurfaceView.requestRender();
  }


  public void requestReset() {
  }

  @Override
  public void onResume() {
    super.onResume();
    glSurfaceView.onResume();
//    startService(intent);
  }

  @Override
  public void onPause() {
    super.onPause();
    glSurfaceView.onPause();
//    stopService(intent);

  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
//    stopService(intent);
  }

  public void requestMoveUp() {
    glSurfaceView.moveUp();
  }

  public void requestMoveDown() {
    glSurfaceView.moveDown();
  }

  public void requestMoveLeft() {
    glSurfaceView.moveLeft();
  }

  public void requestMoveRight() {
    glSurfaceView.moveRight();
  }
}
