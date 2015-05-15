package t90.tapdots;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class landscape_fragment extends Fragment implements View.OnClickListener {

    static int num_dots = 5;
    static int[][] dot_status = new int[num_dots][num_dots];
    View v;
    int sec = 0, min = 0;
    int qnum = 0;
    Handler h;
    Runnable timeupdater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = v = inflater.inflate(R.layout.dots_layout_landscape, container, false);
        makeGrid();
        setQuestion(0);
        ((TextView) v.findViewById(R.id.attemptstxt)).setText("0");
        startGame();
        return view;
    }

    private void setQuestion(int n) {
        int[][] question = {
                {11, 15, 51, 55, 33, 31, 35, 13, 53, 22, 24, 44, 42},
                {11, 55, 22, 44, 33, 31, 35, 53, 13},
                {11, 15, 21, 25, 51, 55, 32, 34, 22, 44, 41, 21},
        };
        int optim = question[n].length;
        ((TextView)v.findViewById(R.id.optimaltxt)).setText(String.valueOf(optim+10));
        for(int i=0; i<optim; i++){
            int id = question[n][i];
            Log.d("ids", "" + id);
            onClick((FrameLayout) v.findViewById(id));
        }

    }

    private void makeGrid() {
        int id;
        LinearLayout l = (LinearLayout) v.findViewById(R.id.dots_layout);

        for (int i=0; i<num_dots; i++){
            LinearLayout lr = new LinearLayout(v.getContext());
            lr.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            lr.setOrientation(LinearLayout.HORIZONTAL);

            for (int j=0; j<num_dots; j++){
                id = (i+1)*10 + (j+1);

                FrameLayout fc = new FrameLayout(v.getContext());
                fc.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                fc.setOnClickListener(this);
                fc.setId(id);

                ImageView im_bot = new ImageView(v.getContext());
                im_bot.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                im_bot.setImageResource(R.drawable.greenbtn);
                im_bot.setId(id+100);

                ImageView im_top = new ImageView(v.getContext());
                im_top.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                im_top.setImageResource(R.drawable.greenbtnpressed);
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

        incrementAttempt();

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

    // id of dot to animate
    // stat = 0 => green to red, stat=1 => red to green
    public void doAnimation(int id, final int stat){

        final ImageView top = (ImageView) v.findViewById(id+200);
        final ImageView bot = (ImageView) v.findViewById(id+100);

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
                    top.setImageResource(R.drawable.redbtnpressed);
                }
                else {
                    top.setImageResource(R.drawable.greenbtnpressed);
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
                    bot.setImageResource(R.drawable.redbtn);
                else
                    bot.setImageResource(R.drawable.greenbtn);
            }
        });
    }


    private void manageUi() {
        final TextView time = (TextView) v.findViewById(R.id.timetxt);
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

    public void incrementAttempt() {
        TextView t = (TextView) v.findViewById(R.id.attemptstxt);
        int current = Integer.parseInt(String.valueOf(t.getText()));
        t.setText(String.valueOf(current+1));
    }

    public void startGame() { manageUi(); }

    public void endGame() {
        Toast.makeText(v.getContext(), "Congratulatios!!!", Toast.LENGTH_SHORT).show();
        h.removeCallbacks(timeupdater);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                setQuestion(qnum+1);
                startGame();
            }
        }
                , 3000);
    }
}
