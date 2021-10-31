package bou.maria.gui.SeptemberProject;

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
import javafx.scene.text.Font;
import java.util.ArrayList;
import bou.maria.model.LandlinePlan;
import bou.maria.model.LineType;
import bou.maria.model.MobilePlan;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

public class PlanScene extends SceneCreator implements EventHandler<MouseEvent> {

	GridPane rootGridPane, inputsGridPane = new GridPane(), chooseCbxPane;
	FlowPane btnFlowPane;
	Button addPlanBtn, searchPlanBtn, modifyPlanBtn, deletePlanBtn, backBtn;
	Label choosePlanLbl, planIdLbl, companyNameLbl, freeCallsLbl, planCostLbl, freeSMSLbl, freeGBLbl, speedLineLbl,
			lineTypeLbl;
	TextField planIdFld, companyNameFld, freeCallsFld, planCostFld, freeSMSFld, freeGBFld, speedLineFld;
	ComboBox<String> lineTypeCbx, choosePlanCbx;
	TableView<LandlinePlan> landlinePlanTable = new TableView<>();
	TableView<MobilePlan> mobilePlanTable = new TableView<>();
	String lineType = LineType.ADSL.toString(), planType;

	public PlanScene(double width, double height) {
		super(width, height);

		createChooseCbx();

		rootGridPane = new GridPane();
		rootGridPane.add(chooseCbxPane, 0, 0);
		rootGridPane.setHgap(10);
		rootGridPane.setVgap(10);
	}

	@Override
	public Scene createScene() {
		return new Scene(rootGridPane, width, height);
	}

	@Override
	public void handle(MouseEvent event) {
		if (event.getSource() == addPlanBtn) {
			triggerAddBtn();
		} else if (event.getSource() == backBtn) {
			goBack();
		} else if (event.getSource() == searchPlanBtn) {
			triggerSearchBtn();
		} else if (event.getSource() == modifyPlanBtn) {
			triggerModifyBtn();
		} else if (event.getSource() == deletePlanBtn) {
			triggerDeleteBtn();
		}
	}
	
	// deletes a plan if contract doesn't exists
	public void triggerDeleteBtn() {
		boolean removed = CommonData.removePlan(companyNameFld.getText(), planType);
		if(removed) {
			if (planType.equals("MOBILE")) {
				mobilePlanTableSync();
				clearMobilePlanTexts();
			} else if (planType.equals("LANDLINE")) {
				landlinePlanTableSync();
				clearLandlinePlanTexts();
			}
		}else {
			Alert info = new Alert(Alert.AlertType.INFORMATION);
			info.setTitle("This plan has at least one contract!");
			info.setContentText("This plan has at least one contract!!!");
			info.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			info.show();
		}
		
	}

	// modify plans
	public void triggerModifyBtn() {
		if (planType.equals("MOBILE")) {
			modifyMobilePlan();
		} else if (planType.equals("LANDLINE")) {
			modifyLandlinePlan();
		}
	}

