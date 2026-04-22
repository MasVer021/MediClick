package it.mediclick.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils 
{
	public static String hashPassword(String passwordInChiaro) 
	{
        return BCrypt.hashpw(passwordInChiaro, BCrypt.gensalt(10));    
    }


    public static boolean checkPassword(String passwordInChiaro, String passwordHashata) 
    {
        return BCrypt.checkpw(passwordInChiaro, passwordHashata);
    }

}
