import java.util.Random;
import static java.lang.Math.*;

public abstract class Distribution {
    protected Random rnd;

    public Distribution() {
        rnd = new Random();
    }

    public Distribution setSeed(long seed) {
        rnd.setSeed(seed);
        return this;
    }

    protected int fact(int n) {
        if (n == 0) 
            return 1;
        else
            return n * fact(n-1);
    }

    public abstract double draw();
}
