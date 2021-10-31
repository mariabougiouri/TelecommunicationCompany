package bou.maria.gui.SeptemberProject;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import java.util.ArrayList;
import bou.maria.model.TelecommunicationCompany;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

public class CompanyScene extends SceneCreator implements EventHandler<MouseEvent> {

	GridPane rootGridPane, inputsGridPane;
	FlowPane btnFlowPane;
	Button addCompanyBtn, modifyCompanyBtn, delCompanyBtn, backBtn;
	TextField companyIdFld, companyNameFld, companyTelFld, companyEmailFld;
	Label companyIdLbl, companyNameLbl, companyTelLbl, companyEmailLbl;
	TelecommunicationCompany selectedTC;
	TableView<TelecommunicationCompany> companiesTable;

	public CompanyScene(double width, double height) {
		
		super(width, height);
		
		createButtons();
		createTableView();
		createInputs();
		getSelectedTableItem();
		searchCompanies();

		rootGridPane = new GridPane();
		rootGridPane.add(companiesTable, 0, 1);
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
		if (event.getSource() == addCompanyBtn) {
			addCompany();
		} else if (event.getSource() == delCompanyBtn) {
			deleteCompany();
		} else if (event.getSource() == backBtn) {
			goBack();
		} else if (event.getSource() == modifyCompanyBtn) {
			modifyCompany();
		}
	}

	// deletes a company which doesn't have plans
	public void deleteCompany() {
		if (!isEmptyString(companyIdFld.getText()) && !CommonData.companyHasPlan(companyNameFld.getText())) {
			CommonData.removeCompany(companyIdFld.getText());
			companyTableViewSync();
			clearTexts();
		} else {
			Alert info = new Alert(Alert.AlertType.INFORMATION);
			info.setTitle("Company cannot be deleted");
			info.setContentText("Company have plans!");
			info.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			info.show();
			clearTexts();
		}
	}

	//modify the company information,except from id number and name of company
	public void modifyCompany() {
		if (!isEmptyString(companyNameFld.getText()) && !isEmptyString(companyTelFld.getText())
				&& !isEmptyString(companyEmailFld.getText()) && companyTelIsValid(companyTelFld.getText())) {

			CommonData.modifyCompany(companyNameFld.getText(), companyTelFld.getText(), companyEmailFld.getText());
			companyTableViewSync();
			clearTexts();
		} else {
			Alert info = new Alert(Alert.AlertType.INFORMATION);
			info.setTitle("Missing Information");
			info.setContentText("Please complete the required information!!!");
			info.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			info.show();
		}
	}

	public boolean isEmptyString(String str) {
		return str.length() == 0;
	}

