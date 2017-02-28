package testclasses;


import utils.analyzer.HelperClass;

/**
 * Array access test which does not violate information flow policy.
 * @author Nicolas Müller
 */
public class ArrayRefSuccess {

	public static void main(String[] args) {
		String secret = read()[1];
		String[] pub = {"x", "y", HelperClass.makeLow(secret)};
		System.out.println(pub[2]);
	}

	public static String[] read() {
		String secret = "42";
		secret = HelperClass.makeHigh(secret);
		return new String[]{"41", secret, "43"};
	}
	
}