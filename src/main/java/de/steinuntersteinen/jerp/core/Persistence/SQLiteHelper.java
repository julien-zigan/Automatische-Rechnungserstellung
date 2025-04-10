package de.steinuntersteinen.jerp.core.Persistence;

import java.sql.Connection;
import java.sql.DriverManager;


public class SQLiteHelper {
    private static Connection c = null;
    public static Connection getConnection() throws Exception {
        if(c == null){
//            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:./jerp_data/jerp.db");
        }
        return c;
    }
}