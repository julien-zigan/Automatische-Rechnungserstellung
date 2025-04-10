package de.steinuntersteinen.jerp.core.Confirmation;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;

public class TextExtractor {

    public static String getText(PDDocument pdf) throws IOException {
        PDFTextStripper textStripper = new PDFTextStripper();
        textStripper.setSortByPosition(true);
        String text = textStripper.getText(pdf);
        String plattformIndText = text.replaceAll("\r", "");
        pdf.close();
        return plattformIndText;
    }

    public static String getOrderNr(String confirmation) {
        String orderNrKeyword = "Auftrags-Nr.";
        String wordSeparator = "\r\n";
        int orderNrStartPos = confirmation.indexOf(orderNrKeyword) + orderNrKeyword.length() + 1;
        int orderNrEndPos = confirmation.indexOf(wordSeparator, orderNrStartPos);
        return confirmation.substring(orderNrStartPos, orderNrEndPos);
    }

    public static String getAddress(String confirmation) {
        String recipientStartKey = "Rechnung an:";
        String recipientEndKey = "Bemerkungen";
        int recipientStartPos = confirmation.indexOf(recipientStartKey) + recipientStartKey.length() + 1;
        int recipientEndPos = confirmation.indexOf(recipientEndKey) - 1;
        return confirmation.substring(recipientStartPos, recipientEndPos);
    }

    public static String getDate(String confirmation) {
        String dateStartKey = "Datum / Uhrzeit: ";
        String dateEndKey = " / ";
        int dateStartPos = confirmation.indexOf(dateStartKey) + dateStartKey.length();
        int dateEndPos = confirmation.indexOf(dateEndKey, dateStartPos);
        return confirmation.substring(dateStartPos, dateEndPos);
    }

    public static double getDuration(String confirmation) {
        String durationEndKey = "Dolmetschsprache: ";
        int durationStartPos = confirmation.indexOf(durationEndKey) - 18;
        int durationEndPos =  durationStartPos + 13;
        String hours = confirmation.substring(durationStartPos, durationEndPos);

        int startHour = Integer.parseInt(hours.substring(0, 2));
        int startMinute = Integer.parseInt(hours.substring(3, 5));
        LocalTime startTime = LocalTime.of(startHour, startMinute);

        int endeHour = Integer.parseInt(hours.substring(8, 10));
        int endeMinute = Integer.parseInt(hours.substring(11, 13));
        LocalTime endTime = LocalTime.of(endeHour, endeMinute);

        Duration duration = Duration.between(startTime, endTime);

        return (double) duration.toMinutes() / 60.;
    }

    public static String getLanguage(String confirmation) {
        String languageStartKey = "Dolmetschsprache";
        String languageEndKey = "\n";
        int languageStartPos = confirmation.indexOf(languageStartKey);
        int languageEndPos = confirmation.indexOf(languageEndKey, languageStartPos);
        return confirmation.substring(languageStartPos, languageEndPos);
    }

    public static String getContractor(String confirmation) {
        String contractorStartKey = "Auftraggeber";
        String contractorEndKey = "\n";
        int contractorStartPos = confirmation.indexOf(contractorStartKey);
        int contractorEndPos = confirmation.indexOf(contractorEndKey, contractorStartPos);
        return confirmation.substring(contractorStartPos, contractorEndPos);
    }

    public static String getClient(String confirmation) {
        String clientStartKey = "Kund*in: ";
        String clientEndKey = "\n";
        int clientStartPos = confirmation.indexOf(clientStartKey) + clientStartKey.length();
        int clientEndPos = confirmation.indexOf(clientEndKey, clientStartPos);
        return confirmation.substring(clientStartPos, clientEndPos);
    }
}
