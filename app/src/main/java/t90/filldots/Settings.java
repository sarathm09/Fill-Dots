package t90.filldots;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;

import java.io.File;


public class Settings extends Activity {

    SQLiteDatabase db;
    SharedPreferences pref;
    int lev, sublev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.scale_up,R.anim.scale_down);
        setContentView(R.layout.activity_settings);

        db = openOrCreateDatabase("t90.db", MODE_PRIVATE, null);

        initui();
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

    private void initui() {
        getCurLev();
        applyStyles();
        addListners();
    }

    private void addListners() {
        ((Button) findViewById(R.id.reset_game)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sublev=0;
                db.execSQL("update settings set value='" + String.valueOf(lev) + "' where key='level';");
                db.execSQL("update settings set value='" + String.valueOf(sublev) + "' where key='sublevel';");

                SharedPreferences prefs = getSharedPreferences("installprefs", MODE_PRIVATE);
                prefs.edit().putString("level", String.valueOf(lev+"."+sublev)).commit();
                db.execSQL("update t_apk set gp='"+HashGeneratorUtils.generateMD5(new File("/data/data/t90.filldots/shared_prefs/installprefs.xml"))+"';");
                AppMsg.makeText(Settings.this, "Game reset to " + lev+"."+sublev, AppMsg.STYLE_INFO).show();
                getCurLev();
            }
        });
        ((Button) findViewById(R.id.jump_level)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, resetTo.class));
                getCurLev();
            }
        });
    }

    private void applyStyles() {
        Typeface tf = Typeface.createFromAsset(getAssets(), "BPdots.ttf");
        ((TextView) findViewById(R.id.set_title)).setTypeface(tf);
        int w = getResources().getDisplayMetrics().widthPixels;
        if(w<1500){
            ((TextView) findViewById(R.id.set_title)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 60);
        }
        else if(w<900){
            ((TextView) findViewById(R.id.set_title)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
        }
    }

    public void getCurLev() {
        Cursor resSet = db.rawQuery("select value from settings where key='level';", null);
        resSet.moveToFirst();
        lev = Integer.parseInt(resSet.getString(0));
        resSet = db.rawQuery("select value from settings where key='sublevel';", null);
        resSet.moveToFirst();
        sublev = Integer.parseInt(resSet.getString(0));
        ((TextView) findViewById(R.id.cur_level)).setText("Current Level: " + lev + "." + sublev);
    }
}
