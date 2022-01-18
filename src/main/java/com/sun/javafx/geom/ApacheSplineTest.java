package com.sun.javafx.geom;

import java.util.Arrays;

import org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class ApacheSplineTest {
	
	public static void main(String[] main) {
	    double [] xDouble = new double [] {24.0, 35.0, 67.0, 78.0,79.0};
	    double [] yDouble = new double [] {13.0, 45.0, 8.0, 45.0, 23.0};

	    AkimaSplineInterpolator asi = new AkimaSplineInterpolator();
	    PolynomialSplineFunction psf = asi.interpolate(xDouble, yDouble);

	    for (PolynomialFunction pf : psf.getPolynomials()) {
	        System.out.println(pf.polynomialDerivative());  
	    }
	    System.out.println(Arrays.toString( psf.getKnots()));
	}

}
