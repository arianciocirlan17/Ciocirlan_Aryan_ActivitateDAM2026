package com.example.laborator4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ImagineAvionAdapter extends BaseAdapter {
    private List<ImagineAvion> imagini;
    private Context ctx;
    private int resursaLayout;

    public ImagineAvionAdapter(List<ImagineAvion> imagini, Context ctx, int resursaLayout) {
        this.imagini = imagini;
        this.ctx = ctx;
        this.resursaLayout = resursaLayout;
    }

    @Override
    public int getCount() {
        return imagini.size();
    }

    @Override
    public Object getItem(int i) {
        return imagini.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(resursaLayout, viewGroup, false);

        ImageView imagineIV = v.findViewById(R.id.imagineIV);
        TextView descriereTV = v.findViewById(R.id.descriereTV);

        ImagineAvion item = (ImagineAvion) getItem(i);

        descriereTV.setText(item.getDescriere());

        if (item.getImagine() != null) {
            imagineIV.setImageBitmap(item.getImagine());
        } else {
            imagineIV.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        return v;
    }
}