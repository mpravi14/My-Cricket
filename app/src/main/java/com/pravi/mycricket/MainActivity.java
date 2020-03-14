package com.pravi.mycricket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.CMYKColor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    final static int[] noOfRuns = {1, 2, 3, 4, 6};

    private static final String BATSMAN_NAME = "batsman_name";
    private static final String BATSMAN_SCORE = "batsman_score";
    private static final String BATSMAN_BALLS = "batsman_balls";
    private static final String BATSMAN_WICKET = "batsman_wicket";

    private static int currentBatsmanPos = 0;

    private PopupWindow mPopupWindow;
    private Context mContext;

    private ArrayList<HashMap<String, String>> batsmanDetailsList;

    private ListAdapter listAdapter;

    private ListView batsmanList;
    private View lastView;

    private EditText newBatsmanName;

    private HashMap<String, String> currentBatsman;

    private boolean noBall;
    private TextView overValue;
    private TextView ballValue;

    private Button export;

    private TextView totalRuns;
    private TextView totalWicket;

    private View addNewBatsmanView;
    private View wicketView;

    private ConstraintLayout newBatsmanConstraintLayout;
    private ConstraintLayout wicketTypeConstraintLayout;

    private Button bowled;
    private Button caught;
    private Button runout;
    private Button hitwicket;
    private Button stumped;
    private Button retired;



    int wides = 0, noBalls = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button runPlus = findViewById(R.id.plus);
        Button runMinus = findViewById(R.id.minus);
        Button wide = findViewById(R.id.wide);
        Button buttonNoBall = findViewById(R.id.no_ball);

        Button out = findViewById(R.id.out);
        Button notOut = findViewById(R.id.not_out);

        Button ballPlus = findViewById(R.id.ball_plus);
        Button ballMinus = findViewById(R.id.ball_minus);

        export = findViewById(R.id.export);

        final Spinner spinner = findViewById(R.id.spinner);

        overValue = findViewById(R.id.over_value);
        ballValue = findViewById(R.id.ball_value);

        batsmanList = findViewById(R.id.batsman_listview);

        totalRuns = findViewById(R.id.score_text);
        totalWicket = findViewById(R.id.wicket_value);

        Button addNewBatsman = (Button) findViewById(R.id.add_new_batsman);

        mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        addNewBatsmanView = inflater.inflate(R.layout.add_new_batsman, null);
        wicketView = inflater.inflate(R.layout.types_of_wicket, null);
        newBatsmanName = (EditText) addNewBatsmanView.findViewById(R.id.text_batsman_name);

        newBatsmanConstraintLayout = (ConstraintLayout) findViewById(R.id.constraint_batting_add_new);
        wicketTypeConstraintLayout = (ConstraintLayout) findViewById(R.id.constraint_wicket);

        Button addNewBatsmanOk = (Button) addNewBatsmanView.findViewById(R.id.add_new_batsman_ok);
        batsmanDetailsList = new ArrayList<HashMap<String, String>>();

        listAdapter = new SimpleAdapter(MainActivity.this, batsmanDetailsList,
                R.layout.batsman_list_fields, new String[]{BATSMAN_NAME, BATSMAN_SCORE, BATSMAN_BALLS},
                new int[]{R.id.batsman_name, R.id.batsman_runs, R.id.batsman_ball});

        batsmanList.setAdapter(listAdapter);

        caught = wicketView.findViewById(R.id.wicket_caught);
        stumped = wicketView.findViewById(R.id.wicket_stumped);
        bowled = wicketView.findViewById(R.id.wicket_bowled);
        runout = wicketView.findViewById(R.id.wicket_runout);
        hitwicket = wicketView.findViewById(R.id.wicket_hitwicket);
        retired = wicketView.findViewById(R.id.wicket_retired);


        runPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int run = noOfRuns[spinner.getSelectedItemPosition()];
                int tRuns = Integer.parseInt(totalRuns.getText().toString());
                int total = run + tRuns;
                totalRuns.setText(String.valueOf(total));

                addBatsmanRun(run);
                if (noBall) {
                    noBall = false;
                } else {
                    addTeamBall();
                }
                addBatsmanBall();

            }
        });

        runMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int run = noOfRuns[spinner.getSelectedItemPosition()];
                int tRuns = Integer.parseInt(totalRuns.getText().toString());
                int total = tRuns - run;
                totalRuns.setText(String.valueOf(total));

                minusBatsmanRun(run);
                if (noBall) {
                    noBall = false;
                } else {
                    minusTeamBall();
                }
                minusBatsmanBall();
            }
        });


        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPopupWindow = new PopupWindow(
                        wicketView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT, true
                );
                mPopupWindow.showAtLocation(wicketTypeConstraintLayout, Gravity.CENTER, 0, 0);

            }
        });

        notOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int wic = Integer.parseInt(totalWicket.getText().toString());
                totalWicket.setText(String.valueOf(--wic));

                currentBatsman.put(BATSMAN_WICKET, getResources().getString(R.string.not_out));
                batsmanDetailsList.remove(currentBatsmanPos);
                batsmanDetailsList.add(currentBatsmanPos, currentBatsman);

                resetListAdapter();
            }
        });

        ballPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noBall) {
                    noBall = false;
                } else {
                    addTeamBall();
                }
                addBatsmanBall();
            }
        });

        ballMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noBall) {
                    noBall = false;
                } else {
                    minusTeamBall();
                }
                minusBatsmanBall();
            }
        });

        addNewBatsman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPopupWindow = new PopupWindow(
                        addNewBatsmanView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT, true
                );
                newBatsmanName.getText().clear();
                mPopupWindow.showAtLocation(newBatsmanConstraintLayout, Gravity.CENTER, 0, 0);
            }
        });

        addNewBatsmanOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();
                map.put(BATSMAN_NAME, newBatsmanName.getText().toString());
                map.put(BATSMAN_SCORE, "0");
                map.put(BATSMAN_BALLS, "0");
                map.put(BATSMAN_WICKET, "Not Out");
                batsmanDetailsList.add(map);
                mPopupWindow.dismiss();

                resetListAdapter();
            }
        });

        batsmanList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentBatsmanPos = position;
                view.setBackgroundColor(Color.GRAY);
                if (lastView != null) {
                    lastView.setBackgroundColor(Color.WHITE);
                }
                lastView = view;
                currentBatsman = (HashMap<String, String>) parent.getItemAtPosition(position);

            }
        });

        wide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tRuns = Integer.parseInt(totalRuns.getText().toString());
                ++tRuns;
                ++wides;
                totalRuns.setText(String.valueOf(tRuns));
            }
        });

        buttonNoBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noBall = true;
                ++noBalls;
            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                try {
                    generatePDF();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        caught.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWicket("Bowled");
            }
        });

        runout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWicket("Run Out");
            }
        });

        hitwicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWicket("Hit Wicket");
            }
        });

        retired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWicket("Retired");
            }
        });

        stumped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWicket("Stumped");
            }
        });

        bowled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWicket("Bowled");
            }
        });


    }

    private void resetListAdapter() {
        listAdapter = new SimpleAdapter(MainActivity.this, batsmanDetailsList,
                R.layout.batsman_list_fields, new String[]{BATSMAN_NAME, BATSMAN_SCORE, BATSMAN_BALLS, BATSMAN_WICKET},
                new int[]{R.id.batsman_name, R.id.batsman_runs, R.id.batsman_ball, R.id.batsman_wicket});
        batsmanList.setAdapter(listAdapter);
    }

    private void addBatsmanRun(int run) {
        int batsmanRun = Integer.parseInt(currentBatsman.get(BATSMAN_SCORE));
        batsmanRun += run;
        currentBatsman.put(BATSMAN_SCORE, String.valueOf(batsmanRun));
        batsmanDetailsList.remove(currentBatsmanPos);
        batsmanDetailsList.add(currentBatsmanPos, currentBatsman);

        resetListAdapter();
    }

    private void minusBatsmanRun(int run) {
        int batsmanRun = Integer.parseInt(currentBatsman.get(BATSMAN_SCORE));
        batsmanRun -= run;
        currentBatsman.put(BATSMAN_SCORE, String.valueOf(batsmanRun));
        batsmanDetailsList.remove(currentBatsmanPos);
        batsmanDetailsList.add(currentBatsmanPos, currentBatsman);

        resetListAdapter();
    }

    private void addBatsmanBall() {
        int batsmanBalls = Integer.parseInt(currentBatsman.get(BATSMAN_BALLS));
        ++batsmanBalls;
        currentBatsman.put(BATSMAN_BALLS, String.valueOf(batsmanBalls));
        batsmanDetailsList.remove(currentBatsmanPos);
        batsmanDetailsList.add(currentBatsmanPos, currentBatsman);

        resetListAdapter();
    }

    private void minusBatsmanBall() {
        int batsmanBalls = Integer.parseInt(currentBatsman.get(BATSMAN_BALLS));
        --batsmanBalls;
        currentBatsman.put(BATSMAN_BALLS, String.valueOf(batsmanBalls));
        batsmanDetailsList.remove(currentBatsmanPos);
        batsmanDetailsList.add(currentBatsmanPos, currentBatsman);

        resetListAdapter();
    }

    private void addTeamBall() {
        int ball = Integer.parseInt(ballValue.getText().toString()) + 1;
        if (ball == 6) {
            int over = Integer.parseInt(overValue.getText().toString());
            ++over;
            ball = 0;
            overValue.setText(String.valueOf(over));
        }
        ballValue.setText(String.valueOf(ball));
    }

    private void minusTeamBall() {
        int ball = Integer.parseInt(ballValue.getText().toString()) - 1;
        int over = Integer.parseInt(overValue.getText().toString());
        if (ball < 0) {
            --over;
            ball = 5;
            overValue.setText(String.valueOf(over));
        }
        ballValue.setText(String.valueOf(ball));
    }

    private void updateWicket(String value){
        int wic = Integer.parseInt(totalWicket.getText().toString());
        totalWicket.setText(String.valueOf(++wic));

        currentBatsman.put(BATSMAN_WICKET, value);
        batsmanDetailsList.remove(currentBatsmanPos);
        batsmanDetailsList.add(currentBatsmanPos, currentBatsman);

        resetListAdapter();
    }

    private void generatePDF() throws IOException {
        File file = null;
        try {

            file = new File(Environment
                    .getExternalStorageDirectory().getAbsolutePath()
                    + "/MyCricket");
            if (!file.exists()) {
                file.mkdir();
            }

            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(file
                    + "/Match" + getDateTime() + ".pdf"));
            Document doc = new Document(pdfDoc, new PageSize(595, 842));
            doc.setMargins(0, 0, 0, 0);
            Font boldFont = FontFactory.getFont(
                    FontFactory.TIMES_ITALIC, 16, Font.BOLD,
                    new CMYKColor(255, 255, 255, 255));
            Paragraph teamScorecard = new Paragraph().add("Team Scorecard");
            teamScorecard.setHorizontalAlignment(HorizontalAlignment.CENTER);
            doc.add(teamScorecard);


            Table table = new Table(3);
            table.setMarginTop(0);
            table.setMarginBottom(0);


            // first row
          /*  Cell runs = new Cell(1,2).add("Runs");
            runs.setTextAlignment(TextAlignment.CENTER);
            table.addCell(runs);*/

            table.addCell("Runs");
            table.addCell("Overs");
            table.addCell("Wickets");

            table.addCell(totalRuns.getText().toString());
            table.addCell(overValue.getText().toString() + "." + ballValue.getText().toString());
            table.addCell(totalWicket.getText().toString());

            doc.add(table);


            Paragraph batsmanScorecard = new Paragraph().add("Batsman Scorecard");
            batsmanScorecard.setHorizontalAlignment(HorizontalAlignment.CENTER);
            doc.add(batsmanScorecard);

            Table table2 = new Table(4);
            table2.setMarginTop(0);
            table2.setMarginBottom(0);

            table2.addCell("Batsman");
            table2.addCell("Runs");
            table2.addCell("Balls");
            table2.addCell("Wicket");

            for (int i = 0; i < listAdapter.getCount(); i++) {
                HashMap<String, String> batsman = (HashMap<String, String>) listAdapter.getItem(i);
                table2.addCell(batsman.get(BATSMAN_NAME));
                table2.addCell(batsman.get(BATSMAN_SCORE));
                table2.addCell(batsman.get(BATSMAN_BALLS));
                table2.addCell(batsman.get(BATSMAN_WICKET));
            }

            doc.add(table2);

            Paragraph extras = new Paragraph().add("Extras");
            doc.add(extras);

            Table table3 = new Table(2);
            table3.setMarginTop(0);
            table3.setMarginBottom(0);

            table3.addCell("Wide");
            table3.addCell("No Ball");

            table3.addCell(String.valueOf(wides));
            table3.addCell(String.valueOf(noBalls));

            doc.add(table3);

            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDateTime() {
        Date date = new Date();
        int day = date.getDate();
        int month = date.getMonth() + 1;
        int year = date.getYear() + 1900;
        int hour = date.getHours();
        int min = date.getMinutes();
        int sec = date.getSeconds();
        StringBuilder sb = new StringBuilder();
        sb.append(day).append(month).append(year).append(hour).append(min).append(sec);
        return sb.toString();
    }

}
