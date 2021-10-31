module bou.maria.gui.SeptemberProject {
    requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	opens bou.maria.model to javafx.base;
    exports bou.maria.gui.SeptemberProject;
}
