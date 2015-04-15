package bigInt;

public class BigIntTest
{
	public static void main(String[] args)
	{
		BigInt a = new BigInt("199");
		BigInt b = new BigInt("100");
		
		System.out.printf("%s\n%s\n****\n", a, b);
		
		a.add(b);
		
		System.out.println(a);
	}
}