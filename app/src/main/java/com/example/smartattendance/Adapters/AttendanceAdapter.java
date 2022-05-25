package com.example.smartattendance.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartattendance.Model.UploadFile;
import com.example.smartattendance.R;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private Context context;
    private List<UploadFile> uploadFileList;

    public AttendanceAdapter(Context context, List<UploadFile> uploadFileList) {
        this.context = context;
        this.uploadFileList = uploadFileList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UploadFile files = uploadFileList.get(position);

        holder.filesTextView.setText(files.getName());
        holder.std_name.setText(files.getStudent());
        holder.date_text.setText(files.getDate());
        holder.time_text.setText(files.getTime());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(files.getUrl()));
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return uploadFileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView filesTextView;
        private TextView std_name;
        private TextView date_text;
        private TextView time_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            filesTextView = itemView.findViewById(R.id.attendance_file);
            std_name = itemView.findViewById(R.id.std_name);
            date_text = itemView.findViewById(R.id.Date);
            time_text = itemView.findViewById(R.id.Time_text);

        }
    }
}
