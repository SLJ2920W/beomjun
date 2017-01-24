package application.cs.mail.handler.file;

import java.io.IOException;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;

import application.cs.mail.common.Selection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Browser extends Region {
	private static final Logger log = LoggerFactory.getLogger(Browser.class);
	private final WebView webView = new WebView();
	private final WebEngine webEngine = webView.getEngine();
	private final String EVENT_TYPE_CLICK = "click";
	private final String EVENT_TAG = "a";

	public Browser() {
		webEngine.load("http://hanwha.eagleoffice.co.kr");
		getChildren().add(webView);

	}

	public Browser(String url) {

		webView.setContextMenuEnabled(false);
		webView.getEngine().setJavaScriptEnabled(false);
		webEngine.load(Paths.get(url).toUri().toString());

		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				if (newValue == Worker.State.SUCCEEDED) {
					Document doc = webView.getEngine().getDocument();
					// EVENT_TAG 값 가져옴
					NodeList nodeList = doc.getElementsByTagName(EVENT_TAG);
					for (int i = 0; i < nodeList.getLength(); i++) {
						EventTarget eventTarget = (EventTarget) nodeList.item(i);

						// EVENT_TAG 클릭하면 이벤트 제어
						eventTarget.addEventListener(EVENT_TYPE_CLICK, new EventListener() {
							@Override
							public void handleEvent(Event evt) {
								EventTarget target = evt.getCurrentTarget();
								HTMLAnchorElement anchorElement = (HTMLAnchorElement) target;
								String href = anchorElement.getHref();
								try {
									// 링크는 외부 브라우저로
									Runtime.getRuntime().exec(new String[] { "cmd", "/c", "start " + Selection.getInstance().getDefaultBrowser() + " " + href + "" });
								} catch (IOException e) {
									log.error(e.toString());
								}
								evt.preventDefault();
							}
						}, false);
					}
				}
			}
		});

		getChildren().add(webView);
	}

	@SuppressWarnings("unused")
	private Node createSpacer() {
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

	@Override
	protected void layoutChildren() {
		double w = getWidth();
		double h = getHeight();
		layoutInArea(webView, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
	}

	@Override
	protected double computePrefWidth(double height) {
		return 1920;
	}

	@Override
	protected double computePrefHeight(double width) {
		return 1080;
	}
}
