package am2;

import am2.preloader.AM2PreloaderContainer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper{
	private static Logger logger = LogManager.getLogger("ArsMagica2");

	public static void log(Level level, String format, Object... data){
		logger.log(level, String.format(format, data));
	}

	public static void log(Level level, Throwable ex, String format, Object... data){
		logger.log(level, String.format(format, data), ex);
	}

	public static void error(String format, Object... data){
		log(Level.ERROR, format, data);
	}

	public static void warn(String format, Object... data){
		log(Level.WARN, format, data);
	}

	public static void info(String format, Object... data){
		log(Level.INFO, format, data);
	}

	public static void debug(String format, Object... data){
	      // Log4j 1.x allowed you to set the logging level easily
	      // Log4j 2.x removed this in favour of some kind of stupidly convoluted method
	      // you can either use yet another config file (which is not portable)
	      // or call some kind of horrible arcane function chain (which is not documented)
	      // this is a rather hacky way of turning on debug output if we're in a dev environment, but with the redeeming feature that it actually works
	      // (the default logging level seems to be INFO)
		if(AM2PreloaderContainer.isDevEnvironment){
			info("AM2 Debug: " + format, data);
		}
		else{
			log(Level.DEBUG, format, data);
		}
	}

	public static void trace(String format, Object... data){
		log(Level.TRACE, format, data);
	}
}
