package com.example.smartattendance.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.example.smartattendance.Model.ItemModel;
import com.example.smartattendance.R;

import java.util.List;

public class ViewpagerAdapter extends PagerAdapter {

    Context context;
    private List<ItemModel> modelArrayList;

    public ViewpagerAdapter(Context context, List<ItemModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_layout_screen,null);

        //find all the views
        TextView titleText = view.findViewById(R.id.title_text);
        TextView descriptionText = view.findViewById(R.id.description_text);
        LottieAnimationView lottieAnimationView = view.findViewById(R.id.Lottie_view);

        ItemModel model = modelArrayList.get(position);

        titleText.setText(model.getTitle());
        descriptionText.setText(model.getDescription());
        lottieAnimationView.setAnimation(model.getImage());

        container.addView(view);

        return view;
    }
    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
