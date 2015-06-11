package t90.filldots;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;


public class StartPage extends ActionBarActivity {

    SQLiteDatabase db;
    int[][] question = {
            {33},
            {11, 51, 15, 55},
            {21, 41, 25, 45, 12, 14, 52, 54},
            {11, 22, 33, 44, 55, 15, 24, 42, 51},
            {11, 55, 22, 44, 33, 31, 35, 53, 13},

            {23, 43, 32, 34},
            {21, 45, 51, 15, 33},
            {22, 23, 24, 42, 43, 44, 33, 35, 31, 33},
            {11, 15, 21, 25, 51, 55, 32, 34, 22, 44, 41, 21},
            {11, 15, 51, 55, 33, 31, 35, 13, 53, 22, 24, 44, 42},

            {11, 13, 44, 51, 53, 24},
            {13, 14, 35, 45, 53, 52, 23, 21},
            {13, 14, 35, 45, 53, 52, 23, 21, 22, 24, 42, 44, 33},
            {11, 55, 15, 51, 21, 12, 14, 25, 41, 45, 52, 54, 32, 34, 23, 43},
            {11, 55, 15, 51, 21, 12, 14, 25, 41, 45, 52, 54, 32, 34, 23, 43, 12, 14, 21, 24, 41, 45, 52, 54},

            {11, 23, 24, 32, 42, 52, 35, 44},
            {25, 45, 34, 13, 43, 32, 21, 41},
            {42, 22, 24, 44, 13, 43, 32, 34},
            {21, 25, 11, 55, 13, 52, 41, 35},
            {51, 13, 24, 35, 32, 44, 35, 31},
            {11, 12, 13, 14, 15, 21, 22, 23, 24, 25, 31, 32, 33, 34, 35, 41, 42, 43, 45, 51, 52, 53, 54, 55},

            {11, 22, 33, 44, 55},
            {12, 23, 34, 45, 51, 42},
            {11, 22, 33, 44, 55, 13, 24, 35, 31, 42, 53},
            {25, 15, 55, 45, 31, 35, 42, 33, 31, 13, 25},
            {25, 15, 31, 13, 22, 24, 31, 12, 22},
            {11, 15, 51, 55, 25, 15, 45, 21, 32, 21, 54, 34}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.scale_up,R.anim.scale_down);
        setContentView(R.layout.activity_start_page);

