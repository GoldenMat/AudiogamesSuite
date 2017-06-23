package it.unimi.di.lim.audiogames;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Game3PlayActivity extends AppCompatActivity {

    private ArrayList<Question> test = new ArrayList<>();
    private int mCorrect = -1;
    private int mScore = 0;
    private int mCount = 0;

    private TextToSpeech t1;

    private void getQuestion() {
        Random rand = new Random();
        int seed = rand.nextInt(test.size());
        Question quest = test.remove(seed);
        final String a1 = quest.getAnswer1();
        final String a2 = quest.getAnswer2();
        final String a3 = quest.getAnswer3();
        final String a4 = quest.getAnswer4();

        TextView question = (TextView) findViewById(R.id.game3_question);
        question.setText(quest.getQuestion());
        TextView answer1 = (TextView) findViewById(R.id.game3_answer_1);
        answer1.setText(a1);
        answer1.setContentDescription(getString(R.string.game3_answer_1));
        TextView answer2 = (TextView) findViewById(R.id.game3_answer_2);
        answer2.setText(a2);
        answer2.setContentDescription(getString(R.string.game3_answer_2));
        TextView answer3 = (TextView) findViewById(R.id.game3_answer_3);
        answer3.setText(a3);
        answer3.setContentDescription(getString(R.string.game3_answer_3));
        TextView answer4 = (TextView) findViewById(R.id.game3_answer_4);
        answer4.setText(a4);
        answer4.setContentDescription(getString(R.string.game3_answer_4));
        mCorrect = quest.getCorrect();

        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak(a1, TextToSpeech.QUEUE_ADD, null);
            }
        });
        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak(a2, TextToSpeech.QUEUE_ADD, null);
            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak(a3, TextToSpeech.QUEUE_ADD, null);
            }
        });
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak(a4, TextToSpeech.QUEUE_ADD, null);
            }
        });
    }

    private void answerQuestion() {
        RadioButton radio1 = (RadioButton) findViewById(R.id.game3_pick_1);
        RadioButton radio2 = (RadioButton) findViewById(R.id.game3_pick_2);
        RadioButton radio3 = (RadioButton) findViewById(R.id.game3_pick_3);
        RadioButton radio4 = (RadioButton) findViewById(R.id.game3_pick_4);

        int index = 0;
        if(radio1.isChecked()) {
            index = 1;
            radio1.setChecked(false);
        }
        if(radio2.isChecked()) {
            index = 2;
            radio2.setChecked(false);
        }
        if(radio3.isChecked()) {
            index = 3;
            radio3.setChecked(false);
        }
        if(radio4.isChecked()) {
            index = 4;
            radio4.setChecked(false);
        }

        if(index == mCorrect) {
            mScore++;
            TextView score = (TextView) findViewById(R.id.game3_score);
            score.setText(String.valueOf(mScore));

            TextView result = (TextView) findViewById(R.id.game3_result);
            result.setText(R.string.game3_result_good);
            result.setTextColor(Color.GREEN);
            result.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
        }
        else {
            TextView result = (TextView) findViewById(R.id.game3_result);
            result.setText(R.string.game3_result_bad);
            result.setTextColor(Color.RED);
            result.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
        }
        mCount++;
        if(mCount == 8) {
            endGame();
        }
        else {
            getQuestion();
        }
    }

    private void endGame() {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        TextView title = new TextView(this);
        title.setText(getString(R.string.game3_end));
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        builder.setMessage(getString(R.string.game3_result) + " " + mScore + "/8")
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
                        Intent lost = new Intent(Game3PlayActivity.this, Game3Activity.class);
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
        setContentView(R.layout.activity_game3_play);

        final RadioButton radio1 = (RadioButton) findViewById(R.id.game3_pick_1);
        final RadioButton radio2 = (RadioButton) findViewById(R.id.game3_pick_2);
        final RadioButton radio3 = (RadioButton) findViewById(R.id.game3_pick_3);
        final RadioButton radio4 = (RadioButton) findViewById(R.id.game3_pick_4);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.game3Dark));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.game3Primary));
        }

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.game3Primary)));
        }

        String lang = getIntent().getStringExtra("lang");
        if(lang.equals("es")) {

            t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR) {
                        t1.setLanguage(new Locale("es", "ES"));
                    }
                }
            });

            String[] questions = getResources().getStringArray(R.array.game3_es_questions);
            String[] answer1 = getResources().getStringArray(R.array.game3_es_answer1);
            String[] answer2 = getResources().getStringArray(R.array.game3_es_answer2);
            String[] answer3 = getResources().getStringArray(R.array.game3_es_answer3);
            String[] answer4 = getResources().getStringArray(R.array.game3_es_answer4);
            String[] correctTemp = getResources().getStringArray(R.array.game3_es_correct);
            int[] correct = new int[correctTemp.length];
            for(int i=0; i<correctTemp.length; i++) {
                correct[i] = Integer.parseInt(correctTemp[i]);
            }
            for(int i=0; i<questions.length; i++) {
                Question q = new Question(questions[i], answer1[i], answer2[i], answer3[i], answer4[i], correct[i]);
                test.add(q);
            }
        } else {

            t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR) {
                        if(Locale.getDefault().getLanguage().equals("it"))
                            t1.setLanguage(Locale.UK);
                        else
                            t1.setLanguage(Locale.ITALIAN);
                    }
                }
            });

            String[] questions = getResources().getStringArray(R.array.game3_it_questions);
            String[] answer1 = getResources().getStringArray(R.array.game3_it_answer1);
            String[] answer2 = getResources().getStringArray(R.array.game3_it_answer2);
            String[] answer3 = getResources().getStringArray(R.array.game3_it_answer3);
            String[] answer4 = getResources().getStringArray(R.array.game3_it_answer4);
            String[] correctTemp = getResources().getStringArray(R.array.game3_it_correct);
            int[] correct = new int[correctTemp.length];
            for(int i=0; i<correctTemp.length; i++) {
                correct[i] = Integer.parseInt(correctTemp[i]);
            }
            for(int i=0; i<questions.length; i++) {
                Question q = new Question(questions[i], answer1[i], answer2[i], answer3[i], answer4[i], correct[i]);
                test.add(q);
            }
        }

        getQuestion();

        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio1.setChecked(true);
                radio2.setChecked(false);
                radio3.setChecked(false);
                radio4.setChecked(false);
            }
        });
        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio1.setChecked(false);
                radio2.setChecked(true);
                radio3.setChecked(false);
                radio4.setChecked(false);
            }
        });
        radio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio1.setChecked(false);
                radio2.setChecked(false);
                radio3.setChecked(true);
                radio4.setChecked(false);
            }
        });
        radio4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio1.setChecked(false);
                radio2.setChecked(false);
                radio3.setChecked(false);
                radio4.setChecked(true);
            }
        });

        Button go = (Button) findViewById(R.id.game3_answer);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radio1.isChecked() || radio2.isChecked() || radio3.isChecked() || radio4.isChecked())
                    answerQuestion();
                else {
                    Toast.makeText(Game3PlayActivity.this, R.string.game3_please, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        if(t1 != null)
            t1.shutdown();
        super.onStop();
    }

    @Override
    protected void onPause() {
        if(t1 != null)
            t1.shutdown();
        super.onPause();
    }
}
