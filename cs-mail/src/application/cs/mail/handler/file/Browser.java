package application.cs.mail.handler.file;

import java.net.MalformedURLException;
import java.nio.file.Paths;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Browser extends Region {
	private final WebView webView = new WebView();
	private final WebEngine webEngine = webView.getEngine();

	public Browser() {
//		getStyleClass().add("browser");
		webEngine.load("http://hanwha.eagleoffice.co.kr");
		getChildren().add(webView);

	}
	
	public Browser(String url) throws MalformedURLException {
		webView.setContextMenuEnabled(false);
		webView.getEngine().setJavaScriptEnabled(false);
		webEngine.load(Paths.get(url).toUri().toString());
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
