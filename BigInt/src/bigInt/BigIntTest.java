package bigInt;

public class BigIntTest
{
	public static void main(String[] args)
	{
		BigInt a = new BigInt("491");
		BigInt b = new BigInt("-1312");
		
		System.out.printf("%s\n%s\n****\n", a, b);
		
		a.add(b);
		
		System.out.println(a);
	}
}