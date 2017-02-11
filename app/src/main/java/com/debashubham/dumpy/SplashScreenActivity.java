package com.debashubham.dumpy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by deba on 10/2/17.
 */
public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        ImageView myImageView= (ImageView)findViewById(R.id.splash_logo);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.image_fadein);
        myImageView.startAnimation(myFadeInAnimation);

        TextView intro_text=(TextView)findViewById(R.id.introduction);
        Animation textFadeIn= AnimationUtils.loadAnimation(this, R.anim.image_fadein);
        intro_text.startAnimation(textFadeIn);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            }
        },3000);
    }
}
