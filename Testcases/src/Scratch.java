
// Quick throw-away examples and testcases for the security type checker
import static security.Definition.*;

public class Scratch {
    
    @FieldSecurity("high")
    boolean high;
    @FieldSecurity("low")
    boolean f;

    @Constraints({"@pc <= low"})
    public boolean leak(boolean init) {
        this.high = init;
        Scratch x = new Scratch();
        x.f = true;
        Scratch y = x;
        if (this.high) {
            y = new Scratch();
        }
        y.f = false;
        return x.f;
    }
   
    @Constraints({"@pc <= low"})
    public static void main(String[] args) {
        Scratch s = new Scratch();
        boolean result1 = s.leak(true);
        boolean result2 = s.leak(false);
        System.out.print(String.format("%s %s", result1, result2));
        if (result1 != result2) {
            System.out.println(": There was a leak!");
        } else {
            System.out.println(": The program is secure!");
        }
    }
}