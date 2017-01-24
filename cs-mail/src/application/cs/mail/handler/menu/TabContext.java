package application.cs.mail.handler.menu;

import application.cs.mail.common.Selection;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabContext extends ContextMenu {

	Selection selection = Selection.getInstance();
	// 현재 닫기
	private MenuItem thisClose;
	// 다른 창 닫기
	private MenuItem otherClose;
	// 좌측 닫기
	private MenuItem leftClose;
	// 우측 닫기
	private MenuItem rightClose;
	// 전체 닫기
	private MenuItem allClose;

	private TabPane tabPane;
	private Tab tab;

	/**
	 * 탭 이벤트를 생성
	 * 중복된 탭 항목을 확인
	 * @param tabPane
	 * @param tab
	 */
	public TabContext(TabPane tabPane, Tab tab) {
		this.tabPane = tabPane;
		this.tab = tab;

		setContextMenu();
		setThisClose();
		setOtherClose();
		setLeftClose();
		setRightClose();
		setAllClose();
	}

	public void setContextMenu() {
		thisClose = new MenuItem("닫기");
		otherClose = new MenuItem("다른창 닫기");
		leftClose = new MenuItem("좌측 닫기");
		rightClose = new MenuItem("우측 닫기");
		allClose = new MenuItem("전체 닫기");
		getItems().addAll(thisClose, new SeparatorMenuItem(), otherClose, leftClose, rightClose, new SeparatorMenuItem(), allClose);
	}

	// 현재 닫기
	public void setThisClose() {
		thisClose.setOnAction((e) -> {
			// 탭닫기
			tabPane.getTabs().remove(tab);
			// 중복 리스트 수정
			tabPane.getSelectionModel().select(tab);
			selection.getDuplicatePrevention().remove(tabPane.getSelectionModel().getSelectedIndex() + 1);
		});
	}

	// 다른 창 닫기
	public void setOtherClose() {
		otherClose.setOnAction((event) -> {
			tabPane.getSelectionModel().select(tab);
			int start = tabPane.getSelectionModel().getSelectedIndex();
			int end = tabPane.getTabs().size();
			// 탭닫기
			tabPane.getTabs().remove(start + 1, end);
			tabPane.getTabs().remove(0, start);
			// 중복 리스트 수정
			selection.getDuplicatePrevention().remove(start + 1, end);
			selection.getDuplicatePrevention().remove(0, start);
		});
	}

	// 좌측 닫기
	public void setLeftClose() {
		leftClose.setOnAction((event) -> {
			tabPane.getSelectionModel().select(tab);
			int end = tabPane.getSelectionModel().getSelectedIndex();
			// 탭닫기
			tabPane.getTabs().remove(0, end);
			// 중복 리스트 수정
			selection.getDuplicatePrevention().remove(0, end);
		});
	}

	// 우측 닫기 - 안됨
	public void setRightClose() {
		rightClose.setOnAction((event) -> {
			tabPane.getSelectionModel().select(tab);
			int start = tabPane.getSelectionModel().getSelectedIndex();
			int end = tabPane.getTabs().size();
			// 탭닫기
			tabPane.getTabs().remove(start + 1, end);
			// 중복 리스트 수정
			selection.getDuplicatePrevention().remove(start + 1, end);
		});
	}

	// 전체 닫기
	public void setAllClose() {
		allClose.setOnAction((e) -> {
			// 탭닫기
			tabPane.getTabs().clear();
			// 중복 리스트 수정
			selection.getDuplicatePrevention().clear();
		});
	}

}
