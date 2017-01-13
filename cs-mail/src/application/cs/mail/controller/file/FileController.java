package application.cs.mail.controller.file;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.cs.mail.common.Selection;
import application.cs.mail.controller.MainController;
import application.cs.mail.handler.DaemonThreadFactory;
import application.cs.mail.handler.file.Browser;
import application.cs.mail.handler.file.FileItem;
import application.cs.mail.handler.file.FileWalker;
import application.cs.mail.handler.mime.MetaData;
import application.cs.mail.handler.search.SearchType;
import application.cs.mail.handler.search.TaskChangeToHtml;
import application.cs.mail.handler.search.TaskLuceneIndex;
import application.cs.mail.handler.search.TaskLuceneIndex.Mode;
import application.cs.mail.handler.search.TaskLuceneSearch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class FileController implements Initializable {

	@FXML
	private TableView<FileItem> fileListView;
	@FXML
	private TableColumn<FileItem, Hyperlink> fileNameColumn;
	@FXML
	private TableColumn<FileItem, String> fileTitleColumn;
	@FXML
	private TableColumn<FileItem, Long> fileCommentColumn;

	@FXML
	private ComboBox<SearchType> searchComboBox;

	@FXML
	private ProgressBar progressbar;

	@FXML
	private TextField searchField;

	@FXML
	private TabPane fileView;
	private Tab tab;

	private ObservableList<Node> textFileData = FXCollections.observableArrayList();
	private ObservableList<Path> duplicatePrevention = FXCollections.observableArrayList();
	private ObservableList<FileItem> listMerge = FXCollections.observableArrayList();

	public void init(MainController mainController) {
		defaultThread(); // 파일 변경 내역 확인
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		setFileColumn(); // 테이블 컬럼
		setFileData(); // 테이블 데이터
		setSearchBox(); // 검색 필드
		setEvent(); // 이벤트
	}
	
	// temp
	public void threadStop(ExecutorService  task){
		task.shutdownNow();
	}

	// 파일 변경 내역 확인 eml -> html
	public void defaultThread() {
		// EML -> HTML 변환함
		TaskChangeToHtml task = new TaskChangeToHtml();
		ExecutorService service = Executors.newCachedThreadPool(new DaemonThreadFactory(task));
		service.submit(task);
	}

	// 검색 항목 설정
	public void setSearchBox() {
		searchComboBox.getItems().addAll(SearchType.TITLE, SearchType.CONTENT);
		searchComboBox.getSelectionModel().select(0);
	}

	// 이벤트 설정
	public void setEvent() {
		// 파일 선택 이벤트
		Selection.INSTANCE.setDocumentListener((observable, oldValue, newValue) -> setFileView(oldValue, newValue));
		// 폴더 선택 이벤트
		Selection.INSTANCE.setDirectoryListener((observable, oldValue, newValue) -> setFileData());
	}

	@FXML
	public void setSearch() {
		String query = searchField.getText();

		// 검색어와 검색 조건
		FileWalker.INSTANCE.setSearchText(query);
		FileWalker.INSTANCE.setSearchType(searchComboBox.getSelectionModel().getSelectedItem());

		if ("".equals(query) || FileWalker.INSTANCE.getSearchType().equals(SearchType.TITLE)) {
			setFileData();
		} else {
			TaskLuceneSearch task = new TaskLuceneSearch(searchField.getText());
			task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					listMerge = FXCollections.observableArrayList(task.getValue());
					fileListView.setItems(listMerge);
					fileListView.getSelectionModel().clearSelection();
				}
			});

			// 검색 스레드
			ExecutorService service = Executors.newCachedThreadPool(new DaemonThreadFactory(task));
			service.submit(task);
		}
	}

	// 인덱스 관련
	@FXML
	public void updateIndex() {
		// HTML만 인덱스 잡음 temp
		TaskLuceneIndex task = new TaskLuceneIndex(Mode.CREATE);
		ExecutorService service = Executors.newCachedThreadPool(new DaemonThreadFactory(task));
		service.submit(task);
	}

	// 파일 컬럼 생성
	public void setFileColumn() {
		fileNameColumn.setCellValueFactory(new PropertyValueFactory<FileItem, Hyperlink>("filePath"));
//		fileTitleColumn.setCellValueFactory(new PropertyValueFactory<FileItem, String>("fileName"));
//		fileCommentColumn.setCellValueFactory(new PropertyValueFactory<FileItem, Long>("fileSize"));

	}

	// 파일 데이터 생성
	public void setFileData() {
		FileWalker.INSTANCE.createFileTree();
		listMerge.clear();
		listMerge.addAll(FileWalker.INSTANCE.getFolders());
		listMerge.addAll(FileWalker.INSTANCE.getFiles());
		fileListView.setItems(listMerge);
		fileListView.getSelectionModel().clearSelection();

	}

	// 파일 내용 보여주기 temp
	public void setFileView(Path oldValue, Path newValue) {
		try {
			// 파일 없으면 처리
			if (newValue.toFile() == null)
				return;

			// 중복된 eml은 탭 건너뜀
			// if (duplicatePrevention.contains(newValue)) {
			// Path p = newValue;
			// Tab tab = fileView.getTabs().stream().filter(new Predicate<Tab>()
			// {
			// @Override
			// public boolean test(Tab t) {
			// return t.getId().equals(getTabId(p.getFileName().toString()));
			// }
			// }).findFirst().orElse(null);
			// fileView.getSelectionModel().select(tab);
			// return;
			// } else {
			// duplicatePrevention.add(newValue);
			// }

			// 마임 파싱
			// File f = MimeUtils.decodeLocalForSearch(newValue.toFile());

			// 탭 타이틀 표시 하기 위해 마임 파싱
			MetaData meta = new MetaData(newValue);
			newValue = Paths.get(newValue.toFile().toString().replaceAll(".eml", ".htm"));

			String url = newValue.toString();// f.getCanonicalPath();
			tab = new Tab();
			tab.setClosable(true);
			tab.setText(meta.getMetaTabTitle());
			tab.setId(getTabId(newValue.getFileName().toString()));

			// 컨텍스트 메뉴 탭마다 독립...
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
			fileView.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);
			fileView.getSelectionModel().select(tab);
			fileView.getTabs().add(tab);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 탭에 컨텍스트 메뉴 이벤트 처리 temp
	public ContextMenu setFileViewContext(Tab tab) {

		// duplicatePrevention.clear();

		final ContextMenu context = new ContextMenu();
		MenuItem menu = null;

		// 현재 닫기
		menu = new MenuItem("닫기");
		menu.setOnAction((ActionEvent e) -> fileView.getTabs().remove(tab));
		context.getItems().add(menu);

		// 다른 창 닫기
		menu = new MenuItem("다른창 닫기");
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				fileView.getSelectionModel().select(tab);
				int si = fileView.getSelectionModel().getSelectedIndex();
				fileView.getTabs().remove(si + 1, fileView.getTabs().size());
				fileView.getTabs().remove(0, si);
			}
		});
		context.getItems().add(menu);

		// 좌측 닫기
		menu = new MenuItem("좌측 닫기");
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				fileView.getSelectionModel().select(tab);
				int end = fileView.getSelectionModel().getSelectedIndex();
				fileView.getTabs().remove(0, end);
			}
		});
		context.getItems().add(menu);

		// 우측 닫기
		menu = new MenuItem("우측 닫기");
		menu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				fileView.getSelectionModel().select(tab);
				int start = fileView.getSelectionModel().getSelectedIndex();
				fileView.getTabs().remove(start + 1, fileView.getTabs().size());
			}
		});
		context.getItems().add(menu);

		menu = new SeparatorMenuItem();
		context.getItems().add(menu);

		// 전체 닫기
		menu = new MenuItem("전체 닫기");
		menu.setOnAction((ActionEvent e) -> fileView.getTabs().clear());
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
