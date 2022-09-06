package com.ims.matrixcalc.Gauss;

public class Mat
{
    public int cols, rows;
    public Num[][] mat;
    public Mat(Num[][] m)
    {
        rows = m.length;
        cols = m[0].length;
        mat = m;
    }
    public Mat()
    {
        rows = 1;
        cols = 1;
        mat = new Num[1][1];
        mat[0][0] = new Num(0);
    }

    public Mat(int rows, int cols, int n)
    {
        this.rows = rows;
        this.cols = cols;
        mat = new Num[rows][cols];
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                mat[j][i] = new Num(n);
            }
        }
    }

    public void changeSize(int rows, int cols)
    {
        Num[][] newMat = new Num[rows][cols];
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                newMat[j][i] = new Num(0);
            }
        }
        int rowMin = Math.min(this.rows, rows);
        int colMin = Math.min(this.cols, cols);
        for (int j = 0; j < rowMin; j++) {
            for (int i = 0; i < colMin; i++) {
                newMat[j][i] = mat[j][i].getCopy();
            }
        }
        this.rows = rows;
        this.cols = cols;
        mat = newMat;
    }
    public Mat getCopy()
    {
        Mat m = new Mat(rows, cols, 0);
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                m.mat[j][i] = mat[j][i];
            }
        }
        return m;
    }

    public String toString()
    {
        String str = "";
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                str += mat[j][i].toStr() + " ";
            }
            str += "\n";
        }
        return str;
    }
}
