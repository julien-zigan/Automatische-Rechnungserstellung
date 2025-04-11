package de.steinuntersteinen.jerp.core.Persistence;

import  de.steinuntersteinen.jerp.core.Invoice.Invoice;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import static java.util.Calendar.YEAR;

public class DataBase {

    public static void init() {
        initDataDirectory();
        initUserTable();
        initInvoiceTable();
    }

    private static void initDataDirectory() {
        File theDir = new File("./jerp_data");
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }

    private static void initUserTable() {
        try {
            Connection connection = SQLiteHelper.getConnection();
            Statement statement = connection.createStatement();
            String sql = """
                            CREATE TABLE if not EXISTS user (
                                id INTEGER PRIMARY KEY autoincrement,
                                userId TEXT,
                                firstName TEXT,
                                lastName TEXT,
                                profession TEXT,
                                phoneNumber TEXT,
                                email TEXT,
                                website TEXT,
                                street TEXT,
                                city TEXT,
                                zipCode TEXT,
                                bankName TEXT,
                                iban TEXT,
                                bic TEXT,
                                taxNumber TEXT,
                                pathToDocumentDirectory TEXT)
                            """;
            statement.executeUpdate(sql);
            statement.close();

        } catch (Exception e) {
            System.err.println(e.getMessage() + "in SetUpUserTable");
        }
    }

    private static void initInvoiceTable() {
        try {
            Connection connection = SQLiteHelper.getConnection();
            Statement statement = connection.createStatement();
            String sql = """
                            CREATE TABLE IF NOT EXISTS invoice (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                user_id  TEXT,
                                invoiceNr INTEGER,
                                invoiceNrYear INTEGER,
                                invoiceAddress TEXT,
                                invoiceDate TEXT,
                                interpretationLanguage TEXT,
                                contractor TEXT,
                                customer TEXT,
                                deploymentDate TEXT,
                                duration DOUBLE,
                                rate DOUBLE,
                                travelFee DOUBLE,
                                sumTotal DOUBLE,
                                pathToPdfDocument TEXT,
                                FOREIGN KEY(user_id) REFERENCES user(user_id)
                                );
                            """;
            statement.executeUpdate(sql);
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getMessage() + "inSetUpInvoiceTable");
        }
    }

    public static void commit(User user, Invoice invoice) throws Exception {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(YEAR);
        Connection connection = SQLiteHelper.getConnection();
        Statement statement = connection.createStatement();

        String sql = String.format("""
                INSERT INTO invoice
                (user_id, invoiceNr, invoiceNrYear, invoiceAddress, invoiceDate,
                interpretationLanguage, contractor, customer, deploymentDate, duration,
                rate, travelFee, sumTotal, pathToPdfDocument)
                values ('%s',
                %d, %d,
                '%s', '%s', '%s', '%s', '%s', '%s',
                '%f', '%f', '%f', '%f',
                '%s');
                """,
                user.getId(), invoice.getId(), year,
                invoice.getInvoiceAddress(), invoice.getInvoiceDate(),
                invoice.getInterpretationLanguage(), invoice.getContractor(),
                invoice.getCustomer(), invoice.getDeploymentDate(),
                invoice.getDurationNumerical(),
                invoice.getRateNumerical(), invoice.getTravelFeeNumerical(),
                invoice.getSumTotalNumerical(), user.getPathToDocumentDirectory()
        );

        statement.executeUpdate(sql);
        statement.close();
    }

    public static void add(User user) throws Exception {
        Connection connection = SQLiteHelper.getConnection();
        Statement statement = connection.createStatement();
        String sql = String.format("""
               INSERT INTO user (
               firstName,
               lastName,
               profession,
               phoneNumber,
               email,
               website,
               street,
               city,
               zipCode,
               bankName,
               iban,
               bic,
               taxNumber,
               pathToDocumentDirectory
               )
               values ('%s', '%s', '%s', '%s', '%s',
                '%s', '%s', '%s', '%s', '%s', '%s',
                '%s', '%s', '%s'
                )
               """,
                user.getFirstName(),
                user.getLastName(),
                user.getProfession(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getWebsite(),
                user.getStreet(),
                user.getCity(),
                user.getZipCode(),
                user.getBankName(),
                user.getIban(),
                user.getBic(),
                user.getTaxNumber(),
                user.getPathToDocumentDirectory()
        );
        statement.executeUpdate(sql);
        statement.close();

        File theDir = new File(user.getPathToDocumentDirectory());
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }

    public static User loadUser() throws Exception {
        //TODO this needs to be changed for multiuser application
        User user = new User();
        Connection connection = SQLiteHelper.getConnection();
        Statement statement = connection.createStatement();
        String sql = """
                            SELECT * FROM user ORDER BY ID DESC LIMIT 1
                            """;
        ResultSet rs = statement.executeQuery(sql);

        user.setId(rs.getString("id"));
        user.setFirstName(rs.getString("firstName"));
        user.setLastName(rs.getString("lastName"));
        user.setProfession(rs.getString("profession"));
        user.setPhoneNumber(rs.getString("phoneNumber"));
        user.setEmail(rs.getString("email"));
        user.setWebsite(rs.getString("website"));
        user.setStreet(rs.getString("street"));
        user.setCity(rs.getString("city"));
        user.setZipCode(rs.getString("zipCode"));
        user.setBankName(rs.getString("bankName"));
        user.setIban(rs.getString("iban"));
        user.setBic(rs.getString("bic"));
        user.setTaxNumber(rs.getString("taxNumber"));
        user.setPathToDocumentDirectory(
                rs.getString("pathToDocumentDirectory"));

        return user;
    }

    public static int loadLastInvoiceNr() throws Exception {
        Connection connection = SQLiteHelper.getConnection();
        Statement statement = connection.createStatement();
        String sql = """
                            SELECT invoiceNr FROM invoice ORDER BY ID DESC LIMIT 1
                            """;
        ResultSet rs = statement.executeQuery(sql);
        return rs.getInt("invoiceNr");
    }
}