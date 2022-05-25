package com.example.smartattendance.OnBoardingScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.smartattendance.Adapters.ViewpagerAdapter;
import com.example.smartattendance.Model.ItemModel;
import com.example.smartattendance.R;
import com.example.smartattendance.SignIn.SignInActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewpagerAdapter adapter;
    private Button nextBtn,getStartedBtn,skipBtn;
    private List<ItemModel> arrItems;
    private Animation buttonAnimation;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //when the activity is launched we need to check if it was open before or not
        if(getPreferenceData()){
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
            finish();
        }
        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_onboarding);

        getSupportActionBar().hide();

        arrItems = new ArrayList<>();
        arrItems.add(new ItemModel("Welcome!","Take All the Attendance of the students On One Platform!",R.raw.class_room));
        arrItems.add(new ItemModel("Smart Attendance System","Facial recognition based attendance system",R.raw.selfi));
        arrItems.add(new ItemModel("Secure","Take attendance with 100% accuracy",R.raw.class_test));
        arrItems.add(new ItemModel("Saves time","Now Enjoy The App With All New Features And User InterFace.",R.raw.time_management));

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setSelectedTabIndicator(null);
        nextBtn = findViewById(R.id.next_button);
        getStartedBtn = findViewById(R.id.get_started_btn);
        skipBtn = findViewById(R.id.skip_button);

        buttonAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);

        //viewpager setup
        adapter = new ViewpagerAdapter(this,arrItems);

        viewPager.setAdapter(adapter);

        //tabLayout setup
        tabLayout.setupWithViewPager(viewPager);

        //tabLayout onchange listener setup
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==arrItems.size()-1){
                    getLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //click listener on next button
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = viewPager.getCurrentItem();
                if(pos<arrItems.size()){
                    pos++;
                    viewPager.setCurrentItem(pos);
                }
                if(pos == arrItems.size()-1){
                    //hide the tabLayout and next button and make visible the getStarted button
                    getLastScreen();
                }
            }
        });

        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);

                //here we need to store a boolean value to storage so next time when the user runs the app
                //we could know that he has already checked the intro activity
                savePreferenceData();
                finish();
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);
                savePreferenceData();
                finish();
            }
        });

    }

    private boolean getPreferenceData() {
        SharedPreferences getPref = getApplicationContext().getSharedPreferences("myPreferences",MODE_PRIVATE);
        boolean check = getPref.getBoolean("isOpened",false);
        return check;
    }

    private void savePreferenceData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPreferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isOpened",true);
        editor.apply();
    }

    private void getLastScreen() {
        tabLayout.setVisibility(View.INVISIBLE);
        nextBtn.setVisibility(View.INVISIBLE);
        skipBtn.setVisibility(View.INVISIBLE);

        getStartedBtn.setVisibility(View.VISIBLE);
        //animation
        getStartedBtn.setAnimation(buttonAnimation);
    }
}