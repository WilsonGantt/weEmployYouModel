package we.employ.you.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtil {

	public LogUtil() {
		super();
	}

	public static void logStackTrace(Exception exception) {
        StringWriter errorOut = new StringWriter();
        exception.printStackTrace(new PrintWriter(errorOut));
        
        System.out.println(errorOut.toString());
    }
}
