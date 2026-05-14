package it.mediclick.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
        return Timestamp.valueOf(String.valueOf(val)).toLocalDateTime();
    }

    public static LocalDate getLocalDate(Map<String, Object> map, String key)
    {
        Object val = map.get(key);
        if (val == null)
        {
        	return null;
        }
        return ((java.sql.Date) val).toLocalDate();
    }
    
    public static BigDecimal getBigDecimal(Map<String, Object> map, String key)
    {
        Object val = map.get(key);
        return val != null ? new BigDecimal(String.valueOf(val)) : null;
    }
}
