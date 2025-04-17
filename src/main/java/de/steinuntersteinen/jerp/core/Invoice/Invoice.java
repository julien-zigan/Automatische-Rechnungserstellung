package de.steinuntersteinen.jerp.core.Invoice;

public class Invoice {
    private int id;
    private String fileName;
    private String path;
    private String invoiceNumber;
    private String returnAddress;
    private String invoiceAddress;
    private String firstName;
    private String lastName;
    private String profession;
    private String phoneNumber;
    private String email;
    private String website;
    private String bankName;
    private String IBAN;
    private String BIC;
    private String accountOwner;
    private String invoiceDate;
    private String letterText;
    private String interpretationLanguage;
    private String contractor;
    private String customer;
    private String deploymentDate;
    private double durationNumerical;
    private String duration;
    private double rateNumerical;
    private String rate;
    private double sum1Numerical;
    private String sum1;
    private String travelPaidInput;
    private boolean isTravelPaid;
    private double travelFeeNumerical;
    private String travelFee;
    private double sumTotalNumerical;
    private String sumTotal;
    private String paymentRequest;
    private String greeting;
    private String signature;
    private String taxNumber;
    private int helperInvoiceNumberB;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getBIC() {
        return BIC;
    }

    public void setBIC(String BIC) {
        this.BIC = BIC;
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getLetterText() {
        return letterText;
    }

    public void setLetterText(String letterText) {
        this.letterText = letterText;
    }

    public String getInterpretationLanguage() {
        return interpretationLanguage;
    }

    public void setInterpretationLanguage(String interpretationLanguage) {
        this.interpretationLanguage = interpretationLanguage;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDeploymentDate() {
        return deploymentDate;
    }

    public void setDeploymentDate(String deploymentDate) {
        this.deploymentDate = deploymentDate;
    }

    public double getDurationNumerical() {
        return durationNumerical;
    }

    public void setDurationNumerical(double durationNumerical) {
        setDuration(durationNumerical);
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        durationNumerical = duration;
        this.duration = String.format("%.1f", durationNumerical);
        updateSums();
    }

    public double getRateNumerical() {
        return rateNumerical;
    }

    public void setRateNumerical(double rateNumerical) {
        setRate(rateNumerical);
    }

    public String getRate() {
        return rate;
    }

    public void setRate(double rate) {
        rateNumerical = rate;
        this.rate = String.format("%.2f €", rateNumerical);
        updateSums();
    }

    public double getSum1Numerical() {
        return sum1Numerical;
    }

    public String getSum1() {
        return sum1;
    }

    private void setSum1(double sum1) {
        sum1Numerical = sum1;
        this.sum1 = String.format("%.2f €", sum1Numerical);
    }

    public boolean getIsTravelPaid() {
        return isTravelPaid;
    }

    public void setTravelPaid(boolean travelPaid) {
        isTravelPaid = travelPaid;
        updateSums();
    }

    public double getTravelFeeNumerical() {
        return travelFeeNumerical;
    }

    public void setTravelFeeNumerical(double travelFeeNumerical) {
       setTravelFee(travelFeeNumerical);
    }

    public String getTravelFee() {
        return travelFee;
    }

    public void setTravelFee(double travelFee) {
        travelFeeNumerical = travelFee;
        this.travelFee = String.format("%.2f €", travelFeeNumerical);
        updateSums();
    }

    public double getSumTotalNumerical() {
        return sumTotalNumerical;
    }

    public String getSumTotal() {
        return sumTotal;
    }

    private void setSumTotal(double sumTotal) {
        sumTotalNumerical = sumTotal;
        this.sumTotal = String.format("%.2f €", sumTotalNumerical);
    }

    public String getPaymentRequest() {
        return paymentRequest;
    }

    public void setPaymentRequest(String paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    private void updateSums() {
        double sum1 = durationNumerical * rateNumerical;
        double sumTotal = sum1 + (getIsTravelPaid() ? travelFeeNumerical : 0.0);
        setSum1(sum1);
        setSumTotal(sumTotal);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTravelPaidInput() {
        return travelPaidInput;
    }

    public void setTravelPaidInput(String travelPaidInput) {
        this.travelPaidInput = travelPaidInput;
    }

    public int getHelperInvoiceNumberB() {
        return helperInvoiceNumberB;
    }

    public void setHelperInvoiceNumberB(int helperInvoiceNumberB) {
        this.helperInvoiceNumberB = helperInvoiceNumberB;
    }
}