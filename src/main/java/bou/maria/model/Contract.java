package bou.maria.model;

import java.time.LocalDate;

public class Contract {

	private String contractId;
	private String companyName;
	private String telNumber;
	private String VATNumber;
	private ContractPlan contractPlan;
	private LocalDate startDate;
	private int duration;
	private double discount;
	private double costAfterDiscount;
	private AccountType accountType;
	private PaymentType paymentType;
	private double cancellationCost;
	private boolean isActive;

	public Contract(String contractId, String companyName ,String telNumber, String VATNumber, ContractPlan contractPlan, LocalDate startDate,
			int duration, double discount, double costAfterDiscount, AccountType accountType, PaymentType paymentType,
			double cancellationCost, boolean isActive) {

		this.contractId = contractId;
		this.companyName = companyName;
		this.telNumber = telNumber;
		this.VATNumber = VATNumber;
		this.contractPlan = contractPlan;
		this.startDate = startDate;
		this.duration = duration;
		this.discount = discount;
		this.costAfterDiscount = costAfterDiscount;
		this.accountType = accountType;
		this.paymentType = paymentType;
		this.cancellationCost = cancellationCost;
		this.isActive = isActive;
	}
	
	public String getCompanyName() {
		return this.companyName;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setContractId(String contractId) {

		this.contractId = contractId;
	}

	public String getContractId() {

		return contractId;
	}

	public void setTelNumber(String telNumber) {

		this.telNumber = telNumber;
	}

	public String getTelNumber() {

		return telNumber;
	}

	public void setVATNumber(String VATNumber) {

		this.VATNumber = VATNumber;
	}

	public String getVATNumber() {

		return VATNumber;
	}

	public void setContractPlan(ContractPlan contractPlan) {

		this.contractPlan = contractPlan;
	}

	public ContractPlan getContractPlan() {

		return contractPlan;
	}

	public void setStartDate(LocalDate startDate) {

		this.startDate = startDate;
	}

	public LocalDate getStartDate() {

		return startDate;
	}

	public void setDuration(int duration) {

		this.duration = duration;
	}

	public int getDuration() {

		return duration;
	}

	public void setDiscount(double discount) {

		this.discount = discount;
	}

	public double getDiscount() {

		return discount;
	}

	public void setCostAfterDiscount(double costAfterDiscount) {

		this.costAfterDiscount = costAfterDiscount;
	}

	public double getCostAfterDiscount() {

		return costAfterDiscount;
	}

	public void setAccountType(AccountType accountType) {

		this.accountType = accountType;
	}

	public AccountType getAccountType() {

		return accountType;
	}

	public void setPaymentType(PaymentType paymentType) {

		this.paymentType = paymentType;
	}

	public PaymentType getPaymentType() {

		return paymentType;
	}

	public void setCancellationCost(double cancellationCost) {

		this.cancellationCost = cancellationCost;
	}

	public double getCancellationCost() {

		return cancellationCost;
	}

	public void setIsActive(boolean isActive) {

		this.isActive = isActive;
	}

	public boolean getIsActive() {

		return isActive;
	}
}
