package application.sample.comm.controller;

import application.sample.comm.controller.tab.Tab1Controller;
import application.sample.comm.controller.tab.Tab2Controller;
import javafx.fxml.FXML;

public class MainController {
	@FXML Tab1Controller tab1Controller;
	@FXML Tab2Controller tab2Controller;
	
	@FXML public void initialize() {
		System.out.println("Application started");
		tab1Controller.init(this);
		tab2Controller.init(this);
	}

	public String loadLblTextFromTab1() {
		return tab1Controller.lbl1.getText();
	}

	public void setTab2LabelText(String text) {
		tab2Controller.lbl2.setText(text);
	}
}
