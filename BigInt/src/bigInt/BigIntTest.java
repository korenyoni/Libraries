package bigInt;

public class BigIntTest
{
	public static void main(String[] args)
	{
		BigInt a = new BigInt("0");
		BigInt b = new BigInt("1491");
		
		System.out.printf("%s\n%s\n****\n", a, b);
		
		a.subtract(b);
		
		System.out.println(a);
	}
}