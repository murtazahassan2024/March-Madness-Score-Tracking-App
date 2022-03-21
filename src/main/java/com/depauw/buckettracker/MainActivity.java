package com.depauw.buckettracker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import com.depauw.buckettracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int DEFAULT_NUM_MINS = 20;
    private static final int MILLIS_PER_MIN = 60000;
    private static final int MILLIS_PER_SEC = 1000;
    private static final int SECS_PER_MIN = 60;
    private static final int COUNTDOWN_START = 0;
    private long timeRemaining = DEFAULT_NUM_MINS * MILLIS_PER_MIN;
    private CountDownTimer countDownTimer;

    public void changeHomeSetting()
    {
        binding.labelGuest.setTextColor(getResources().getColor(R.color.red, getTheme()));
        binding.textviewGuestScore.setTextColor(getResources().getColor(R.color.red, getTheme()));
        binding.labelHome.setTextColor(getResources().getColor(R.color.black, getTheme()));
        binding.textviewHomeScore.setTextColor(getResources().getColor(R.color.black, getTheme()));
    }

    public void changeGuestSetting()
    {
        binding.labelGuest.setTextColor(getResources().getColor(R.color.black, getTheme()));
        binding.textviewGuestScore.setTextColor(getResources().getColor(R.color.black, getTheme()));
        binding.labelHome.setTextColor(getResources().getColor(R.color.red, getTheme()));
        binding.textviewHomeScore.setTextColor(getResources().getColor(R.color.red, getTheme()));
    }

    private View.OnClickListener toggle_is_guest_ClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if (binding.toggleIsGuest.isChecked())
            {
                changeHomeSetting();
            }
            else
            {
                changeGuestSetting();
            }
        }
    };

    private View.OnLongClickListener button_add_score_LongClickListener = new View.OnLongClickListener()
    {
        @Override
        public boolean onLongClick(View view)
        {
            if(!binding.checkboxAddOne.isChecked() && !binding.checkboxAddTwo.isChecked() && !binding.checkboxAddThree.isChecked())
            {
                return true;
            }
            int score = 0;
            if(binding.checkboxAddOne.isChecked())
            {
                score += 1;
            }
            if (binding.checkboxAddTwo.isChecked())
            {
                score +=2;
            }
            if(binding.checkboxAddThree.isChecked())
            {
                score +=3;
            }

            if(!binding.toggleIsGuest.isChecked())
            {
                int afterScore = score + Integer.valueOf(binding.textviewHomeScore.getText().toString());
                binding.textviewHomeScore.setText(String.valueOf(afterScore));
            }
            else
            {
                int afterScore = score + Integer.valueOf(binding.textviewHomeScore.getText().toString());
                binding.textviewGuestScore.setText(String.valueOf(afterScore));
            }

            binding.checkboxAddOne.setChecked(false);
            binding.checkboxAddTwo.setChecked(false);
            binding.checkboxAddThree.setChecked(false);

            if(binding.toggleIsGuest.isChecked())
            {
                binding.toggleIsGuest.setChecked(false);
                changeHomeSetting();
            }
            else
            {
                binding.toggleIsGuest.setChecked(true);
                changeGuestSetting();
            }
            return true;
        }
    };

    public CountDownTimer getNewTimer(long timerTotalLength, long timerTickLength)
    {
        return new CountDownTimer(timerTotalLength,timerTickLength)
        {
            @Override
            public void onTick(long l)
            {
                long minuteValue = (l/MILLIS_PER_SEC) / SECS_PER_MIN;
                long otherValue = (l/MILLIS_PER_SEC) % SECS_PER_MIN;
                binding.textviewTimeRemaining.setText(String.valueOf(minuteValue) + ":" + String.valueOf(otherValue));
            }

            @Override
            public void onFinish()
            {
                binding.switchGameClock.setChecked(false);
            }
        };
    }

    private View.OnClickListener switch_game_clockClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            if(binding.switchGameClock.isChecked())
            {
                  countDownTimer = getNewTimer(timeRemaining, MILLIS_PER_SEC);
                  countDownTimer.start();
            }
            else
            {
                String[] temp = binding.textviewTimeRemaining.getText().toString().split(":");
                long minutesLeft = Integer.valueOf(temp[0]) * MILLIS_PER_MIN;
                long secondsLeft = Integer.valueOf(temp[1]) * MILLIS_PER_SEC;
                timeRemaining = minutesLeft + secondsLeft;
                countDownTimer.cancel();
            }

            if(binding.toggleIsGuest.isChecked())
            {
                changeGuestSetting();
            }
            else
            {
                changeHomeSetting();
            }
        }
    };

    private View.OnClickListener button_set_time_ClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
             if(!(TextUtils.isEmpty(binding.edittextNumMins.getText().toString()) && TextUtils.isEmpty(binding.edittextNumSecs.getText().toString()))) //To check if the string is null or 0-length.
            {
                String tempTextMinutes = binding.edittextNumMins.getText().toString();
                String tempTextSeconds = binding.edittextNumSecs.getText().toString();
                int minutes = Integer.valueOf(tempTextMinutes);
                int seconds = Integer.valueOf(tempTextSeconds);

                if((minutes >= COUNTDOWN_START && minutes< DEFAULT_NUM_MINS) && (seconds >= COUNTDOWN_START && seconds < SECS_PER_MIN))
                {
                    binding.switchGameClock.setChecked(false);
                    if(countDownTimer != null)
                    {
                        countDownTimer.cancel();
                    }
                    timeRemaining = (minutes * MILLIS_PER_MIN) + (seconds * MILLIS_PER_SEC);
                    long minuteValue = (timeRemaining / MILLIS_PER_SEC) / SECS_PER_MIN;
                    long secondValue = (timeRemaining / MILLIS_PER_SEC) % SECS_PER_MIN;
                    binding.textviewTimeRemaining.setText(minuteValue + ":" + secondValue);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toggleIsGuest.setOnClickListener(toggle_is_guest_ClickListener);
        binding.buttonAddScore.setOnLongClickListener(button_add_score_LongClickListener);
        binding.switchGameClock.setOnClickListener(switch_game_clockClickListener);
        binding.buttonSetTime.setOnClickListener(button_set_time_ClickListener);
    }
}