package kg.apc.jmeter.perfmon;

import java.io.IOException;

public class PerfMonException extends IOException {
   public PerfMonException(String message, Throwable cause) {
      super(message, cause);
   }

   public PerfMonException(String message) {
      super(message);
   }
}
