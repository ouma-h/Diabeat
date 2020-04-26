package com.example.diabeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;


public class onBoarding extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotsLayout;

    private SliderAdapter sliderAdapter;

    private TextView [] mDots;

    private Button nNextBtn;
    private Button nGetStartedBtn;
    private Button nSkipBtn;

    private int nCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        mSlideViewPager = (ViewPager) findViewById(R.id.viewPager);
        mDotsLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        nNextBtn = (Button) findViewById(R.id.nextButton);
        nGetStartedBtn = (Button) findViewById(R.id.getStartedButton);
        nSkipBtn = (Button) findViewById(R.id.skipButton);

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewlistener);

        nNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nCurrentPage == mDots.length){
                    onButtonClick(v);
                }else{
                    mSlideViewPager.setCurrentItem(nCurrentPage + 1);
                }
            }
        });
    }

    public void addDotsIndicator(int position){

        mDots = new TextView[3];
        mDotsLayout.removeAllViews();

        for(int i =0; i<mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(HtmlCompat.fromHtml("&#8226;",HtmlCompat.FROM_HTML_MODE_LEGACY));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            mDotsLayout.addView(mDots[i]);
        }

        if (mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorAccent));
        }

    }

    ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            nCurrentPage = i;

            if (i == mDots.length -1 ){
                nNextBtn.setEnabled(false);
                nGetStartedBtn.setEnabled(true);
                nNextBtn.setVisibility(View.INVISIBLE);
                nGetStartedBtn.setVisibility(View.VISIBLE);
                nSkipBtn.setEnabled(false);
                nSkipBtn.setVisibility(View.INVISIBLE);

            }else{
                nNextBtn.setEnabled(true);
                nGetStartedBtn.setEnabled(false);
                nNextBtn.setVisibility(View.VISIBLE);
                nGetStartedBtn.setVisibility(View.INVISIBLE);
                nSkipBtn.setEnabled(true);
                nSkipBtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void onButtonClick (View v){
        Intent myIntent = new Intent(onBoarding.this, activity_login.class);
        startActivity(myIntent);
    }
}
