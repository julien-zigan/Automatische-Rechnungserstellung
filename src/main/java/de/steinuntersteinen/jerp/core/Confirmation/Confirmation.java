package de.steinuntersteinen.jerp.core.Confirmation;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;

public class Confirmation {
    private final String deploymentDate;
    private final String duration;
    private final String interpretationLanguage;
    private final String contractor;
    private final String customer;
    private final String invoiceAddress;

    private Confirmation(String fullText) {
        if (fullText != null) {
            deploymentDate = TextExtractor.getDate(fullText);
            duration = String.valueOf(TextExtractor.getDuration(fullText));
            interpretationLanguage = TextExtractor.getLanguage(fullText);
            contractor = TextExtractor.getContractor(fullText);
            customer = TextExtractor.getClient(fullText);
            invoiceAddress = TextExtractor.getAddress(fullText);
        } else {
            deploymentDate = "";
            duration = "";
            interpretationLanguage = "";
            contractor = "";
            customer = "";
            invoiceAddress = "";
        }
    }

    public static Confirmation from(File pdfFile) {
        if (pdfFile == null) {return new Confirmation(null);}
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            String fullText = TextExtractor.getText(document);
            return new Confirmation(fullText);
        } catch (Exception e) {
            System.out.println(e.getMessage()); //TODO: NEXT: Integrate Logging
            return new Confirmation(null);
        }
    }

    public String getDeploymentDate() {
        return deploymentDate;
    }

    public String getDuration() {
        return duration;
    }

    public String getInterpretationLanguage() {
        return interpretationLanguage;
    }

    public String getContractor() {
        return contractor;
    }

    public String getCustomer() {
        return customer;
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }
}
