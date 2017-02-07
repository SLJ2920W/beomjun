package application.cs.mail.handler.file;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.cs.mail.common.Selection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * http://docs.oracle.com/javafx/2/api/index.html
 * http://www.java2s.com/Tutorials/Java/JavaFX/0660__JavaFX_Tree_View.htm
 */
public class PathTreeItem extends TreeItem<PathItem> {
	private static final Logger log = LoggerFactory.getLogger(PathTreeItem.class);

	private boolean isLeaf = false;
	private boolean isFirstTimeChildren = true;
	private boolean isFirstTimeLeft = true;

	private PathTreeItem(PathItem pathItem) {
		super(pathItem);
	}

	public static TreeItem<PathItem> createNode(PathItem pathItem) {
		return new PathTreeItem(pathItem);
	}

	@Override
	public ObservableList<TreeItem<PathItem>> getChildren() {
		// 트리 리스트 왼쪽 기능키 눌렀을때
		if (isFirstTimeChildren) {
			isFirstTimeChildren = false;
			super.getChildren().setAll(buildChildren(this));
		}
		return super.getChildren();
	}

	@Override
	public boolean isLeaf() {
		if (isFirstTimeLeft) {
			isFirstTimeLeft = false;
			Path path = getValue().getPath();

			// false : 펼치기 없음 , true : 펼치기 있음
			isLeaf = !isSubFolder(path.toString());
			// isLeaf = !Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
		}
		return isLeaf;
	}

	// 하위 펼치기 리스트 후 하위에 폴더가 없으면 펼치기 없애기
	private boolean isSubFolder(String parentPath) {
		Path dir = null;
		try {

			// 리스트는 폴더와 제외할 규칙명을 빼고
			DirectoryStream.Filter<Path> filter2 = (Path file) -> {
				if (Files.isDirectory(file)) {
					long c = Selection.getInstance().getMailViewIgnore().stream().filter((e) -> file.getFileName().startsWith(e)).count();
					if (c == 0)
						return true;
				}
				return false;
			};

			dir = FileSystems.getDefault().getPath(parentPath);
			DirectoryStream<Path> s = Files.newDirectoryStream(dir, filter2);
			return s.iterator().hasNext();

		} catch (AccessDeniedException e) {
			log.error("_AccessDenied_" + dir.toString() + "\n" + e.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private ObservableList<TreeItem<PathItem>> buildChildren(TreeItem<PathItem> treeItem) {

		Path path = treeItem.getValue().getPath();

		if (path != null && Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
			ObservableList<TreeItem<PathItem>> children = FXCollections.observableArrayList();
			try (DirectoryStream<Path> dirs = Files.newDirectoryStream(path)) {
				for (Path dir : dirs) {
					// 리스트는 폴더와 제외할 규칙명을 빼고 표시
					if (Files.isDirectory(dir)) {
						long c = Selection.getInstance().getMailViewIgnore().stream().filter((e) -> dir.getFileName().startsWith(e)).count();
						if (c != 0)
							continue;
					} else {
						continue;
					}

					PathItem pathItem = new PathItem(dir);
					children.add(createNode(pathItem));
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return children;
		}
		return FXCollections.emptyObservableList();
	}
}