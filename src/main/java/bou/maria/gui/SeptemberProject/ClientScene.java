package bou.maria.gui.SeptemberProject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import bou.maria.model.Client;
import bou.maria.model.Status;

public class ClientScene extends SceneCreator implements EventHandler<MouseEvent> {

	GridPane rootGridPane, inputsGridPane;
	FlowPane btnFlowPane;
	Button addClientBtn, modifyClientBtn, delClientBtn, backBtn;
	TextField identityNumFld, VATNumFld, fullNameFld, addressFld, telFld, emailFld;
	Label identityNumLbl, VATNumLbl, fullNameLbl, statusLbl, addressLbl, telLbl, emailLbl;
	Client selectedClient;
	TableView<Client> clientsTable;
	ComboBox<String> statusCbx;
	String clientStatus = Status.CITIZEN.toString();

	public ClientScene(double width, double height) {
		
		super(width, height);
		
		createButtons();
		createTableView();
		createInputs();
		getSelectedTableItem();
		searchClients();

		rootGridPane = new GridPane();
		rootGridPane.add(clientsTable, 0, 1);
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
		if (event.getSource() == addClientBtn) {
			addClient();
		} else if (event.getSource() == modifyClientBtn) {
			modifyClient(identityNumFld.getText(), VATNumFld.getText());
		} else if (event.getSource() == delClientBtn) {
			deleteClient();
		} else if (event.getSource() == backBtn) {
			goBack();
		}

	}

	public void goBack() {
		App.primaryStage.setTitle("Telecommunication Services Store");
		App.primaryStage.setScene(App.mainScene);
	}

