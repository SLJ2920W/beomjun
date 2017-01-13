package application.cs.mail.handler.file.watch;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import application.cs.mail.handler.DaemonThreadFactory;
import application.cs.mail.handler.search.TaskChangeToHtml;

/**
 * jdk8 sample code 참조 -> WatchDir.java
 * https://docs.oracle.com/javase/tutorial/essential/io/walk.html#ex
 */
public class TaskWatchDir implements Runnable {
	private String setPath;

	public TaskWatchDir(String setPath) {
		this.setPath = setPath;
	}

	@Override
	public void run() {
		final FileSystem fs = FileSystems.getDefault();
		WatchService ws = null;
		final Map<WatchKey, Path> keys = new ConcurrentHashMap<>();
		try {
			ws = fs.newWatchService();
			reg(fs.getPath(setPath), keys, ws);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println("WATCH START");
		while (Thread.interrupted() == false) {
			WatchKey key;
			try {
				key = ws.poll(10, TimeUnit.MILLISECONDS);
			} catch (InterruptedException | ClosedWatchServiceException e) {
				break;
			}
			if (key != null) {
				Path path = keys.get(key);
				for (WatchEvent<?> i : key.pollEvents()) {
					WatchEvent<Path> event = cast(i);
					WatchEvent.Kind<Path> kind = event.kind();
					Path name = event.context();
					Path child = path.resolve(name);

					String[] pathName = child.getFileName().toString().split("\\\\");
					String folderName = pathName[pathName.length - 1];
					System.out.println(folderName);
					System.out.printf("%s: %s %s%n", kind.name(), path, child);
					if (kind == ENTRY_CREATE) {
						if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
							try {
								walk(child, keys, ws);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					System.out.println("done");
				}
				if (key.reset() == false) {
					System.out.printf("%s is invalid %n", key);
					keys.remove(key);
					if (keys.isEmpty()) {
						break;
					}
				}
			}
		}
		System.out.println("END");
	}

	void walk(Path root, final Map<WatchKey, Path> keys, final WatchService ws) throws IOException {
		Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				reg(dir, keys, ws);
				return super.preVisitDirectory(dir, attrs);
			}
		});
	}

	void reg(Path dir, Map<WatchKey, Path> keys, WatchService ws) throws IOException {
		WatchKey key = dir.register(ws, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		keys.put(key, dir);
	}

	@SuppressWarnings("unchecked")
	<T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

}
