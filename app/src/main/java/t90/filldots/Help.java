package t90.filldots;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.widget.TextView;


public class Help extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.scale_up,R.anim.scale_down);
        setContentView(R.layout.activity_help);

        int w = getResources().getDisplayMetrics().widthPixels;
        if(w<1500){
            ((TextView) findViewById(R.id.help_title)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 60);
        }
        else if(w<900){
            ((TextView) findViewById(R.id.help_title)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
        }
        else {
            ((TextView) findViewById(R.id.helptxt)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        }

        String HelpTxt = "First of all, let me thank you for supporting me by downloading this app." +
                "I also thank my friends @ SuperBad for allowing me to share this app.\n\n" +
                "This is a simple app where your goal is to achieve a completely filled grid of " +
                "blue dots.\n" +
                "There is a grid (5x5) of dots, of which some are filled(blue) and some empty(white)." +
                " When you tap any of the dots, that corresponding dot, along with the four surrounding" +
                " dots toggle their state, ie filled dots become empty and empty dots get filled. So " +
                "you have to plan and tap the dots such that they all become blue.\n\n" +
                "There are five levels(main) and each has five sub levels, labelled 0.1, 0.2, 0.3... etc. " +
                "They are arranged in the order of complexity. The first four levels (0-3) follow similar rules, " +
                "but the five sub-levels in level 4 are special, as some dots automatically toggle in " +
                "between the game, making it really tough.\n\n" +
                "Your score will depend on the number of extra taps you use(based on certain parameters) to " +
                "solve a particular game, along with the time taken. The score is out of 1000.\n\n" +
                "If you have any issues, do mention them in the comments section in the play store, and" +
                " if you like this app, do give the app a good rating.\n\n\n" +
                "" +
                "Happy Filling!!! :)";

        Typeface tf = Typeface.createFromAsset(getAssets(), "BPdots.ttf");
        ((TextView) findViewById(R.id.help_title)).setTypeface(tf);

        ((TextView) findViewById(R.id.helptxt)).setText(HelpTxt);
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
