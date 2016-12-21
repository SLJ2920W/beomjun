package application.cs.mail.handler.file;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * http://docs.oracle.com/javafx/2/api/index.html
 * http://www.java2s.com/Tutorials/Java/JavaFX/0660__JavaFX_Tree_View.htm
 */
public class PathTreeItem extends TreeItem<PathItem> {
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
            isLeaf = !Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
        }
        return isLeaf;
    }

    private ObservableList<TreeItem<PathItem>> buildChildren(TreeItem<PathItem> treeItem) {
        Path path = treeItem.getValue().getPath();
        if (path != null && Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            ObservableList<TreeItem<PathItem>> children = FXCollections.observableArrayList();
            try (DirectoryStream<Path> dirs = Files.newDirectoryStream(path)) {
                for (Path dir : dirs) {
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