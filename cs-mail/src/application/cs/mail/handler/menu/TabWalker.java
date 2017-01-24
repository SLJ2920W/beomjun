package application.cs.mail.handler.menu;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.cs.mail.common.Selection;
import application.cs.mail.handler.file.Browser;
import application.cs.mail.handler.mime.MetaData;
import javafx.collections.ObservableSet;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TabWalker extends Tab {

	private static final Logger log = LoggerFactory.getLogger(TabWalker.class);
	private List<Node> textFileData = new ArrayList<>();
	private Path path;
	private TabPane tabPane;
	private Selection selection = Selection.getInstance();

	public TabWalker(Path path, TabPane tabPane) throws MalformedURLException {
		this.path = path;
		this.tabPane = tabPane;
	}

	/**
	 * 중복된 탭이 있는지 확인한다
	 * @return true면 중복값 존재
	 */
	public boolean filter() {
		// 파일 없으면 처리
		if (path.toFile() == null)
			return true;

		// 중복된 eml은 별도 로직 타지 않고 건너뜀
		if (selection.getDuplicatePrevention().contains(path)) {
			Path p = path;
			Tab tab = tabPane.getTabs().stream().filter(new Predicate<Tab>() {
				@Override
				public boolean test(Tab t) {
					return t.getId().equals(getTabId(p.getFileName().toString()));
				}
			}).findFirst().orElse(null);
			tabPane.getSelectionModel().select(tab);
			return true;
		} else {
			selection.setDuplicatePrevention(path);
		}
		return false;
	}

	/**
	 * 웹뷰에 실제 데이터 맵핑을 함
	 * @throws MalformedURLException
	 */
	public void createNode() throws MalformedURLException {
		// 탭 타이틀 표시 하기 위해 마임 파싱
		MetaData meta = new MetaData(path);
		path = Paths.get(path.toFile().toString().replaceAll(".eml", ".htm"));

		String url = path.toString();
		setClosable(true);
		setText(meta.getMetaTabTitle());
		setId(getTabId(path.getFileName().toString())); // 각 탭 마다 고유 아이디 추가

		setContextMenu(new TabContext(tabPane, this));
		
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

		setContent(root);
		
		// 탭에서 지원하는 기본 닫기 기능 눌렀을때
		setOnCloseRequest(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {				
				selection.getDuplicatePrevention().remove(tabPane.getSelectionModel().getSelectedIndex());
			}
		});
	}
	

	/**
	 * 발신자 수신자등 정보를 텍스트 필드로 만듬
	 * @param labelName
	 * @param value
	 * @param list
	 */
	private void createLabel(String labelName, String value, List<Node> list) {
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

	/**
	 * 탭 마다 고유 아이디를 생성 2016-07-25 030819_시스템운영자(System Administrator)_업무....
	 * 만약 양식에 맞지 않다면 base64인코딩 해서 처리
	 * @param fileName
	 * @return 2016-07-25_030819
	 */
	private String getTabId(String fileName) {
		Matcher m = Pattern.compile("\\d{4}[-]?\\d{2}[-]?\\d{2}\\s\\d{6}").matcher(fileName);
		if (m.find()) {
			return m.group().replaceAll("\\s", "_");
		} else {
			String asB64 = null;
			try {
				asB64 = Base64.getEncoder().encodeToString(fileName.getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				log.error(e.toString());
			}
			return asB64.substring(0, asB64.length() > 10 ? 10 : asB64.length());
		}
	}

}
