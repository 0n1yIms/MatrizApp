package com.ims.matrixcalc.Gauss;

public class Num {
    public long num;
    public long den = 1;

    public Num(long n) {
        num = n;
    }

    public Num(long n, long d) {
        num = n;
        den = d;
    }

    public Num sum(Num n) {
        long denominator = mcm(den, n.den);
        long numerator = ((denominator / den) * num) + ((denominator / n.den) * n.num);
        return new Num(numerator, denominator);
    }

    public Num res(Num n) {
        long denominator = mcm(den, n.den);
        long numerator = ((denominator / den) * num) - ((denominator / n.den) * n.num);
        return new Num(numerator, denominator);
    }

    public Num mult(Num n) {
        return new Num(num * n.num, den * n.den);
    }

    public Num div(Num n) {
        Num d = new Num(num * n.den, den * n.num);
        d.simplify();
        if(d.num == 0)
            d.den = 1;
        return d;
    }

    public void simplify()
    {
        long mcd = mcd(num, den);
        num = num / mcd;
        den = den / mcd;
        if(num == 0)
            den = 1;
    }

    public Num getCopy()
    {
        return new Num(num, den);
    }
    public String toStr()
    {
        simplify();
        if(den == 1)
            return "" + num;
        else
            return num + "/" + den;
    }
    public int toInt()
    {
        return (int) (num / den);
    }
    public float toFloat()
    {
        return (float) num / (float) den;
    }

    private long mcm(long a, long b)
    {
        return (a * b) / mcd(a, b);
    }
    private long mcd(long n1, long n2)
    {
        long a = n1 % n2;
        long b = n2;
        while(a != 0)
        {
            long res = b % a;
            b = a;
            a = res;
        }
        return b;
    }
}
