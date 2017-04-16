package us.marktan.scarnes_dice;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    public TextView userTurnScoreView;
    public TextView userTotalScoreView;
    public TextView compTotalScoreView;
    public TextView compTurnScoreView;
    public ImageView diceView;
    public Button rollButton;
    public Button holdButton;

    private int userOverrallScore = 0;
    private int userTurnScore = 0;
    private int compOverrallScore = 0;
    private int compTurnScore = 0;

    final Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            boolean hold = false;
            int value = rollDice();
            if (value == 1){
                compTurnScore = 0;
                compTurnScoreView.setText("Computer Holds");
                hold = true;
            }
            else {
                compTurnScore += value;
                compTurnScoreView.setText("" + compTurnScore);
            }
            if(!hold && compTurnScore < 20)
                timerHandler.postDelayed(this, 500);
            else {
                compOverrallScore += compTurnScore;
                compTotalScoreView.setText("" + compOverrallScore);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userTurnScoreView = (TextView)findViewById(R.id.turn_score);
        userTotalScoreView = (TextView) findViewById(R.id.user_score);
        compTotalScoreView = (TextView) findViewById(R.id.computer_score);
        compTurnScoreView = (TextView) findViewById(R.id.computer_turn_score);
        diceView = (ImageView) findViewById(R.id.dice);
        rollButton = (Button) findViewById(R.id.roll_btn);
        holdButton = (Button) findViewById(R.id.hold_btn);


    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    public void computerTurn(){
        rollButton.setEnabled(false);
        holdButton.setEnabled(false);

        compTurnScore = 0;
        compTurnScoreView.setText("0");

        timerHandler.postDelayed(timerRunnable, 500);

        compOverrallScore += compTurnScore;
        compTotalScoreView.setText(""+compOverrallScore);

        rollButton.setEnabled(true);
        holdButton.setEnabled(true);
    }

    public int rollDice(){
        Random rand = new Random();
        int value = rand.nextInt(6) + 1;

        // update dice ui
        Uri otherPath = Uri.parse("android.resource://us.marktan.scarnes_dice/drawable/dice"+value);
        diceView.setImageURI(otherPath);
        Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        diceView.setAnimation(animShake);

        return value;
    }


    // Event handler for when user rolls the die
    public void userTurn(View v){
        int value = rollDice();
        if (value == 1){
            userTurnScore = 0;
            holdScore(getCurrentFocus());
        }
        else
            userTurnScore += value;
        userTurnScoreView.setText(""+userTurnScore);
    }

    // Event handler when user presses hold button
    public void holdScore(View v){
        userOverrallScore += userTurnScore;
        userTurnScore = 0;
        userTotalScoreView.setText(""+userOverrallScore);
        userTurnScoreView.setText(""+userTurnScore);
        computerTurn();
    }


    // Event handler when user presses reset button
    public void resetGame(View v){
        userOverrallScore = 0;
        userTurnScore = 0;
        compOverrallScore = 0;
        compTurnScore = 0;
        userTotalScoreView.setText(""+userOverrallScore);
        userTurnScoreView.setText(""+userTurnScore);
        compTotalScoreView.setText(""+compOverrallScore);
        compTurnScoreView.setText(""+compTurnScore);
    }


}
