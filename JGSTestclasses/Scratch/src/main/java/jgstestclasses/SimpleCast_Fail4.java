package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;

/**
 * Created by Nicolas Müller on 16.03.17.
 * Same as SimpleCast_Fail2, but using string instead of ints
 */
public class SimpleCast_Fail4 {
    @Constraints({"LOW <= @0"})
    public static void main(String[] args) {
        String v = "42";
        String x = Casts.cast("HIGH ~> ?", v);
        System.out.println(Casts.cast("? ~> LOW", x));
    }
}
