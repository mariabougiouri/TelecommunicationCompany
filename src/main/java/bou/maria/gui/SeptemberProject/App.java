package bou.maria.gui.SeptemberProject;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class App extends Application{

	static Stage primaryStage;
	static Scene mainScene,companyScene,planScene,clientScene,contractScene;
	
	@Override
	public void start(Stage primaryStage) {
		
		this.primaryStage = primaryStage;
	
		SceneCreator mainSceneCreator = new MainScene(850,500);
		mainScene = mainSceneCreator.createScene();
		
		SceneCreator companySceneCreator = new CompanyScene(850,500);
		companyScene = companySceneCreator.createScene();
	
		SceneCreator planSceneCreator = new PlanScene(1050,530);
		planScene = planSceneCreator.createScene();
		
		SceneCreator clientSceneCreator = new ClientScene(850,500);
		clientScene = clientSceneCreator.createScene();
		
		SceneCreator contractSceneCreator = new ContractScene(1400,500);
		contractScene = contractSceneCreator.createScene();
		
		primaryStage.setScene(mainScene);
    	primaryStage.setTitle("Telecommunication Services Store");
    	primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch();
	}
	
}