package de.steinuntersteinen.jerp.core;

import de.steinuntersteinen.jerp.core.Confirmation.Confirmation;
import de.steinuntersteinen.jerp.core.Invoice.Invoice;
import de.steinuntersteinen.jerp.core.Invoice.InvoiceBuilder;
import de.steinuntersteinen.jerp.core.Invoice.PDFInvoice;
import de.steinuntersteinen.jerp.core.Persistence.User;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class MainTest {
    @Test
    void shouldCreateInvoiceFromConfirmation() throws IOException {
        User user = mockUser();
        File file;
        try {
            file = new File("/home/julien/Downloads/GDD-Einsatzbestätigung.pdf");
        } catch (Exception e) {
            e.printStackTrace();
            file  = null;
        }
        Confirmation confirmation = Confirmation.from(file);
        Invoice invoice = InvoiceBuilder.build(user, confirmation);
        PDFInvoice pdfInvoice = new PDFInvoice(invoice);
        PDDocument document = pdfInvoice.getDocument();
        document.save("Invoices/MainTEST.pdf");
        document.close();

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
        user.setPathToDocumentDirectory("/home/julien/Documents/Invoices/");
        return user;
    }
}