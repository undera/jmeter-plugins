/**
 * 
 * core ID
 * process id
 * image name
 * 
 * 
 */
package kg.apc.perfmon.metrics;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemMap;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
class DiskIOMetric extends AbstractPerfMonMetric {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final byte AVAILABLE = 0;
    public static final byte DISK_QUEUE = 1;
    public static final byte READ_BYTES = 2;
    public static final byte READS = 3;
    public static final byte SERVICE_TIME = 4;
    public static final byte WRITE_BYTES = 5;
    public static final byte WRITES = 6;
    public static final byte FILES = 7;
    public static final byte FREE = 8;
    public static final byte FREE_FILES = 9;
    public static final byte TOTAL = 10;
    public static final byte USE_PERCENT = 11;
    public static final byte USED = 12;
    public static final String[] types = {"available", "queue", "readbytes",
        "reads", "service", "writebytes", "writes", "files", "free", "freefiles",
        "total", "useperc", "used"};
    private int type = -1;
    private final String[] filesystems;
    private double prev = -1;

    public DiskIOMetric(SigarProxy aSigar, String metricParams) {
        super(aSigar);
        MetricParams params = MetricParams.createFromString(metricParams, sigarProxy);
        type = Arrays.asList(types).indexOf(params.type);
        if (type < 0) {
            type = USE_PERCENT;
        }
        log.debug("Disk metric type: " + type);

        LinkedList list = new LinkedList();
        if (!params.fs.isEmpty()) {
            list.add(params.fs);
        } else {
            getAllDiskFilesystems(aSigar, list);
        }

        filesystems = (String[]) list.toArray(new String[0]);
    }

    private void getAllDiskFilesystems(SigarProxy aSigar, LinkedList list) {
        try {
            FileSystemMap map = aSigar.getFileSystemMap();
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                FileSystem fs = (FileSystem) map.get(key);
                if (fs.getType() == FileSystem.TYPE_LOCAL_DISK) {
                    list.add(key);
                }
            }
        } catch (SigarException e) {
            log.warn("Can't get filesystems map", e);
        }
    }

    public static void logAllAvailableFilesystems(SigarProxy aSigar) {
        log.info("*** Logging available filesystems ***");
        try {
            FileSystemMap map = aSigar.getFileSystemMap();
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                FileSystem fs = (FileSystem) map.get(key);
                log.info("Filesystem fs="+fs.toString()+" type=" + fs.getSysTypeName());
            }
        } catch (SigarException e) {
            log.warn("Can't get filesystems map", e);
        }
    }

    public void getValue(StringBuilder res) throws SigarException {
        double val = 0;
        long used = 0;
        long total = 0;
        double cur;
        for (int n = 0; n < filesystems.length; n++) {
            FileSystemUsage usage = sigarProxy.getFileSystemUsage(filesystems[n]);
            switch (type) {
                case AVAILABLE:
                    val += usage.getAvail();
                    break;
                case DISK_QUEUE:
                    val += usage.getDiskQueue();
                    break;
                case READ_BYTES:
                    val += usage.getDiskReadBytes();
                    break;
                case READS:
                    val += usage.getDiskReads();
                    break;
                case SERVICE_TIME:
                    val += usage.getDiskServiceTime();
                    break;
                case WRITE_BYTES:
                    val += usage.getDiskWriteBytes();
                    break;
                case WRITES:
                    val += usage.getDiskWrites();
                    break;
                case FILES:
                    val += usage.getFiles();
                    break;
                case FREE:
                    val += usage.getFree();
                    break;
                case FREE_FILES:
                    val += usage.getFreeFiles();
                    break;
                case TOTAL:
                    val += usage.getTotal();
                    break;
                case USE_PERCENT:
                    // special case for multiple percentages
                    if (filesystems.length > 1) {
                        used += usage.getUsed();
                        total += usage.getTotal();
                    } else {
                        val += 100 * usage.getUsePercent();
                    }
                    break;
                case USED:
                    val = usage.getUsed();
                    break;
                default:
                    throw new SigarException("Unknown swap type " + type);
            }
        }

        // some post-processing
        switch (type) {
            case READ_BYTES:
                cur = val;
                val = prev > 0 ? cur - prev : 0;
                prev = cur;
                break;
            case READS:
                cur = val;
                val = prev > 0 ? cur - prev : 0;
                prev = cur;
                break;
            case SERVICE_TIME:
                cur = val;
                break;
            case WRITE_BYTES:
                cur = val;
                val = prev > 0 ? cur - prev : 0;
                prev = cur;
                break;
            case WRITES:
                cur = val;
                val = prev > 0 ? cur - prev : 0;
                prev = cur;
                break;
            case USE_PERCENT:
                if (filesystems.length > 1) {
                    val = 100 * (double) used / total;
                }
                break;
        }

        res.append(Double.toString(val));
    }
}
