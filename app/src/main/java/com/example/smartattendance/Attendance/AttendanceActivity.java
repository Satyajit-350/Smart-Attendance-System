package com.example.smartattendance.Attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.smartattendance.Model.UploadFile;
import com.example.smartattendance.R;
import com.example.smartattendance.databinding.ActivityAttendanceBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    private ActivityAttendanceBinding binding;
    DatabaseReference  databaseReference ;
    List<UploadFile> uploadFiles;
    AttendanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        uploadFiles = new ArrayList<>();
        viewAllFiles();
        adapter = new AttendanceAdapter(this,uploadFiles);

        binding.listView.setAdapter(adapter);

        binding.listView.setLayoutManager(new LinearLayoutManager(this));
        binding.listView.setHasFixedSize(true);

//        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceActivity.this, FaceRecogntionActivity.class);
                startActivity(intent);
            }
        });

    }

    private void viewAllFiles() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadFiles.clear();
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    UploadFile uploadFile = postSnapshot.getValue(UploadFile.class);
                    uploadFile.getData(postSnapshot.getKey());
                    uploadFiles.add(uploadFile);
                }

//                String[] uploads = new String[uploadFiles.size()];
//                for(int i=0; i<uploads.length;i++){
//                    uploads[i] = uploadFiles.get(i).getName();
//                    Log.d("TAG",uploads[i]);
//                }
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,uploads);
//                binding.listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Toast.makeText(AttendanceActivity.this, "Data Load", Toast.LENGTH_SHORT).show();
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
        switch(item.getItemId()){
            case R.id.delete:
                Toast.makeText(this, "All Attendance deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}