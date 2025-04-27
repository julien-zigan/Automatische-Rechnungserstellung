package de.steinuntersteinen.jerp.core.Invoice;

import de.steinuntersteinen.jerp.core.Persistence.DataBase;

public class LastInvoiceDAO {
    private String  lastInvoiceNr;
    private String lastCustomer;

    public LastInvoiceDAO() {
        this.lastInvoiceNr = "";
        this.lastCustomer = "Last Customer could not be found";

        try {
            this.lastInvoiceNr = DataBase.loadLastInvoiceNrString();
            this.lastCustomer = DataBase.loadLastCustomer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getLastInvoiceNr() {
        return lastInvoiceNr;
    }

    public String getLastCustomer() {
        return lastCustomer;
    }
}
