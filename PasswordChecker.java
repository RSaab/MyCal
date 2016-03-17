/* Rashad Saab,  rms78 */

public class PasswordChecker {

	public PasswordChecker(){
		
	}
	
	public boolean isGood(String pass){
		boolean goodPass= isLong(pass) && containsDigit(pass) && checkUpperAndLowerCase(pass) && hasNonAlphaNumericChar(pass);		
		return goodPass;
	}

	public static boolean isLong(String pass)//check if length greater or equal to 8
	{
		boolean acceptedLength=false;
		if(pass.length()>=8)
		{
			acceptedLength=true;
		}
		return acceptedLength;
	}

	public static boolean containsDigit(String pass)//check if pass contains a digit
	{
		boolean hasDigit=false;
		for(int i=0; i< pass.length() && !hasDigit; i++)
		{

			if(Character.isDigit(pass.charAt(i)))
			{
				hasDigit=true;
			}	
		}
		return hasDigit;
	}

	public static boolean checkUpperAndLowerCase(String pass)//check if pass has an upper case and lower case letter
	{
		boolean hasUpperCase=false;
		boolean hasLowerCase=false;


		for(int i=0; i<pass.length() && !hasUpperCase; i++)
		{
			for (char letter='A'; letter<='Z' && !hasUpperCase; letter++)
			{
				if (pass.charAt(i)==letter)
				{
					hasUpperCase=true;
				}
			}
		}

		for(int i=0; i<pass.length() && !hasLowerCase; i++)
		{
			for (char letter='a'; letter<='z' && !hasLowerCase; letter++)
			{
				if (pass.charAt(i)==letter)
				{
					hasLowerCase=true;
				}
			}
		}
		boolean hasBoth=hasUpperCase && hasLowerCase;

		return hasBoth;
	}

	public static boolean hasNonAlphaNumericChar(String pass)
	{
		boolean hasSpecialChar=false;
		for(int i=0; i<pass.length() ; i++)
		{
			if(!Character.isLetter(pass.charAt(i)) && !Character.isDigit(pass.charAt(i)) )
			{
				hasSpecialChar=true;
			}
		}

		return hasSpecialChar;
	}
}
