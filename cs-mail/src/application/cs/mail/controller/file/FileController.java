package application.cs.mail.controller.file;

import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.cs.mail.common.Selection;
import application.cs.mail.controller.MainController;
import application.cs.mail.handler.DaemonThreadFactory;
import application.cs.mail.handler.file.FileBean;
import application.cs.mail.handler.file.FileTree;
import application.cs.mail.handler.menu.TabWalker;
import application.cs.mail.handler.search.SearchType;
import application.cs.mail.handler.search.TaskChangeToHtml;
import application.cs.mail.handler.search.TaskLuceneIndex;
import application.cs.mail.handler.search.TaskLuceneIndex.Mode;
import application.cs.mail.handler.search.TaskLuceneSearch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class FileController implements Initializable {

	Selection selection = Selection.getInstance();

	private static final Logger log = LoggerFactory.getLogger(FileController.class);

	@FXML
	private TableView<FileBean> fileListView;
	@FXML
	private TableColumn<FileBean, HBox> fileNameColumn;
	@FXML
	private TableColumn<FileBean, Hyperlink> filePathColumn;
	@FXML
	private TableColumn<FileBean, String> fileTitleColumn;
	@FXML
	private TableColumn<FileBean, Long> fileCommentColumn;
	@FXML
	private ComboBox<SearchType> searchComboBox;
	@FXML
	private ProgressBar progressbar;
	@FXML
	private TextField searchField;
	@FXML
	private TabPane tabPane;

	private ObservableList<FileBean> listMerge = FXCollections.observableArrayList();

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
	public void threadStop(ExecutorService task) {
		task.shutdownNow();
	}

	// 파일 변경 내역 확인 EML -> HTML
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
		selection.setDocumentListener((observable, oldValue, newValue) -> setFileView(oldValue, newValue));
		// 폴더 선택 이벤트
		selection.setDirectoryListener((observable, oldValue, newValue) -> setFileData());
	}

	@FXML
	public void setSearch() {
		String query = searchField.getText();
		FileTree ft = FileTree.getInstance();
		// 검색어와 검색 조건
		ft.setSearchText(query);
		ft.setSearchType(searchComboBox.getSelectionModel().getSelectedItem());

		// 파일 검색은 IO, 내용 검색은 루씬
		if ("".equals(query) || ft.getSearchType().equals(SearchType.TITLE)) {
			setFileData();
		} else {
			TaskLuceneSearch task = new TaskLuceneSearch(searchField.getText());
			// 검색 스레드
			ExecutorService service = Executors.newCachedThreadPool(new DaemonThreadFactory(task));
			service.submit(task);
			task.setOnSucceeded((e) -> {
				listMerge = FXCollections.observableArrayList(task.getValue());
				fileListView.setItems(listMerge);
				fileListView.getSelectionModel().clearSelection();
				selection.setProgress(0.0);
			});
		}
	}

	// 인덱스 관련
	@FXML
	public void updateIndex() {
		// HTML만 인덱스 잡음
		TaskLuceneIndex task = new TaskLuceneIndex(Mode.UPDATE);
		ExecutorService service = Executors.newCachedThreadPool(new DaemonThreadFactory(task));
		service.submit(task);
	}

	// 파일 컬럼 생성
	public void setFileColumn() {
		fileNameColumn.setCellValueFactory(new PropertyValueFactory<FileBean, HBox>("filePathIcon"));
	}

	// 파일 데이터 생성
	public void setFileData() {
		FileTree ft = FileTree.getInstance();
		ft.createFileTree();
		listMerge.clear();
		listMerge.addAll(ft.getFolders());
		listMerge.addAll(ft.getFiles());
		fileListView.setItems(listMerge);
		fileListView.getSelectionModel().clearSelection();

	}

	// 파일 내용 보여주기
	public void setFileView(Path oldValue, Path newValue) {
		try {
			TabWalker tab = new TabWalker(newValue, tabPane);
			if (tab.filter())
				return;
			tab.createNode();
			tabPane.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);
			tabPane.getSelectionModel().select(tab);
			tabPane.getTabs().add(tab);

		} catch (Exception e) {
			log.error("파일뷰 에러 = " + getClass() + "\n" + e.toString());
			e.printStackTrace();
		}
	}
}
