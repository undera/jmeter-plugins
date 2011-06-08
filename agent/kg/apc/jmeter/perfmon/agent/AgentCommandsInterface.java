// TODO: move it and its tests to separate project, don't forget to update build instruction
// TODO: update building from source doc
package kg.apc.jmeter.perfmon.agent;

/**
 *
 * @author Stephane Hoblingre
 */
public interface AgentCommandsInterface {
   public static final String BADCMD = "badCmd";
   public static final String BYE = "bye";
   public static final String CPU = "cpu";
   public static final String DISKIO = "dio";
   public static final String MEMORY = "mem";
   public static final String NAME = "name";
   public static final String NETWORK = "nio";
   public static final String SWAP = "swp";

   //APC: why don't use custom exceptions instead of error constants?
   public final static long SIGAR_ERROR = -1L;
   public final static long AGENT_ERROR = -2L;
   public final static long[] SIGAR_ERROR_ARRAY =
   {
      -1L, -1L
   };
   public final static long[] AGENT_ERROR_ARRAY =
   {
      -2L, -2L
   };
}
