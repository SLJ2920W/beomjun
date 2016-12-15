package application.cs.mail.view;

import java.io.File;

import application.cs.mail.Main;
import application.sample.filetreeviewsample.PathItem;
import application.sample.filetreeviewsample.PathTreeCell;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;

public class MailViewController {

	private Main main;
	
	@FXML
	private TreeView<File> treeView;
	
	
//	@FXML
//	private TableColumn<Person, String> firstNameColumn;
//	@FXML
//	private TableColumn<Person, String> lastNameColumn;

	public void setTreeView(TreeItem<File> value) {
		treeView.setRoot(value);
	}



	@FXML
	private void initialize() {
		
		
//		treeView.setCellFactory((TreeView<PathItem> p) -> {
//            final PathTreeCell cell = new PathTreeCell(stage, messageProp);
//            setDragDropEvent(stage, cell);
//            return cell;
//        });
		
		// 연락처 테이블의 두 열을 초기화한다.
//		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
//		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

		// 연락처 정보를 지운다.
		// showPersonDetails(null);

		// modern 선택을 감지하고 그 때마다 연락처의 자세한 정보를 보여준다.
//		personTable.getSelectionModel().selectedItemProperty().addListener(
//				(observable, oldValue, newValue) -> showPersonDetails(newValue));
	}

//	private void showPersonDetails(Person person) {
//		if (person != null) {
//			// person 객체로 label에 정보를 채운다.
//			firstNameLabel.setText(person.getFirstName());
//			lastNameLabel.setText(person.getLastName());
//			streetLabel.setText(person.getStreet());
//			postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
//			cityLabel.setText(person.getCity());
//			birthdayLabel.setText(DateUtil.format(person.getBirthday()));
//		} else {
//			// person이 null이면 모든 텍스트를 지운다.
//			firstNameLabel.setText("");
//			lastNameLabel.setText("");
//			streetLabel.setText("");
//			postalCodeLabel.setText("");
//			cityLabel.setText("");
//			birthdayLabel.setText("");
//		}
//	}

}
