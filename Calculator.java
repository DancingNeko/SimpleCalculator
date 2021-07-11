import java.util.Scanner;

public class Calculator {
	String expression;
	public Calculator()
	{
		calculate();
	}
	public void calculate()
	{
		Scanner input = new Scanner(System.in);
		while(true)
		{
			expression = input.nextLine();
			expression = convert(expression);
			while(true){
				String result = trim(expression);
				if(result.equals("error"))
					break;
				else
					expression = result;
			}
			expression = ""+simpleCalculate(expression);
			expression.replace('@', '-');
			System.out.println(expression);
		}
	}
	public String trim(String e)
	{
		int begin = e.lastIndexOf('(');
		if(begin == -1)
			return "error";
		int end = e.substring(begin).indexOf(')') + begin;
		String result = simpleCalculate(e.substring(begin + 1, end));
		e = e.substring(0,begin) + result + e.substring(end + 1);
		return e;
	}
	public String simpleCalculate(String e)
	{
		int mIndex = e.indexOf('*');
		int dIndex = e.indexOf('/');
		while(mIndex != -1 || dIndex != -1)
		{
			double product;
			if(mIndex == -1) mIndex = Integer.MAX_VALUE;
			if(dIndex == -1) dIndex = Integer.MAX_VALUE;
			if(mIndex < dIndex)
			{
				BetterDouble A = getNumber(e, mIndex, true);
				BetterDouble B = getNumber(e, mIndex, false);
				double a = A.number;
				double b = B.number;
				product = a*b;
				String a1 = String.format("%."+A.decimalDigits+"f", Math.abs(a)),b1 =String.format("%."+B.decimalDigits+"f", Math.abs(b));
				if(a<0)
					a1 = "@"+a1;
				if(b<0)
					b1 = "@"+b1;
				String p1 = Math.abs(product)+"";
				if(p1.length() - p1.indexOf('.') - 1 > 5)
					p1 = p1.substring(0, p1.indexOf('.')+6);
				if(product < 0)
					p1 = "@"+p1;
				e = e.replace(a1+"*"+b1, p1);
			}
			else
			{
				BetterDouble A = getNumber(e, dIndex, true);
				BetterDouble B = getNumber(e, dIndex, false);
				double a = A.number;
				double b = B.number;
				product = a/b;
				String a1 = String.format("%."+A.decimalDigits+"f", Math.abs(a)),b1 =String.format("%."+B.decimalDigits+"f", Math.abs(b));
				if(a<0)
					a1 = "@"+a1;
				if(b<0)
					b1 = "@"+b1;
				String p1 = Math.abs(product)+"";
				if(p1.length() - p1.indexOf('.') - 1 > 5)
					p1 = p1.substring(0, p1.indexOf('.')+6);
				if(product < 0)
					p1 = "@"+p1;
				e = e.replace(a1+"/"+b1, p1);
			}
			mIndex = e.indexOf('*');
			dIndex = e.indexOf('/');
		}
		int pIndex = e.indexOf('+');
		int miIndex = e.indexOf('-');
		while(pIndex != -1 || miIndex != -1)
		{
			double product;
			if(pIndex == -1) pIndex = Integer.MAX_VALUE;
			if(miIndex == -1) miIndex = Integer.MAX_VALUE;
			if(pIndex < miIndex)
			{
				BetterDouble A = getNumber(e, pIndex, true);
				BetterDouble B = getNumber(e, pIndex, false);
				double a = A.number;
				double b = B.number;
				product = a+b;
				String a1 = String.format("%."+A.decimalDigits+"f", Math.abs(a)),b1 =String.format("%."+B.decimalDigits+"f", Math.abs(b));
				if(a<0)
					a1 = "@"+a1;
				if(b<0)
					b1 = "@"+b1;
				String p1 = Math.abs(product)+"";
				if(p1.length() - p1.indexOf('.') - 1 > 5)
					p1 = p1.substring(0, p1.indexOf('.')+6);
				if(product < 0)
					p1 = "@"+p1;
				e = e.replace(a1+"+"+b1, p1);
			}
			else
			{
				BetterDouble A = getNumber(e, miIndex, true);
				BetterDouble B = getNumber(e, miIndex, false);
				double a = A.number;
				double b = B.number;
				product = a-b;
				String a1 = String.format("%."+A.decimalDigits+"f", Math.abs(a)),b1 =String.format("%."+B.decimalDigits+"f", Math.abs(b));
				if(a<0)
					a1 = "@"+a1;
				if(b<0)
					b1 = "@"+b1;
				String p1 = Math.abs(product)+"";
				if(p1.length() - p1.indexOf('.') - 1 > 5)
					p1 = p1.substring(0, p1.indexOf('.')+6);
				if(product < 0)
					p1 = "@"+p1;
				e = e.replace(a1+"-"+b1, p1);
			}
			pIndex = e.indexOf('+');
			miIndex = e.indexOf('-');
		}
		return e;
	}
	public BetterDouble getNumber(String e, int signPos, boolean isLeft)
	{
		if(signPos == 0)
			return new BetterDouble(0,-1);
		if(isLeft)
		{
			BetterDouble num = new BetterDouble();
			boolean isFloat = false;
			boolean isNegative = false;
			String strNum = "";
			int i = signPos - 1;
			while(i >= 0 && (Character.isDigit(e.charAt(i))||e.charAt(i)=='@'||e.charAt(i)=='.'))
			{
				if(e.charAt(i) == '@')
				{
					isNegative = true;
					break;
				}
				if(e.charAt(i) == '.')
				{
					isFloat = true;
					strNum = e.charAt(i) + strNum;
				}
				else
				{
					if(!isFloat)
						num.decimalDigits++;
					strNum = e.charAt(i) + strNum;
				}
				i--;
			}
			if(isFloat) num.number = Float.parseFloat(strNum);
			else num.number = Integer.parseInt(strNum);
			if(isNegative)num.number*=-1;
			return num;
		}
		else
		{
			BetterDouble num = new BetterDouble();
			boolean isFloat = false;
			boolean isNegative = false;
			String strNum = "";
			int i = signPos + 1;
			while(i < e.length() && (Character.isDigit(e.charAt(i))||e.charAt(i)=='@'||e.charAt(i)=='.'))
			{
				if(e.charAt(i) == '@')
				{
					isNegative = true;
					i++;
					continue;
				}
				if(e.charAt(i) == '.')
				{
					isFloat = true;
					strNum += e.charAt(i);
				}
				else
				{
					if(isFloat)
						num.decimalDigits++;
					strNum += e.charAt(i);
				}
				i++;
			}
			if(isFloat) num.number = Float.parseFloat(strNum);
			else num.number = Integer.parseInt(strNum);
			if(isNegative)num.number*=-1;
			return num;
		}
	}
	public String convert(String e)
	{
		if(e.charAt(0) == '-')
			e = '@'+e.substring(1);
		int searchIndex = 0;
		while(e.indexOf('-', searchIndex+1)!=-1)
		{
			int index = e.indexOf('-', searchIndex+1);
			if(!Character.isDigit(e.charAt(index - 1)))
			{
				e = e.substring(0, index) + '@' + e.substring(index+1);
			}
			else
				searchIndex = index + 1;
		}

		int i = 0;
		boolean ignore = false;
		while(i < e.length() - 1)
		{
			if(e.charAt(i+1) == '.')
				ignore = true;
			if(Character.isDigit(e.charAt(i)) && (!Character.isDigit(e.charAt(i+1))&&e.charAt(i+1)!='.'))
			{
				if(ignore)
				{
					ignore = false;
				}
				else
				e = e.substring(0,i+1) + "|" + e.substring(i+1);
			}
			i++;
		}
		if(Character.isDigit(e.charAt(e.length()-1)))
			e+="|";
		e = e.replace("|", ".0");
		return e;
	}
	
	public static void main(String args[])
	{
		Calculator test = new Calculator();
	}
}


class BetterDouble
{
	public double number;
	public int decimalDigits = 0;
	BetterDouble(double num, int digits)
	{
		number = num;
		decimalDigits = digits;
	}

	BetterDouble()
	{
		number = 0;
		decimalDigits = 0;
	}
}
