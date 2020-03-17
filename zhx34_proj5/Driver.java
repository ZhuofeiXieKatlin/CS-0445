import java.util.*;

public class Driver {
	public static void main(String[] args) {
		Map<String, Double> vars = new HashMap<>();
		// you can change or add more variables here.
		vars.put("x", 10.0);
		vars.put("y", 27.0);
		vars.put("w",2.0);

		Expression expr = Expression.quickParse("4*x + y/9 + 12^w");

		System.out.println("toString:        " + expr);
		System.out.println("toPostfix:       " + expr.toPostfix());
		System.out.println("evaluate:        " + expr.evaluate(vars));
		System.out.println("reciprocal:      " + expr.reciprocal());
		System.out.println("reciprocal(num): " + Expression.Number(187).reciprocal());
		System.out.println("reciprocal(div): " + Expression.quickParse("9 /x").reciprocal());
		System.out.println("getVariables:    " + expr.getVariables());

		Expression mean = Expression.geometricMean(new double[]{4,9,3,7,6});
		System.out.println("geometricMean:   " + mean);
		System.out.println("it evalutes to:  " + mean.evaluate(vars));

		System.out.println("===================================================");
		System.out.println("NOW TEST MORE THOROUGHLY!!!");
	}
}