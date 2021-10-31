package bou.maria.gui.SeptemberProject;

import java.util.ArrayList;
import bou.maria.model.Client;
import bou.maria.model.Contract;
import bou.maria.model.LandlinePlan;
import bou.maria.model.LineType;
import bou.maria.model.MobilePlan;
import bou.maria.model.Plan;
import bou.maria.model.Status;
import bou.maria.model.TelecommunicationCompany;

// a class which contains all required lists of data.

public class CommonData {
	private static ArrayList<TelecommunicationCompany> companies = new ArrayList<>();
	private static ArrayList<Client> clients = new ArrayList<>();
	private static ArrayList<Contract> contracts = new ArrayList<>();
	private static ArrayList<Plan> plans = new ArrayList<>();

	public static ArrayList<TelecommunicationCompany> getCompanies() {
		return companies;
	}

	public static void addCompany(TelecommunicationCompany company) {
		companies.add(company);
	}

	// modify company information by name
	public static void modifyCompany(String name, String tel, String email) {
		for (TelecommunicationCompany tc : companies) {
			if (tc.getCompanyName().equalsIgnoreCase(name)) {
				tc.setCompanyTel(tel);
				tc.setCompanyEmail(email);
			}
		}
	}

	// remove company by id
	public static void removeCompany(String companyId) {
		for (int counter = 0; counter < companies.size(); counter++) {
			if (companies.get(counter).getCompanyId().equals(companyId))
				companies.remove(counter);
		}
	}

	// check if company exists
	public static boolean companyExists(String companyName) {
		for (TelecommunicationCompany tc : companies) {
			if (tc.getCompanyName().equals(companyName))
				return true;
		}
		return false;
	}

	// check if company has plan
	public static boolean companyHasPlan(String companyName) {
		for (Plan p : plans) {
			if (p.getCompanyName().equals(companyName))
				return true;
		}
		return false;
	}

	public static void addPlan(Plan plan) {
		plans.add(plan);
	}

	// modify plan information by id
	public static void modifyPlan(String planId, String companyName, int freeCalls, int freeGB, int freeSMS,
			double planCost, LineType lineType, int speedLine, String planType) {
		for (Plan p : plans) {
			if (p.getPlanId().equals(planId) && planType.equals("MOBILE")) {
				p.setCompanyName(companyName);
				p.setFreeCalls(freeCalls);
				((MobilePlan) p).setFreeGB(freeGB);
				((MobilePlan) p).setFreeSMS(freeSMS);
				p.setPlanCost(planCost);
			} else if (p.getPlanId().equals(planId) && planType.equals("LANDLINE")) {
				p.setCompanyName(companyName);
				p.setFreeCalls(freeCalls);
				((LandlinePlan) p).setLineType(lineType);
				p.setPlanCost(planCost);
				((LandlinePlan) p).setSpeedLine(speedLine);
			}
		}
	}

	public static ArrayList<MobilePlan> getMobilePlans() {
		ArrayList<MobilePlan> mobilePlans = new ArrayList<>();
		for (Plan p : plans) {
			if (p instanceof MobilePlan)
				mobilePlans.add((MobilePlan) p);
		}
		return mobilePlans;
	}

	public static ArrayList<LandlinePlan> getLandlinePlans() {
		ArrayList<LandlinePlan> landlinePlans = new ArrayList<>();
		for (Plan p : plans) {
			if (p instanceof LandlinePlan)
				landlinePlans.add((LandlinePlan) p);
		}
		return landlinePlans;
	}

	public static ArrayList<Plan> getPlans() {
		return plans;
	}

	public static ArrayList<Client> getClients() {
		return clients;
	}

	public static void addClient(Client client) {
		clients.add(client);
	}

	// modify client information by identity number or VAT number
	public static void modifyClient(String indentityNum, String VATNum, String fullName, Status status, String address,
			String tel, String email) {
		for (Client cl : clients) {
			if (cl.getIdentityNumber().equals(indentityNum) || cl.getVATNumber().equals(VATNum)) {
				cl.setFullName(fullName);
				cl.setStatus(status);
				cl.setAddress(address);
				cl.setTel(tel);
				cl.setEmail(email);
			}
		}
	}

	public static ArrayList<Contract> getContracts() {
		return contracts;
	}

	public static void addContract(Contract contract) {
		contracts.add(contract);
	}

	// get plan cost by company name and plan type
	public static double getPlanCost(String companyName, String planType) {
		for (Plan p : plans) {
			if (p.getCompanyName().equals(companyName) && p.getClass().toString().toUpperCase().contains(planType)) {
				return p.getPlanCost();
			}
		}
		return -1;
	}

	// check if contracts are covered
	public static boolean checkCoveredContracts(String telNumber, String planType) {
		for (Contract p : contracts) {
			if (p.getTelNumber().equals(telNumber) && p.getIsActive() && p.getClass().toString().toUpperCase().contains(planType))
				return true;
		}
		return false;
	}

	// set inactive contract 
	public static void setInactiveContract(boolean isAt3Months, String contractId) {
		// if contract cancelled over a period of 3 months, calculates the cost of cancellation
		if (isAt3Months) {
			for (Contract c : contracts) {
				if (c.getContractId().equals(contractId)) {
					c.setCancellationCost(c.getCostAfterDiscount() * 0.1); 
					c.setIsActive(false);
					break;
				}
			}
		} else {
			for (Contract c : contracts) {
				if (c.getContractId().equals(contractId)) {
					c.setIsActive(false);
					break;
				}
			}
		}
	}

	// remove a client by VAT number
	public static boolean removeClient(String VATNumber) {
		boolean existsClientContract = false;
		for (Contract c : contracts) {
			if (c.getVATNumber().equals(VATNumber)) {
				existsClientContract = true;
				break;
			}
		}
		if (!existsClientContract) {
			for (Client c : clients) {
				if (c.getVATNumber().equals(VATNumber)) {
					clients.remove(c);
					break;
				}
			}
		}
		return !existsClientContract;
	}

	public static boolean removePlan(String companyName, String planType) {
		boolean existsContract = false;
		for (Contract c : contracts) {
			if (c.getCompanyName().equals(companyName) && c.getContractPlan().toString().equals(planType)) {
				existsContract = true;
				break;
			}
		}
		if (!existsContract) {
			for (Plan p : plans) {
				if (p.getCompanyName().equals(companyName)
						&& p.getClass().toString().toUpperCase().contains(planType)) {
					plans.remove(p);
					break;
				}
			}
		}
		return !existsContract;
	}
}
