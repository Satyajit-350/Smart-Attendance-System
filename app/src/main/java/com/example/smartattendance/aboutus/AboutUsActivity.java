package com.example.smartattendance.aboutus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.smartattendance.Adapters.ViewpagerAdapter;
import com.example.smartattendance.Model.ItemModel;
import com.example.smartattendance.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AboutUsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewpagerAdapter adapter;
    private Button nextBtn;
    private List<ItemModel> arrItems;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        arrItems = new ArrayList<>();
        arrItems.add(new ItemModel("Welcome!", "Take All the Attendance of the students On One Platform!", R.raw.class_room));
        arrItems.add(new ItemModel("Smart Attendance System", "Facial recognition based attendance system", R.raw.selfi));
        arrItems.add(new ItemModel("Secure", "Take attendance with 100% accuracy", R.raw.class_test));
        arrItems.add(new ItemModel("Saves time", "Now Enjoy The App With All New Features And User InterFace.", R.raw.time_management));

        viewPager = findViewById(R.id.about_viewPager);
        nextBtn = findViewById(R.id.about_next_button);

        //viewpager setup
        adapter = new ViewpagerAdapter(this, arrItems);

        viewPager.setAdapter(adapter);

        //click listener on next button
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = viewPager.getCurrentItem();
                if (pos < arrItems.size()) {
                    pos++;
                    viewPager.setCurrentItem(pos);
                }
                if (pos == arrItems.size() - 1) {
                    //hide the tabLayout and next button and make visible the getStarted button
                    getLastScreen();
                }
            }
        });
    }

    private void getLastScreen() {
        nextBtn.setVisibility(View.INVISIBLE);
    }

}