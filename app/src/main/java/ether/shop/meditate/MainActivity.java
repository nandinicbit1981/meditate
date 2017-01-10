package ether.shop.meditate;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    public int seconds = 60;
    public int minutes = 10;

    @Bind(R.id.minutes)
    TextView minutesText;

    @Bind(R.id.seconds)
    TextView secondsText;

    MediaPlayer omPlayer;
    MediaPlayer waterDropPlayer;
    int waterDropFrequency = 0;
    Spinner minutesSpinner;

    int minutesFromDD = 0;
    Timer dropTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);
        ButterKnife.bind(this);
        int omSoundID = getResources().getIdentifier("om", "raw", getPackageName());
        int dropSoundID = getResources().getIdentifier("water_droplet", "raw", getPackageName());

        omPlayer = MediaPlayer.create(this,omSoundID);
        omPlayer.setLooping(true);

        waterDropPlayer = MediaPlayer.create(this, dropSoundID);
        minutesSpinner = (Spinner) findViewById(R.id.med_minutes);
        minutesSpinner.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        minutes = Integer.parseInt(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyClass extends TimerTask {

        @Override
        public void run() {

            if(!waterDropPlayer.isPlaying()) {
                waterDropPlayer.start();
                System.out.println("$$$$$$$$$$$$$$$$$$$$$ Drops $$$$$$$$$$$$$$$$$$$$$$$$" + waterDropFrequency);
                waterDropFrequency++;
            } else {
                waterDropPlayer.pause();
            }

            dropTimer.cancel();
            dropTimer.purge();


            dropTimer = new Timer();
            int i1 = (15 + new Random().nextInt(15))* 1000;
            dropTimer.schedule(new MyClass(),i1);

        }
    }


    @OnClick(R.id.meditateBtn)
    public void meditateBtnClicked() {
            MyClass newTimer = new MyClass();
            newTimer.run();
            System.out.println("***************************************************");
            //Declare the timer
            final Timer t = new Timer();
//            if(minutesText != null && secondsText != null) {
//                seconds = Integer.parseInt(secondsText.getText().toString());
//                minutes = Integer.parseInt(minutesText.getText().toString());
//            }
            //Set the schedule function and rate
            t.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            secondsText.setText(String.valueOf(seconds));
                            if(!omPlayer.isPlaying()) {
                                omPlayer.start();
                            }
                            if(seconds != 0 ){
                                seconds -= 1;
                            } else
                            {
                                seconds=60;
                                if(minutesFromDD > 0) {
                                    minutesFromDD = minutesFromDD - 1;
                                    minutesText.setText(String.valueOf(minutesFromDD));
                                } else {
                                    omPlayer.stop();
                                    waterDropPlayer.stop();
                                    t.cancel();
                                    t.purge();
                                    dropTimer.cancel();
                                    dropTimer.purge();
                                    Intent intent = new Intent(MainActivity.this, DropCountActivity.class);
                                    Bundle sendBundle = new Bundle();
                                    sendBundle.putLong("drops", waterDropFrequency);
                                    intent.putExtras(sendBundle);

                                    startActivity(intent);
                                    finish();
                                }
                            }

                        }

                    });
                }

            }, 0, 1000);
    }
}

