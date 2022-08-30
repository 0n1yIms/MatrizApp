package com.ims.matrixcalc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.ims.matrixcalc.Gauss.GaussJordan;
import com.ims.matrixcalc.Gauss.Mat;
import com.ims.matrixcalc.Gauss.Num;

public class GaussActivity extends AppCompatActivity {
    private final int DATA_TYPE_MATRIX = 0;
    private final int DATA_TYPE_SYSTEM = 1;

    private int dataType = DATA_TYPE_MATRIX;

    private EditText etMatRows, etMatCols;
    private GridLayout gridMat;
    private GridLayout gridRta;
    boolean changed;

    private Mat matMain = new Mat(3,3,0);
    private Mat matRta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gauss);

        etMatRows = findViewById(R.id.et_matrix_size_rows);
        etMatCols = findViewById(R.id.et_matrix_size_cols);

        gridMat = findViewById(R.id.grid);
        gridRta = findViewById(R.id.grid_rta);

//        changeDataType(DATA_TYPE_MATRIX);
        changeDataType(DATA_TYPE_SYSTEM);

        etMatRows.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    int matRows = Integer.parseInt(charSequence.toString());
                    matMain.changeSize(matRows, matMain.cols);
                    updateData();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etMatCols.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    int matCols = Integer.parseInt(charSequence.toString());
                    matMain.changeSize(matMain.rows, matCols);
                    updateData();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        findViewById(R.id.btn_sistema_matriz).setOnClickListener((view) ->
        {
            if(changed)
            {
                changed = false;
                changeDataType(DATA_TYPE_SYSTEM);
            }
            else
            {
                changeDataType(DATA_TYPE_MATRIX);
                changed = true;
            }
        });

        findViewById(R.id.btn_calc).setOnClickListener((view) ->
        {
            GaussJordan gaussJordan = new GaussJordan();
            matRta = gaussJordan.calc(matMain.getCopy()).getCopy();
//            matRta = matMain.getCopy();
            showMatrix();
//            showMatrixs(new Mat[]{new Mat()});
        });

        findViewById(R.id.btnBack).setOnClickListener((view ->
                startActivity(new Intent(GaussActivity.this, MainActivity.class))));
    }

    void updateSystem()
    {
        new Thread(() ->
        {
            runOnUiThread(()->
            {
                gridMat.removeAllViews();
                gridMat.setColumnCount(matMain.cols);
            });

            for (int j = 0; j < matMain.rows; j++) {
                for (int i = 0; i < matMain.cols; i++) {
                    if(i == matMain.cols - 1)
                    {
                        ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_system2, null, false);
                        EditText et = ll.findViewById(R.id.et);
                        et.setText(matMain.mat[j][i].toStr());
                        int finalJ = j;
                        int finalI = i;
                        et.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if(!TextUtils.isDigitsOnly(charSequence) || charSequence.toString().isEmpty())
                                    return;
                                int num = Integer.parseInt(charSequence.toString());
                                matMain.mat[finalJ][finalI] = new Num(num);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        runOnUiThread(() -> gridMat.addView(ll));
                    }
                    else {
                        ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_system, null, false);
                        EditText et = ll.findViewById(R.id.et);
                        TextView tv = ll.findViewById(R.id.tv2);
                        tv.setText("" + i);
                        et.setText(matMain.mat[j][i].toStr());
                        int finalJ = j;
                        int finalI = i;
                        et.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if(!TextUtils.isDigitsOnly(charSequence) || charSequence.toString().isEmpty())
                                    return;
                                int num = Integer.parseInt(charSequence.toString());
                                matMain.mat[finalJ][finalI] = new Num(num);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        runOnUiThread(() -> gridMat.addView(ll));
                    }
                }
            }
        }).start();
    }

    void updateMatrix()
    {
        new Thread(() ->
        {
            runOnUiThread(()->
            {
                gridMat.removeAllViews();
                gridMat.setColumnCount(matMain.cols);
            });

            //add header
            for (int i = 0; i < matMain.cols; i++) {
                ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_matrix2, null, false);
                TextView tv2 = ll.findViewById(R.id.tv2);
                if(i == matMain.cols - 1)
                {
                    TextView tv = ll.findViewById(R.id.tv);
                    tv.setText("ti");
                    tv2.setText("");
                }
                else
                    tv2.setText("" + i);
                runOnUiThread(() -> gridMat.addView(ll));
            }

            for (int j = 0; j < matMain.rows; j++) {
                for (int i = 0; i < matMain.cols; i++) {
                    ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_matrix, null, false);
                    runOnUiThread(()->gridMat.addView(ll));
                    EditText et = ll.findViewById(R.id.et);
                    et.setText(matMain.mat[j][i].toStr());
                    et.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);
                    et.setTextColor(Color.BLACK);
                    et.setWidth(200);
                    int finalJ = j;
                    int finalI = i;
                    et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if(!TextUtils.isDigitsOnly(charSequence) || charSequence.toString().isEmpty())
                                return;
                            int num = Integer.parseInt(charSequence.toString());
                            matMain.mat[finalJ][finalI] = new Num(num);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }
            }
        }).start();
    }

    void updateData()
    {
        if(dataType == DATA_TYPE_MATRIX)
            updateMatrix();
        else if(dataType == DATA_TYPE_SYSTEM)
            updateSystem();
        showMatrix();
    }

    void changeDataType(int dataType)
    {
        this.dataType = dataType;
//        if(dataType == DATA_TYPE_MATRIX)
//        {
//            ViewGroup parent = (ViewGroup) gridMat.getParent();
//            int index = parent.indexOfChild(gridMat);
//            parent.removeView(gridMat);
//            parent.addView(gridLayoutMatrix, index);
//            gridMat = gridLayoutMatrix;
//        }
//        else
//        {
//            ViewGroup parent = (ViewGroup) gridMat.getParent();
//            int index = parent.indexOfChild(gridMat);
//            parent.removeView(gridMat);
//            parent.addView(gridLayoutSystem, index);
//            gridMat = gridLayoutSystem;
//        }
        updateData();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GaussActivity.this, MainActivity.class));
    }

    void showMatrix() {
        if(matRta == null)
            return;
        findViewById(R.id.sv_rta).setBackground(getDrawable(R.drawable.border));
        if (dataType == DATA_TYPE_MATRIX) {
            runOnUiThread(()->
            {
                gridRta.removeAllViews();
                gridRta.setColumnCount(matRta.cols);
            });

            //add header
            for (int i = 0; i < matRta.cols; i++) {
                ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_matrix2, null, false);
                TextView tv2 = ll.findViewById(R.id.tv2);
                if(i == matRta.cols - 1)
                {
                    TextView tv = ll.findViewById(R.id.tv);
                    tv.setText("ti");
                    tv2.setText("");
                }
                else
                    tv2.setText("" + i);
                runOnUiThread(() -> gridRta.addView(ll));
            }

            for (int j = 0; j < matRta.rows; j++) {
                for (int i = 0; i < matRta.cols; i++) {
                    ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_matrix, null, false);
                    runOnUiThread(()->gridRta.addView(ll));
                    EditText et = ll.findViewById(R.id.et);
                    et.setText(matRta.mat[j][i].toStr());
                    et.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);
                    et.setTextColor(Color.BLACK);
                    et.setWidth(200);
                }
            }
        }
        else if(dataType == DATA_TYPE_SYSTEM)
        {
            runOnUiThread(()->
            {
                gridRta.removeAllViews();
                gridRta.setColumnCount(matRta.cols);
            });

            for (int j = 0; j < matRta.rows; j++) {
                for (int i = 0; i < matRta.cols; i++) {
                    if(i == matMain.cols - 1)
                    {
                        ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_system2, null, false);
                        EditText et = ll.findViewById(R.id.et);
                        et.setText(matRta.mat[j][i].toStr());
                        runOnUiThread(() -> gridRta.addView(ll));
                    }
                    else {
                        ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_system, null, false);
                        EditText et = ll.findViewById(R.id.et);
                        TextView tv = ll.findViewById(R.id.tv2);
                        tv.setText("" + i);
                        et.setText(matRta.mat[j][i].toStr());
                        runOnUiThread(() -> gridRta.addView(ll));
                    }
                }
            }
        }
    }


