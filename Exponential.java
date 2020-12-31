import java.util.Random;

public class Exponential extends Distribution 
{
    private double scale;

    public Exponential(double scale) {
        this.scale = scale;
    }

    public double draw() {
        return (-Math.log(1 - rnd.nextDouble())) / scale;
    }
}
