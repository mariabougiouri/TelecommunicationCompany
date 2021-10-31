package bou.maria.model;

import java.util.UUID;

public class TelecommunicationCompany {

	private String companyId;
	private String companyName;
	private String companyTel;
	private String companyEmail;

	public TelecommunicationCompany(String companyName, String companyTel, String companyEmail) {

		this.companyId = UUID.randomUUID().toString(); // create random company id
		this.companyName = companyName;
		this.companyTel = companyTel;
		this.companyEmail = companyEmail;
	}

	public String getCompanyId() {

		return companyId;
	}

	public void setCompanyName(String companyName) {

		this.companyName = companyName;
	}

	public String getCompanyName() {

		return companyName;
	}

	public void setCompanyTel(String companyTel) {

		this.companyTel = companyTel;
	}

	public String getCompanyTel() {

		return companyTel;
	}

	public void setCompanyEmail(String companyEmail) {

		this.companyEmail = companyEmail;
	}

	public String getCompanyEmail() {

		return companyEmail;
	}

}
