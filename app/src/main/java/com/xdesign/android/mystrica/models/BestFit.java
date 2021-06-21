package com.xdesign.android.mystrica.models;

/**
 * @author keithkirk
 */
public class BestFit {

    private final double slope;
    private final double intercept;

    public BestFit(double slope, double intercept) {
        this.slope = slope;
        this.intercept = intercept;
    }

    public double getSlope() {
        return slope;
    }

    public double getIntercept() {
        return intercept;
    }

    public double getY(double x) {
        return (slope * x) + intercept;
    }
}
