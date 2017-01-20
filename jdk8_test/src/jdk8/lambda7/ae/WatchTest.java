package jdk8.lambda7.ae;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * jdk8 sample code -> WatchDir.java 
 */
public class WatchTest {
	// file moved original
	public static final String ORIGINAL_PATH = "D:\\다운로드";
	// watching folder pattern
//	public static final String PATTERN_MATCH = "([0-9]+)-([0-9]+)-([0-9]+)-(220)-(17420|6130)";


	public static void main(String[] args) {
		WatchTest w = new WatchTest();
		try {
			// watch agent
			w.start();

			// file scheduler

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() throws Exception {
		ExecutorService service = Executors.newCachedThreadPool();
		final FileSystem fs = FileSystems.getDefault();
		final WatchService ws = fs.newWatchService();
		final Map<WatchKey, Path> keys = new ConcurrentHashMap<>();

		reg(fs.getPath(WatchTest.ORIGINAL_PATH), keys, ws);

		service.submit(new Runnable() {
			@Override
			public void run() {

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
//							if (folderName.matches(WatchTest.PATTERN_MATCH)) {
								Calendar c = Calendar.getInstance();
								c.setTime(new Date());
								int nowHour = c.get(Calendar.HOUR_OF_DAY);
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
//								if (nowHour >= WatchTest.PROHIBITED_TIME_START && nowHour <= WatchTest.PROHIBITED_TIME_END) {
									// do not move
//								} else {
//									try {
										System.out.println("done");
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
//								}
//							} else {
//								continue;
//							}
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
		});
	}

	static void walk(Path root, final Map<WatchKey, Path> keys, final WatchService ws) throws IOException {
		Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				reg(dir, keys, ws);
				return super.preVisitDirectory(dir, attrs);
			}
		});
	}

	static void reg(Path dir, Map<WatchKey, Path> keys, WatchService ws) throws IOException {
		WatchKey key = dir.register(ws, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		keys.put(key, dir);
	}

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}
}
