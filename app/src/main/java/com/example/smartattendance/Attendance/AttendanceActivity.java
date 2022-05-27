package com.example.smartattendance.Attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

   private ActivityAttendanceBinding binding;
    private ArrayList<StudentModel> studentList;
    private DBHelper dbHelper;
    DatabaseReference  databaseReference ;
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
        viewAllFiles();
        adapter = new AttendanceAdapter(this,uploadFiles);

        dbHelper = new DBHelper(this);
        database = FirebaseDatabase.getInstance();

        binding.listView.setAdapter(adapter);

        binding.listView.setLayoutManager(new LinearLayoutManager(this));
        binding.listView.setHasFixedSize(true);

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceActivity.this, FaceRecogntionActivity.class);
                startActivity(intent);
            }
        });

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
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    UploadFile uploadFile = postSnapshot.getValue(UploadFile.class);
                    uploadFile.getData(postSnapshot.getKey());
                    uploadFiles.add(uploadFile);
                }
                if(uploadFiles.size()==0){
                    binding.shimmer.stopShimmer();;
                    binding.emptyView.setVisibility(View.VISIBLE);
                }else{
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
        switch(item.getItemId()){
            case R.id.delete:
                database.getReference().child("uploads").removeValue();
                dbHelper.deleteAll();
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
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);
            } else {
                importData();
            }
        } else {
            importData();
        }

    }

    private void importData() {
        studentList = new ArrayList<>();
        studentList = dbHelper.getAllStudents();
        if(studentList.size()>0){
            createXlFile();
        }else {
            Toast.makeText(this, "list are empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void createXlFile() {
        Workbook wb = new HSSFWorkbook();
        Cell cell = null;
        Sheet sheet = null;

        sheet = wb.createSheet("Student Excel Sheet");
        //Now column and row
        Row row = sheet.createRow(0);

        cell = row.createCell(0);
        cell.setCellValue("Student Name");


        cell = row.createCell(1);
        cell.setCellValue("Date");


        cell = row.createCell(2);
        cell.setCellValue("Time");


        //column width
        sheet.setColumnWidth(0, (20 * 200));
        sheet.setColumnWidth(1, (30 * 200));
        sheet.setColumnWidth(2, (30 * 200));


        for (int i = 0; i < studentList.size(); i++) {
            Row row1 = sheet.createRow(i + 1);

            cell = row1.createCell(0);
            cell.setCellValue(studentList.get(i).getsName());

            cell = row1.createCell(1);
            cell.setCellValue((studentList.get(i).getsDate()));
            //  cell.setCellStyle(cellStyle);

            cell = row1.createCell(2);
            cell.setCellValue(studentList.get(i).getsTime());


            sheet.setColumnWidth(0, (20 * 200));
            sheet.setColumnWidth(1, (30 * 200));
            sheet.setColumnWidth(2, (30 * 200));

        }
        String folderName = "Import Excel";
        String fileName = folderName + System.currentTimeMillis() + ".xls";
        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + folderName + File.separator + fileName;

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + folderName);
        if (!file.exists()) {
            file.mkdirs();
        }

        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(path);
            wb.write(outputStream);
            // ShareViaEmail(file.getParentFile().getName(),file.getName());
            Toast.makeText(getApplicationContext(), "Excel Created in " + path, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Not OK", Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            importData();
        } else {
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}