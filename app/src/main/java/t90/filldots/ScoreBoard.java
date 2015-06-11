package t90.filldots;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class ScoreBoard extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.scale_up,R.anim.scale_down);
        setContentView(R.layout.activity_score_board);

        addStyles();
        addContents();
    }

    private void addContents() {
        int i, j;
        TableLayout tl = (TableLayout) findViewById(R.id.scoretable);

        SQLiteDatabase db = openOrCreateDatabase("t90.db", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("select * from scores;", null);
        c.moveToFirst();

        if(c.getCount() == 0){
            Toast.makeText(getApplicationContext(), "No data", Toast.LENGTH_SHORT).show();
        }

        for(i=0; i<c.getCount(); i++){
            TableRow tr = new TableRow(this);
            tr.setGravity(Gravity.CENTER_HORIZONTAL);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView date = new TextView(this);
            TextView time = new TextView(this);
            TextView attempts = new TextView(this);

            date.setGravity(Gravity.CENTER_HORIZONTAL);
            time.setGravity(Gravity.CENTER_HORIZONTAL);
            attempts.setGravity(Gravity.CENTER_HORIZONTAL);

            date.setTextColor(Color.WHITE);
            time.setTextColor(Color.WHITE);
            attempts.setTextColor(Color.WHITE);

            date.setTextSize((float)20);
            time.setTextSize((float)20);
            attempts.setTextSize((float)20);

            date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            attempts.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


            date.setText(c.getString(0));
            time.setText(c.getString(1));
            attempts.setText(c.getString(2));

            c.moveToNext();

            tr.addView(date);
            tr.addView(time);
            tr.addView(attempts);

            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private void addStyles() {
        Typeface tf = Typeface.createFromAsset(getAssets(), "BPdots.ttf");
        ((TextView) findViewById(R.id.titletxt)).setTypeface(tf);
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
}
