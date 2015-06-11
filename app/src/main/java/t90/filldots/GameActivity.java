package t90.filldots;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;


public class GameActivity extends Activity implements View.OnClickListener{

    static int num_dots = 5;
    static int[][] dot_status = new int[num_dots][num_dots];

    int question[] = new int[25];
    int clickHistory[] = new int[100];

    int lev, sublev, tot, optim;
    int msgIndex = 0;
    int sec = 0, min = 0;

    SQLiteDatabase db = null;

    Handler h, messagehandle, lev5;
    Runnable timeupdater, msgrunner, r_lev5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.scale_up,R.anim.scale_down);
        setContentView(R.layout.activity_game);

        initUi();
        setQuestion();
        startGame();
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
        db = openOrCreateDatabase("t90.db", Context.MODE_PRIVATE, null);
        ((TextView) findViewById(R.id.attemptstxt)).setText("0");
        addStyle();
        makeGrid();
        int l1, l2, l3, l4, l0;
        Cursor c;

        c = db.rawQuery("select sum(score) from scores where level like '0%';", null);
        c.moveToFirst();
        l0 = Integer.parseInt(c.getString(0))/5;

        c = db.rawQuery("select sum(score) from scores where level like '1%';", null);
        c.moveToFirst();
        l1 = Integer.parseInt(c.getString(0))/5;

        c = db.rawQuery("select sum(score) from scores where level like '2%';", null);
        c.moveToFirst();
        l2 = Integer.parseInt(c.getString(0))/5;

        c = db.rawQuery("select sum(score) from scores where level like '3%';", null);
        c.moveToFirst();
        l3 = Integer.parseInt(c.getString(0))/5;

        c = db.rawQuery("select sum(score) from scores where level like '4%';", null);
        c.moveToFirst();
        l4 = Integer.parseInt(c.getString(0))/5;

        final String mesgs[] = {
                "Tap the dots to fill them all with blue.",
                "In most cases symmetry with help you a lot.",
                "Score is calculated based on time and taps.",
                "Try to score 1000 in all levels.",
                "Total Scores(level): \n" + l0 + "(0), " + l1 + "(1), " + l2 + "(2), " + l3 + "(3), " + l4 + "(4)."

        };
        messagehandle = new Handler();
        msgrunner = new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.message)).setText(mesgs[msgIndex]);
                msgIndex = (msgIndex+1)%5;
                messagehandle.postDelayed(msgrunner, 5000);
            }
        };
        messagehandle.postDelayed(msgrunner, 5000);
    }


    private void addStyle() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(getResources().getColor(R.color.darkblue));
        Typeface title = Typeface.createFromAsset(getAssets(), "BPdots.ttf");
        ((TextView) findViewById(R.id.title)).setTypeface(title);
    }


    private void setQuestion() {
        getQuestions();
        int i=0;
        optim = 0;
        while(question[i]!=-1){
            optim ++;
            int id = question[i];
            onClick((FrameLayout) findViewById(id));
            i++;
        }
        if(lev==4){
            lev5 = new Handler();
            r_lev5 = new Runnable() {
                @Override
                public void run() {
                    int r, c, n, t;
                    n = randInt(1, 5);
                    for(int i=0; i<n; i++){
                        r = randInt(1, 5);
                        c = randInt(1, 5);
                        onClick((FrameLayout) findViewById(r*10+c));
                    }
                    t = randInt(10, 30);
                    lev5.postDelayed(r_lev5, t*1000);
                }
            };
            lev5.postDelayed(r_lev5, 10*1000);

        }
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return (rand.nextInt((max - min) + 1) + min);
    }

    private void makeGrid() {
        int id;
        LinearLayout l = (LinearLayout) findViewById(R.id.dots_layout);

        for (int i=0; i<num_dots; i++){
            LinearLayout lr = new LinearLayout(this);
            lr.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            lr.setOrientation(LinearLayout.HORIZONTAL);

            for (int j=0; j<num_dots; j++){
                id = (i+1)*10 + (j+1);

                FrameLayout fc = new FrameLayout(this);
                fc.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                fc.setOnClickListener(this);
                fc.setId(id);

                ImageView im_bot = new ImageView(this);
                im_bot.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                im_bot.setImageResource(R.drawable.b);
                im_bot.setId(id+100);

                ImageView im_top = new ImageView(this);
                im_top.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                im_top.setImageResource(R.drawable.b);
                im_top.setId(id+200);

                fc.addView(im_bot);
                fc.addView(im_top);
                lr.addView(fc);

                dot_status[i][j] = 0;
            }
            l.addView(lr);
        }
    }

    @Override
    public void onClick(View btn) {
        int id = btn.getId();
        int r = (id-11) / 10;
        int c = (id-11) % 10;
        int[][] affected_dots = {{r-1,c}, {r,c+1}, {r+1,c}, {r,c-1}};

        incrementAttempt(id);

        // pressed dot
        if(dot_status[r][c] == 0) { // currently green
            doAnimation(id, 0);
            dot_status[r][c] = 1;
        }
        else if(dot_status[r][c] == 1) { // currently red
            doAnimation(id, 1);
            dot_status[r][c] = 0;
        }

        // surrounding dots
        for(int i=0; i<4; i++){
            if(affected_dots[i][0]>-1 && affected_dots[i][0]<num_dots){ // to avoid edge cases (row)
                if(affected_dots[i][1]>-1 && affected_dots[i][1]<num_dots) { // to avoid edge cases (col)
                    int newid = (affected_dots[i][0]*10) + affected_dots[i][1] + 11;

                    if(dot_status[affected_dots[i][0]][affected_dots[i][1]] == 0) { //Green
                        doAnimation(newid, 0);
                        dot_status[affected_dots[i][0]][affected_dots[i][1]] = 1;
                    }
                    else if(dot_status[affected_dots[i][0]][affected_dots[i][1]] == 1) { //red
                        doAnimation(newid, 1);
                        dot_status[affected_dots[i][0]][affected_dots[i][1]] = 0;
                    }

                }
            }
        }

        if(gameCompleted()){
            endGame();
        }
    }

    public boolean gameCompleted(){
        int i, j;
        for(i=0; i<num_dots; i++){
            for(j=0; j<num_dots; j++){
                if(dot_status[i][j] != 0)
                    return false;
            }
        }
        return true;
    }

    public void incrementAttempt(int id) {
        TextView t = (TextView) findViewById(R.id.attemptstxt);
        int current = Integer.parseInt(String.valueOf(t.getText()));
        t.setText(String.valueOf(current+1));
        clickHistory[current] = id;
    }

    public void startGame() {
        ((TextView) findViewById(R.id.attemptstxt)).setText("0");
        final TextView time = (TextView) findViewById(R.id.timetxt);
        min = sec = 0;
        time.setText("0:0");
        h = new Handler();
        timeupdater = new Runnable() {
            @Override
            public void run() {
                sec++;
                if(sec == 60){
                    sec = 0;
                    min++;
                }
                time.setText(min + ":" + sec);
                h.postDelayed(timeupdater, 1000);
            }
        };
        h.postDelayed(timeupdater, 1000);
    }

    public void endGame() {
        h.removeCallbacks(timeupdater);
        String score = String.valueOf(calculateScore());
        String level = String.valueOf(lev) + "." + String.valueOf(sublev);

        AppMsg.Style sty;
        if(Integer.parseInt(score)>995) sty = AppMsg.STYLE_INFO;
        else if(Integer.parseInt(score)>949) sty = AppMsg.STYLE_CONFIRM;
        else if(Integer.parseInt(score)>495) sty = AppMsg.STYLE_ALERT;
        else sty = new AppMsg.Style(AppMsg.LENGTH_SHORT, Color.LTGRAY);

        AppMsg sc = AppMsg.makeText(GameActivity.this, "Score : " + score, sty);
        sc.setLayoutGravity(Gravity.BOTTOM);
        sc.show();

        sublev++;
        if(sublev == 5){
            sublev = 0;
            lev++;
        }

        db.execSQL("update settings set value='" + String.valueOf(lev) + "' where key='level';");
        db.execSQL("update settings set value='" + String.valueOf(sublev) + "' where key='sublevel';");
        db.execSQL("update settings set value='" + String.valueOf(sublev) + "' where key='total';");

        SharedPreferences prefs = getSharedPreferences("installprefs", MODE_PRIVATE);
        prefs.edit().putString("level", String.valueOf(lev+"."+sublev)).commit();
        db.execSQL("update t_apk set gp='"+HashGeneratorUtils.generateMD5(new File("/data/data/t90.filldots/shared_prefs/installprefs.xml"))+"';");

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        String month = c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
        String date = String.valueOf(day)+ "-" + month + " | " + String.valueOf(hour) + ":" + String.valueOf(min);

        db.execSQL("update scores set date='" + date + "', score='" + score + "' where level='" + level +"';");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setQuestion();
                startGame();
            }
        }, 2500);

    }

    private int calculateScore() {
        int score=0;

        int attempts = Integer.parseInt(((TextView) findViewById(R.id.attemptstxt)).getText().toString());

        int scoreratio = attempts-optim;
        int timeratio = (min*60+sec)-((lev+1)*10);

        if(scoreratio<0) scoreratio=0;
        if(timeratio<0) timeratio=0;

        score += 500 - scoreratio;
        score += 500 - (timeratio/2);


        return score;
    }

    public void getQuestions() {

        //Get level and sublevel
        Cursor resSet = db.rawQuery("select value from settings where key='level';", null);
        resSet.moveToFirst();
        lev = Integer.parseInt(resSet.getString(0));
        resSet = db.rawQuery("select value from settings where key='sublevel';", null);
        resSet.moveToFirst();
        sublev = Integer.parseInt(resSet.getString(0));

        //get total score
        resSet = db.rawQuery("select value from settings where key='total';", null);
        resSet.moveToFirst();
        tot = Integer.parseInt(resSet.getString(0));

        //set level in screen
        ((TextView) findViewById(R.id.leveltxt)).setText(String.valueOf(lev) + "." + String.valueOf(sublev));

        //get question for this level
        resSet = db.rawQuery("select dots from game where level='"+String.valueOf(lev)+"' and sublevel='" +
                String.valueOf(sublev) + "';", null);
        resSet.moveToFirst();
        // parse question to array
        String dots = resSet.getString(0);
        int i=0;
        for (String retval: dots.split(",")){
            if(!retval.isEmpty()) {
                question[i] = Integer.parseInt(retval);
                i++;
            }
            question[i] = -1;
        }
    }

    // id of dot to animate
    // stat = 0 => green to red, stat=1 => red to green
    public void doAnimation(int id, final int stat){

        final ImageView top = (ImageView) findViewById(id + 200);
        final ImageView bot = (ImageView) findViewById(id + 100);

        final ScaleAnimation growAnim = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        final ScaleAnimation shrinkAnim = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        growAnim.setDuration(250);
        growAnim.setFillAfter(true);
        shrinkAnim.setDuration(1);
        shrinkAnim.setFillAfter(true);

        top.startAnimation(shrinkAnim);

        shrinkAnim.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation animation){}
            @Override
            public void onAnimationRepeat(Animation animation){}
            @Override
            public void onAnimationEnd(Animation animation){
                if(stat==0) {
                    top.setImageResource(R.drawable.w);
                }
                else {
                    top.setImageResource(R.drawable.b);
                }
                top.startAnimation(growAnim);
            }
        });

        growAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                if(stat==0)
                    bot.setImageResource(R.drawable.w);
                else
                    bot.setImageResource(R.drawable.b);
            }
        });
    }
}
