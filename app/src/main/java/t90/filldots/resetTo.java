package t90.filldots;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.devspark.appmsg.AppMsg;

import java.io.File;


public class resetTo extends Activity {

    SQLiteDatabase db;
    int lev, sublev;
    SharedPreferences prefs;
    Button r0, r1, r2, r3, r4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.scale_up,R.anim.scale_down);
        setContentView(R.layout.activity_reset_to);
        initUi();
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

    private void initUi() {

        r0 = (Button)findViewById(R.id.resetto0);
        r1 = (Button)findViewById(R.id.resetto1);
        r2 = (Button)findViewById(R.id.resetto2);
        r3 = (Button)findViewById(R.id.resetto3);
        r4 = (Button)findViewById(R.id.resetto4);

        db = openOrCreateDatabase("t90.db", MODE_PRIVATE, null);
        Cursor resSet = db.rawQuery("select value from settings where key='level';", null);
        resSet.moveToFirst();
        lev = Integer.parseInt(resSet.getString(0));

        if(lev==0){
            r1.setClickable(false);
            r1.setTextColor(Color.GRAY);

            r2.setClickable(false);
            r2.setTextColor(Color.GRAY);

            r3.setClickable(false);
            r3.setTextColor(Color.GRAY);

            r4.setClickable(false);
            r4.setTextColor(Color.GRAY);


        }
        else if(lev==1){
            r2.setClickable(false);
            r2.setTextColor(Color.GRAY);

            r3.setClickable(false);
            r3.setTextColor(Color.GRAY);

            r4.setClickable(false);
            r4.setTextColor(Color.GRAY);

        }
        else if(lev==2){
            r3.setClickable(false);
            r3.setTextColor(Color.GRAY);

            r4.setClickable(false);
            r4.setTextColor(Color.GRAY);

        }
        else if(lev==3){
            r4.setClickable(false);
            r4.setTextColor(Color.GRAY);

        }
    }

    public void resetClick(View v){
        switch (v.getId()){
            case R.id.resetto0:
                lev=0;
                db.execSQL("update settings set value='0' where key='level';");
                sublev=0;
                db.execSQL("update settings set value='" + String.valueOf(lev) + "' where key='level';");
                db.execSQL("update settings set value='" + String.valueOf(sublev) + "' where key='sublevel';");

                prefs = getSharedPreferences("installprefs", MODE_PRIVATE);
                prefs.edit().putString("level", String.valueOf(lev+"."+sublev)).commit();
                db.execSQL("update t_apk set gp='"+HashGeneratorUtils.generateMD5(new File("/data/data/t90.filldots/shared_prefs/installprefs.xml"))+"';");
                break;
            case R.id.resetto1:
                lev = 1;
                sublev=0;
                db.execSQL("update settings set value='" + String.valueOf(lev) + "' where key='level';");
                db.execSQL("update settings set value='" + String.valueOf(sublev) + "' where key='sublevel';");

                prefs = getSharedPreferences("installprefs", MODE_PRIVATE);
                prefs.edit().putString("level", String.valueOf(lev+"."+sublev)).commit();
                db.execSQL("update t_apk set gp='"+HashGeneratorUtils.generateMD5(new File("/data/data/t90.filldots/shared_prefs/installprefs.xml"))+"';");
                break;
            case R.id.resetto2:
                lev = 2;
                sublev=0;
                db.execSQL("update settings set value='" + String.valueOf(lev) + "' where key='level';");
                db.execSQL("update settings set value='" + String.valueOf(sublev) + "' where key='sublevel';");

                prefs = getSharedPreferences("installprefs", MODE_PRIVATE);
                prefs.edit().putString("level", String.valueOf(lev+"."+sublev)).commit();
                db.execSQL("update t_apk set gp='"+HashGeneratorUtils.generateMD5(new File("/data/data/t90.filldots/shared_prefs/installprefs.xml"))+"';");
                break;
            case R.id.resetto3:
                lev = 3;
                sublev=0;
                db.execSQL("update settings set value='" + String.valueOf(lev) + "' where key='level';");
                db.execSQL("update settings set value='" + String.valueOf(sublev) + "' where key='sublevel';");

                prefs = getSharedPreferences("installprefs", MODE_PRIVATE);
                prefs.edit().putString("level", String.valueOf(lev+"."+sublev)).commit();
                db.execSQL("update t_apk set gp='"+HashGeneratorUtils.generateMD5(new File("/data/data/t90.filldots/shared_prefs/installprefs.xml"))+"';");
                break;
            case R.id.resetto4:
                lev = 4;
                sublev=0;
                db.execSQL("update settings set value='" + String.valueOf(lev) + "' where key='level';");
                db.execSQL("update settings set value='" + String.valueOf(sublev) + "' where key='sublevel';");

                prefs = getSharedPreferences("installprefs", MODE_PRIVATE);
                prefs.edit().putString("level", String.valueOf(lev+"."+sublev)).commit();
                db.execSQL("update t_apk set gp='"+HashGeneratorUtils.generateMD5(new File("/data/data/t90.filldots/shared_prefs/installprefs.xml"))+"';");
                break;

        }
        db.execSQL("update settings set value='0' where key='sublevel';");
        AppMsg.makeText(resetTo.this, "Game has been Reset", AppMsg.STYLE_INFO).show();
    }

}
