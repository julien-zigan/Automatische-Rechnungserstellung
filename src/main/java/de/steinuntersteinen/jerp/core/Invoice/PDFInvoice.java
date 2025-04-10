package de.steinuntersteinen.jerp.core.Invoice;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.StringTokenizer;

public class PDFInvoice {
    private String pathToPdf;
    private final Invoice invoice;
    private final PDDocument document;
    private final PDPageContentStream contentStream;

    private final String EMPTY_LINE = "";
    private static final String STD_TEXT_LINE1 = "Ich bedanke mich für die "
            + "gute Zusammenarbeit und stelle Ihnen vereinbarungsgemäß folgende";
    private static final String STD_TEXT_LINE2 = "Leistungen in Rechnung:";
    private static final String ENDING_TEXT = "Zahlung bitte " +
            "innerhalb von 21 Tagen an die oben angegebene Bankverbindung.";


    protected static final PDType1Font FONT =
            new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    protected static final PDType1Font FONT_BOLD =
            new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    protected static final PDType1Font FONT_OBLIQUE =
            new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE);
    protected static final int RETURN_ADDRESS_FONTSIZE = 8;
    protected static final int FONTSIZE_SMALL = 10;
    protected static final int FONTSIZE_BIG = 12;
    protected final int HEADING_FONT_SIZE = 20;
    protected final float HEADING_LINE_HEIGHT = 30;
    protected static final int PARAGRAPH_MARGIN = 22;
    protected static final float RETURN_ADDRESS_WIDTH = 226.772f;

    public PDFInvoice(Invoice invoice) throws IOException {
        this.invoice = invoice;
        document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        contentStream = new PDPageContentStream(document, page);
        document.addPage(page);

        printFoldingMarks();
        printReturnInfo();
        printInvoiceAddress();
        printInfoBlock();
        printContent();

        contentStream.close();
    }

    public PDDocument getDocument() {
        return document;
    }

    private void printFoldingMarks() throws IOException {
        float fontSize = 12f;
        float pos_x = 0f;
        float pos_y = 595.2758f;

        contentStream.beginText();
        contentStream.setFont(FONT, fontSize);
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.newLineAtOffset(pos_x, pos_y);
        contentStream.showText("__");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(pos_x, 420.94492f);
        contentStream.showText("____");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(pos_x, 297.6378f);
        contentStream.showText("__");
        contentStream.endText();
    }

    private void printReturnInfo() throws IOException {
        float pos_x = 70.8661f;
        float pos_y = 765.3544f;
        float lineHeight = 10.034646f;
        float lines = 5;

        contentStream.beginText();
        contentStream.setNonStrokingColor(0.47F, 0.47F, 0.47F);
        contentStream.setFont(FONT, RETURN_ADDRESS_FONTSIZE);
        contentStream.newLineAtOffset(pos_x, pos_y - lineHeight * lines); // DIN prescribes to begin at bottom line
        contentStream.showText(invoice.getReturnAddress());
        contentStream.endText();
    }

    private void printInvoiceAddress() throws IOException {
        float pos_x = 70.8661f;
        float pos_y = 715.1811f;
        float lineHeight = 12.89764f;
        StringTokenizer st = tokenize(invoice.getInvoiceAddress());

        contentStream.beginText();
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.setFont(FONT, RETURN_ADDRESS_FONTSIZE);
        contentStream.newLineAtOffset(pos_x, pos_y - lineHeight);
        contentStream.showText(EMPTY_LINE);
        contentStream.newLineAtOffset(0, -(lineHeight));
        contentStream.setFont(FONT_BOLD, FONTSIZE_BIG);
        contentStream.showText(st.nextToken());
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        while (st.hasMoreTokens()) {
            contentStream.newLineAtOffset(0, -(lineHeight));
            contentStream.showText(st.nextToken().trim());
        }
        contentStream.endText();
    }

    private static StringTokenizer tokenize(String s) {
        if (s.isEmpty()) {
            return new StringTokenizer("Address_could_not_be_parsed. Please_enter_manually!");
        }
        String noR = s.replaceAll("\r", "");
        String noRN = noR.replaceAll("\n", "");
        return new StringTokenizer(noRN, ",");
    }

    private void printInfoBlock() throws IOException {
        float pos_x = 354.331f;
        float pos_y = 751.1811f;
        float lineHeight = 15.0f;
        String name = invoice.getFirstName()
                + " "
                + invoice.getLastName();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

        contentStream.beginText();
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        contentStream.newLineAtOffset(pos_x, pos_y - lineHeight);
        contentStream.showText("Rechnungsnr. " + invoice.getInvoiceNumber());
        contentStream.newLineAtOffset(0, -(PARAGRAPH_MARGIN));
        contentStream.setFont(FONT_BOLD, FONTSIZE_SMALL);
        contentStream.showText(name);
        contentStream.newLineAtOffset(0, -(lineHeight));
        contentStream.showText(invoice.getProfession());
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        contentStream.newLineAtOffset(0, -(PARAGRAPH_MARGIN));
        contentStream.showText("Tel: " + invoice.getPhoneNumber());
        contentStream.newLineAtOffset(0, -(lineHeight));
        contentStream.showText(invoice.getEmail());
        contentStream.newLineAtOffset(0, -(lineHeight));
        contentStream.showText(invoice.getWebsite());
        contentStream.newLineAtOffset(0, -(PARAGRAPH_MARGIN));
        contentStream.setFont(FONT_OBLIQUE, FONTSIZE_SMALL);
        contentStream.showText("Zahlungsinformation");
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        contentStream.newLineAtOffset(0, -(lineHeight));
        contentStream.showText(invoice.getBankName());
        contentStream.newLineAtOffset(0, -(lineHeight));
        contentStream.showText("IBAN: " + invoice.getIBAN());
        contentStream.newLineAtOffset(0, -(lineHeight));
        contentStream.showText("BIC: " + invoice.getBIC());
        contentStream.newLineAtOffset(0, -(lineHeight));
        contentStream.showText("Kto. Inh.: " + invoice.getAccountOwner());
        contentStream.newLineAtOffset(0, -(lineHeight));
        contentStream.showText("Steuer-Nr.: " + invoice.getTaxNumber());
        contentStream.newLineAtOffset(0, -(PARAGRAPH_MARGIN));
        contentStream.showText("Datum: " + format.format(new Date()));
        contentStream.endText();
    }

    private void printContent() throws IOException {
        float pos_x = 70.8661F;
        float pos_y = 503.0267F;
        float width = 467.717F;
        float lineHeight = 15F;
        float bezeichnungOffset = 42.8f;
        float mengeOffsetLeftAlign = 346f - bezeichnungOffset - pos_x;
        float einzelOffsetLeftAlign = 118f;

        contentStream.beginText();
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.newLineAtOffset(pos_x, pos_y - HEADING_LINE_HEIGHT);
        contentStream.setFont(FONT_BOLD, HEADING_FONT_SIZE);
        contentStream.showText("Rechnung Nr. " + invoice.getInvoiceNumber());

        contentStream.newLineAtOffset(0, - HEADING_LINE_HEIGHT);
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        contentStream.showText(STD_TEXT_LINE1);

        contentStream.newLineAtOffset(0, -(lineHeight));
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        contentStream.showText(STD_TEXT_LINE2);

        contentStream.newLineAtOffset(0, -(lineHeight));
        contentStream.setFont(FONT_BOLD, FONTSIZE_SMALL);
        float underscoreWidth = (FONT.getStringWidth("_") / 1000.0f) * FONTSIZE_SMALL;
        int numberOfUnderscores = (int) (width / underscoreWidth);
        String horizontalLine = "_".repeat(numberOfUnderscores - 2);
        contentStream.showText(horizontalLine);

        contentStream.newLineAtOffset(0, - HEADING_LINE_HEIGHT);
        contentStream.showText("Pos.");

        contentStream.newLineAtOffset(bezeichnungOffset, 0);
        contentStream.showText("Bezeichnung");

        String menge = "Menge";
        float mengeWidth = (FONT.getStringWidth(menge) / 1000.0f) * FONTSIZE_SMALL;
        contentStream.newLineAtOffset(mengeOffsetLeftAlign - mengeWidth, 0);
        contentStream.showText(menge);

        String einzel = "Einzel (€)";
        float einzelWidth = (FONT.getStringWidth(einzel) / 1000.0f) * FONTSIZE_SMALL;
        contentStream.newLineAtOffset(einzelOffsetLeftAlign + mengeWidth - einzelWidth, 0);
        contentStream.showText(einzel);

        String gesamt = "Gesamt (€)";
        float gesamtWidth = (FONT.getStringWidth(gesamt) / 1000.0f) * FONTSIZE_SMALL;
        contentStream.newLineAtOffset(pos_x
                + width
                - bezeichnungOffset
                - mengeOffsetLeftAlign
                - einzelOffsetLeftAlign
                - einzelWidth
                - gesamtWidth, 0);
        contentStream.showText(gesamt);

        float lineWidth = (FONT.getStringWidth(horizontalLine) / 1000.0f) * FONTSIZE_SMALL - 2F;
        contentStream.newLineAtOffset(gesamtWidth - lineWidth, -(lineHeight));
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        contentStream.showText(horizontalLine);

        int invoicePosition = 0;

        // First Line
        contentStream.newLineAtOffset(0, - HEADING_LINE_HEIGHT);
        contentStream.showText(String.valueOf(++invoicePosition));

        contentStream.newLineAtOffset(bezeichnungOffset + 2f, 0);
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        contentStream.showText("Dolmetschen");

        String placeholderMenge = invoice.getDuration() + " Std";
        float placeholderMengeWidth = (FONT.getStringWidth(placeholderMenge) / 1000.0f) * FONTSIZE_SMALL;
        contentStream.newLineAtOffset(mengeOffsetLeftAlign - placeholderMengeWidth, 0);
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        contentStream.showText(placeholderMenge);

        String placeholderEinzel = invoice.getRate();
        float placeholderEinzelWidth = (FONT.getStringWidth(placeholderEinzel) / 1000.0f) * FONTSIZE_SMALL;
        contentStream.newLineAtOffset(einzelOffsetLeftAlign + placeholderMengeWidth - placeholderEinzelWidth, 0);
        contentStream.showText(placeholderEinzel);

        String placeholderGesamt = invoice.getSum1();
        float yConsideringError = Objects.equals(placeholderGesamt,
                "Sum could not be calculated, enter manually!") ? -15 : 0;
        float placeholderGesamtWidth = (FONT.getStringWidth(placeholderGesamt) / 1000.0f) * FONTSIZE_SMALL;
        contentStream.newLineAtOffset(pos_x
                + width
                - bezeichnungOffset
                - mengeOffsetLeftAlign
                - einzelOffsetLeftAlign
                - placeholderEinzelWidth
                - placeholderMengeWidth
                - placeholderGesamtWidth
                + 12f,
                yConsideringError);
        contentStream.showText(placeholderGesamt);

        //additional Information
        contentStream.newLineAtOffset(bezeichnungOffset + placeholderGesamtWidth - lineWidth + 2f, - lineHeight);
        contentStream.setNonStrokingColor(0.5f, 0.5f, 0.5f);
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        String placeholerBezeichnungInfo = invoice.getInterpretationLanguage();
        contentStream.showText(placeholerBezeichnungInfo);

        contentStream.newLineAtOffset(0, - lineHeight);
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        String placeholerAuftraggeberName = invoice.getContractor();
        contentStream.showText(placeholerAuftraggeberName);

        contentStream.newLineAtOffset(0, - lineHeight);
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        String placeholerClient = invoice.getCustomer();
        contentStream.showText(placeholerClient);

        contentStream.newLineAtOffset(0, - lineHeight);
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        String am = invoice.getDeploymentDate().isEmpty() ? "": "am:";
        String placeholerDeploymentDate = String.format("%s %s",am, invoice.getDeploymentDate());
        contentStream.showText(placeholerDeploymentDate);

        // Second Line
        contentStream.newLineAtOffset(-bezeichnungOffset, - HEADING_LINE_HEIGHT);

        if (invoice.isTravelPaid()){
            contentStream.setNonStrokingColor(Color.BLACK);
        } else {
            contentStream.setNonStrokingColor(1F, 1F, 1F);
        }

        contentStream.showText(String.valueOf(++invoicePosition));

        String anfahrt = "Anfahrt";
        float anfahrtWidth = (FONT.getStringWidth(anfahrt) / 1000.0f) * FONTSIZE_SMALL;
        contentStream.newLineAtOffset(bezeichnungOffset, 0);
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        contentStream.showText(anfahrt);

        String pauschal = "1 Pauschal";
        contentStream.newLineAtOffset(mengeOffsetLeftAlign - anfahrtWidth, 0);
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        contentStream.showText(pauschal);

        String placeholderEinzelLine2 = invoice.getTravelFee();
        float placeholderEinzelWidthLine2 = (FONT.getStringWidth(placeholderEinzelLine2) / 1000.0f) * FONTSIZE_SMALL;
        contentStream.newLineAtOffset(einzelOffsetLeftAlign + anfahrtWidth - placeholderEinzelWidthLine2, 0);
        contentStream.showText(placeholderEinzelLine2);

        float placeholderGesamtWidthLine2 = (FONT.getStringWidth(placeholderEinzelLine2) / 1000.0f) * FONTSIZE_SMALL;
        contentStream.newLineAtOffset(pos_x
                + width
                - bezeichnungOffset
                - mengeOffsetLeftAlign
                - einzelOffsetLeftAlign
                - placeholderEinzelWidthLine2
                - placeholderGesamtWidthLine2
                - placeholderGesamtWidthLine2
                + 12, 0);
        contentStream.showText(placeholderEinzelLine2);

        //Total
        contentStream.setNonStrokingColor(Color.BLACK);
        String totalLine = "_".repeat((numberOfUnderscores - 2) / 2);
        float totalLineWidth = (FONT.getStringWidth(totalLine) / 1000.0f) * FONTSIZE_SMALL;
        contentStream.newLineAtOffset(placeholderGesamtWidthLine2 - totalLineWidth, - HEADING_LINE_HEIGHT);
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        contentStream.showText(totalLine);

        String total = invoice.getSumTotal();
        float totalWidth = (FONT.getStringWidth(total) / 1000.0f) * FONTSIZE_SMALL;
        contentStream.newLineAtOffset(totalLineWidth - totalWidth,  -25f);
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        contentStream.showText(total);

        String sumTotal = "Rechnungsbetrag";
        contentStream.newLineAtOffset(-66.5F - totalWidth, 0);
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        contentStream.showText(sumTotal);

        contentStream.newLineAtOffset(66.5F + totalWidth + totalWidth - totalLineWidth , - HEADING_LINE_HEIGHT);
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        contentStream.setNonStrokingColor(0.75F, 0.75F, 0.75F);
        contentStream.showText(totalLine);

        //ending
        contentStream.newLineAtOffset(totalLineWidth - lineWidth, - HEADING_LINE_HEIGHT);
        contentStream.setFont(FONT, FONTSIZE_SMALL);
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.showText(ENDING_TEXT);

        contentStream.newLineAtOffset(0, -lineHeight);

        contentStream.newLineAtOffset(0, -lineHeight);
        contentStream.showText("Mit freundlichen Grüßen");

        contentStream.newLineAtOffset(0, -25);
        contentStream.setNonStrokingColor(0.40F, 0.40F, 0.40F);
        contentStream.setFont(FONT, 14F);
        contentStream.showText(invoice.getFirstName() + " " + invoice.getLastName());

        contentStream.endText();
    }
}