package it.unimi.di.lim.audiogames;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Button game1_button = (Button) findViewById(R.id.game1_button);
        game1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game1Intent = new Intent(PlayActivity.this, Game1Activity.class);
                startActivity(game1Intent);
            }
        });

        Button game2_button = (Button) findViewById(R.id.game2_button);
        game2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game2Intent = new Intent(PlayActivity.this, Game2Activity.class);
                startActivity(game2Intent);
            }
        });

        Button game3_button = (Button) findViewById(R.id.game3_button);
        game3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game3Intent = new Intent(PlayActivity.this, Game3Activity.class);
                startActivity(game3Intent);
            }
        });

        Button game4_button = (Button) findViewById(R.id.game4_button);
        game4_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game4Intent = new Intent(PlayActivity.this, Game4Activity.class);
                startActivity(game4Intent);
            }
        });
    }
}
