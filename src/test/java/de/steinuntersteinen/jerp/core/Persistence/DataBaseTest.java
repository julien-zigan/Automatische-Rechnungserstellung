package de.steinuntersteinen.jerp.core.Persistence;

import de.steinuntersteinen.jerp.core.Invoice.Invoice;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import static java.util.Calendar.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DataBaseTest {
    @Test
    void testAll() throws Exception {
        shouldCreateDatabase();
        shouldAddUser();
        shouldLoadUser();
        shouldCommitInvoice();
    }

    @Test
    void shouldCreateDatabase() throws Exception {
        DataBase.init();

        Connection connection = SQLiteHelper.getConnection();
        Statement statement = connection.createStatement();
        String sql = """
                            SELECT invoiceNr FROM invoice ORDER BY ID DESC LIMIT 1
                            """;
        ResultSet rs = statement.executeQuery(sql);
        if (rs.getString("invoiceNr") == null) {
            System.out.println("1");
        } else {
            System.out.println(rs.getString("invoiceNr"));
        }
    }

    @Test
    void shouldAddUser() throws Exception {
        User user = mockUser();
        DataBase.add(user);
    }

    @Test
    void shouldLoadUser() throws Exception {
        User user = mockUser();
        User user2 = DataBase.loadUser();
        assertEquals(user.getFirstName(), user2.getFirstName());
        assertEquals(user.getBic(), user2.getBic());
        assertEquals(user.getEmail(), user2.getEmail());
        assertEquals(user.getPathToDocumentDirectory(),
                user2.getPathToDocumentDirectory());
    }

    @Test
    void shouldCommitInvoice () throws Exception {
        User user = mockUser();
        Invoice invoice = mockInvoice();
        DataBase.commit(user, invoice);
    }

    private User mockUser() {
        User user = new User();
        user.setFirstName("Arndt");
        user.setLastName("Arnoldtson-Smooth");
        user.setProfession("Interpreter");
        user.setPhoneNumber("55555-5555-5555555555");
        user.setEmail("john-smith-doe-peterson@gmail.com");
        user.setWebsite("www.john-smith-doe-peterson.com");
        user.setStreet("Arnoldson-Street-Str. 55B");
        user.setCity("Toolit");
        user.setZipCode("12345");
        user.setBankName("Wahington Devils Corp.");
        user.setIban("XY16546153546887845546");
        user.setBic("ABCDE15456145FG");
        user.setTaxNumber("123456789-1234556/234XD");
        user.setPathToDocumentDirectory("/home/julien/IdeaProjects/Jerp/Invoices");
        return user;
    }

    private Invoice mockInvoice() throws Exception {
        User user = mockUser();
        Invoice invoice = new Invoice();
        int lastId = DataBase.loadLastInvoiceNr();
        int id = ++lastId;
        String returnAddress = user.getFirstName()
                .charAt(0)
                + ". "
                + user.getLastName()
                + " | "
                + user.getStreet()
                + " | "
                + user.getZipCode()
                +" "
                + user.getCity();
        String invoiceAddress = "Company\nBEST Como.\nDepartment\n"
                + "Street Nr.234\n12345 New York";
        String interpretationLanguage = "Swaheli";
        String contractor = "Smitereen Pr. Dr.";
        String deploymentDate = "4. Januar 1992";
        String customer = "Company FutureFixers, Accounting";
        String duration = "3.0";
        double rate = 50.00;
        double travelFee = 10.00;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(YEAR);
        String date = calendar.get(DAY_OF_MONTH)
                + "."
                + calendar.get(MONTH)
                + "."
                + calendar.get(YEAR);
        String lettertext = "Ich bedanke mich für die gute Zusammenarbeit"
                + " und stelle Ihnen vereinbarungsgemäß folgende\n"
                + "Leistungen in Rechnung:";
        String paymentRequest = "Zahlung bitte innerhalb von 21 Tagen"
                + " an die oben angegebene Bankverbindung.";
        String greeting = "Mit freundlichen Grüßen";

        invoice.setId(id);
        invoice.setInvoiceNumber(id + "/" + year);
        invoice.setReturnAddress(returnAddress);
        invoice.setInvoiceAddress(invoiceAddress);
        invoice.setFirstName(user.getFirstName());
        invoice.setLastName(user.getLastName());
        invoice.setProfession(user.getProfession());
        invoice.setPhoneNumber(user.getPhoneNumber());
        invoice.setEmail(user.getEmail());
        invoice.setWebsite(user.getWebsite());
        invoice.setBankName(user.getBankName());
        invoice.setIBAN(user.getIban());
        invoice.setBIC(user.getBic());
        invoice.setAccountOwner(user.getFirstName() + " " + user.getLastName());
        invoice.setInvoiceDate(date);
        invoice.setLetterText(lettertext);
        invoice.setInterpretationLanguage(interpretationLanguage);
        invoice.setContractor(contractor);
        invoice.setCustomer(customer);
        invoice.setDeploymentDate(deploymentDate);
        invoice.setDuration(duration);
        invoice.setRate(String.format("%.2f €", rate));
        invoice.setSum1(
                String.format("%.2f €",
                        rate * Double.parseDouble(invoice.getDuration())
                )
        );
        invoice.setTravelPaid(true);
        invoice.setTravelFee(String.format("%.2f €", travelFee));
        invoice.setSumTotal(
                String.format("%.2f €",
                        rate * Double.parseDouble(invoice.getDuration())
                )
        );
        invoice.setPaymentRequest(paymentRequest);
        invoice.setGreeting(greeting);
        invoice.setSignature(user.getFirstName() + " " + user.getLastName());
        invoice.setTaxNumber(user.getTaxNumber());

        return invoice;
    }
}