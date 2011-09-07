package kg.apc.jmeter.perfmon;

/**
 *
 * @author Stephane Hoblingre
 */
public class PerfMonException extends Exception {
   public PerfMonException(String message, Throwable cause) {
      super(message, cause);
   }

   public PerfMonException(String message) {
      super(message);
   }
}
