package it.unimi.di.lim.audiogames;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Game4PlayActivity extends AppCompatActivity {

    private int mAttempts = 9;

    private void endGame(boolean won, String a, String b, String c, String d) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        TextView title = new TextView(this);
        if(won)
            title.setText(getString(R.string.game4_win));
        else
            title.setText(getString(R.string.game4_lose));
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        builder.setMessage(getString(R.string.game4_code_was) + " " + a + " " + b + " " + c + " " + d)
                .setPositiveButton(R.string.new_game, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent again = getIntent();
                        finish();
                        startActivity(again);
                    }
                })
                .setNegativeButton(R.string.lost_game, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent lost = new Intent(Game4PlayActivity.this, Game4Activity.class);
                        lost.putExtra("end", 1);
                        startActivity(lost);
                        finish();
                    }
                })
                .setCustomTitle(title);
        AlertDialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);
        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
        TextView titleView = (TextView) dialog.findViewById(android.R.id.title);
        titleView.setGravity(Gravity.CENTER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game4_play);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.game4Dark));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.game4Primary));
        }

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.game4Primary)));
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Random rand = new Random();
        final String a = Integer.toString(rand.nextInt(10));
        final String b = Integer.toString(rand.nextInt(10));
        final String c = Integer.toString(rand.nextInt(10));
        final String d = Integer.toString(rand.nextInt(10));
        final String code = a + b + c + d;

        Button submit = (Button) findViewById(R.id.game4_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText attempt = (EditText) findViewById(R.id.game4_attempt);
                String codeAttempt = attempt.getText().toString();
                if(codeAttempt.length() == 4) {
                    attempt.clearFocus();
                    attempt.getText().clear();
                    String code1 = String.valueOf(codeAttempt.charAt(0));
                    String code2 = String.valueOf(codeAttempt.charAt(1));
                    String code3 = String.valueOf(codeAttempt.charAt(2));
                    String code4 = String.valueOf(codeAttempt.charAt(3));

                    if (codeAttempt.equals(code)) {
                        endGame(true, a, b, c, d);
                    } else if (mAttempts == 1) {
                        endGame(false, a, b, c, d);
                    } else {
                        mAttempts--;
                        TextView attempts = (TextView) findViewById(R.id.game4_score);
                        attempts.setText(String.valueOf(mAttempts));
                        TextView attempt1 = (TextView) findViewById(R.id.game4_digit_1);
                        attempt1.setText(code1);
                        attempt1.setVisibility(View.VISIBLE);
                        attempt1.setContentDescription(getString(R.string.game4_digit) + " 1. " + code1);
                        TextView attempt2 = (TextView) findViewById(R.id.game4_digit_2);
                        attempt2.setText(code2);
                        attempt2.setVisibility(View.VISIBLE);
                        attempt2.setContentDescription(getString(R.string.game4_digit) + " 2. " + code2);
                        TextView attempt3 = (TextView) findViewById(R.id.game4_digit_3);
                        attempt3.setText(code3);
                        attempt3.setVisibility(View.VISIBLE);
                        attempt3.setContentDescription(getString(R.string.game4_digit) + " 3. " + code3);
                        TextView attempt4 = (TextView) findViewById(R.id.game4_digit_4);
                        attempt4.setText(code4);
                        attempt4.setVisibility(View.VISIBLE);
                        attempt4.setContentDescription(getString(R.string.game4_digit) + " 4. " + code4);

                        ImageView light1 = (ImageView) findViewById(R.id.game4_check_1);
                        light1.setVisibility(View.VISIBLE);
                        ImageView light2 = (ImageView) findViewById(R.id.game4_check_2);
                        light2.setVisibility(View.VISIBLE);
                        ImageView light3 = (ImageView) findViewById(R.id.game4_check_3);
                        light3.setVisibility(View.VISIBLE);
                        ImageView light4 = (ImageView) findViewById(R.id.game4_check_4);
                        light4.setVisibility(View.VISIBLE);
                        if (code1.charAt(0) == code.charAt(0)) {
                            light1.setImageResource(R.drawable.game4_green);
                            light1.setContentDescription(getString(R.string.game4_digit1_ok));
                        } else if (code.contains(code1)) {
                            light1.setImageResource(R.drawable.game4_yellow);
                            light1.setContentDescription(getString(R.string.game4_digit1_almost));
                        } else {
                            light1.setImageResource(R.drawable.game4_red);
                            light1.setContentDescription(getString(R.string.game4_digit1_wrong));
                        }
                        if (code2.charAt(0) == code.charAt(1)) {
                            light2.setImageResource(R.drawable.game4_green);
                            light2.setContentDescription(getString(R.string.game4_digit2_ok));
                        } else if (code.contains(code2)) {
                            light2.setImageResource(R.drawable.game4_yellow);
                            light2.setContentDescription(getString(R.string.game4_digit2_almost));
                        } else {
                            light2.setImageResource(R.drawable.game4_red);
                            light2.setContentDescription(getString(R.string.game4_digit2_wrong));
                        }
                        if (code3.charAt(0) == code.charAt(2)) {
                            light3.setImageResource(R.drawable.game4_green);
                            light3.setContentDescription(getString(R.string.game4_digit3_ok));
                        } else if (code.contains(code3)) {
                            light3.setImageResource(R.drawable.game4_yellow);
                            light3.setContentDescription(getString(R.string.game4_digit3_almost));
                        } else {
                            light3.setImageResource(R.drawable.game4_red);
                            light3.setContentDescription(getString(R.string.game4_digit3_wrong));
                        }
                        if (code4.charAt(0) == code.charAt(3)) {
                            light4.setImageResource(R.drawable.game4_green);
                            light4.setContentDescription(getString(R.string.game4_digit4_ok));
                        } else if (code.contains(code4)) {
                            light4.setImageResource(R.drawable.game4_yellow);
                            light4.setContentDescription(getString(R.string.game4_digit4_almost));
                        } else {
                            light4.setImageResource(R.drawable.game4_red);
                            light4.setContentDescription(getString(R.string.game4_digit4_wrong));
                        }

                        TextView attemptsLeft = (TextView) findViewById(R.id.game4_attempts_left);
                        attemptsLeft.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
                    }
                }
                else {
                    Toast.makeText(Game4PlayActivity.this, getString(R.string.game4_code_missing), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
