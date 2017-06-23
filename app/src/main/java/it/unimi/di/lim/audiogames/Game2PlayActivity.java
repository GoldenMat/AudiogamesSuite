package it.unimi.di.lim.audiogames;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Random;

public class Game2PlayActivity extends AppCompatActivity {

    private int mScore = 0;
    private LinkedList<Integer> temp = new LinkedList<>();
    private boolean mClickable = false;
    private boolean mWait = false;
    private int mChecking = 0;

    private Handler hand = new Handler();

    private AudioManager mAudioManager;

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange != AudioManager.AUDIOFOCUS_GAIN) {
                releaseMediaPlayer(mCMediaPlayer);
                releaseMediaPlayer(mDMediaPlayer);
                releaseMediaPlayer(mEMediaPlayer);
                releaseMediaPlayer(mFMediaPlayer);
            }
        }
    };

    private MediaPlayer mCMediaPlayer;
    private MediaPlayer mDMediaPlayer;
    private MediaPlayer mEMediaPlayer;
    private MediaPlayer mFMediaPlayer;

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer(mp);
        }
    };

    private void releaseMediaPlayer(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            Button mc_button = (Button) findViewById(R.id.c_button);
            Button md_button = (Button) findViewById(R.id.d_button);
            Button me_button = (Button) findViewById(R.id.e_button);
            Button mf_button = (Button) findViewById(R.id.f_button);
            if(mediaPlayer == mCMediaPlayer) {
                mc_button.setBackgroundResource(R.color.game2_c_off);
            } else if (mediaPlayer == mDMediaPlayer) {
                md_button.setBackgroundResource(R.color.game2_d_off);
            } else if (mediaPlayer == mEMediaPlayer) {
                me_button.setBackgroundResource(R.color.game2_e_off);
            } else {
                mf_button.setBackgroundResource(R.color.game2_f_off);
            }
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    private void playButton(int i) {
        Button mc_button = (Button) findViewById(R.id.c_button);
        Button md_button = (Button) findViewById(R.id.d_button);
        Button me_button = (Button) findViewById(R.id.e_button);
        Button mf_button = (Button) findViewById(R.id.f_button);
        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            switch (i) {
                case 0:
                    mc_button.setBackgroundResource(R.color.game2_c_on);
                    mCMediaPlayer = MediaPlayer.create(Game2PlayActivity.this, R.raw.dog);
                    mCMediaPlayer.start();
                    mCMediaPlayer.setOnCompletionListener(mCompletionListener);
                    break;
                case 1:
                    md_button.setBackgroundResource(R.color.game2_d_on);
                    mDMediaPlayer = MediaPlayer.create(Game2PlayActivity.this, R.raw.cat);
                    mDMediaPlayer.start();
                    mDMediaPlayer.setOnCompletionListener(mCompletionListener);
                    break;
                case 2:
                    me_button.setBackgroundResource(R.color.game2_e_on);
                    mEMediaPlayer = MediaPlayer.create(Game2PlayActivity.this, R.raw.horse);
                    mEMediaPlayer.start();
                    mEMediaPlayer.setOnCompletionListener(mCompletionListener);
                    break;
                case 3:
                    mf_button.setBackgroundResource(R.color.game2_f_on);
                    mFMediaPlayer = MediaPlayer.create(Game2PlayActivity.this, R.raw.cow);
                    mFMediaPlayer.start();
                    mFMediaPlayer.setOnCompletionListener(mCompletionListener);
                    break;
            }
        }
    }

    private void createSequence(final int i) {
        Random rand = new Random();
        for (int j = 0; j < i-1; j++) {
            final int a = j;
            hand.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playButton(temp.get(a));
                }
            }, 1500*j);
        }
        final int seed = rand.nextInt(4);
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                playButton(seed);
                temp.add(seed);
                mWait = false;
                }
            }, 1500*(i-1));
        Button status = (Button) findViewById(R.id.game2_start_button);
        status.setVisibility(View.GONE);
        final TextView repeat = (TextView) findViewById(R.id.game2_repeat_text);
        repeat.setVisibility(View.VISIBLE);
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                repeat.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
            }
        }, 1500*i);
    }

    private void endGame() {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        TextView title = new TextView(this);
        title.setText(getString(R.string.game2_loss));
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        builder.setMessage(getString(R.string.game2_result) + " " + mScore)
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
                        Intent lost = new Intent(Game2PlayActivity.this, Game2Activity.class);
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
    protected void onPause() {
        super.onPause();
        hand.removeCallbacksAndMessages(null);
        releaseMediaPlayer(mCMediaPlayer);
        releaseMediaPlayer(mDMediaPlayer);
        releaseMediaPlayer(mEMediaPlayer);
        releaseMediaPlayer(mFMediaPlayer);
        mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        hand.removeCallbacksAndMessages(null);
        releaseMediaPlayer(mCMediaPlayer);
        releaseMediaPlayer(mDMediaPlayer);
        releaseMediaPlayer(mEMediaPlayer);
        releaseMediaPlayer(mFMediaPlayer);
        mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2_play);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Button mc_button = (Button) findViewById(R.id.c_button);
        Button md_button = (Button) findViewById(R.id.d_button);
        Button me_button = (Button) findViewById(R.id.e_button);
        Button mf_button = (Button) findViewById(R.id.f_button);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.game2Dark));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.game2Primary));
        }

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.game2Primary)));
        }

        mc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mClickable && !mWait) {
                    playButton(0);
                    int check = temp.get(mChecking);
                    mChecking++;
                    if (check != 0) {
                        endGame();
                    }
                    if (temp.size() == mChecking) {
                        mScore++;
                        TextView score = (TextView) findViewById(R.id.game2_score);
                        score.setText(String.valueOf(mScore));
                        mClickable = false;
                        mChecking = 0;
                        Button start = (Button) findViewById(R.id.game2_start_button);
                        start.setText(R.string.game2_next);
                        start.setVisibility(View.VISIBLE);
                        TextView repeat = (TextView) findViewById(R.id.game2_repeat_text);
                        repeat.setVisibility(View.GONE);
                        final TextView scoreText = (TextView) findViewById(R.id.game2_score_text);
                        scoreText.setText(R.string.game2_success);
                        scoreText.setTextColor(Color.GREEN);
                        hand.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scoreText.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
                            }
                        }, 1500);
                    }
                }
            }
        });

        md_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mClickable && !mWait) {
                    playButton(1);
                    int check = temp.get(mChecking);
                    mChecking++;
                    if (check != 1) {
                        endGame();
                    }
                    if (temp.size() == mChecking) {
                        mScore++;
                        TextView score = (TextView) findViewById(R.id.game2_score);
                        score.setText(String.valueOf(mScore));
                        mClickable = false;
                        mChecking = 0;
                        Button start = (Button) findViewById(R.id.game2_start_button);
                        start.setText(R.string.game2_next);
                        start.setVisibility(View.VISIBLE);
                        TextView repeat = (TextView) findViewById(R.id.game2_repeat_text);
                        repeat.setVisibility(View.GONE);
                        final TextView scoreText = (TextView) findViewById(R.id.game2_score_text);
                        scoreText.setText(R.string.game2_success);
                        scoreText.setTextColor(Color.GREEN);
                        hand.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scoreText.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
                            }
                        }, 1500);
                    }
                }
            }
        });

        me_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mClickable && !mWait) {
                    playButton(2);
                    int check = temp.get(mChecking);
                    mChecking++;
                    if (check != 2) {
                        endGame();
                    }
                    if (temp.size() == mChecking) {
                        mScore++;
                        TextView score = (TextView) findViewById(R.id.game2_score);
                        score.setText(String.valueOf(mScore));
                        mClickable = false;
                        mChecking = 0;
                        Button start = (Button) findViewById(R.id.game2_start_button);
                        start.setText(R.string.game2_next);
                        start.setVisibility(View.VISIBLE);
                        TextView repeat = (TextView) findViewById(R.id.game2_repeat_text);
                        repeat.setVisibility(View.GONE);
                        final TextView scoreText = (TextView) findViewById(R.id.game2_score_text);
                        scoreText.setText(R.string.game2_success);
                        scoreText.setTextColor(Color.GREEN);

                        hand.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scoreText.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
                            }
                        }, 1500);
                    }
                }
            }
        });

        mf_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mClickable && !mWait) {
                    playButton(3);
                    int check = temp.get(mChecking);
                    mChecking++;
                    if (check != 3) {
                        endGame();
                    }
                    if (temp.size() == mChecking) {
                        mScore++;
                        TextView score = (TextView) findViewById(R.id.game2_score);
                        score.setText(String.valueOf(mScore));
                        mClickable = false;
                        mChecking = 0;
                        Button start = (Button) findViewById(R.id.game2_start_button);
                        start.setText(R.string.game2_next);
                        start.setVisibility(View.VISIBLE);
                        TextView repeat = (TextView) findViewById(R.id.game2_repeat_text);
                        repeat.setVisibility(View.GONE);
                        final TextView scoreText = (TextView) findViewById(R.id.game2_score_text);
                        scoreText.setText(R.string.game2_success);
                        scoreText.setTextColor(Color.GREEN);
                        hand.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scoreText.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
                            }
                        }, 1500);
                    }
                }
            }
        });


        Button start_button = (Button) findViewById(R.id.game2_start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mClickable && !mWait) {
                    mWait = true;
                    mClickable = true;
                    createSequence(mScore+1);
                }
            }
        });
    }
}
