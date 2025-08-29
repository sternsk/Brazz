package com.example;

class Vector4{
    double[] v = new double[4];

    Vector4(){
        basisVector();
    }

    Vector4(double x, double y, double z){
        v[0] = x;
        v[1] = y;
        v[2] = z;
        v[3] = 1;
    }

    Vector4 basisVector(){
        for (int i = 0; i < 3; i++){
            v[0] = 0;
        }
        v[3] = 1;
        return this;
    }

    void add(Vector4 other){
        v[0] += other.v[0];
        v[1] += other.v[1];
        v[2] += other.v[2];
        v[3] += other.v[3];
    }

    void multiplyMatrix(Matrix4 matrix){
        double[] result = new double[4];

        for (int row = 0; row < 4; row++) {
            result[row] = 0;
            for (int col = 0; col < 4; col++) {
                result[row] += matrix.m[row][col] * v[col];
            }
        }

        for(int i = 0; i<4; i++){
            v[i] = result[i];
        }
    }
}