	public void getSelectedTableItem() {

		clientsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Client>() {

			@Override
			public void changed(ObservableValue<? extends Client> observable, Client oldValue, Client newValue) {
				if (newValue != null) {

					identityNumFld.setDisable(true);
					VATNumFld.setDisable(true);
					identityNumFld.setEditable(false);
					VATNumFld.setEditable(false);

					identityNumFld.setText(newValue.getIdentityNumber());
					VATNumFld.setText(newValue.getVATNumber());
					fullNameFld.setText(newValue.getFullName());
					statusCbx.setValue(newValue.getStatus().toString());
					addressFld.setText(newValue.getAddress());
					telFld.setText(newValue.getTel());
					emailFld.setText(newValue.getEmail());
				}
			}

		});
	}

	//deletes a client which doesn't have contracts
	public void deleteClient() {
		if (!isEmptyString(VATNumFld.getText())) {
			boolean removed = CommonData.removeClient(VATNumFld.getText());
			if(removed) {
				clientTableViewSync();
				clearTexts();
				identityNumFld.setDisable(false);
				VATNumFld.setDisable(false);
				identityNumFld.setEditable(true);
				VATNumFld.setEditable(true);
			}
			else {
				Alert info = new Alert(Alert.AlertType.INFORMATION);
				info.setTitle("Client has contract");
				info.setContentText("Client has contract!!!");
				info.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				info.show();
				clearTexts();
				identityNumFld.setDisable(false);
				VATNumFld.setDisable(false);
				identityNumFld.setEditable(true);
				VATNumFld.setEditable(true);
			}
			
		} else {
			Alert info = new Alert(Alert.AlertType.INFORMATION);
			info.setTitle("Client has contract");
			info.setContentText("Client has contract!!!");
			info.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			info.show();
			clearTexts();
			identityNumFld.setDisable(false);
			VATNumFld.setDisable(false);
			identityNumFld.setEditable(true);
			VATNumFld.setEditable(true);
		}
	}

	//modify the client information,except from identity number and VAT number
	public void modifyClient(String indentityNumber, String VATNumber) {
		if (!isEmptyString(identityNumFld.getText()) && !isEmptyString(VATNumFld.getText())
				&& !isEmptyString(fullNameFld.getText()) && !isEmptyString(clientStatus)
				&& !isEmptyString(addressFld.getText()) && !isEmptyString(telFld.getText())
				&& !isEmptyString(emailFld.getText()) && clientTelIsValid(telFld.getText())) {

			CommonData.modifyClient(identityNumFld.getText(), VATNumFld.getText(), fullNameFld.getText(),
					Status.valueOf(clientStatus), addressFld.getText(), telFld.getText(), emailFld.getText());
			clientTableViewSync();
			clearTexts();
		} else {
			Alert info = new Alert(Alert.AlertType.INFORMATION);
			info.setTitle("Missing Information");
			info.setContentText("Please complete the required information!!!");
			info.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			info.show();
		}

		identityNumFld.setDisable(false);
		VATNumFld.setDisable(false);
		identityNumFld.setEditable(true);
		VATNumFld.setEditable(true);
	}

	public boolean isEmptyString(String str) {
		return str.length() == 0;
	}

	//search clients by identity number or VAT number
	public void searchClients() {
		ArrayList<Client> clients = CommonData.getClients();
		ObservableList<Client> items = clientsTable.getItems();

		ChangeListener<String> searchListener = new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				items.clear();
				for (Client cl : clients) {
					if (cl.getIdentityNumber().contains(newValue) || cl.getVATNumber().contains(newValue)) {
						items.add(cl);
					}
				}
			}
		};

		identityNumFld.textProperty().addListener(searchListener);
		VATNumFld.textProperty().addListener(searchListener);
	}

	public void addClient() {
		String indentityNumber = identityNumFld.getText();
		String VATNumber = VATNumFld.getText();
		String fullName = fullNameFld.getText();
		Status status = Status.valueOf(clientStatus);
		String address = addressFld.getText();
		String tel = telFld.getText();
		String email = emailFld.getText();

		if (!clientTelIsValid(tel) || isEmptyString(identityNumFld.getText()) || isEmptyString(VATNumFld.getText())
				|| isEmptyString(fullNameFld.getText()) || isEmptyString(addressFld.getText())
				|| isEmptyString(emailFld.getText())) {
			Alert info = new Alert(Alert.AlertType.INFORMATION);
			info.setTitle("You dont complete the required data!");
			info.setContentText(
					"Please insert a telephone number like 2XXXXXXXXX or 6XXXXXXXXX with 10 digits length"
					+ "and check if inputs are completed!");
			info.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			info.show();
		} else {
			Client client = new Client(indentityNumber, VATNumber, fullName, status, address, tel, email);
			CommonData.addClient(client);

			clientTableViewSync();
			clearTexts();

		}

	}

	public boolean clientTelIsValid(String clientTel) {
		return clientTel.length() == 10 && (clientTel.charAt(0) == '2' || clientTel.charAt(0) == '6')
				&& isTelNumber(clientTel);
	}

	public boolean isTelNumber(String clientTel) {
		char[] telNumArray = clientTel.toCharArray();
		for (char tn : telNumArray) {
			if (!Character.isDigit(tn))
				return false;
		}
		return true;
	}

	public void clientTableViewSync() {
		ArrayList<Client> clients = CommonData.getClients();
		ObservableList<Client> items = clientsTable.getItems();
		items.clear();

		for (Client cl : clients) {
			items.add(cl);
		}
	}

	public void createInputs() {
		identityNumLbl = new Label("Client Identity:");
		VATNumLbl = new Label("Client VAT Number:");
		fullNameLbl = new Label("Full Name:");
		statusLbl = new Label("Client Status:");
		addressLbl = new Label("Client Address:");
		telLbl = new Label("Client Telephone:");
		emailLbl = new Label("Client Email:");

		identityNumFld = new TextField();
		VATNumFld = new TextField();
		fullNameFld = new TextField();
		statusCbx = new ComboBox<>();
		addressFld = new TextField();
		telFld = new TextField();
		emailFld = new TextField();

		identityNumFld.setPrefColumnCount(15);
		VATNumFld.setPrefColumnCount(15);
		fullNameFld.setPrefColumnCount(15);
		addressFld.setPrefColumnCount(15);
		telFld.setPrefColumnCount(15);
		emailFld.setPrefColumnCount(15);

		statusCbx.getItems().addAll(Status.CITIZEN.toString(), Status.STUDENT.toString(),
				Status.PROFESSIONAL.toString());
		statusCbx.getSelectionModel().selectFirst();
		statusCbx.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				clientStatus = newValue;
			}

		});

		inputsGridPane = new GridPane();
		inputsGridPane.add(identityNumLbl, 0, 0);
		inputsGridPane.add(identityNumFld, 0, 1);
		inputsGridPane.add(VATNumLbl, 0, 2);
		inputsGridPane.add(VATNumFld, 0, 3);
		inputsGridPane.add(fullNameLbl, 0, 4);
		inputsGridPane.add(fullNameFld, 0, 5);
		inputsGridPane.add(addressLbl, 0, 6);
		inputsGridPane.add(addressFld, 0, 7);
		inputsGridPane.add(telLbl, 0, 8);
		inputsGridPane.add(telFld, 0, 9);
		inputsGridPane.add(emailLbl, 0, 10);
		inputsGridPane.add(emailFld, 0, 11);
		inputsGridPane.add(statusLbl, 0, 12);
		inputsGridPane.add(statusCbx, 0, 13);

		inputsGridPane.setVgap(5);

	}

	public void createButtons() {
		addClientBtn = new Button("Add Client");
		modifyClientBtn = new Button("Modify Client");
		delClientBtn = new Button("Delete Client");
		backBtn = new Button("Back");

		addClientBtn.setOnMouseClicked(this);
		modifyClientBtn.setOnMouseClicked(this);
		delClientBtn.setOnMouseClicked(this);
		backBtn.setOnMouseClicked(this);

		btnFlowPane = new FlowPane();
		btnFlowPane.getChildren().addAll(addClientBtn, modifyClientBtn, delClientBtn, backBtn);
		btnFlowPane.setHgap(10);
		btnFlowPane.setAlignment(Pos.BOTTOM_CENTER);
	}

	public void createTableView() {
		clientsTable = new TableView();
		clientsTable.setMinWidth(600);

		TableColumn<Client, String> indentityNumCol = new TableColumn<>("Identity");
		indentityNumCol.setCellValueFactory(new PropertyValueFactory<>("identityNumber"));
		indentityNumCol.setMinWidth(60);
		clientsTable.getColumns().add(indentityNumCol);

		TableColumn<Client, String> VATNumCol = new TableColumn<>("VAT Number");
		VATNumCol.setCellValueFactory(new PropertyValueFactory<>("VATNumber"));
		VATNumCol.setMinWidth(80);
		clientsTable.getColumns().add(VATNumCol);

		TableColumn<Client, String> fullNameCol = new TableColumn<>("Client");
		fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
		fullNameCol.setMinWidth(100);
		clientsTable.getColumns().add(fullNameCol);

		TableColumn<Client, String> statusCol = new TableColumn<>("Status");
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		statusCol.setMinWidth(85);
		clientsTable.getColumns().add(statusCol);

		TableColumn<Client, String> addressCol = new TableColumn<>("Address");
		addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
		addressCol.setMinWidth(85);
		clientsTable.getColumns().add(addressCol);

		TableColumn<Client, String> telCol = new TableColumn<>("Tel");
		telCol.setCellValueFactory(new PropertyValueFactory<>("tel"));
		telCol.setMinWidth(80);
		clientsTable.getColumns().add(telCol);

		TableColumn<Client, String> emailCol = new TableColumn<>("Email");
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		emailCol.setMinWidth(110);
		clientsTable.getColumns().add(emailCol);
	}

	public void clearTexts() {
		identityNumFld.setText("");
		VATNumFld.setText("");
		fullNameFld.setText("");
		statusCbx.getSelectionModel().selectFirst();
		addressFld.setText("");
		telFld.setText("");
		emailFld.setText("");
	}

}