	// modify a mobile plan,except from plan id and company
	public void modifyMobilePlan() {
		if (!isEmptyString(planIdFld.getText()) && !isEmptyString(companyNameFld.getText())
				&& !isEmptyString(freeCallsFld.getText()) && !isEmptyString(planCostFld.getText())
				&& !isEmptyString(freeSMSFld.getText()) && !isEmptyString(freeGBFld.getText())) {
			CommonData.modifyPlan(planIdFld.getText(), companyNameFld.getText(),
					Integer.valueOf(freeCallsFld.getText()), Integer.valueOf(freeGBFld.getText()),
					Integer.valueOf(freeSMSFld.getText()), Double.valueOf(planCostFld.getText()), null, 0, "MOBILE");
			mobilePlanTableSync();
			clearMobilePlanTexts();
		} else {
			Alert info = new Alert(Alert.AlertType.INFORMATION);
			info.setTitle("Missing Information");
			info.setContentText("Please complete the required information!!!");
			info.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			info.show();
		}
	}

	
	// modify a landline plan,except from plan id and company
	public void modifyLandlinePlan() {
		if (!isEmptyString(planIdFld.getText()) && !isEmptyString(companyNameFld.getText())
				&& !isEmptyString(freeCallsFld.getText()) && !isEmptyString(planCostFld.getText())
				&& !isEmptyString(speedLineFld.getText()) && !isEmptyString(lineType)) {
			CommonData.modifyPlan(planIdFld.getText(), companyNameFld.getText(),
					Integer.valueOf(freeCallsFld.getText()), 0, 0, Double.valueOf(planCostFld.getText()),
					LineType.valueOf(lineType), Integer.valueOf(speedLineFld.getText()), "LANDLINE");
			landlinePlanTableSync();
			clearLandlinePlanTexts();
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

	// search plans
	public void triggerSearchBtn() {
		if (planType.equals("MOBILE")) {
			searchMobilePlan();
		} else if (planType.equals("LANDLINE")) {
			searchLandlinePlan();
		}
	}

	// search mobile plan by company name
	public void searchMobilePlan() {
		ArrayList<MobilePlan> mobilePlans = CommonData.getMobilePlans();
		ObservableList<MobilePlan> items = mobilePlanTable.getItems();
		items.clear();
		for (MobilePlan mp : mobilePlans) {
			if (mp.getCompanyName().equals(companyNameFld.getText())) {
				items.add(mp);
			}
		}
	}

	// search landline plan by company name
	public void searchLandlinePlan() {
		ArrayList<LandlinePlan> landlinePlans = CommonData.getLandlinePlans();
		ObservableList<LandlinePlan> items = landlinePlanTable.getItems();
		items.clear();
		for (LandlinePlan lp : landlinePlans) {
			if (lp.getCompanyName().equals(companyNameFld.getText())) {
				items.add(lp);
			}
		}
	}

	public void goBack() {
		App.primaryStage.setTitle("Telecommunication Services Store");
		App.primaryStage.setScene(App.mainScene);
	}

	// add plans
	public void triggerAddBtn() {
		if (planType.equals("LANDLINE")) {
			addLandlinePlan();
		} else if (planType.equals("MOBILE")) {
			addMobilePlan();
		}
	}

	public void addMobilePlan() {
		boolean companyExists = CommonData.companyExists(companyNameFld.getText());
		if (companyExists && !isEmptyString(freeCallsFld.getText()) && !isEmptyString(planCostFld.getText())
				&& !isEmptyString(freeSMSFld.getText()) && !isEmptyString(freeGBFld.getText())) {
			MobilePlan mbp = new MobilePlan(companyNameFld.getText(), Integer.valueOf(freeCallsFld.getText()),
					Double.valueOf(planCostFld.getText()), Integer.valueOf(freeSMSFld.getText()),
					Integer.valueOf(freeGBFld.getText()));
			CommonData.addPlan(mbp);
			mobilePlanTableSync();
			clearMobilePlanTexts();
		} else {
			Alert info = new Alert(Alert.AlertType.INFORMATION);
			info.setTitle("You dont complete the required data!");
			info.setContentText("Check if inputs are completed and the company name is right!");
			info.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			info.show();
		}
	}

	public void addLandlinePlan() {
		boolean companyExists = CommonData.companyExists(companyNameFld.getText());
		if (companyExists && !isEmptyString(freeCallsFld.getText()) && !isEmptyString(planCostFld.getText())
				&& !isEmptyString(speedLineFld.getText())) {
			LandlinePlan ldp = new LandlinePlan(companyNameFld.getText(), Integer.valueOf(freeCallsFld.getText()),
					Double.valueOf(planCostFld.getText()), Integer.valueOf(speedLineFld.getText()),
					LineType.valueOf(lineType));
			CommonData.addPlan(ldp);
			landlinePlanTableSync();
			clearLandlinePlanTexts();
		} else {
			Alert info = new Alert(Alert.AlertType.INFORMATION);
			info.setTitle("You dont complete the required data!");
			info.setContentText("Check if inputs are completed and the company name is right!");
			info.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			info.show();
		}
	}

	public void createChooseCbx() {
		choosePlanLbl = new Label("Choose your plan:");
		choosePlanLbl.setFont(new Font("Serif", 16));
		choosePlanCbx = new ComboBox<>();
		choosePlanCbx.getItems().addAll("Choose...", "LANDLINE", "MOBILE");
		choosePlanCbx.getSelectionModel().selectFirst();

		choosePlanCbx.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				planType = newValue;
				if (newValue.equals("MOBILE")) {
					createMobilePlanTable();
					getSelectedMobileTable();
					createMobileInputs();
					createButtons();
				} else if (newValue.equals("LANDLINE")) {
					createLandlinePlanTable();
					getSelectedLandlineTable();
					createLandlineInputs();
					createButtons();
				}
			}

		});

