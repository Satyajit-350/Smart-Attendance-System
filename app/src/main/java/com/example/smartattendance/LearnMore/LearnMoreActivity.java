package com.example.smartattendance.LearnMore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.smartattendance.R;
import com.example.smartattendance.databinding.ActivityLearnMoreBinding;

public class LearnMoreActivity extends AppCompatActivity {

    private ActivityLearnMoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearnMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

    }
}