package edu.up.cs301.hearts;

import edu.up.cs301.game.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


/**
 * MainActivity connects our java code with widgets
 * and GUI elements from xml files needed for the
 * game of Hearts
 *
 * @author Lindsey Lavee
 * @version December 2017
 */
public class MainActivity extends AppCompatActivity {


    ImageButton info;
    boolean isClicked = true;
    PopupWindow popUpWindow;
    ViewGroup.LayoutParams layoutParams;
    LinearLayout mainLayout;
    LinearLayout containerLayout;
    TextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info =(ImageButton) findViewById(R.id.infoBtn);

        containerLayout = new LinearLayout(this);
        mainLayout = new LinearLayout(this);
        popUpWindow = new PopupWindow(this);


        info = new ImageButton(this);
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

        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.addView(tvMsg, layoutParams);
        popUpWindow.setContentView(containerLayout);
        mainLayout.addView(info, layoutParams);
        setContentView(mainLayout);

    }
}