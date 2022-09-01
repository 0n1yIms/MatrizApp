package com.ims.matrixcalc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ims.matrixcalc.Gauss.Escalonador;
import com.ims.matrixcalc.Gauss.Mat;
import com.ims.matrixcalc.Gauss.Num;

import java.util.ArrayList;

public class GaussStepsActivity extends AppCompatActivity {

    public static ArrayList<Escalonador.MatInfo> steps;

    private GridLayout gridPrevious;
    private GridLayout gridLater;
    private TextView tvInfo;

    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gauss_steps);
        gridPrevious = findViewById(R.id.grid_previous);
        gridLater = findViewById(R.id.grid_later);
        tvInfo = findViewById(R.id.tv_info);
        show();

//        findViewById(R.id.btn_back_activity).setOnClickListener((view ->
//                startActivity(new Intent(GaussStepsActivity.this, GaussActivity.class))));
        findViewById(R.id.btn_back_activity).setOnClickListener((view ->
                finish()));

        findViewById(R.id.btn_back).setOnClickListener(view -> {
            if (index > 0) {
                index--;
                show();
            }
        });

        findViewById(R.id.btn_next).setOnClickListener(view -> {
            if (index < steps.size() - 1) {
                index++;
                show();
            }
        });

    }

    void show() {
        gridPrevious.setVisibility(View.INVISIBLE);
        gridLater.setVisibility(View.INVISIBLE);

        Escalonador.MatInfo matInfo = steps.get(steps.size() - 1 - index);
        Mat matPrevious = matInfo.matPrevious;
        Mat matLater = matInfo.matLater;
        runOnUiThread(() ->
        {
            tvInfo.setText(matInfo.info);
            gridPrevious.removeAllViews();
            gridPrevious.setColumnCount(matPrevious.cols);
            gridLater.removeAllViews();
            gridLater.setColumnCount(matLater.cols);
        });

        //add header
        for (int i = 0; i < matPrevious.cols; i++) {
            ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_matrix2, null, false);
            TextView tv2 = ll.findViewById(R.id.tv2);
            if (i == matPrevious.cols - 1) {
                TextView tv = ll.findViewById(R.id.tv);
                tv.setText("ti");
                tv2.setText("");
            } else
                tv2.setText("" + i);
            runOnUiThread(() -> gridPrevious.addView(ll));
        }
        for (int i = 0; i < matPrevious.cols; i++) {
            ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_matrix2, null, false);
            TextView tv2 = ll.findViewById(R.id.tv2);
            if (i == matPrevious.cols - 1) {
                TextView tv = ll.findViewById(R.id.tv);
                tv.setText("ti");
                tv2.setText("");
            } else
                tv2.setText("" + i);
            runOnUiThread(() -> gridLater.addView(ll));
        }

        for (int j = 0; j < matPrevious.rows; j++) {
            for (int i = 0; i < matPrevious.cols; i++) {
                ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_matrix, null, false);
                runOnUiThread(() -> gridPrevious.addView(ll));
                EditText et = ll.findViewById(R.id.et);
                et.setText(matPrevious.mat[j][i].toStr());
                et.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);
                et.setEnabled(false);
                et.setTextColor(Color.BLACK);
                et.setWidth(200);
            }
        }
        for (int j = 0; j < matPrevious.rows; j++) {
            for (int i = 0; i < matPrevious.cols; i++) {
                ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_matrix, null, false);
                runOnUiThread(() -> gridLater.addView(ll));
                EditText et = ll.findViewById(R.id.et);
                et.setText(matLater.mat[j][i].toStr());
                et.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);
                et.setEnabled(false);
                et.setTextColor(Color.BLACK);
                et.setWidth(200);
            }
        }
        gridPrevious.setVisibility(View.VISIBLE);
        gridLater.setVisibility(View.VISIBLE);
    }
}