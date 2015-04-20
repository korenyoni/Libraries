package bigInt;

import java.util.ArrayList;

/*Class BigInt*/
// my implementation of Big Integer
// behaves like a linked list
public class BigInt {
	/*Attributes*/
	private ArrayList<Integer> digits; // the digits of the BigInt
		// they are represented in reverse order
	private boolean negative;
	/*Methods used in the constructor*/
	private void populateDigits(long number)
	{
		this.digits = new ArrayList<Integer>();
		int size = 0; // represents the number of digits in the number
		long test = number; // used to test the number of digits in the number
		while (test > 0)
		{
			test /= 10; // truncate the test by 1 digit
			size += 1; // increase the size by a digit
		}
		for (int i = 0; i < size; i++)
		{
			digits.add((int)(number % 10)); // add the last digit of the number
			number /= 10; // truncate the number by 10
		}
	}
	/*default constructor*/
	public BigInt()
	{
		this.digits = new ArrayList<Integer>();
		digits.add(0);
		this.negative = false;
			// not negative
	}
	// integer constructor
	public BigInt(int number)
	{
		populateDigits((long)number);
		negative = number < 0;
			// negative if the number is less than 0
	}
	// long constructor
	public BigInt(long number)
	{
		populateDigits(number);
		negative = number < 0;
			// negative if the number is less than 0
	}
	// string constructor
	public BigInt(String number)
	{
		this.digits = new ArrayList<Integer>();
		char[] charArray = number.toCharArray();
		if (!number.matches("-\\d+") && !number.matches("\\d+"))
			// if the user does not enter a number,
			// throw an exception
		{
			throw new IllegalArgumentException(
			"\nMust enter a number with or without a negative sign at the front.");
		}
		negative = number.matches("-\\d+");
        // if the string is in the format "-(number)", then it is negative
		for (int i = number.length() - 1; i >= 0; i--)
		{
			// parse the character at the given index to an integer
			this.digits.add(Integer.parseInt(Character.toString(charArray[i])));
			if (i == 1 && negative)
				// if the number is negative and we are almost at the negative sign
				// stop this loop by having it not meet the condition
			{
				i = 0;
			}
		}

	}

