package ether.shop.meditate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nandpa on 1/9/17.
 */
public class DropCountActivity extends AppCompatActivity {

    @Bind(R.id.dropCountBtn)
    Button dropCountButton;

    @Bind(R.id.dropCountResult)
    TextView dropCountResult;

    @Bind(R.id.dropCount)
    TextView dropCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drop_count);
        ButterKnife.bind(this);
        System.out.println("DROPS ARE $$$$$$$$$$$$$$$$$$$$$" + getIntent().getExtras().get("drops"));

    }


    @OnClick(R.id.dropCountBtn)
    public void dropCountMethod() {
        final int dropCountByUser = Integer.parseInt(dropCount.getText().toString());
        final int actualDropCount = Integer.parseInt(getIntent().getExtras().get("drops").toString());

            dropCountButton.setEnabled(false);
            dropCount.setEnabled(false);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dropCountByUser == actualDropCount) {
                        dropCountResult.setText("Good Work!");
                    } else {
                        dropCountResult.setText("uh oh! Please try Again");
                    }
                }
            });
    }

    @OnClick(R.id.back_button)
    public void clickBackButton() {
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

}
