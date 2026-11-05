package com.example.laborator4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class AvionAdapter extends BaseAdapter {
    private List<Avion> avioane = null;
    private Context ctx;
    private int resursaLayout;

    public AvionAdapter(List<Avion> avioane, Context ctx, int resursaLayout) {
        this.avioane = avioane;
        this.ctx = ctx;
        this.resursaLayout = resursaLayout;
    }

    @Override
    public int getCount() {
        return avioane.size();
    }

    @Override
    public Object getItem(int i) {
        return avioane.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(resursaLayout, viewGroup, false);

        TextView modelTV = v.findViewById(R.id.modelTV);
        TextView producatorTV = v.findViewById(R.id.producatorTV);
        TextView utilizareTV = v.findViewById(R.id.utilizareTV);
        TextView detaliiTV = v.findViewById(R.id.detaliiTV);

        Avion avion = (Avion) getItem(i);

        modelTV.setText(avion.getModel());
        producatorTV.setText(avion.getProducator());
        utilizareTV.setText(avion.getTipUtilizare());
        detaliiTV.setText("Rating: " + avion.getRating()
                + " | Reacție: " + (avion.getAreMotorReactie() ? "Da" : "Nu")
                + " | În serviciu: " + (avion.isEsteInServiciu() ? "Da" : "Nu"));

        return v;
    }
}