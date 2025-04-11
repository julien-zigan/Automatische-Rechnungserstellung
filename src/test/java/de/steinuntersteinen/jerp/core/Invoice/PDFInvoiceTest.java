package de.steinuntersteinen.jerp.core.Invoice;

import de.steinuntersteinen.jerp.core.Persistence.User;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Calendar;

import static java.util.Calendar.*;

class PDFInvoiceTest {
    @Test
    public void shouldPrintInvoice() throws IOException {
        User user = mockUser();
        Invoice invoice = mockInvoice();
        PDFInvoice pdfInvoice = new PDFInvoice(invoice);
        PDDocument document = pdfInvoice.getDocument();
        document.save("Invoices/InvoiceTEST.pdf");
        document.close();
    }

    private User mockUser() {
        User user = new User();
        user.setFirstName("Arndt");
        user.setLastName("Arnoldtson-Smooth");
        user.setProfession("Interpreter");
        user.setPhoneNumber("55555-5555-5555555555");
        user.setEmail("john.smith-doe-peterson@gmail.com");
        user.setWebsite("www.john-smith-doe-peterson.com");
        user.setStreet("Arnoldson-Street-Str. 55B");
        user.setCity("Toolit");
        user.setZipCode("12345");
        user.setBankName("Wahington Devils Corp.");
        user.setIban("XY16546153546887845546");
        user.setBic("ABCDE15456145FG");
        user.setTaxNumber("123456789-1234556/234XD");
        user.setPathToDocumentDirectory("/home/julien/Documents/Invoices/");
        return user;
    }

    private Invoice mockInvoice() {
        User user = mockUser();
        Invoice invoice = new Invoice();
        int id = 4711;
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
        String invoiceAddress = "Company, Name Name, Department, \r\n"
                + "Street Nr.234,\n12345 New York";
        String interpretationLanguage = "Swaheli";
        String contractor = "Smitereen Pr. Dr.";
        String deploymentDate = "4. Januar 1992";
        String customer = "Company FutureFixers, Accounting";
        double duration = 3.0;
        double rate = 50.00;
        double travelFee = 10.00;
        String legalInfo = "Lorem ipsum";
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
        invoice.setRate(rate);
        invoice.setTravelPaid(true);
        invoice.setTravelFee(travelFee);
        invoice.setPaymentRequest(paymentRequest);
        invoice.setGreeting(greeting);
        invoice.setSignature(user.getFirstName() + " " + user.getLastName());
        invoice.setTaxNumber(user.getTaxNumber());

        return invoice;
    }
}