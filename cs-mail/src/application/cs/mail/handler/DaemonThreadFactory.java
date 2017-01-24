package application.cs.mail.handler;

import java.util.concurrent.ThreadFactory;

import application.cs.mail.common.Selection;
import javafx.concurrent.Task;

/**
 * http://stackoverflow.com/questions/10030808/making-callable-threads-as-daemon
 * http://blog.daum.net/dkfuds/12
 */
public class DaemonThreadFactory implements ThreadFactory {
	
//	private static final Logger log = LoggerFactory.getLogger(DaemonThreadFactory.class);
	
	public DaemonThreadFactory(){
		
	}

	public DaemonThreadFactory(Task<?> task) {
//		log.debug(task.getState().toString());
		// 스레드에 진행 상태 가져옴
		Selection.getInstance().setProgress(task.progressProperty().get());
		// 스레드에 메시지 가져옴
		task.messageProperty().addListener((o, oldValue, newValue) -> Selection.getInstance().setMessage(task.messageProperty().get()));
		// 스레드 이상 없을시 종료 표시만..
		task.setOnSucceeded((event) -> Selection.getInstance().setProgress(0));
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r);
		thread.setName("hahaha");
		thread.setDaemon(true);
		return thread;
	}

}
