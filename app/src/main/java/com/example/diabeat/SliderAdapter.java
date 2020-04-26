package com.example.diabeat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.call_doctor,
            R.drawable.forgot_password,
            R.drawable.signup
    };

    public String[] slide_headings = {
            "Reach for Help",
            "Receive Reminder",
            "Track your Health"
    };

    public String[] slide_description = {
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent tempor facilisis lectus vel sollicitudin. Integer tincidunt, sapien vitae pretium feugiat, lorem justo bibendum",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent tempor facilisis lectus vel sollicitudin. Integer tincidunt, sapien vitae pretium feugiat, lorem justo bibendum",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent tempor facilisis lectus vel sollicitudin. Integer tincidunt, sapien vitae pretium feugiat, lorem justo bibendum"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView= (ImageView)view.findViewById(R.id.slide_images);
        TextView slideHeading= (TextView)view.findViewById(R.id.slide_headings);
        TextView slideDescription= (TextView)view.findViewById(R.id.slide_description);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_description[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
