package bou.maria.model;

public class MobilePlan extends Plan {

	private int freeSMS;
	private int freeGB;

	public MobilePlan(String companyName, int freeCalls, double planCost, int freeSMS, int freeGB) {

		super(companyName, freeCalls, planCost);
		this.freeSMS = freeSMS;
		this.freeGB = freeGB;
	}

	public void setFreeSMS(int freeSMS) {

		this.freeSMS = freeSMS;
	}

	public int getFreeSMS() {

		return freeSMS;
	}

	public void setFreeGB(int freeGB) {

		this.freeGB = freeGB;
	}

	public int getFreeGB() {

		return freeGB;
	}
}