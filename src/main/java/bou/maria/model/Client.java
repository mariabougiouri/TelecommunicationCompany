package bou.maria.model;

public class Client {

	private String identityNumber;
	private String VATNumber;
	private String fullName;
	private Status status;
	private String address;
	private String tel;
	private String email;

	public Client(String identityNumber, String VATNumber, String fullName, Status status, String address, String tel,
			String email) {

		this.identityNumber = identityNumber;
		this.VATNumber = VATNumber;
		this.fullName = fullName;
		this.status = status;
		this.address = address;
		this.tel = tel;
		this.email = email;
	}

	public void setIdentityNumber(String identityNumber) {

		this.identityNumber = identityNumber;
	}

	public String getIdentityNumber() {

		return identityNumber;
	}

	public void setVATNumber(String VATNumber) {

		this.VATNumber = VATNumber;
	}

	public String getVATNumber() {

		return VATNumber;
	}

	public void setFullName(String fullName) {

		this.fullName = fullName;
	}

	public String getFullName() {

		return fullName;
	}

	public void setStatus(Status status) {

		this.status = status;
	}

	public Status getStatus() {

		return status;
	}

	public void setAddress(String address) {

		this.address = address;
	}

	public String getAddress() {

		return address;
	}

	public void setTel(String tel) {

		this.tel = tel;
	}

	public String getTel() {

		return tel;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public String getEmail() {

		return email;
	}

}
