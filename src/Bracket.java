package spool;

import java.util.function.DoubleUnaryOperator;
import java.util.function.DoublePredicate;
import java.util.OptionalDouble;
import static java.lang.Math.abs;

public class Bracket
{
    private final int MAX_ITER = 10000;
    private DoubleUnaryOperator fn;
    private OptionalDouble lower;
    private OptionalDouble upper;

    public Bracket(DoubleUnaryOperator fn)
    {
        this.fn = fn;
        lower = calculateBound(x -> x <= 0);
        upper = calculateBound(x -> x >= 0); 
    }

    private OptionalDouble calculateBound(DoublePredicate p)
    {
        DoubleUnaryOperator[] parameters = {
            x ->  x/1.0, x ->  1.0/x,
        };

        for (int i = 1; i <= MAX_ITER; i += 10)
        {
            for (DoubleUnaryOperator param : parameters)
            {
                double u = param.applyAsDouble(i);

                if (p.test(fn.applyAsDouble(u)))
                {
                    return OptionalDouble.of(u);
                }
            }
        }

        return OptionalDouble.empty();
    }

    public boolean isBracketing()
    {
        return lower.isPresent() && upper.isPresent();
    }

    public OptionalDouble lower()
    {
        return lower;
    }

    public OptionalDouble upper()
    {
        return upper;
    }

    public static void main(String[] args) {
        DoubleUnaryOperator fn = (x) -> {
          return Math.sin(x) * Math.pow(Math.cos(x), 2);
        };
        Bracket bracket = new Bracket(fn);
        System.out.println(bracket.isBracketing());
        System.out.println(bracket.lower().getAsDouble());
        System.out.println(bracket.upper().getAsDouble());
    }
}