		chooseCbxPane = new GridPane();
		chooseCbxPane.add(choosePlanLbl, 0, 0);
		chooseCbxPane.add(choosePlanCbx, 0, 1);
	}

	public void createLandlinePlanTable() {
		mobilePlanTable.setVisible(false);
		landlinePlanTable.setVisible(true);
		rootGridPane.getChildren().remove(mobilePlanTable);
		rootGridPane.getChildren().remove(landlinePlanTable);
		landlinePlanTable.getColumns().clear();
		landlinePlanTable.setMinWidth(750);

		TableColumn<LandlinePlan, String> planIdCol = new TableColumn<>("Plan ID");
		planIdCol.setCellValueFactory(new PropertyValueFactory<>("planId"));
		planIdCol.setMinWidth(250);
		landlinePlanTable.getColumns().add(planIdCol);

		TableColumn<LandlinePlan, String> companyNameCol = new TableColumn<>("Company");
		companyNameCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
		companyNameCol.setMinWidth(100);
		landlinePlanTable.getColumns().add(companyNameCol);

		TableColumn<LandlinePlan, Integer> freeCallsCol = new TableColumn<>("Free Calls");
		freeCallsCol.setCellValueFactory(new PropertyValueFactory<>("freeCalls"));
		freeCallsCol.setMinWidth(100);
		landlinePlanTable.getColumns().add(freeCallsCol);

		TableColumn<LandlinePlan, Double> planCostCol = new TableColumn<>("Plan Cost");
		planCostCol.setCellValueFactory(new PropertyValueFactory<>("planCost"));
		planCostCol.setMinWidth(100);
		landlinePlanTable.getColumns().add(planCostCol);

		TableColumn<LandlinePlan, Integer> speedLineCol = new TableColumn<>("Line Speed");
		speedLineCol.setCellValueFactory(new PropertyValueFactory<>("speedLine"));
		speedLineCol.setMinWidth(100);
		landlinePlanTable.getColumns().add(speedLineCol);

		TableColumn<LandlinePlan, String> lineTypeCol = new TableColumn<>("Line Type");
		lineTypeCol.setCellValueFactory(new PropertyValueFactory<>("lineType"));
		lineTypeCol.setMinWidth(100);
		landlinePlanTable.getColumns().add(lineTypeCol);

		rootGridPane.add(landlinePlanTable, 0, 2);
	}

	public void createMobilePlanTable() {
		mobilePlanTable.setVisible(true);
		landlinePlanTable.setVisible(false);
		rootGridPane.getChildren().remove(landlinePlanTable);
		rootGridPane.getChildren().remove(mobilePlanTable);
		mobilePlanTable.getColumns().clear();
		mobilePlanTable.setMinWidth(750);

		TableColumn<MobilePlan, String> planIdCol = new TableColumn<>("Plan ID");
		planIdCol.setCellValueFactory(new PropertyValueFactory<>("planId"));
		planIdCol.setMinWidth(250);
		mobilePlanTable.getColumns().add(planIdCol);

		TableColumn<MobilePlan, String> companyNameCol = new TableColumn<>("Company");
		companyNameCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
		companyNameCol.setMinWidth(100);
		mobilePlanTable.getColumns().add(companyNameCol);

		TableColumn<MobilePlan, Integer> freeCallsCol = new TableColumn<>("Free Calls");
		freeCallsCol.setCellValueFactory(new PropertyValueFactory<>("freeCalls"));
		freeCallsCol.setMinWidth(100);
		mobilePlanTable.getColumns().add(freeCallsCol);

		TableColumn<MobilePlan, Double> planCostCol = new TableColumn<>("Plan Cost");
		planCostCol.setCellValueFactory(new PropertyValueFactory<>("planCost"));
		planCostCol.setMinWidth(100);
		mobilePlanTable.getColumns().add(planCostCol);

		TableColumn<MobilePlan, Integer> freeSMSCol = new TableColumn<>("Free SMS");
		freeSMSCol.setCellValueFactory(new PropertyValueFactory<>("freeSMS"));
		freeSMSCol.setMinWidth(100);
		mobilePlanTable.getColumns().add(freeSMSCol);

		TableColumn<MobilePlan, Integer> freeGBCol = new TableColumn<>("Free GB");
		freeGBCol.setCellValueFactory(new PropertyValueFactory<>("freeGB"));
		freeGBCol.setMinWidth(100);
		mobilePlanTable.getColumns().add(freeGBCol);

		rootGridPane.add(mobilePlanTable, 0, 2);
	}

	public void createMobileInputs() {
		planIdLbl = new Label("Plan ID:");
		companyNameLbl = new Label("Company Name:");
		freeCallsLbl = new Label("Free Calls:");
		planCostLbl = new Label("Plan Cost:");
		freeSMSLbl = new Label("Free SMS:");
		freeGBLbl = new Label("Free GB:");

		planIdFld = new TextField();
		companyNameFld = new TextField();
		freeCallsFld = new TextField();
		planCostFld = new TextField();
		freeSMSFld = new TextField();
		freeGBFld = new TextField();

		planIdFld.setPrefColumnCount(20);
		companyNameFld.setPrefColumnCount(20);
		freeCallsFld.setPrefColumnCount(20);
		planCostFld.setPrefColumnCount(20);
		freeSMSFld.setPrefColumnCount(20);
		freeGBFld.setPrefColumnCount(20);

		planIdFld.setEditable(false);
		planIdFld.setDisable(true);

		rootGridPane.getChildren().remove(inputsGridPane);
		inputsGridPane.getChildren().clear();
		inputsGridPane.add(planIdLbl, 0, 0);
		inputsGridPane.add(planIdFld, 0, 1);
		inputsGridPane.add(companyNameLbl, 0, 2);
		inputsGridPane.add(companyNameFld, 0, 3);
		inputsGridPane.add(planCostLbl, 0, 4);
		inputsGridPane.add(planCostFld, 0, 5);
		inputsGridPane.add(freeCallsLbl, 0, 6);
		inputsGridPane.add(freeCallsFld, 0, 7);
		inputsGridPane.add(freeSMSLbl, 0, 8);
		inputsGridPane.add(freeSMSFld, 0, 9);
		inputsGridPane.add(freeGBLbl, 0, 10);
		inputsGridPane.add(freeGBFld, 0, 11);
		rootGridPane.add(inputsGridPane, 1, 2);
	}

	public void createLandlineInputs() {
		planIdLbl = new Label("Plan ID:");
		companyNameLbl = new Label("Company Name:");
		freeCallsLbl = new Label("Free Calls:");
		planCostLbl = new Label("Plan Cost:");
		speedLineLbl = new Label("Speed Line:");
		lineTypeLbl = new Label("Line Type:");

		planIdFld = new TextField();
		companyNameFld = new TextField();

		freeCallsFld = new TextField();
		planCostFld = new TextField();
		speedLineFld = new TextField();
		lineTypeCbx = new ComboBox<>();

		planIdFld.setPrefColumnCount(20);
		companyNameFld.setPrefColumnCount(20);
		freeCallsFld.setPrefColumnCount(20);
		planCostFld.setPrefColumnCount(20);
		speedLineFld.setPrefColumnCount(20);

		planIdFld.setEditable(false);
		planIdFld.setDisable(true);

		lineTypeCbx.getItems().addAll(LineType.ADSL.toString(), LineType.VDSL.toString());
		lineTypeCbx.getSelectionModel().selectFirst();
		lineTypeCbx.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				lineType = newValue;
			}

		});

		rootGridPane.getChildren().remove(inputsGridPane);
		inputsGridPane.getChildren().clear();
		inputsGridPane.add(planIdLbl, 0, 0);
		inputsGridPane.add(planIdFld, 0, 1);
		inputsGridPane.add(companyNameLbl, 0, 3);
		inputsGridPane.add(companyNameFld, 0, 4);
		inputsGridPane.add(freeCallsLbl, 0, 5);
		inputsGridPane.add(freeCallsFld, 0, 6);
		inputsGridPane.add(planCostLbl, 0, 7);
		inputsGridPane.add(planCostFld, 0, 8);
		inputsGridPane.add(speedLineLbl, 0, 9);
		inputsGridPane.add(speedLineFld, 0, 10);
		inputsGridPane.add(lineTypeLbl, 0, 11);
		inputsGridPane.add(lineTypeCbx, 0, 12);
		rootGridPane.add(inputsGridPane, 1, 2);
	}

	public void createButtons() {
		addPlanBtn = new Button("Add Plan");
		searchPlanBtn = new Button("Search Plan");
		modifyPlanBtn = new Button("Modify Plan");
		deletePlanBtn = new Button("Delete Plan");
		backBtn = new Button("Back");

		addPlanBtn.setOnMouseClicked(this);
		searchPlanBtn.setOnMouseClicked(this);
		modifyPlanBtn.setOnMouseClicked(this);
		deletePlanBtn.setOnMouseClicked(this);
		backBtn.setOnMouseClicked(this);

		FlowPane btnFlowPane = new FlowPane();
		btnFlowPane.getChildren().addAll(addPlanBtn, searchPlanBtn, modifyPlanBtn, deletePlanBtn, backBtn);
		btnFlowPane.setHgap(10);
		btnFlowPane.setAlignment(Pos.BASELINE_CENTER);

		rootGridPane.add(btnFlowPane, 0, 3);
	}

	public void clearMobilePlanTexts() {
		planIdFld.setText("");
		companyNameFld.setText("");
		freeCallsFld.setText("");
		planCostFld.setText("");
		freeSMSFld.setText("");
		freeGBFld.setText("");
	}

	public void clearLandlinePlanTexts() {
		planIdFld.setText("");
		companyNameFld.setText("");
		freeCallsFld.setText("");
		planCostFld.setText("");
		speedLineFld.setText("");
		lineTypeCbx.getSelectionModel().selectFirst();
	}

	public void mobilePlanTableSync() {
		ArrayList<MobilePlan> mobilePlans = CommonData.getMobilePlans();
		ObservableList<MobilePlan> items = mobilePlanTable.getItems();
		items.clear();
		for (MobilePlan mp : mobilePlans) {
			items.add(mp);
		}
	}

	public void landlinePlanTableSync() {
		ArrayList<LandlinePlan> landlinePlans = CommonData.getLandlinePlans();
		ObservableList<LandlinePlan> items = landlinePlanTable.getItems();
		items.clear();
		for (LandlinePlan lp : landlinePlans) {
			items.add(lp);
		}
	}

	public void getSelectedMobileTable() {
		mobilePlanTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MobilePlan>() {

			@Override
			public void changed(ObservableValue<? extends MobilePlan> observable, MobilePlan oldValue,
					MobilePlan newValue) {
				if (newValue != null) {
					planIdFld.setText(newValue.getPlanId());
					companyNameFld.setText(newValue.getCompanyName());
					freeCallsFld.setText(String.valueOf(newValue.getFreeCalls()));
					planCostFld.setText(String.valueOf(newValue.getPlanCost()));
					freeSMSFld.setText(String.valueOf(newValue.getFreeSMS()));
					freeGBFld.setText(String.valueOf(newValue.getFreeGB()));
				}
			}
		});
	}

	public void getSelectedLandlineTable() {
		landlinePlanTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LandlinePlan>() {

			@Override
			public void changed(ObservableValue<? extends LandlinePlan> observable, LandlinePlan oldValue,
					LandlinePlan newValue) {
				if (newValue != null) {
					planIdFld.setText(newValue.getPlanId());
					companyNameFld.setText(newValue.getCompanyName());
					freeCallsFld.setText(String.valueOf(newValue.getFreeCalls()));
					planCostFld.setText(String.valueOf(newValue.getPlanCost()));
					speedLineFld.setText(String.valueOf(newValue.getSpeedLine()));
					lineTypeCbx.setValue(newValue.getLineType().toString());
				}
			}

		});
	}
}
