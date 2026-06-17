package it.mediclick.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

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

	public static String hashSHA256(String input)
	{
		try
		{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
			return HexFormat.of().formatHex(hash);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException("Errore nell'hashing SHA-256", e);
		}
	}

}
