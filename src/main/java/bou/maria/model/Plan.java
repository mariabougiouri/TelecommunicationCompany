package bou.maria.model;

import java.util.UUID;

public class Plan {

	private String planId;
	private String companyName;
	private int freeCalls;
	private double planCost;

	public Plan(String companyName, int freeCalls, double planCost) {

		this.planId = UUID.randomUUID().toString(); // create random plan id
		this.companyName = companyName;
		this.freeCalls = freeCalls;
		this.planCost = planCost;
	}

	public String getPlanId() {

		return planId;
	}

	public void setCompanyName(String companyName) {

		this.companyName = companyName;
	}

	public String getCompanyName() {

		return companyName;
	}

	public void setFreeCalls(int freeCalls) {

		this.freeCalls = freeCalls;
	}

	public int getFreeCalls() {

		return freeCalls;
	}

	public void setPlanCost(double planCost) {

		this.planCost = planCost;
	}

	public double getPlanCost() {

		return planCost;
	}
}
