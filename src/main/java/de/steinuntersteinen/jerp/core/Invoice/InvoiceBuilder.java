package de.steinuntersteinen.jerp.core.Invoice;

import de.steinuntersteinen.jerp.core.Confirmation.Confirmation;
import de.steinuntersteinen.jerp.core.Persistence.DataBase;
import de.steinuntersteinen.jerp.core.Persistence.User;

import java.util.Calendar;
import java.util.Objects;

import static java.util.Calendar.*;

public class InvoiceBuilder {

    public static Invoice build(User user, Confirmation confirmation) {

        int lastId = 999999999; //TODO: integrate text msg in the case db loading fails
        try {
            lastId = DataBase.loadLastInvoiceNr();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        int id = ++lastId;
        double rate = 50.00;
        double travelFee = 10.00;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(YEAR);
        String date = calendar.get(DAY_OF_MONTH)
                + "."
                + calendar.get(MONTH)
                + "."
                + calendar.get(YEAR);
        String path = user.getPathToDocumentDirectory();
        String returnAddress = composeReturnAddress(user);
        String lettertext = "Ich bedanke mich für die gute Zusammenarbeit"
                + " und stelle Ihnen vereinbarungsgemäß folgende\n"
                + "Leistungen in Rechnung:";
        String sum1;
        if(Objects.equals(confirmation.getDuration(), "")) {
            sum1 = "Sum could not be calculated, enter manually!";
        } else {
            sum1 = String.format("%.2f €",
                    rate * Double.parseDouble(confirmation.getDuration()));
        }
        String paymentRequest = "Zahlung bitte innerhalb von 21 Tagen"
                + " an die oben angegebene Bankverbindung.";
        String greeting = "Mit freundlichen Grüßen";

        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setPath(path);
        invoice.setInvoiceNumber(id + "/" + year);
        invoice.setReturnAddress(returnAddress);
        invoice.setInvoiceAddress(confirmation.getInvoiceAddress());
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
        invoice.setInterpretationLanguage(confirmation.getInterpretationLanguage());
        invoice.setContractor(confirmation.getContractor());
        invoice.setCustomer(confirmation.getCustomer());
        invoice.setDeploymentDate(confirmation.getDeploymentDate());
        invoice.setDuration(confirmation.getDuration());
        invoice.setRate(String.format("%.2f €", rate));
        invoice.setSum1(sum1);
        invoice.setTravelPaid(false);
        invoice.setTravelFee(String.format("%.2f €", travelFee));
        invoice.setSumTotal(sum1);
        invoice.setPaymentRequest(paymentRequest);
        invoice.setGreeting(greeting);
        invoice.setSignature(user.getFirstName() + " " + user.getLastName());
        invoice.setTaxNumber(user.getTaxNumber());

        return invoice;
    }

    private static String composeReturnAddress(User user) {
        String separator = " | ";
        StringBuilder name = new StringBuilder();
        StringBuilder mailingAddress = new StringBuilder();

        mailingAddress.append(separator);
        mailingAddress.append(user.getStreet());
        mailingAddress.append(separator);
        mailingAddress.append(user.getZipCode());
        mailingAddress.append(" ");
        mailingAddress.append(user.getCity());

        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        name.append(firstName);
        name.append(" ");
        name.append(lastName);

        try {
            float addressWidth =
                    PDFInvoice.FONT.getStringWidth(mailingAddress.toString())
                    / 1000.0f * PDFInvoice.RETURN_ADDRESS_FONTSIZE;
            float nameWidth = PDFInvoice.FONT.getStringWidth(name.toString())
                    / 1000.0f* PDFInvoice.RETURN_ADDRESS_FONTSIZE;
            if (addressWidth + nameWidth > PDFInvoice.RETURN_ADDRESS_WIDTH) {
                int nameLength = firstName.length();
                String initialLetter = user.getFirstName().charAt(0) + ".";
                name.replace(0, nameLength, initialLetter);
            }
            if (addressWidth + nameWidth > PDFInvoice.RETURN_ADDRESS_WIDTH) {
                throw new RuntimeException("Address is too long. Please abbreviate where appropriate");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return name.append(mailingAddress).toString();
    }
}
