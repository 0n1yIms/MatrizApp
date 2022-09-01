package com.ims.matrixcalc.Gauss;

import android.util.Log;

import java.util.ArrayList;

public class Escalonador {
    private static final String TAG = "Escalonador";
    public static final int SCD = 0;
    public static final int SCI = 1;
    public static final int SI = 2;

    private Mat mat;
    private int result = -1;
    private ArrayList<MatInfo> steps = new ArrayList<>(0);

    public class MatInfo {
        public Mat matPrevious;
        public Mat matLater;
        public String info;

        public MatInfo(Mat matPrevious, Mat matLater, String info) {
            this.matPrevious = matPrevious;
            this.matLater = matLater;
            this.info = info;
        }
    }

    public Escalonador(Mat mat) {
        this.mat = mat;
    }

    public void resolver() {
        escalonar();
        invertSteps();
        result = analyzeResult();
    }
    public ArrayList<MatInfo> getSteps()
    {
        return (ArrayList<MatInfo>) steps.clone();
    }
    public Mat getMat()
    {
        return mat.getCopy();
    }
    public int getResult()
    {
        return result;
    }

    private void invertSteps()
    {
        ArrayList<MatInfo> info = (ArrayList<MatInfo>) steps.clone();
        steps.clear();
        for (int i = info.size() - 1; i >= 0; i--) {
            steps.add(info.get(i));
        }
    }
    private void escalonar() {
        for (int i = 0; i < Math.min(mat.cols - 1, mat.rows); i++) {
            if (check(i, i)) {
                gaussjordan(i, i);
            }
        }
    }

    private boolean check(int col, int fila) {
        if (mat.mat[fila][col].toInt() != 0)
            return true;
        else {
            for (int i = fila + 1; i < mat.rows; i++) {
                if (mat.mat[i][col].toInt() != 0) {
                    intercambiar(fila, i);
                    return true;
                }
            }
            return false;
        }
    }

    void intercambiar(int f1, int f2) {
        Num aux;
        Mat previousMat = mat.getCopy();
        for (int j = 0; j < mat.cols; j++) {
            aux = mat.mat[f1][j].getCopy();
            mat.mat[f1][j] = mat.mat[f2][j];
            mat.mat[f2][j] = aux;
        }

        String str = "Intercambio " + (f1 + 1) + " con " + (f2 + 1);
        steps.add(new MatInfo(previousMat, mat.getCopy(), str));
    }

    void gaussjordan(int col, int fila) {
        if (fila > 0)
            ceros_arriba(col, fila);
        if (fila < mat.rows - 1)
            ceros_abajo(col, fila);
    }

    void ceros_abajo(int col, int fila) {
        for (int i = fila + 1; i < mat.rows; i++) {
            if (mat.mat[i][col].toInt() != 0)
                hacer_cero(col, fila, i);
        }
    }

    void ceros_arriba(int col, int fila) {
        for (int i = fila - 1; i >= 0; i--) {
            if (mat.mat[i][col].toInt() != 0)
                hacer_cero(col, fila, i);
        }
    }

    void hacer_cero(int col, int fpivote, int fcero) {
//            Log.e(TAG, str);
        String str = "f" + (fcero + 1) + " = " +
                mat.mat[fpivote][col].toStr() + "*f" + (fcero + 1) +
                " - " + mat.mat[fcero][col].toStr() + "*f" + (fpivote + 1);
        Mat previousMat = mat.getCopy();

        Num nro_pivote = mat.mat[fpivote][col].getCopy();
        Num nro_cero = mat.mat[fcero][col].getCopy();
        for (int j = 0; j < mat.cols; j++) {
            Num n1 = mat.mat[fcero][j].mult(nro_pivote);
            Num n2 = mat.mat[fpivote][j].mult(nro_cero);
            mat.mat[fcero][j] = n1.res(n2);
        }

        steps.add(new MatInfo(previousMat, mat.getCopy(), str));
//            mostrar_matriz();
    }

    int analyzeResult()
    {
        int nullRows = 0;
        int incompatibleRows = 0;
        for (int r = 0; r < mat.rows; r++) {
            boolean isNullRow = true;
            for (int c = 0; c < mat.cols; c++) {
                if(mat.mat[r][c].toFloat() != 0.f) {
                    isNullRow = false;
                    break;
                }
            }
            boolean isIncompatibleRow = true;
            for (int c = 0; c < mat.cols; c++) {
                if(c < mat.cols - 1 && mat.mat[r][c].toFloat() != 0.f || c == mat.cols - 1 && mat.mat[r][c].toFloat() == 0.f) {
                    isIncompatibleRow = false;
                    break;
                }
            }

            if(isNullRow)
                nullRows++;
            if(isIncompatibleRow)
                incompatibleRows++;
        }
        if(incompatibleRows > 0)
            return SI;
        if(mat.rows - nullRows == mat.cols - 1)
            return SCD;
        else
            return SCI;
    }

    void mostrar_matriz() {

        String str = "";
        for (int j = 0; j < mat.rows; j++) {
            for (int i = 0; i < mat.cols; i++) {
                str += mat.mat[j][i].toStr() + " ";
            }
            str += "\n";
        }
        Log.e("", str);
    }
}


