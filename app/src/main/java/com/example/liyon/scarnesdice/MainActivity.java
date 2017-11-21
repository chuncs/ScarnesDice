package com.example.liyon.scarnesdice;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static int user_overall_score;
    private static int user_turn_score;
    private static int comp_overall_score;
    private static int comp_turn_score;
    private Random random = new Random();
    private int dice_value;
    private Integer[] dice_image = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
                                       R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};
    private TextView textView;
    private ImageView imageView;
    private Button roll_button;
    private Button hold_button;
    private final static String TEXT_VIEW_KEY = "com.example.scarnesdice.textview";
    private final static String IMAGE_VIEW_KEY = "com.example.scarnesdice.imageview";
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        roll_button = findViewById(R.id.button);
        hold_button = findViewById(R.id.button2);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(TEXT_VIEW_KEY, textView.getText().toString());
        outState.putInt(IMAGE_VIEW_KEY, dice_value);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        textView.setText(savedInstanceState.getString(TEXT_VIEW_KEY));
        imageView.setImageResource(dice_image[savedInstanceState.getInt(IMAGE_VIEW_KEY) - 1]);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        user_overall_score = 0;
        user_turn_score = 0;
        comp_overall_score = 0;
        comp_turn_score = 0;
    }

    public void rollButton(View view) {
        dice_value = random.nextInt(6) + 1;
        imageView.setImageResource(dice_image[dice_value - 1]);

        if(dice_value == 1) {
            user_turn_score = 0;
            textView.setText(String.format("Your score: %s Computer score: %s Your turn score: %s",
                                            user_overall_score, comp_overall_score, user_turn_score));
            computerTurn();
        }
        else {
            user_turn_score += dice_value;
            textView.setText(String.format("Your score: %s Computer score: %s Your turn score: %s",
                                            user_overall_score, comp_overall_score, user_turn_score));
        }
    }

    public void holdButton(View view) {
        user_overall_score += user_turn_score;
        user_turn_score = 0;
        textView.setText(String.format("Your score: %s Computer score: %s Your turn score: %s",
                                        user_overall_score, comp_overall_score, user_turn_score));
        if(user_overall_score >= 100) {
            Toast.makeText(this, "You won!", Toast.LENGTH_LONG).show();
        }
        else {
            computerTurn();
        }
    }

    public void resetButton(View view) {
        user_overall_score = 0;
        user_turn_score = 0;
        comp_overall_score = 0;
        comp_turn_score = 0;
        textView.setText(String.format("Your score: %s Computer score: %s",
                                        user_overall_score, comp_overall_score));
    }

    public void computerTurn() {
        roll_button.setEnabled(false);
        hold_button.setEnabled(false);

        handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                dice_value = random.nextInt(6) + 1;
                comp_turn_score += dice_value;
                imageView.setImageResource(dice_image[dice_value - 1]);

                if(dice_value == 1) {
                    comp_turn_score = 0;
                    roll_button.setEnabled(true);
                    hold_button.setEnabled(true);
                    textView.setText(String.format("Your score: %s Computer score: %s Computer rolled a one",
                                                    user_overall_score, comp_overall_score));
                }
                else if(comp_turn_score >= 20 || comp_turn_score >= 100 - comp_overall_score) {
                    comp_overall_score += comp_turn_score;
                    comp_turn_score = 0;

                    if(comp_overall_score >= 100) {
                        Toast.makeText(getApplicationContext(), "Computer won!", Toast.LENGTH_LONG).show();
                    }

                    roll_button.setEnabled(true);
                    hold_button.setEnabled(true);
                    textView.setText(String.format("Your score: %s Computer score: %s Computer holds",
                                                    user_overall_score, comp_overall_score));
                }
                else {
                    textView.setText(String.format("Your score: %s Computer score: %s" + System.getProperty("line.separator") + "Computer turn score: %s",
                                                    user_overall_score, comp_overall_score, comp_turn_score));
                    handler.postDelayed(this, 500);
                }
            }
        };
        handler.postDelayed(runnable, 500);
    }
}
