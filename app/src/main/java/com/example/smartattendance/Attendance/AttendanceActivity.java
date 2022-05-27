package com.example.smartattendance.Attendance;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.smartattendance.Adapters.AttendanceAdapter;
import com.example.smartattendance.FaceRecognition.FaceRecogntionActivity;
import com.example.smartattendance.Model.StudentModel;
import com.example.smartattendance.Model.UploadFile;
import com.example.smartattendance.R;
import com.example.smartattendance.databinding.ActivityAttendanceBinding;
import com.example.smartattendance.db.DBHelper;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVWriter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    private ActivityAttendanceBinding binding;
    private ArrayList<StudentModel> studentList;
    private DBHelper dbHelper;
    DatabaseReference databaseReference;
    List<UploadFile> uploadFiles;
    AttendanceAdapter adapter;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //start the shimmer effect
        binding.shimmer.startShimmer();

        uploadFiles = new ArrayList<>();
        //display all the attendance
        viewAllFiles();
        adapter = new AttendanceAdapter(this, uploadFiles);

        dbHelper = new DBHelper(this);
        database = FirebaseDatabase.getInstance();

        binding.listView.setAdapter(adapter);

        binding.listView.setLayoutManager(new LinearLayoutManager(this));
        binding.listView.setHasFixedSize(true);

        //check for permission
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceActivity.this, FaceRecogntionActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 200);
    }

    private void viewAllFiles() {
        binding.emptyView.setVisibility(View.GONE);
        binding.shimmer.startShimmer();
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.listView.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadFiles.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    UploadFile uploadFile = postSnapshot.getValue(UploadFile.class);
                    uploadFile.getData(postSnapshot.getKey());
                    uploadFiles.add(uploadFile);
                }
                if (uploadFiles.size() == 0) {
                    binding.shimmer.stopShimmer();
                    ;
                    binding.emptyView.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyView.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
                binding.shimmer.stopShimmer();
                binding.shimmer.setVisibility(View.GONE);
                Toast.makeText(AttendanceActivity.this, "Data Loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.attendance_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                database.getReference().child("uploads").removeValue();
                binding.emptyView.setVisibility(View.VISIBLE);
                Toast.makeText(this, "All Attendance deleted", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.export_xl:
                exportIV();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void exportIV() {
        if (ContextCompat.checkSelfPermission(AttendanceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AttendanceActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
        else{

            try{
                final String[][] csvMatrix = new String[3][3];
                csvMatrix[0][0] = "first0";
                csvMatrix[0][1] = "second0";
                csvMatrix[0][2] = "third0";
                csvMatrix[1][0] = "first1";
                csvMatrix[1][1] = "second1";
                csvMatrix[1][2] = "third1";
                csvMatrix[2][0] = "first2";
                csvMatrix[2][1] = "second2";
                csvMatrix[2][2] = "third2";

//                        String name = "Buddy";
//                        String no= "2";
                File file = new File("/sdcard/satyajit/");
                file.mkdirs();

                String csv="/sdcard/satyajit/s.csv";
                CSVWriter csvWriter=new CSVWriter(new FileWriter(csv, true));

                for (int i = 0; i < csvMatrix.length; i++) {
                    csvWriter.writeNext(csvMatrix[i]);
                }
                csvWriter.close();
                Toast.makeText(AttendanceActivity.this, "File Successfully Created", Toast.LENGTH_SHORT).show();
            }
            catch(Exception e){
                Toast.makeText(AttendanceActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}