	/*Fill the BigInt object with 0's*/
	// used to ensure capacity
	private void fill(int minCapacity)
	{
		int size = this.digits.size();
		for (int i = 0; i < minCapacity - size; i++)
		{
			this.digits.add(0);
			// add a 0 to the end of the ArrayList
		}
	}
	/*private methods used locally for addition, multiplication etc*/
	/*Leaves the number column in place and carries the other digits over*/
	 // carries to the right of the ArrayList
	 //(in non-BigInt notation this would be carrying it over to the left)
	private void carry()
	{
		int minCapacity = this.digits.size();
			// the capacity of this BigInt object
		for (int i = 0; i < this.digits.size(); i++)
		{
			int element = this.digits.get(i);
			int lastDigitElement = 0;
			int carry = 0;
			if (element > 9)
				// if the number is bigger then 9 or smaller then 0
			{
				// extend the BigInt's ArrayList by one if the next index is out of bounds
				if (i + 1 >= this.digits.size())
				{
					minCapacity++;
					this.fill(minCapacity);
				}

				lastDigitElement = element % 10;
				// remainder ten i.e. the last digit
				carry = element / 10;

				this.digits.set(i + 1, this.digits.get(i + 1) + carry);
				this.digits.set(i, lastDigitElement);
				// set the next digit to be the current element plus the amount carried over
				// set the current element to simply be the last digit thereof
			}
			else if (element < 0 && i + 1 < minCapacity)
			{
				this.digits.set(i + 1, this.digits.get(i + 1) - 1);
				this.digits.set(i, 10 + this.digits.get(i));
			}
		}

		if (this.digits.get(this.digits.size() - 1) < 0)
		{
			this.deNegate();
		}

	}
	/*if the BigInt is negative, negate all the digits*/
	private void negate()
	{
		for (int i = 0; i < this.digits.size(); i++)
		{
			this.digits.set(i, this.digits.get(i) * - 1);
		}
		/**
		 * 1 9 1	1 8 11		0 1 8 1
		 * 2 1 3 	3 1 2		 -3-1-2
		 * ***		**			***
		 * 		    1 1			  8 7 9
		 * 		   -2 7 9
		 * 			1 2 1
		 */
	}
	/*Represent a negative number*/
	// if the the last digit of an ArrayList is negative, set the number to be
	// zero minus the entire BigInt
	// this happens when subtracting a larger number from a smaller number
	// and achieving a negative result
	private void deNegate()
	{
		BigInt result = new BigInt();
		result.fill(this.digits.size());
		// create a BigInt object to store the final result

		int size = this.digits.size();
		for (int i = 0; i < size ; i++)
		{
			if (this.digits.get(i) < 0)
			{
				result.digits.set(i, Math.abs(this.digits.get(i)));
				// if the number is negative then the difference from 0
				// is the absolute value of that number
			}
			else if (this.digits.get(i) != 0)
			{
				// otherwise the difference is from 10 (because it cannot
				//exceed 9)
				result.digits.set(i, 10 - this.digits.get(i));
			}
			if (i + 1 < size && this.digits.get(i) != 0)
			{
				this.digits.set(i + 1, this.digits.get(i + 1) + 1);
			}
		}
		this.digits = result.digits;
		this.negative = true;
		// this number is negative.
	}
	/*Removes zeros at the beginning of the number*/
	private void trimZeros()
	{
		boolean stop = false;
		int size = this.digits.size();
		// go through the ArrayList backwards
		// remove all zeros until you reach a number that is not zero
		// stop the loop
		for (int i = size - 1; i > 0 && !stop; i--)
		{
			if (this.digits.get(i) == 0)
			{
				this.digits.remove(i);
			}
			else
			{
				stop = true;
			}
		}
	}
	/*Public methods*/
	/*Returns true if this BigInt is bigger than the other*/
	// the BigInt with the most digits is bigger
	// if the number of digits is the same, look at the last digit until one is bigger
	public boolean greaterThan(BigInt other)
	{
		boolean biggerThan = false;
		boolean same = false; // the digits are the same
			// (regardless of negative sign)

		if (this.digits.size() == other.digits.size())
		{
			int size = this.digits.size();
			// the size of either the objects
			for (int i = size - 1; i >= 0; i--)
			{
				if (this.digits.get(i) != other.digits.get(i))
				{
					biggerThan = this.digits.get(i) > other.digits.get(i);
					// if the current digit for this is bigger than other,
					// then this is bigger than other
					if (this.negative && !other.negative
							|| !this.negative && other.negative)
					{
						biggerThan = !biggerThan;
						// however the opposite is true if this is negative
					}
					return biggerThan;
				}
				if (i == 0)
					// if every digit is the same...
				{
					same = true;
				}
			}
		}
		biggerThan = this.digits.size() > other.digits.size();

		if (this.negative && !same)
		{
			biggerThan = !biggerThan;
		}

		return biggerThan;
	}
	/*Add another BigInt to this BigInt*/
	public void add(BigInt other)
	{
		// copy the two argument BigIntnegative = number.matches("-\\d+"); by using their toString
		// and the BigInt(String) constructor
		int minCapacity = Math.max(this.digits.size(), other.digits.size());
			// the minimum capacity both BigInt objects have to
		this.fill(minCapacity);
		other.fill(minCapacity);
			// ensure that both BigInt objects have the same number of digits
			// in their digits (ArrayList<Integer>) attribute
		/*Negate any negative BigInts*/
		if (this.negative)
		{
			this.negate();
		}
		if (other.negative)
		{
			other.negate();
		}
		/*Add all the digits for each number column*/
		for (int i = 0; i < minCapacity; i++)
		{
			int thisElement = this.digits.get(i);
			int otherElement = other.digits.get(i);
			// if other is negative, then we are going to subtract instead of add
			this.digits.set(i, thisElement + otherElement);
				// add the two digits
		}

		this.carry(); // carry the ones
		this.trimZeros();// trim the zeros for this BigInt object
			// (remove zeros at the beginning of the number)

	}
	/*Subtraction method*/
	// subtract other BigInt from this BigInt
	public void subtract(BigInt other)
	{
		other.negative = !other.negative;
		this.add(other);
	}
	/*Multiplication method*/
	// BigInt this is called on is the one that is multiplied
	public void multiply(BigInt other)
	{
		// the BigInt object to store the result in temporarily
		BigInt result = new BigInt();
		int maxDigits = Math.max(this.digits.size(), other.digits.size());
		result.fill(maxDigits); // fill the result so it contains as many
			// zeros as the largest object's digit ArrayList
		// long multiplication
		// multiply each digit of the second number by each digit in the first number
		// each time you move on to another digit in the first number, skip a space
		// carry the one to the right if the column exceeds 9

		int indexSpace = 0; // number of spaces to be skipped (you skip after finishing a digit
		// in the first number)
		for (int i = 0; i < this.digits.size(); i++)
			// loop through the first number's digits
		{
			for (int n = 0; n < other.digits.size(); n++)
				// loop through the second number's digits
			{
				int nextIndex = indexSpace + n + 1;
				if (result.digits.size() >= nextIndex)
				{
					maxDigits++;
					result.fill(maxDigits);
				}
				int product = this.digits.get(i) * other.digits.get(n);
				int thisElement = result.digits.get(indexSpace + n);
				thisElement += product;
				result.digits.set(indexSpace + n, thisElement);
				// set the product for the current space
			}
			indexSpace++; // skip a space
		}
		result.trimZeros(); // trim the zeros (remove zeros at the beginning of the number)
		result.carry(); // carry the ones
		System.out.println(result.digits);
		this.digits = result.digits;
		this.negative = ((this.negative && !other.negative) ||
				(other.negative && !this.negative));
		// like signs make positive, unlike make negative
	}
	@Override
	/* Returns the String representation of the BigInt*/
	// adds negative sign if negative
	public String toString()
	{
		String string = "";
		if (negative) {string += "-";}
		for (int i = this.digits.size() - 1; i >= 0; i--)
		{
			string = new String(string + Math.abs(digits.get(i)));
		}
		return string;
	}
}
