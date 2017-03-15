package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.Sec;

/**
 * Created by Nicolas Müller on 14.03.17.
 */
public class SimpleCasts {

    @Constraints({"LOW <= @0 "})
    public static void main(String[] args) {
        String hello1 = null;
        System.out.println(attemptFailingCast());
        String hello = (aStaticMethodWithCasts(hello1)); // fails at run-time due to an bad cast in aStaticMethodWithCasts
    }

    @Constraints({"LOW <= @ret"})
    static String attemptFailingCast() {
        String x = Casts.cast("HIGH ~> ?", "secret: 42");
        return Casts.cast("? ~> LOW", x);
    }

    @Constraints({"HIGH <= @0 ", "@0 <= @ret"})
    @Effects({})
    static String aStaticMethodWithCasts(String i) {
        String x = Casts.cast("HIGH ~> ?", i);
        String result;
        if (i == null) {
            System.out.println("Attempting that illegal cast!");
            result =  Casts.cast("? ~> LOW", x);
        } else {
            result = null;
        }
        return result;
    }

}
