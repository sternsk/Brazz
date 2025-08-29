package com.example;

class Basis {
    final double[] u;  // Ost-West (phi+, "Osten")
    final double[] v;  // Nord-Süd (hier: "Norden")
    final double[] n;  // Normalenrichtung (radial, nach außen)
    Basis(double[] u, double[] v, double[] n){ this.u=u; this.v=v; this.n=n; }
}


