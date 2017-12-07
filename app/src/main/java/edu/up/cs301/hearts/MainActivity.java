package edu.up.cs301.hearts;

import edu.up.cs301.game.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView HumanScoreDisplay;
    TextView AI1ScoreDisplay;
    TextView AI2ScoreDisplay;
    TextView AI3ScoreDisplay;
    ImageButton info;
    boolean isClicked = true;
    PopupWindow popUpWindow;
    ViewGroup.LayoutParams layoutParams;
    LinearLayout mainLayout;
    // Button btnClickHere;
    LinearLayout containerLayout;
    TextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("POPUP!!", "Please");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info =(ImageButton) findViewById(R.id.infoBtn);

        containerLayout = new LinearLayout(this);
        mainLayout = new LinearLayout(this);
        popUpWindow = new PopupWindow(this);


        info = new ImageButton(this);
        //info.setText("Click Here For Pop Up Window !!!");
        info.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (isClicked) {
                    isClicked = false;
                    popUpWindow.showAtLocation(mainLayout, Gravity.BOTTOM, 10, 10);
                    popUpWindow.update(50, 50, 320, 90);
                } else {
                    isClicked = true;
                    popUpWindow.dismiss();
                }
            }

        });

        tvMsg = new TextView(this);
        tvMsg.setText("Hi this is pop up window...");

        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.addView(tvMsg, layoutParams);
        popUpWindow.setContentView(containerLayout);
        mainLayout.addView(info, layoutParams);
        setContentView(mainLayout);

/*
        HumanScoreDisplay = (TextView)findViewById(R.id.HumanScore);
        HumanScoreDisplay.setText("Your score is: 0");

        AI1ScoreDisplay = (TextView)findViewById(R.id.AI1Score);
        AI1ScoreDisplay.setText("AI1 score is: 0");

        AI2ScoreDisplay = (TextView)findViewById(R.id.AI2Score);
        AI2ScoreDisplay.setText("AI2 score is: 0");

        AI3ScoreDisplay = (TextView)findViewById(R.id.AI3Score);
        AI3ScoreDisplay.setText("AI3 score is: 0");
        */

    }
}