//    void showMatrixs(Mat[] mats)
//    {
//        svRta.removeAllViews();
//        LinearLayout ll = new LinearLayout(this);
//        ll.setOrientation(LinearLayout.HORIZONTAL);
//
//        for (int i = 0; i < mats.length; i++) {
//            if(dataType == DATA_TYPE_MATRIX) {
//                ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_matrix_view, null);
//                GridLayout grid = layout.findViewById(R.id.grid);
//                setGrid(grid, mats[i]);
//                ll.addView(grid);
//            }
//        }
//    }

    void setGrid(GridLayout grid, Mat mat)
    {
        runOnUiThread(()->
        {
            grid.removeAllViews();
            grid.setColumnCount(mat.cols);
        });

        for (int i = 0; i < mat.cols; i++) {
            ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_matrix2, null, false);
            TextView tv2 = ll.findViewById(R.id.tv2);
            if(i == mat.cols - 1)
            {
                TextView tv = ll.findViewById(R.id.tv);
                tv.setText("ti");
                tv2.setText("");
            }
            else
                tv2.setText("" + i);
            runOnUiThread(() -> grid.addView(ll));
        }

        for (int i = 0; i < mat.rows; i++) {
            for (int j = 0; j < mat.cols; j++) {
                ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_matrix, null, false);
                EditText et = ll.findViewById(R.id.et);
                et.setText("" + mat.mat[i][j]);
//                EditText et = ll.findViewById(R.id.et);
//                et.setText(datos.get(i) + "");
//                et.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);
//                et.setTextColor(Color.BLACK);
//                et.setWidth(200);
                runOnUiThread(()->grid.addView(ll));
            }
        }
    }
}