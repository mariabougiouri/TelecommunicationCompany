package bou.maria.gui.SeptemberProject;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import bou.maria.model.Contract;
import bou.maria.model.ContractPlan;
import bou.maria.model.AccountType;
import bou.maria.model.PaymentType;
import bou.maria.model.Plan;
import bou.maria.model.Status;
import bou.maria.model.Client;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ContractScene extends SceneCreator implements EventHandler<MouseEvent> {

	GridPane rootGridPane, inputsGridPane;
	FlowPane btnFlowPane;
	Button addContractBtn, cancelContractBtn, backBtn, searchContractBtn;
	TextField contractIdFld, telNumFld, VATNumFld, isActiveFld, companyFld;
	Label contractIdLbl, telNumLbl, VATNumLbl, contractPlanLbl, startDateLbl, durationLbl, accTypeLbl, payTypeLbl,
			isActiveLbl, companyLbl;
	Contract selectedContract;
	TableView<Contract> contractsTable;
	ComboBox<String> contractPlanCbx, durationCbx, accTypeCbx, payTypeCbx;
	String planType = ContractPlan.MOBILE.toString(), contractDuration = "12",
			accType = AccountType.ELECTRONIC.toString(), payType = PaymentType.CASH.toString();
	DatePicker datePicker;
	LocalDate date;

	public ContractScene(double width, double height) {

		super(width, height);

		createButtons();
		createTableView();
		createInputs();
		getSelectedTableItem();

		rootGridPane = new GridPane();
		rootGridPane.add(contractsTable, 0, 1);
		rootGridPane.add(inputsGridPane, 1, 1);
		rootGridPane.add(btnFlowPane, 0, 2);

		rootGridPane.setVgap(20);
		rootGridPane.setHgap(30);
	}

	@Override
	public Scene createScene() {
		return new Scene(rootGridPane, width, height);
	}

	@Override
	public void handle(MouseEvent event) {
		if (event.getSource() == addContractBtn) {
			addContract();
		} else if (event.getSource() == backBtn) {
			goBack();
		} else if (event.getSource() == searchContractBtn) {
			searchContract();
		} else if (event.getSource() == cancelContractBtn) {
			cancelContract();
		}
	}

	
	public void cancelContract() {
		int contractMonth = date.getMonthValue();
		Date dateNow = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateNow);
		int monthNow = cal.get(Calendar.MONTH);
		boolean isAt3Months = isAt3Months(contractMonth, monthNow);
		CommonData.setInactiveContract(isAt3Months, contractIdFld.getText());
		isActiveFld.setText("false");
		contractTableViewSync();
	}

	//check if contract cancelled over a period of 3 months
	public boolean isAt3Months(int contractMonth, int monthNow) {
		if (Math.abs(monthNow - contractMonth) <= 3)
			return true;
		if (monthNow < contractMonth) {
			if (contractMonth == 10 && monthNow == 1)
				return true;
			if (contractMonth == 11 && monthNow <= 2)
				return true;
			if (contractMonth == 12 && monthNow <= 3)
				return true;
		}
		return false;
	}

	//search contract by plan type and the telephone number
	public void searchContract() {
		ArrayList<Contract> contracts = CommonData.getContracts();
		ObservableList<Contract> items = contractsTable.getItems();
		items.clear();
		for (Contract c : contracts) {
			if (c.getContractPlan().toString().equals(contractPlanCbx.getValue())
					&& c.getTelNumber().toString().equals(telNumFld.getText())) {
				items.add(c);
			}
		}
	}

	public void goBack() {
		App.primaryStage.setTitle("Telecommunication Services Store");
		App.primaryStage.setScene(App.mainScene);
	}

	public void addContract() {
		String telNumber = telNumFld.getText();
		String VATNumber = VATNumFld.getText();
		ContractPlan contractPlan = ContractPlan.valueOf(planType);

		String company = companyFld.getText();
		AccountType accountType = AccountType.valueOf(accType);
		PaymentType paymentType = PaymentType.valueOf(payType);
		int duration = Integer.parseInt(contractDuration);

		String contractId = createId(VATNumber, contractPlan);

		if (!CommonData.checkCoveredContracts(telNumber,planType) && contractMobileTelIsValid(telNumber) && planType == "MOBILE"
				&& !isEmptyString(VATNumFld.getText()) && !isEmptyString(companyFld.getText())) {
				double planCost = CommonData.getPlanCost(company, planType);
				double statusDiscountAmount = statusDiscountAmount(VATNumber, planCost);
				double freeCallsDiscountAmount = freeCallsDiscountAmount(company, planCost);
				double paymentTypeDiscountAmount = paymentTypeDiscountAmount(paymentType, planCost);
				double accountTypeDiscountAmount = accountTypeDiscountAmount(accountType, planCost);
				double discount = statusDiscountAmount + freeCallsDiscountAmount + paymentTypeDiscountAmount
						+ accountTypeDiscountAmount;
				double costAfterDiscount = planCost - discount;
				Contract contract = new Contract(contractId, company, telNumber, VATNumber, contractPlan, date,
						duration, discount, costAfterDiscount, accountType, paymentType, 0, true);
				CommonData.addContract(contract);
				contractTableViewSync();
				clearTexts();
		} 
		else if(!CommonData.checkCoveredContracts(telNumber,planType) && contractLandlineTelIsValid(telNumber) && planType == "LANDLINE"
				&& !isEmptyString(VATNumFld.getText()) && !isEmptyString(companyFld.getText())) {
			double planCost = CommonData.getPlanCost(company, planType);
			double statusDiscountAmount = statusDiscountAmount(VATNumber, planCost);
			double freeCallsDiscountAmount = freeCallsDiscountAmount(company, planCost);
			double paymentTypeDiscountAmount = paymentTypeDiscountAmount(paymentType, planCost);
			double accountTypeDiscountAmount = accountTypeDiscountAmount(accountType, planCost);
			double discount = statusDiscountAmount + freeCallsDiscountAmount + paymentTypeDiscountAmount
					+ accountTypeDiscountAmount;
			double costAfterDiscount = planCost - discount;
			Contract contract = new Contract(contractId, company, telNumber, VATNumber, contractPlan, date,
					duration, discount, costAfterDiscount, accountType, paymentType, 0, true);
			CommonData.addContract(contract);
			contractTableViewSync();
			clearTexts();
		}
		else if (CommonData.checkCoveredContracts(telNumber,planType) || isEmptyString(VATNumFld.getText()) || isEmptyString(companyFld.getText())) {
			Alert info = new Alert(Alert.AlertType.INFORMATION);
			info.setTitle("Covered Contracts or empty fiels");
			info.setContentText("Check if inputs are completed and if there are covered contracts!");
			info.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			info.show();
		}
	}
	
	public boolean isEmptyString(String str) {
		return str.length() == 0;
	}

	public void getSelectedTableItem() {

		contractsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Contract>() {

			@Override
			public void changed(ObservableValue<? extends Contract> observable, Contract oldValue, Contract newValue) {
				if (newValue != null) {

					contractIdFld.setText(newValue.getContractId());
					telNumFld.setText(newValue.getTelNumber());
					VATNumFld.setText(newValue.getVATNumber());
					isActiveFld.setText(Boolean.toString(newValue.getIsActive()));
					contractPlanCbx.setValue(newValue.getContractPlan().toString());
					datePicker.setValue(newValue.getStartDate());
					durationCbx.setValue(Integer.toString(newValue.getDuration()));
					accTypeCbx.setValue(newValue.getAccountType().toString());
					payTypeCbx.setValue(newValue.getPaymentType().toString());

				}
			}
		});
	}

	// calculate the status discount
	public double statusDiscountAmount(String VATNumber, double planCost) {
		for (Client cl : CommonData.getClients()) {
			if (cl.getVATNumber().equals(VATNumber)) {
				if (cl.getStatus().equals(Status.CITIZEN)) {
					double discount = 0;
					return calcDiscountAmount(discount, planCost);
				} else if (cl.getStatus().equals(Status.STUDENT)) {
					double discount = 0.15;
					return calcDiscountAmount(discount, planCost);
				} else if (cl.getStatus().equals(Status.PROFESSIONAL)) {
					double discount = 0.10;
					return calcDiscountAmount(discount, planCost);
				}
			}
		}
		return 0.0;
	}

	// calculate the free calls discount
	public double freeCallsDiscountAmount(String companyName, double planCost) {
		for (Plan p : CommonData.getPlans()) {
			if (p.getCompanyName().equalsIgnoreCase(companyName)) {
				if (p.getFreeCalls() > 1000 && planType.equals(ContractPlan.MOBILE.toString())) {
					double discount = 0.11;
					return calcDiscountAmount(discount, planCost);
				} else if (p.getFreeCalls() > 1000 && planType.equals(ContractPlan.LANDLINE.toString())) {
					double discount = 0.08;
					return calcDiscountAmount(discount, planCost);
				}
			}
		}

		return 0.0;
	}

	// calculate the payment type discount
	public double paymentTypeDiscountAmount(PaymentType payType, double planCost) {
		if (payType.equals(PaymentType.CREDIT_CARD)) {
			double discount = 0.05;
			return calcDiscountAmount(discount, planCost);
		} else if (payType.equals(PaymentType.CASH)) {
			double discount = 0;
			return calcDiscountAmount(discount, planCost);
		}
		return 0.0;
	}

	// calculate the account type discount
	public double accountTypeDiscountAmount(AccountType accType, double planCost) {
		if (accType.equals(AccountType.ELECTRONIC)) {
			double discount = 0.02;
			return calcDiscountAmount(discount, planCost);
		} else if (accType.equals(AccountType.PRINTED)) {
			double discount = 0;
			return calcDiscountAmount(discount, planCost);
		}
		return 0.0;
	}

	public double calcDiscountAmount(double discount, double cost) {
		return discount * cost;
	}

	// create contract id 
	public String createId(String VATNumber, ContractPlan contractPlan) {
		if (contractPlan.equals(ContractPlan.MOBILE.toString())) {
			String id = date + VATNumber + "Mobile";
			return id;
		} else {
			String id = date + VATNumber + "Landline";
			return id;
		}

	}

	public boolean contractMobileTelIsValid(String tel) {
		return tel.length() == 10 && tel.charAt(0) == '6' && isTelNumber(tel);
	}

	public boolean contractLandlineTelIsValid(String tel) {
		return tel.length() == 10 && tel.charAt(0) == '2' && isTelNumber(tel);
	}

	public boolean isTelNumber(String contractTel) {
		char[] telNumArray = contractTel.toCharArray();
		for (char tn : telNumArray) {
			if (!Character.isDigit(tn))
				return false;
		}
		return true;
	}

	public void contractTableViewSync() {
		ArrayList<Contract> contracts = CommonData.getContracts();
		ObservableList<Contract> items = contractsTable.getItems();
		items.clear();

		for (Contract c : contracts) {
			items.add(c);
		}
	}

	public void clearTexts() {
		contractIdFld.setText("");
		telNumFld.setText("");
		VATNumFld.setText("");
		isActiveFld.setText("");
		companyFld.setText("");
		contractPlanCbx.setValue(ContractPlan.MOBILE.toString());
		durationCbx.setValue("12");
		accTypeCbx.setValue(AccountType.ELECTRONIC.toString());
		payTypeCbx.setValue(PaymentType.CASH.toString());
		datePicker.setValue(null);
	}

	public void createInputs() {
		companyLbl = new Label("Plan company");
		contractIdLbl = new Label("Contract Id:");
		telNumLbl = new Label("Contract Telephone:");
		VATNumLbl = new Label("Client VAT Number:");
		contractPlanLbl = new Label("Contract Plan:");
		startDateLbl = new Label("Contract Start Date:");
		durationLbl = new Label("Contract Duration:");
		accTypeLbl = new Label("Account Type:");
		payTypeLbl = new Label("Payment Type:");
		isActiveLbl = new Label("Contract Activity:");

		companyFld = new TextField();
		contractIdFld = new TextField();
		telNumFld = new TextField();
		VATNumFld = new TextField();
		isActiveFld = new TextField();
		datePicker = new DatePicker();

		EventHandler<ActionEvent> datePickerEvent = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				date = datePicker.getValue();
			}
		};

		datePicker.setOnAction(datePickerEvent);

		contractPlanCbx = new ComboBox<>();
		durationCbx = new ComboBox<>();
		accTypeCbx = new ComboBox<>();
		payTypeCbx = new ComboBox<>();

		contractIdFld.setDisable(true);
		contractIdFld.setEditable(false);
		isActiveFld.setDisable(true);
		isActiveFld.setEditable(false);

		companyFld.setPrefColumnCount(15);
		contractIdFld.setPrefColumnCount(20);
		telNumFld.setPrefColumnCount(15);
		VATNumFld.setPrefColumnCount(15);
		isActiveFld.setPrefColumnCount(15);

		contractPlanCbx.getItems().addAll(ContractPlan.MOBILE.toString(), ContractPlan.LANDLINE.toString());
		contractPlanCbx.getSelectionModel().selectFirst();
		contractPlanCbx.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				planType = newValue;
			}

		});

		durationCbx.getItems().addAll("12", "24");
		durationCbx.getSelectionModel().selectFirst();
		durationCbx.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				contractDuration = newValue;
			}

		});

		accTypeCbx.getItems().addAll(AccountType.ELECTRONIC.toString(), AccountType.PRINTED.toString());
		accTypeCbx.getSelectionModel().selectFirst();
		accTypeCbx.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				accType = newValue;
			}

		});

		payTypeCbx.getItems().addAll(PaymentType.CASH.toString(), PaymentType.CREDIT_CARD.toString());
		payTypeCbx.getSelectionModel().selectFirst();
		payTypeCbx.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				payType = newValue;
			}

		});

		inputsGridPane = new GridPane();
		inputsGridPane.add(contractIdLbl, 0, 0);
		inputsGridPane.add(contractIdFld, 0, 1);
		inputsGridPane.add(telNumLbl, 0, 2);
		inputsGridPane.add(telNumFld, 0, 3);
		inputsGridPane.add(VATNumLbl, 0, 4);
		inputsGridPane.add(VATNumFld, 0, 5);
		inputsGridPane.add(companyLbl, 0, 6);
		inputsGridPane.add(companyFld, 0, 7);
		inputsGridPane.add(startDateLbl, 0, 8);
		inputsGridPane.add(datePicker, 0, 9);
		inputsGridPane.add(isActiveLbl, 0, 10);
		inputsGridPane.add(isActiveFld, 0, 11);
		inputsGridPane.add(contractPlanLbl, 0, 12);
		inputsGridPane.add(contractPlanCbx, 0, 13);
		inputsGridPane.add(durationLbl, 0, 14);
		inputsGridPane.add(durationCbx, 0, 15);
		inputsGridPane.add(accTypeLbl, 0, 16);
		inputsGridPane.add(accTypeCbx, 0, 17);
		inputsGridPane.add(payTypeLbl, 0, 18);
		inputsGridPane.add(payTypeCbx, 0, 19);

		inputsGridPane.setVgap(5);
	}

	public void createButtons() {
		addContractBtn = new Button("Add Contract");
		searchContractBtn = new Button("Search Contract");
		cancelContractBtn = new Button("Cancel Contract");
		backBtn = new Button("Back");

		addContractBtn.setOnMouseClicked(this);
		searchContractBtn.setOnMouseClicked(this);
		cancelContractBtn.setOnMouseClicked(this);
		backBtn.setOnMouseClicked(this);

		btnFlowPane = new FlowPane();
		btnFlowPane.getChildren().addAll(addContractBtn, searchContractBtn, cancelContractBtn, backBtn);
		btnFlowPane.setHgap(10);
		btnFlowPane.setAlignment(Pos.BOTTOM_CENTER);
	}

	public void createTableView() {
		contractsTable = new TableView();
		contractsTable.setMinWidth(1080);

		TableColumn<Contract, String> contractIdCol = new TableColumn<>("Id");
		contractIdCol.setCellValueFactory(new PropertyValueFactory<>("contractId"));
		contractIdCol.setMinWidth(80);
		contractsTable.getColumns().add(contractIdCol);

		TableColumn<Contract, String> telNumCol = new TableColumn<>("Tel");
		telNumCol.setCellValueFactory(new PropertyValueFactory<>("telNumber"));
		telNumCol.setMinWidth(80);
		contractsTable.getColumns().add(telNumCol);

		TableColumn<Contract, String> VATNumCol = new TableColumn<>("VAT Number");
		VATNumCol.setCellValueFactory(new PropertyValueFactory<>("VATNumber"));
		VATNumCol.setMinWidth(80);
		contractsTable.getColumns().add(VATNumCol);

		TableColumn<Contract, String> contractPlanCol = new TableColumn<>("Plan");
		contractPlanCol.setCellValueFactory(new PropertyValueFactory<>("contractPlan"));
		contractPlanCol.setMinWidth(80);
		contractsTable.getColumns().add(contractPlanCol);

		TableColumn<Contract, LocalDate> startDateCol = new TableColumn<>("Start");
		startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
		startDateCol.setMinWidth(80);
		contractsTable.getColumns().add(startDateCol);

		TableColumn<Contract, String> durationCol = new TableColumn<>("Duration");
		durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
		durationCol.setMinWidth(80);
		contractsTable.getColumns().add(durationCol);

		TableColumn<Contract, String> discountCol = new TableColumn<>("Discount");
		discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
		discountCol.setMinWidth(80);
		contractsTable.getColumns().add(discountCol);

		TableColumn<Contract, String> costAfterDiscCol = new TableColumn<>("Cost");
		costAfterDiscCol.setCellValueFactory(new PropertyValueFactory<>("costAfterDiscount"));
		costAfterDiscCol.setMinWidth(80);
		contractsTable.getColumns().add(costAfterDiscCol);

		TableColumn<Contract, String> accTypeCol = new TableColumn<>("Account");
		accTypeCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));
		accTypeCol.setMinWidth(80);
		contractsTable.getColumns().add(accTypeCol);

		TableColumn<Contract, String> payTypeCol = new TableColumn<>("Payment");
		payTypeCol.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
		payTypeCol.setMinWidth(80);
		contractsTable.getColumns().add(payTypeCol);

		TableColumn<Contract, String> cancelCostCol = new TableColumn<>("Cancellation Cost");
		cancelCostCol.setCellValueFactory(new PropertyValueFactory<>("cancellationCost"));
		cancelCostCol.setMinWidth(120);
		contractsTable.getColumns().add(cancelCostCol);

		TableColumn<Contract, String> isActiveCol = new TableColumn<>("Active");
		isActiveCol.setCellValueFactory(new PropertyValueFactory<>("isActive"));
		isActiveCol.setMinWidth(80);
		contractsTable.getColumns().add(isActiveCol);

		TableColumn<Contract, String> companyNameCol = new TableColumn<>("Company");
		companyNameCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
		companyNameCol.setMinWidth(80);
		contractsTable.getColumns().add(companyNameCol);
	}
}
