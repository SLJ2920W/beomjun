package logTest.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logback01 {

	static Logger logger = LoggerFactory.getLogger(Logback01.class);

	public static void main(String[] args) throws InterruptedException {
//		for (int i = 0; i < 1000; i++) {
//			Thread.sleep(100);
	        logger.trace("trace");
	        logger.debug("debug");
	        logger.info("info");
	        logger.warn("warn");
	        logger.error("error");
//		}

	}
}
