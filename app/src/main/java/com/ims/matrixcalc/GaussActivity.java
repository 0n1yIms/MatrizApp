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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ims.matrixcalc.Gauss.Escalonador;
import com.ims.matrixcalc.Gauss.Mat;
import com.ims.matrixcalc.Gauss.Num;

import java.util.ArrayList;

public class GaussActivity extends AppCompatActivity {
    private static final String TAG = "GaussActivity";
    private final int DATA_TYPE_MATRIX = 0;
    private final int DATA_TYPE_SYSTEM = 1;
    public final String ERASE = "erase";
    public final String STEPS = "steps";

    private int dataType = DATA_TYPE_MATRIX;

    private EditText etMatRows, etMatCols;
    private GridLayout gridMat;
    private GridLayout gridRta;
    private TextView tvRta;

    private Mat matMain = new Mat(3, 4, 0);
    private Mat matRta;
    private boolean resolved = false;
    private ArrayList<Escalonador.MatInfo> steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gauss);

        if (getIntent().hasExtra(ERASE)) {
            if ((boolean) getIntent().getExtras().get(ERASE))
                resolved = false;
        }

        etMatRows = findViewById(R.id.et_matrix_size_rows);
        etMatCols = findViewById(R.id.et_matrix_size_cols);
        tvRta = findViewById(R.id.tv_rta);

        gridMat = findViewById(R.id.grid);
        gridRta = findViewById(R.id.grid_rta);

        tvRta.setVisibility(View.INVISIBLE);

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
                    int matCols = Integer.parseInt(charSequence.toString()) + 1;
                    matMain.changeSize(matMain.rows, matCols);
                    updateData();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        findViewById(R.id.btn_steps).setOnClickListener((view) ->
        {
            if (resolved && steps.size() != 0) {
                GaussStepsActivity.steps = steps;
                Intent intent = new Intent(GaussActivity.this, GaussStepsActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.btn_resolve).setOnClickListener((view) ->
        {
            Escalonador gaussJordan = new Escalonador(matMain.getCopy());
            gaussJordan.resolver();
            steps = gaussJordan.getSteps();
            if(steps.size() > 0) {
                matRta = gaussJordan.getMat();
                if (gaussJordan.getResult() == Escalonador.SCD)
                    tvRta.setText("Sistema Compatible Determinado");
                else if (gaussJordan.getResult() == Escalonador.SCI)
                    tvRta.setText("Sistema Compatible Indeterminado");
                else
                    tvRta.setText("Sistema Incompatible");
                showMatrix();
                resolved = true;
            }
        });

        findViewById(R.id.btn_back).setOnClickListener((view ->
                startActivity(new Intent(GaussActivity.this, MainActivity.class))));

        updateData();
    }

    void updateMatrix() {
        new Thread(() ->
        {
            runOnUiThread(() ->
            {
                gridMat.setVisibility(View.INVISIBLE);
                gridMat.removeAllViews();
                gridMat.setColumnCount(matMain.cols);
            });

            //add header
            for (int i = 0; i < matMain.cols; i++) {
                ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_matrix2, null, false);
                TextView tv2 = ll.findViewById(R.id.tv2);
                if (i == matMain.cols - 1) {
                    TextView tv = ll.findViewById(R.id.tv);
                    tv.setText("ti");
                    tv2.setText("");
                } else
                    tv2.setText("" + i);
                runOnUiThread(() -> gridMat.addView(ll));
            }

            for (int j = 0; j < matMain.rows; j++) {
                for (int i = 0; i < matMain.cols; i++) {
                    ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_matrix, null, false);
                    runOnUiThread(() -> gridMat.addView(ll));
                    EditText et = ll.findViewById(R.id.et);
                    if (matMain.mat[j][i].toFloat() != 0.f)
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
//                            if (!TextUtils.isDigitsOnly(charSequence) || charSequence.toString().isEmpty())
//                                return;
//                            int num = Integer.parseInt(charSequence.toString());
//                            Log.e(TAG, "onTextChanged: " + num);
//                            matMain.mat[finalJ][finalI] = new Num(num);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            String str = editable.toString();
//                            if (TextUtils.isDigitsOnly(editable.toString()) && !editable.toString().isEmpty()) {
                            if(!str.isEmpty()) {
                                boolean notN = str.length() == 1 && (str.charAt(0) == '-' || str.charAt(0) == '.') || str.contains("/");
                                if (!notN && numberFilter(str)) {
                                    double num = Double.parseDouble(editable.toString());
                                    Num val = new Num((long) (num * 10000), 10000);
                                    val.simplify();
                                    matMain.mat[finalJ][finalI] = val;
                                    Log.e(TAG, "afterTextChanged: changed");
                                }
                            }
                            Log.e(TAG, "afterTextChanged: \n" + matMain.toString());


                            if(!editable.equals(matMain.mat[finalJ][finalI]))
                                Log.e(TAG, "afterTextChanged: not equals");
                            else
                                Log.e(TAG, "afterTextChanged: equals");
                        }
                    });
                }
            }
            runOnUiThread(() -> gridMat.setVisibility(View.VISIBLE));
        }).start();
    }

    boolean numberFilter(String str)
    {
        boolean filter = false;
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i))
            {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '.':
                case '-':
                    filter = true;
                    break;
            }
        }
        return filter;
    }

    void updateData() {
        updateMatrix();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GaussActivity.this, MainActivity.class));
    }

    void showMatrix() {
        if (matRta == null)
            return;
        runOnUiThread(() -> gridRta.setVisibility(View.INVISIBLE));
        findViewById(R.id.sv_rta).setBackground(getDrawable(R.drawable.border));
        tvRta.setVisibility(View.VISIBLE);
        runOnUiThread(() ->
        {
            gridRta.removeAllViews();
            gridRta.setColumnCount(matRta.cols);
        });

        //add header
        for (int i = 0; i < matRta.cols; i++) {
            ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_matrix2, null, false);
            TextView tv2 = ll.findViewById(R.id.tv2);
            if (i == matRta.cols - 1) {
                TextView tv = ll.findViewById(R.id.tv);
                tv.setText("ti");
                tv2.setText("");
            } else
                tv2.setText("" + i);
            runOnUiThread(() -> gridRta.addView(ll));
        }

        for (int j = 0; j < matRta.rows; j++) {
            for (int i = 0; i < matRta.cols; i++) {
                ConstraintLayout ll = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_item_matrix, null, false);
                runOnUiThread(() -> gridRta.addView(ll));
                EditText et = ll.findViewById(R.id.et);
                et.setText(matRta.mat[j][i].toStr());
                et.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);
                et.setTextColor(Color.BLACK);
                et.setEnabled(false);
                et.setWidth(200);
            }
        }


        runOnUiThread(() -> gridRta.setVisibility(View.VISIBLE));
    }
}