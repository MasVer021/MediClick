package it.mediclick.util;

import java.sql.SQLException;
import java.util.Map;

@FunctionalInterface
public interface ResultMapper<T>
{
    T map(Map<String, Object> row) throws SQLException;
}