	// search companies by name
	public void searchCompanies() {
		ArrayList<TelecommunicationCompany> companies = CommonData.getCompanies();
		ObservableList<TelecommunicationCompany> items = companiesTable.getItems();

		ChangeListener<String> searchListener = new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				items.clear();
				for (TelecommunicationCompany c : companies) {
					if (c.getCompanyName().contains(newValue)) {
						items.add(c);
					}
				}
			}

		};

		companyNameFld.textProperty().addListener(searchListener);
	}

	public void goBack() {
		App.primaryStage.setTitle("Telecommunication Services Store");
		App.primaryStage.setScene(App.mainScene);
	}

	public void getSelectedTableItem() {
		companiesTable.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<TelecommunicationCompany>() {

					@Override
					public void changed(ObservableValue<? extends TelecommunicationCompany> observable,
							TelecommunicationCompany oldValue, TelecommunicationCompany newValue) {
						if (newValue != null) {
							companyIdFld.setText(newValue.getCompanyId());
							companyNameFld.setText(newValue.getCompanyName());
							companyTelFld.setText(newValue.getCompanyTel());
							companyEmailFld.setText(newValue.getCompanyEmail());
						}
					}
				});
	}

	public void addCompany() {
		String companyName = companyNameFld.getText();
		String companyTel = companyTelFld.getText();
		String companyEmail = companyEmailFld.getText();

		if (!companyTelIsValid(companyTel) || isEmptyString(companyNameFld.getText()) || isEmptyString(companyEmailFld.getText())) {
			Alert info = new Alert(Alert.AlertType.INFORMATION);
			info.setTitle("Wrong Telephone Number");
			info.setContentText(
					"Please insert a telephone number like 2XXXXXXXXX with 10 digits length!!!");
			info.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			info.show();
			companyTelFld.setText("");
		} else if(!isEmptyString(companyNameFld.getText()) && !isEmptyString(companyEmailFld.getText())){
			TelecommunicationCompany company = new TelecommunicationCompany(companyName, companyTel, companyEmail);
			CommonData.addCompany(company);

			companyTableViewSync();
			clearTexts();
		}
	}

	public boolean companyTelIsValid(String companyTel) {
		return companyTel.length() == 10 && (companyTel.charAt(0) == '2')
				&& isTelNumber(companyTel);
	}

	public boolean isTelNumber(String companyTel) {
		char[] telNumArray = companyTel.toCharArray();
		for (char tn : telNumArray) {
			if (!Character.isDigit(tn))
				return false;
		}
		return true;
	}

	public void companyTableViewSync() {
		ArrayList<TelecommunicationCompany> companies = CommonData.getCompanies();
		ObservableList<TelecommunicationCompany> items = companiesTable.getItems();
		items.clear();
		for (TelecommunicationCompany tc : companies) {
			items.add(tc);
		}
	}

	public void createInputs() {
		companyIdLbl = new Label("Company Id:");
		companyNameLbl = new Label("Company Name:");
		companyTelLbl = new Label("Company Telephone:");
		companyEmailLbl = new Label("Company Email:");

		companyIdFld = new TextField();
		companyNameFld = new TextField();
		companyTelFld = new TextField();
		companyEmailFld = new TextField();

		companyIdFld.setEditable(false);
		companyIdFld.setDisable(true);

		companyIdFld.setPrefColumnCount(20);
		companyNameFld.setPrefColumnCount(15);
		companyTelFld.setPrefColumnCount(15);
		companyEmailFld.setPrefColumnCount(15);

		inputsGridPane = new GridPane();
		inputsGridPane.add(companyIdLbl, 0, 0);
		inputsGridPane.add(companyIdFld, 0, 1);
		inputsGridPane.add(companyNameLbl, 0, 2);
		inputsGridPane.add(companyNameFld, 0, 3);
		inputsGridPane.add(companyTelLbl, 0, 4);
		inputsGridPane.add(companyTelFld, 0, 5);
		inputsGridPane.add(companyEmailLbl, 0, 6);
		inputsGridPane.add(companyEmailFld, 0, 7);

		inputsGridPane.setVgap(5);
	}

	public void createButtons() {
		addCompanyBtn = new Button("Add Company");
		modifyCompanyBtn = new Button("Modify Company");
		delCompanyBtn = new Button("Delete Company");
		backBtn = new Button("Back");

		addCompanyBtn.setOnMouseClicked(this);
		modifyCompanyBtn.setOnMouseClicked(this);
		delCompanyBtn.setOnMouseClicked(this);
		backBtn.setOnMouseClicked(this);

		btnFlowPane = new FlowPane();
		btnFlowPane.getChildren().addAll(addCompanyBtn, modifyCompanyBtn, delCompanyBtn, backBtn);
		btnFlowPane.setHgap(10);
		btnFlowPane.setVgap(10);
		btnFlowPane.setAlignment(Pos.BOTTOM_CENTER);
	}

	public void createTableView() {
		companiesTable = new TableView<>();
		companiesTable.setMinWidth(550);

		TableColumn<TelecommunicationCompany, String> companyIdCol = new TableColumn<>("Id");
		companyIdCol.setCellValueFactory(new PropertyValueFactory<>("companyId"));
		companyIdCol.setMinWidth(250);
		companiesTable.getColumns().add(companyIdCol);

		TableColumn<TelecommunicationCompany, String> companyNameCol = new TableColumn<>("Company");
		companyNameCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
		companyNameCol.setMinWidth(100);
		companiesTable.getColumns().add(companyNameCol);

		TableColumn<TelecommunicationCompany, String> companyTelCol = new TableColumn<>("Tel");
		companyTelCol.setCellValueFactory(new PropertyValueFactory<>("companyTel"));
		companyTelCol.setMinWidth(100);
		companiesTable.getColumns().add(companyTelCol);

		TableColumn<TelecommunicationCompany, String> companyEmailCol = new TableColumn<>("Email");
		companyEmailCol.setCellValueFactory(new PropertyValueFactory<>("companyEmail"));
		companyEmailCol.setMinWidth(100);
		companiesTable.getColumns().add(companyEmailCol);
	}

	public void clearTexts() {
		companyIdFld.setText("");
		companyNameFld.setText("");
		companyTelFld.setText("");
		companyEmailFld.setText("");
	}
}
