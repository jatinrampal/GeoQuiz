package com.example.jatin.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;

    //Solutions to the True/False Questions
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0; //Question Index
    private boolean mIsCheater;
    private int updateCounter = 0; //Counter to keep a track of the question loop
    private int score = 0; // Score for the final scoring after a count of six questions




    //A2 Modifications
    private static final String TAG = "QuizActivity"; //Corresponding TAG to check the logs in the Logcat window
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    //A2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //A2
        Log.d(TAG, "onCreate(Bundle) called");
        //A2
        setContentView(R.layout.activity_quiz);

        //A2
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        //A2

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                mCheatButton = (Button)findViewById(R.id.cheat_button);
                mCheatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    // Start CheatActivity
                        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                        Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                        startActivityForResult(intent, REQUEST_CODE_CHEAT);
                    }
                });

         if(updateCounter < 6) {
             updateQuestion();
             updateCounter++;
         }
         else
         {
             mNextButton.setEnabled(false);
        }
            }
        });

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                if(mCurrentIndex < 0)
                {
                    mCurrentIndex = 0;
                }
                if(updateCounter < 6) {
                    updateQuestion();
                    updateCounter++;
                }
                else
                {
                    mNextButton.setEnabled(false);
                }
            }
        });
        if(updateCounter < 6) {
            updateQuestion();
            updateCounter++;
        }
        else
        {
            mNextButton.setEnabled(false);
        }

    }

    //A2

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");

    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean("Did Cheat",mIsCheater);
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    //A2

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
        Log.d("QuizActivity", String.valueOf(updateCounter));
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();


        if(updateCounter == 6)
        {
            Toast.makeText(QuizActivity.this, "Your score is: " + String.valueOf((score/6.00) * 100.00) , Toast.LENGTH_SHORT).show();
        }

        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);


    }
}
