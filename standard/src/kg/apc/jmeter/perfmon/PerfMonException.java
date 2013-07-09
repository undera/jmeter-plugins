package kg.apc.jmeter.perfmon;

import java.io.IOException;

/**
 *
 * @author Stephane Hoblingre
 */
public class PerfMonException extends IOException {
   public PerfMonException(String message, Throwable cause) {
      super(message, cause);
   }

   public PerfMonException(String message) {
      super(message);
   }
}
