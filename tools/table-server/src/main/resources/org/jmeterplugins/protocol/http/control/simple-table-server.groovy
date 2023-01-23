print("Startup jsr223 groovy script running");

import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.util.JMeterException;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.reflect.ClassTools;
import org.apache.log.Logger;

Logger log = LoggingManager.getLoggerForClass();

int simpleTablePort = JMeterUtils.getPropDefault("jmeterPlugin.sts.port", 0);
boolean loadAndRunOnStartup = JMeterUtils.getPropDefault("jmeterPlugin.sts.loadAndRunOnStartup", false);

if (simpleTablePort > 0 && loadAndRunOnStartup) {
    log.info("Starting Simple Table server (" + simpleTablePort + ")");
    try {
        Object instance = ClassTools
                .construct("org.jmeterplugins.protocol.http.control.HttpSimpleTableControl"
                );
        ClassTools.invoke(instance, "startHttpSimpleTable");
        msg = "Simple Table Server is running on port : " + simpleTablePort;
        log.info(msg);
        print(msg);
        
        // Load csv files at STS startup
        String sInitFileAtStartup = JMeterUtils.getPropDefault("jmeterPlugin.sts.initFileAtStartup", "");
        boolean bInitFileAtStartupRegex = JMeterUtils.getPropDefault("jmeterPlugin.sts.initFileAtStartupRegex", false);
        
        if (sInitFileAtStartup.length() > 0) {
            log.info("INITFILE at STS startup")
            print("INITFILE at STS startup")
            log.info("jmeterPlugin.sts.initFileAtStartup=" + sInitFileAtStartup);
            log.info("jmeterPlugin.sts.initFileAtStartupRegex=" + bInitFileAtStartupRegex);
        }
        
        if (sInitFileAtStartup.length() > 0 && bInitFileAtStartupRegex == false) {
            // E.g : jmeterPlugin.sts.initFileAtStartup=file1.csv,file2.csv,file3.csv
            
            def tabFileName = org.apache.commons.lang3.StringUtils.splitPreserveAllTokens(sInitFileAtStartup,',');
            for (int i = 0; i < tabFileName.length; i++) {
                String fileName = tabFileName[i].trim();
                log.info("INITFILE : i = " + i + ", fileName = " + fileName);
                String response = new URL("http://localhost:" + simpleTablePort +"/sts/INITFILE?FILENAME=" + fileName).text;
                log.info("INITFILE?FILENAME=" + fileName + ", response=" + response);
                print("INITFILE?FILENAME=" + fileName + ", response=" + response);
            }
        }
        
        if (sInitFileAtStartup.length() > 0 && bInitFileAtStartupRegex == true) {
            // E.g : jmeterPlugin.sts.initFileAtStartup=file\d+\.csv regex match : file1.csv, file2.csv, file3.csv, file44.csv
            
            String dataDir = JMeterUtils.getPropDefault("jmeterPlugin.sts.datasetDirectory", JMeterUtils.getJMeterBinDir());
            File fDir = new File(dataDir);
            File [] files = fDir.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.matches(sInitFileAtStartup);
                }
            });
        
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                log.info("INITFILE : i = " + i + ", fileName = " + fileName);
                String response = new URL("http://localhost:" + simpleTablePort +"/sts/INITFILE?FILENAME=" + fileName).text;
                log.info("INITFILE?FILENAME=" + fileName + ", response=" + response);
                print("INITFILE?FILENAME=" + fileName + ", response=" + response);
            }
        }
     } catch (JMeterException e) {
        log.warn("Could not start Simple Table server", e);
    }
}
else {
    msg = "jmeterPlugin.sts.port == 0 OR jmeterPlugin.sts.loadAndRunOnStartup != true => Simple Table Server is NOT running";
    log.info(msg);
    print(msg);
}


print("Startup jsr223 groovy script completed");
