package com.pravi.mycricket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final static int[] noOfRuns = {1, 2, 3, 4, 6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button runPlus = (Button) findViewById(R.id.plus);
        Button runMinus = (Button) findViewById(R.id.minus);

        Button out = (Button) findViewById(R.id.out);
        Button notOut = (Button) findViewById(R.id.not_out);

        Button ballPlus = (Button) findViewById(R.id.ball_plus);
        Button ballMinus = (Button) findViewById(R.id.ball_minus);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        runPlus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView totalRuns = (TextView) findViewById(R.id.score_text);
                System.out.println(spinner.getSelectedItem().toString());
                int run = noOfRuns[spinner.getSelectedItemPosition()];
                int tRuns = Integer.parseInt(totalRuns.getText().toString());
                int total = run + tRuns;
                totalRuns.setText(String.valueOf(total));
            }
        } );

        runMinus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView totalRuns = (TextView) findViewById(R.id.score_text);
                System.out.println(spinner.getSelectedItem().toString());
                int run = noOfRuns[spinner.getSelectedItemPosition()];
                int tRuns = Integer.parseInt(totalRuns.getText().toString());
                int total = tRuns - run;
                totalRuns.setText(String.valueOf(total));
            }
        } );


        out.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView totalWicket = (TextView) findViewById(R.id.wicket_value);
                int wic = Integer.parseInt(totalWicket.getText().toString());
                totalWicket.setText(String.valueOf(++wic));
            }
        } );

        notOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView totalWicket = (TextView) findViewById(R.id.wicket_value);
                int wic = Integer.parseInt(totalWicket.getText().toString());
                totalWicket.setText(String.valueOf(--wic));
            }
        } );

        ballPlus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView overValue = (TextView) findViewById(R.id.over_value);
                TextView ballValue = (TextView) findViewById(R.id.ball_value);
                int ball = Integer.parseInt(ballValue.getText().toString()) + 1;
                int over = Integer.parseInt(overValue.getText().toString());
                if(ball == 6){
                    ++over;
                    ball = 0;
                    overValue.setText(String.valueOf(over));
                }
                ballValue.setText(String.valueOf(ball));
            }
        } );

        ballMinus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView overValue = (TextView) findViewById(R.id.over_value);
                TextView ballValue = (TextView) findViewById(R.id.ball_value);
                int ball = Integer.parseInt(ballValue.getText().toString()) - 1;
                int over = Integer.parseInt(overValue.getText().toString());
                if(ball < 0){
                    --over;
                    ball = 5;
                    overValue.setText(String.valueOf(over));
                }
                ballValue.setText(String.valueOf(ball));
            }
        } );




    }
}
