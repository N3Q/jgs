package testclasses;

import testclasses.utils.C;
import utils.analyzer.HelperClass;

public class NSU_FieldAccess {
	//static int f = 0;
	public static void main(String[] args) {

		C b = new C();
		C c = HelperClass.makeHigh(b);
		c.f = true; // should throw an error, since we access access f through
					// high-sec c and PC = LOW
	}
}
