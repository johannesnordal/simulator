import java.lang.reflect.*;
import java.util.*;
import spool.*;

public class Program
{
    public static void main(String[] args)
    {
        Client x = new Client(0.0, 1.0);
        Client y = x.clone();

        Distribution arrival = new Exponential(1.0);
        System.out.println(Exponential.class == arrival.getClass());
    }
}
