package bou.maria.gui.SeptemberProject;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.input.MouseEvent;

public class MainScene extends SceneCreator implements EventHandler<MouseEvent> {

	FlowPane rootFlowPane;
	Button telCompanyBtn,planBtn,clientBtn,contractBtn;
	
	public MainScene(double width,double height) {
		
		super(width,height);
		
		rootFlowPane = new FlowPane();
		
		telCompanyBtn = new Button("Telecommunications Companies");
		planBtn = new Button("Mobile & Landline Telephony Plans");
		clientBtn = new Button("Clients");
		contractBtn = new Button("Contracts");
		
		telCompanyBtn.setOnMouseClicked(this);
		planBtn.setOnMouseClicked(this);
		clientBtn.setOnMouseClicked(this);
		contractBtn.setOnMouseClicked(this);
		
		rootFlowPane.setHgap(10);
		rootFlowPane.setAlignment(Pos.CENTER);
		
		rootFlowPane.getChildren().add(telCompanyBtn);
		rootFlowPane.getChildren().add(planBtn);
		rootFlowPane.getChildren().add(clientBtn);
		rootFlowPane.getChildren().add(contractBtn);
	
	 }
		
		@Override 
		public void handle(MouseEvent event) {
			
			if (event.getSource() == telCompanyBtn) {
				App.primaryStage.setScene(App.companyScene);
				App.primaryStage.setTitle("Management Of Telecommunications Companies");
			}
			else if (event.getSource() == planBtn) {
				App.primaryStage.setScene(App.planScene);
				App.primaryStage.setTitle("Management Of Telephony Plans");
			}
			else if (event.getSource() == clientBtn) {
				App.primaryStage.setScene(App.clientScene);
				App.primaryStage.setTitle("Client Management");
			}
			else if (event.getSource() == contractBtn) {
				App.primaryStage.setScene(App.contractScene);
				App.primaryStage.setTitle("Contract Management");
				App.primaryStage.setMinWidth(1300);
				App.primaryStage.setMinHeight(650);
			}
		}
		
		@Override
		public Scene createScene() {
			return new Scene(rootFlowPane, width, height);
		}
}
