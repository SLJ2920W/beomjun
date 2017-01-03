package application.cs.mail.handler.file;

import java.io.File;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.util.MimeUtils;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class FileView extends TabPane {


	private Tab tab = new Tab();

	private final ObservableList<Node> textFileData = FXCollections.observableArrayList();
	private final ObservableList<FileItem> duplicatePrevention = FXCollections.observableArrayList();



	public FileView(FileItem newValue) throws Exception {
		
		if (newValue == null)
			return;
		if (duplicatePrevention.contains(newValue)) {
			tab = getTabs().stream().filter(new Predicate<Tab>() {
				@Override
				public boolean test(Tab t) {
					return t.getId().equals(getTabId(newValue.getFileName()));
				}
			}).findFirst().orElse(null);
			getSelectionModel().select(tab);
			return;
		} else {
			duplicatePrevention.add(newValue);
		}

		// 마임 파싱
		File f = MimeUtils.decodeLocalForSearch(new File(newValue.getFilePath()));

		// 탭 타이틀 표시 하기 위해 마임 파싱
		MetaData meta = new MetaData(Paths.get(newValue.getFilePath()));

		String url = f.getCanonicalPath();
		tab.setClosable(true);
		tab.setText(meta.getMetaTabTitle());
		tab.setId(getTabId(newValue.getFileName()));

		// 컨텍스트 메뉴 탭마다 독립적으로...
		tab.setContextMenu(setFileViewContext(tab));

		// [s] 컨텐츠 생성
		VBox vbox = new VBox();
		textFileData.clear();
		createLabel("제목 : ", meta.getMetaSubject(), textFileData);
		createLabel("전송일 : ", meta.getMetaSentDate(), textFileData);
		createLabel("보낸 사람 : ", meta.getMetaFrom(), textFileData);
		createLabel("받는 사람 : ", meta.getMetaTo(), textFileData);
		createLabel("회신 받는 주소 :", meta.getMetaReplyTo(), textFileData);
		createLabel("참조 : ", meta.getMetaCC(), textFileData);
		createLabel("숨은 참조 : ", meta.getMetaBCC(), textFileData);
		textFileData.add(new Browser(url));
		vbox.getChildren().addAll(textFileData);
		// [e] 컨텐츠 생성

		// 탭 셋
		HBox root = new HBox();
		HBox.setHgrow(vbox, Priority.ALWAYS);
		root.getChildren().addAll(vbox);

		tab.setContent(root);
		setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);
		getSelectionModel().select(tab);
		getTabs().add(tab);
		
		
	}

	// 탭에 컨텍스트 메뉴 이벤트 처리
	public ContextMenu setFileViewContext(Tab tab) {
		final ContextMenu context = new ContextMenu();
		MenuItem menu = null;

		// 현재 닫기
		menu = new MenuItem("닫기");
		menu.setOnAction((ActionEvent e) -> getTabs().remove(tab));
		context.getItems().add(menu);

		// 다른 창 닫기
		menu = new MenuItem("다른창 닫기");
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				getSelectionModel().select(tab);
				int si = getSelectionModel().getSelectedIndex();
				getTabs().remove(si + 1, getTabs().size());
				getTabs().remove(0, si);
			}
		});
		context.getItems().add(menu);

		// 좌측 닫기
		menu = new MenuItem("좌측 닫기");
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				getSelectionModel().select(tab);
				int end = getSelectionModel().getSelectedIndex();
				getTabs().remove(0, end);
			}
		});
		context.getItems().add(menu);

		// 우측 닫기
		menu = new MenuItem("우측 닫기");
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				getSelectionModel().select(tab);
				int start = getSelectionModel().getSelectedIndex();
				getTabs().remove(start + 1, getTabs().size());
			}
		});
		context.getItems().add(menu);

		menu = new SeparatorMenuItem();
		context.getItems().add(menu);

		// 전체 닫기
		menu = new MenuItem("전체 닫기");
		menu.setOnAction((ActionEvent e) -> getTabs().clear());
		context.getItems().add(menu);

		return context;
	}

	// 발신자 수신자등 정보를 텍스트 필드로 만듬
	private void createLabel(String labelName, String value, ObservableList<Node> list) {
		if (value != null) {
			HBox row = new HBox(20);
			Label label = new Label(labelName);
			label.setMinWidth(120);
			TextField tf = new TextField(value);
			tf.setEditable(false);
			HBox.setHgrow(tf, Priority.ALWAYS);
			row.getChildren().addAll(label, tf);
			list.add(row);
		}
	}

	// 탭 마다 고유 아이디를 생성 2016-07-25 030819_시스템운영자(System Administrator)_업무.... ->
	// 2016-07-25_030819
	private String getTabId(String fileName) {
		Matcher m = Pattern.compile("\\d{4}[-]?\\d{2}[-]?\\d{2}\\s\\d{6}").matcher(fileName);
		if (m.find()) {
			return m.group().replaceAll("\\s", "_");
		}
		return fileName.replaceAll("\\s", "_");
	}

}
