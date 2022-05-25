package com.example.smartattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.smartattendance.OnBoardingScreen.OnboardingActivity;
import com.example.smartattendance.SignIn.SignInActivity;
import com.example.smartattendance.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {

    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());

        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(4000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    Intent intent = new Intent(SplashScreen.this, OnboardingActivity.class);
                    startActivity(intent);

                    finish();
                }
            }
        };thread.start();
    }
}