        applyStyles();
        addListners();
        setDB();
   }

    private void setDB() {
        Boolean dbpresent = false;
        dbpresent = DBexixts();
        db = openOrCreateDatabase("t90.db", MODE_PRIVATE, null);
        if (dbpresent){
            verifyDB();
        }
        else{
            initialRun();
        }
    }

    private void verifyDB() {
        Cursor resSet = db.rawQuery("select gp from t_apk;", null);
        resSet.moveToFirst();
        String check = resSet.getString(0);
        String hash = HashGeneratorUtils.generateMD5(new File("/data/data/t90.filldots/shared_prefs/installprefs.xml"));


        if(check.equalsIgnoreCase(hash)){
            SharedPreferences prefs = getSharedPreferences("installprefs", MODE_PRIVATE);
            String chklevel = prefs.getString("level", "");
            resSet = db.rawQuery("select value from settings where key='level';", null);
            resSet.moveToFirst();
            int lev = Integer.parseInt(resSet.getString(0));
            resSet = db.rawQuery("select value from settings where key='sublevel';", null);
            resSet.moveToFirst();
            int sublev = Integer.parseInt(resSet.getString(0));
            String level = lev+"."+sublev;
            Log.d("errorman", level+"::"+chklevel);
            if(!level.equalsIgnoreCase(chklevel)){
                initialRun();
            }

        }
        else{
            initialRun();
        }
    }

    private void initialRun() {
        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.darkblue);
        AppMsg sc = AppMsg.makeText(StartPage.this, "Running Initial Configurations...", style);
        sc.setLayoutGravity(Gravity.BOTTOM);
        sc.show();

        try {
            File database = getApplicationContext().getDatabasePath("t90.db");
            database.delete();
        }catch (Exception e){}
        try {
            File sp = new File("/data/data/t90.filldots/shared_prefs/installprefs.xml");
            sp.delete();
        }catch (Exception e){}

        try {
            db = openOrCreateDatabase("t90.db", MODE_PRIVATE, null);
            db.execSQL("create table scores (date varchar, level varchar, score varchar);");
            db.execSQL("create table settings (key varchar, value varchar);");
            db.execSQL("create table game (level varchar, sublevel varchar, dots varchar);");
            db.execSQL("create table t_apk (gp varchar);");

            db.execSQL("insert into settings values ('level', '0');");
            db.execSQL("insert into settings values ('sublevel', '0');");
            db.execSQL("insert into settings values ('total', '0');");

            for(int i=0; i<5; i++){
                for(int j=0; j<5; j++){
                    String level = String.valueOf(i) + "." + String.valueOf(j);
                    db.execSQL("insert into scores values('nil', " + level + " , '0');");
                }
            }

            int lev=0, sublev=0;
            for(int i=0; i<question.length; i++){
                String temp = "";
                for(int j=0; j<question[i].length; j++){
                    temp += String.valueOf(question[i][j])+",";
                }
                String sql = "insert into game values ('" +String.valueOf(lev)+"','"+String.valueOf(sublev)+"','"+temp+"');";
                db.execSQL(sql);

                sublev++;
                if(sublev==5){
                    sublev = 0;
                    lev++;
                }
            }

            String quest = "";
            for(int i=0;i<question.length; i++){
                for(int j=0; j<question[i].length; j++){
                    quest += String.valueOf(question[i][j]);
                }
            }

            // for security

            SharedPreferences prefs = getSharedPreferences("installprefs", MODE_PRIVATE);
            prefs.edit().putString("level", String.valueOf("0.0")).commit();
            db.execSQL("insert into t_apk values('"+HashGeneratorUtils.generateMD5(new File("/data/data/t90.filldots/shared_prefs/installprefs.xml"))+"');");

            db.close();


            new AlertDialog.Builder(this)
                    .setTitle("First Run")
                    .setMessage("It Seems this is your first time with this app. Wanna learn to play this game?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), Help.class));
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            sc = AppMsg.makeText(StartPage.this, "Done... Ready to Rumble!!!", AppMsg.STYLE_INFO);
            sc.setLayoutGravity(Gravity.BOTTOM);
            sc.show();

        }catch (Exception e){
            e.printStackTrace();
            style = new AppMsg.Style(AppMsg.LENGTH_SHORT, Color.RED);
            sc = AppMsg.makeText(StartPage.this, "ERROR", style);
            sc.setLayoutGravity(Gravity.BOTTOM);
            sc.show();
            new AlertDialog.Builder(this)
                    .setTitle("Error!!!")
                    .setMessage("Oops!, some error has occured in the initial setup. Please restart app.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            StartPage.this.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                    .show();
        }

    }

    private boolean DBexixts() {

        try {
            File database = getApplicationContext().getDatabasePath("t90.db");
            File sp = new File("/data/data/t90.filldots/shared_prefs/installprefs.xml");
            if (!database.exists() || !sp.exists()) {
                try {
                    database.delete();
                }catch (Exception e){}
                try {
                    sp.delete();
                }catch (Exception e){}
                return false;
            } else {
                return true;
            }
        }catch (Exception e){
            return false;
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.scale_up,R.anim.scale_down);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.scale_up,R.anim.scale_down);
    }

    private void addListners() {
        // settings btn
        ((ImageView) findViewById(R.id.settings)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
        });
        // ScoreBoard btn
        ((ImageView) findViewById(R.id.scoreboard)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScoreBoard.class));
            }
        });
        // start btn
        ((ImageView) findViewById(R.id.start)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
            }
        });
        // Help btn
        ((ImageView) findViewById(R.id.help)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help.class));
            }
        });
        // Rate btn
        ((ImageView) findViewById(R.id.rate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" +
                                    getApplicationContext().getPackageName())));
                }
            }
        });
    }

    private void applyStyles() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(getResources().getColor(R.color.darkblue));

        Typeface title = Typeface.createFromAsset(getAssets(), "BPdots.ttf");
        ((TextView)findViewById(R.id.maintitle)).setTypeface(title);

    }

}
