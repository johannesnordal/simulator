import java.util.function.DoubleUnaryOperator;

public class Demo {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        DoubleUnaryOperator[] function = {
            x ->  1.0/x, x ->  x/1.0,
            x -> -1.0/x, x -> -x/1.0
        };
        for (int i = 1; i < n; i++) {
            for (DoubleUnaryOperator fn : function)
                System.out.printf("%f\t", fn.applyAsDouble(i));
            System.out.println();
        }
    }
}
