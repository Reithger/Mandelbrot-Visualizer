import java.math.BigDecimal;
import java.math.MathContext;

/*
 * Ada Clevinger
 * April 10, 2018
 * Assignment 3
 * Class: Complex
 * 
 * This is a helper class for the MandelbrotViewer class that represents Complex values, which are
 * composed of two elements: Real and Complex values. Allows for Adding, Subtracting, Multiplying, and
 * performing Modulus.
 * 
 * Additional Feature: Multiple versions of the multiply() method to allow for variations of the Mandelbrot
 * to be calculated.
 * 
 */

/**
 * This is a helper class for the MandelbrotViewer class that represents Complex values, which are
 * composed of two elements: Real and Complex values. Allows for Adding, Subtracting, Multiplying, and
 * performing Modulus.
 *  
 * @author Ada Clevinger
 * @version 1.0.0
 * @since 2018-03-26
 */

public class ComplexBigDecimal {
							/** Instance Variable of double type for the Real number value of the Complex object */
	private BigDecimal real;	/** Instance Variable of double type for the Complex number value of the Complex object */
	private BigDecimal complex; 
	
	/**
	 * This method constructs a new Complex Object from two double values representing
	 * its Real and Complex values.
	 * 
	 * @param rl This parameter is the Real value of the new Complex object.
	 * @param cmplx This parameter is the Complex value of the new Complex object.
	 */
	
	public ComplexBigDecimal(double rl, double cmplx){
		real = new BigDecimal(rl);
		complex = new BigDecimal(cmplx);
	}
	
	
	/**
	 * This method constructs a new Complex Object from a pre-existing Complex object,
	 * copying the values over.
	 * 
	 * @param c This parameter is a Complex object that's values are copied.
	 */
	public ComplexBigDecimal(ComplexBigDecimal c){
		real = c.getReal();
		complex = c.getComplex();
	}
	
	/**
	 * This method returns the Real value instance variable 
	 * 
	 * @return Returns the value of the instance variable Real associated to this object
	 */
	
	public BigDecimal getReal(){
		return real;
	}
	
	/**
	 * This method returns the Complex value instance variable
	 * 
	 * @return Returns the value of the instance variable Complex associated to this object
	 */
	
	public BigDecimal getComplex(){
		return complex;
	}
	
	/**
	 * This method calculates the summation of two Complex objects, saving the
	 * produced values to the Complex object calling this method.
	 * 
	 * @param val This parameter is a Complex object that's values are added to the calling Complex object.
	 */
	
	public void add(ComplexBigDecimal val){
		real = real.add(val.getReal());
		complex = complex.add(val.getComplex());
	}
	
	/**
	 * This method calculates the difference of two Complex objects, saving the
	 * produced values to the Complex object calling this method.
	 * 
	 * @param val This parameter is a Complex object that's values are deducted from the calling Complex object.
	 */
	
	public void subtract(ComplexBigDecimal val){
		real = real.subtract(val.getReal());
		complex = complex.subtract(val.getComplex());
	}
	
	/**
	 * This method calculates the product of two Complex objects, saving the
	 * produced values to the Complex object calling this method.
	 * 
	 * @param val This parameter is a Complex object that's values are multiplied to the calling Complex object.
	 */
	
	public void multiply(ComplexBigDecimal val){
		BigDecimal copyReal = getReal();	//Values are copied due to issues of values changing between equations; maintains consistency.
		BigDecimal copyCompl = getComplex();
		BigDecimal othReal = val.getReal();
		BigDecimal othCompl = val.getComplex();
		real = (copyReal.multiply(othReal)).subtract(copyCompl.multiply(othCompl));
		complex = (copyReal.multiply(othCompl)).add(copyCompl.multiply(othReal));
		//This is a segment of code left in because, while it was wrong for the Mandelbrot, it looked really cool and I want easy access to getting it again.
		//The problem was that, in the second equation, it recognized that the first had changed when it should have been using the original Real value.
		/*
		  real = getReal() * val.getReal() - getComplex() * val.getComplex();
		  complex = getReal() * val.getComplex() + getComplex() * val.getReal();
		*/
	}
	
	/**
	 * This method is a variation of the previously described multiply(Complex c) method;
	 * the calculation of the Complex variable is done using the Real value calculated in the
	 * same process, instead of using values from before any calculations are performed.
	 * 
	 * @param val This parameter is a Complex object that's values are multiplied to the calling Complex object.
	 */
	
	public void multiplyVar1(ComplexBigDecimal val){
		real = (real.multiply(val.getReal())).subtract(complex.multiply(val.getComplex()));
		complex = (real.multiply(val.getComplex())).add(complex.multiply(val.getReal()));
	}
	
	/**
	 * This method is the second variation of the previously described multiply(Complex c) method;
	 * the calculation of the Real variable is done using the Complex value calculated in the
	 * same process, instead of using values from before any calculations are performed.
	 * 
	 * @param val This parameter is a Complex object that's values are multiplied to the calling Complex object.
	 */
	
	public void multiplyVar2(ComplexBigDecimal val){
		complex = (real.multiply(val.getComplex())).add(complex.multiply(val.getReal()));
		real = (real.multiply(val.getReal())).subtract(complex.multiply(val.getComplex()));
	}
	
	/**
	 * This method calculates the distance of this Complex value from the origin using
	 * the pythagoras theorem to do so.
	 * 
	 * @return Returns a double value that is the square root of the sum of the Real and Complex values squared.
	 */
	
	public BigDecimal modulus(){
		return ((getReal().pow(2)).add(getComplex().pow(2))).sqrt(MathContext.DECIMAL32);
	}
	
}