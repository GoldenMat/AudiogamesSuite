package it.unimi.di.lim.audiogames;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.TextView;

public class Game4Activity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;

    void getInstructions() {
        if(!mSharedPreferences.contains("Game4") && getIntent().getIntExtra("end", 0) != 1) {

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }

            TextView title = new TextView(this);
            title.setText(getString(R.string.instructions));
            title.setBackgroundColor(Color.DKGRAY);
            title.setPadding(10, 10, 10, 10);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(Color.WHITE);
            title.setTextSize(20);

            builder.setMessage(R.string.game4_how_content)
                    .setPositiveButton(R.string.instr_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton(R.string.instr_dont, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putBoolean("Game4", true);
                            editor.apply();
                        }
                    })
                    .setCustomTitle(title);
            AlertDialog dialog = builder.show();
            dialog.setCanceledOnTouchOutside(false);
            TextView titleView = (TextView) dialog.findViewById(android.R.id.title);
            titleView.setGravity(Gravity.CENTER);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game4);

        mSharedPreferences = getSharedPreferences("Instructions", MODE_PRIVATE);

        Button game4_play_button = (Button) findViewById(R.id.game4_play_button);
        game4_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game4PlayIntent = new Intent(Game4Activity.this, Game4PlayActivity.class);
                startActivity(game4PlayIntent);
            }
        });

        Button game4_how_button = (Button) findViewById(R.id.game4_how_button);
        game4_how_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game4HowIntent = new Intent(Game4Activity.this, Game4HowActivity.class);
                startActivity(game4HowIntent);
            }
        });

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

        if(getIntent().hasExtra("hasWon")) {
            boolean hasWon = getIntent().getBooleanExtra("hasWon", false);
            String code = getIntent().getStringExtra("code");
            TextView result = (TextView) findViewById(R.id.game4_result);
            if(hasWon) {
                result.setText(getString(R.string.game4_win) + " " + code);
            } else {
                result.setText(getString(R.string.game4_lose) + " " + code);
            }
        }

        getInstructions();
    }
}
