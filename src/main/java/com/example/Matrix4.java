package com.example;

class Matrix4 {
    double[][] m = new double[4][4];

    Matrix4() { identity(); }

    Matrix4 identity() {
        for (int i=0;i<4;i++) for (int j=0;j<4;j++) m[i][j] = 0;
        for (int i=0;i<4;i++) m[i][i] = 1;
        return this;
    }

    Matrix4 multiply(Matrix4 other) {
        double[][] r = new double[4][4];
        for (int i=0;i<4;i++)
            for (int j=0;j<4;j++)
                for (int k=0;k<4;k++)
                    r[i][j] += m[i][k] * other.m[k][j];
        m = r;
        return this;
    }

    Matrix4 translate(double x, double y, double z) {
        Matrix4 t = new Matrix4();
        t.m[0][3] = x;
        t.m[1][3] = y;
        t.m[2][3] = z;
        return multiply(t);
    }

    Matrix4 rotateX(double angle) {
        double c = Math.cos(angle), s = Math.sin(angle);
        Matrix4 r = new Matrix4();
        r.m[1][1] = c;  r.m[1][2] = -s;
        r.m[2][1] = s;  r.m[2][2] = c;
        return multiply(r);
    }

    Matrix4 rotateY(double angle) {
        double c = Math.cos(angle), s = Math.sin(angle);
        Matrix4 r = new Matrix4();
        r.m[0][0] = c;  r.m[0][2] = s;
        r.m[2][0] = -s; r.m[2][2] = c;
        return multiply(r);
    }

    Matrix4 rotateZ(double angle) {
        double c = Math.cos(angle), s = Math.sin(angle);
        Matrix4 r = new Matrix4();
        r.m[0][0] = c;  r.m[0][1] = -s;
        r.m[1][0] = s;  r.m[1][1] = c;
        return multiply(r);
    }

    // Mit Pivot:
    Matrix4 rotateX(double angle, double px, double py, double pz) {
        return translate(px, py, pz)
              .rotateX(angle)
              .translate(-px, -py, -pz);
    }

    Matrix4 rotateY(double angle, double px, double py, double pz) {
        return translate(px, py, pz)
              .rotateY(angle)
              .translate(-px, -py, -pz);
    }

    Matrix4 rotateZ(double angle, double px, double py, double pz) {
        return translate(px, py, pz)
              .rotateZ(angle)
              .translate(-px, -py, -pz);
    }
}
