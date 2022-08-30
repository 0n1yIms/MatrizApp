package com.ims.matrixcalc;

import android.app.Activity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MatrixView {
    private int sizeX, sizeY;

    private RecyclerView recyclerView;


    public MatrixView(int sizeX, int sizeY, Activity activity)
    {
        this.sizeX = sizeX;
        this.sizeY = sizeY;

//        recyclerView = activity.findViewById(R.id.recyclerView);

        ArrayList<Integer> datos = new ArrayList<>(10);
        for (int i = 0; i < 100; i++) {
            datos.add(i);
        }


        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(datos);

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        recyclerView.setAdapter(recyclerAdapter);


    }


}
