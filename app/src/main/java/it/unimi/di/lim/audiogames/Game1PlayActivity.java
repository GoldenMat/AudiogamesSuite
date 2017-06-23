package it.unimi.di.lim.audiogames;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class Game1PlayActivity extends AppCompatActivity {

    private AudioTrack audioTrack;

    private AudioManager mAudioManager;

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                audioTrack.play();
            } else {
                audioTrack.release();
            }
        }
    };

    void genTone(double freqOfTone){
        int duration = 2; // seconds
        int sampleRate = 8000;
        int numSamples = duration * sampleRate;
        double sample[] = new double[numSamples];
        byte generatedSnd[] = new byte[2 * numSamples];
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            // sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
            sample[i] = Math.sin((2 * Math.PI - .001) * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        int ramp = numSamples / 20;

        for (int i = 0; i < ramp; i++) {
            // scale to maximum amplitude
            final short val = (short) ((sample[i] * 32767) * i / ramp);
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        for (int i = ramp; i < numSamples - ramp; i++) {
            // scale to maximum amplitude
            final short val = (short) ((sample[i] * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        for (int i = numSamples - ramp; i < numSamples; i++) {
            // scale to maximum amplitude
            final short val = (short) ((sample[i] * 32767) * (numSamples - i) / ramp);
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                    AudioTrack.MODE_STATIC);
            audioTrack.write(generatedSnd, 0, generatedSnd.length);
            audioTrack.play();
        }
    }

    void setFrequencies() {
        boolean upper2;
        Random rand = new Random();
        sound1 = 300 + rand.nextInt(701);
        boolean what = rand.nextBoolean();

        if(what) {
            sound2 = sound1 * range;
            upper2 = true;
        }
        else {
            sound2 = sound1 / range;
            upper2 = false;
        }

        Log.v("sound1", String.valueOf(sound1));
        Log.v("sound2", String.valueOf(sound2));

        boolean high = rand.nextBoolean();
        if(high) {
            higher = true;

            if(upper2)
                correct = 1;
            else
                correct = 0;
        }
        else {
            higher = false;

            if(upper2)
                correct = 0;
            else
                correct = 1;
        }
    }

    void selectTone(int i) {
        if(i == correct) {
            mScore++;
            TextView scoreView = (TextView) findViewById(R.id.game1_score);
            scoreView.setText(String.valueOf(mScore));
            Button startButton = (Button) findViewById(R.id.game1_start);
            startButton.setText(R.string.game1_next);
            range = (range+1)/2;
            setFrequencies();
            TextView criteria = (TextView) findViewById(R.id.game1_criteria);
            criteria.setText("");
            TextView scoreText = (TextView) findViewById(R.id.game1_score_text);
            scoreText.setText(R.string.game1_success);
            scoreText.setTextColor(Color.GREEN);
            scoreText.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);

            Button firstSound = (Button) findViewById(R.id.game1_left_button);
            Button secondSound = (Button) findViewById(R.id.game1_right_button);
            if(Build.VERSION.SDK_INT >= 16) {
                firstSound.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                secondSound.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
            }
        } else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }

            TextView title = new TextView(this);
            title.setText(getString(R.string.game1_loss));
            title.setBackgroundColor(Color.DKGRAY);
            title.setPadding(10, 10, 10, 10);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(Color.WHITE);
            title.setTextSize(20);

            builder.setMessage(getString(R.string.game1_result) + " " + mScore)
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
                    Intent lost = new Intent(Game1PlayActivity.this, Game1Activity.class);
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
    }

    private double range = 2;
    private int correct;
    private int mScore;

    private double sound1;
    private double sound2;
    private boolean higher;
    private boolean clickable = false;

    private Handler hand = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1_play);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.game1Dark));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.game1Primary));
        }

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.game1Primary)));
        }

        setFrequencies();

        Button startButton = (Button) findViewById(R.id.game1_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button firstSound = (Button) findViewById(R.id.game1_left_button);
                Button secondSound = (Button) findViewById(R.id.game1_right_button);
                if(Build.VERSION.SDK_INT >= 16) {
                    firstSound.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
                    secondSound.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
                }
                clickable = false;
                Button startButton = (Button) findViewById(R.id.game1_start);
                startButton.setText(R.string.game1_repeat);
                final TextView criteria = (TextView) findViewById(R.id.game1_criteria);
                if(higher) {
                    criteria.setText(R.string.game1_high);
                } else {
                    criteria.setText(R.string.game1_low);
                }
                genTone(sound1);
                hand.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        genTone(sound2);
                    }
                }, 2500);
                hand.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clickable = true;
                        criteria.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
                        mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
                    }
                }, 4500);
            }
        });

        Button leftButton = (Button) findViewById(R.id.game1_left_button);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickable) {
                    selectTone(0);
                    clickable = false;
                }
            }
        });

        Button rightButton = (Button) findViewById(R.id.game1_right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickable) {
                    selectTone(1);
                    clickable = false;
                }
            }
        });
    }

    @Override
    protected void onStop() {
        if(audioTrack != null && audioTrack.getState() == AudioTrack.STATE_INITIALIZED)
            audioTrack.release();
        hand.removeCallbacksAndMessages(null);
        mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        super.onStop();
    }

    @Override
    protected void onPause() {
        if(audioTrack != null && audioTrack.getState() == AudioTrack.STATE_INITIALIZED)
            audioTrack.release();
        hand.removeCallbacksAndMessages(null);
        mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        super.onPause();
    }
}
