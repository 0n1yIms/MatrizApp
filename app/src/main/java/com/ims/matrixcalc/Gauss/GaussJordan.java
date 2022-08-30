package com.ims.matrixcalc.Gauss;

import android.util.Log;

import java.util.ArrayList;

public class GaussJordan {
    private static final String TAG = "GaussJordan";
    public static final int RESULT_SCD = 0;
    public static final int RESULT_SCI = 1;
    public static final int RESULT_SI = 2;

    public GaussJordan() {

    }

    private ArrayList<Mat> steps = new ArrayList<>(1);

    public Mat calc(Mat m) {
        Escalonador escalonador = new Escalonador(m);
        return escalonador.resolver();
    }


    public static class Escalonador {
        private static final String TAG = "Escalonador";
        private static final int PIVOTE_NULO = 0;
        private static final int PIVOTE_UBICADO = 1;
        private Mat mat;

        public Escalonador(Mat mat) {
            this.mat = mat;
        }

        public Mat resolver()
        {
            escalonar();
            return mat;
        }

        private void escalonar() {
            mostrar_matriz();
            int fila = 0;
            for (int col = 0; col < mat.cols - 1; col++) {
                if (ubicar_pivote(col, fila) == PIVOTE_UBICADO) {
                    gaussjordan(col, fila);
                    fila++;
                }
            }
        }

        private int ubicar_pivote(int col, int fila) {
            int sol;
            int i = fila;

            if (mat.mat[i][col].toInt() != 0)
                sol = PIVOTE_UBICADO;
            else
            {
                while (mat.mat[i][col].toInt() == 0 && i < mat.rows - 1) i++;
                if (i < mat.rows) {
                    intercambiar(fila, i);
                    sol = PIVOTE_UBICADO;
                } else
                    sol = PIVOTE_NULO;
            }
            return sol;
        }

        void intercambiar(int f1, int f2) {
            Num aux;
            for (int j = 0; j < mat.cols; j++) {
                aux = mat.mat[f1][j].getCopy();
                mat.mat[f1][j] = mat.mat[f2][j];
                mat.mat[f2][j] = aux;
            }
            String str =  "Intercambio " + f1  + " con " + f2;
            Log.e(TAG,str);
            mostrar_matriz();
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
            String str = "f" + fcero + " = " + mat.mat[fpivote][col].toStr() + " * f" + fcero + " - f" + fpivote + " * " + mat.mat[fcero][col].toStr();
            Log.e(TAG, str);
            int j;
            Num nro_pivote, nro_cero;
            nro_pivote = mat.mat[fpivote][col].getCopy();
            nro_cero = mat.mat[fcero][col].getCopy();
            for (j = 0; j < mat.cols; j++) {
                Num n1 = mat.mat[fcero][j].mult(nro_pivote);
                Num n2 = mat.mat[fpivote][j].mult(nro_cero);
                mat.mat[fcero][j] = n1.res(n2);
            }
            mostrar_matriz();
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


}
