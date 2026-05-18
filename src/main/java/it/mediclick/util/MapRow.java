package it.mediclick.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class MapRow
{
	public static int getInt(Map<String, Object> map, String key)
	{
		Object val = map.get(key);
		if (val == null)
		{
			throw new IllegalStateException("Colonna obbligatoria NULL: " + key);
		}
		return Integer.parseInt(String.valueOf(val));
	}

	public static Integer getIntOrNull(Map<String, Object> map, String key)
	{
		Object val = map.get(key);
		return val != null ? Integer.parseInt(String.valueOf(val)) : null;
	}

	public static String getString(Map<String, Object> map, String key)
	{
		return (String) map.get(key);
	}

	public static double getDouble(Map<String, Object> map, String key)
	{
		Object val = map.get(key);
		if (val == null)
		{
			throw new IllegalStateException("Colonna obbligatoria NULL: " + key);
		}
		return Double.parseDouble(String.valueOf(val));
	}

	public static boolean getBoolean(Map<String, Object> map, String key)
	{
		Object val = map.get(key);
		if (val == null)
		{
			return false;
		}
		return val instanceof Boolean b ? b : Integer.parseInt(String.valueOf(val)) != 0;
	}

	public static LocalDateTime getLocalDateTime(Map<String, Object> map, String key)
	{
		Object val = map.get(key);

		if (val == null)
		{
			return null;
		}

		if (val instanceof java.sql.Timestamp)
		{
			return ((java.sql.Timestamp) val).toLocalDateTime();
		}

		if (val instanceof java.sql.Date)
		{
			return ((java.sql.Date) val).toLocalDate().atStartOfDay();
		}

		if (val instanceof LocalDateTime)
		{
			return (LocalDateTime) val;
		}

		return LocalDateTime.parse(String.valueOf(val));
	}

	public static LocalDate getLocalDate(Map<String, Object> map, String key)
	{
		Object val = map.get(key);
		if (val == null)
		{
			return null;
		}

		if (val instanceof java.sql.Date)
		{
			return ((java.sql.Date) val).toLocalDate();
		}

		if (val instanceof LocalDate)
		{
			return (LocalDate) val;
		}

		return LocalDate.parse(String.valueOf(val));
	}

	public static BigDecimal getBigDecimal(Map<String, Object> map, String key)
	{
		Object val = map.get(key);
		return val != null ? new BigDecimal(String.valueOf(val)) : null;
	}
}
