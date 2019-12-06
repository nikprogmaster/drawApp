package com.sandrlab.drawapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class PicturesAdapter extends BaseAdapter {

    private List<Integer> images;

    public void setImages(List<Integer> images){
        this.images = images;
    }

    PicturesAdapter(List<Integer> list){
        images = list;
    }

    @Override
    public int getCount() {
        return images == null ? 0 : images.size();
    }

    @Override
    public Object getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
            view.setTag(new SpinnerHolder(view));
        }
        SpinnerHolder spinnerHolder = (SpinnerHolder) view.getTag();
        Integer pictureId = images.get(i);
        spinnerHolder.picture.setImageResource(pictureId);
        return view;
    }

    private static class SpinnerHolder {
        private final ImageView picture;

        private SpinnerHolder(View root){
            picture = root.findViewById(R.id.picture);
        }

    }
}
