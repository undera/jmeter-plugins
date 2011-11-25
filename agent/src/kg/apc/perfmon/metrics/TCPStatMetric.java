package kg.apc.perfmon.metrics;

import java.util.Arrays;
import org.hyperic.sigar.NetStat;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/**
 *
 * @author undera
 */
class TCPStatMetric extends AbstractPerfMonMetric {

    public static final byte BOUND = 0;
    public static final byte CLOSE = 1;
    public static final byte CLOSE_WAIT = 2;
    public static final byte CLOSING = 3;
    public static final byte ESTAB = 4;
    public static final byte FIN_WAIT1 = 5;
    public static final byte FIN_WAIT2 = 6;
    public static final byte IDLE = 7;
    public static final byte INBOUND_TOTAL = 8;
    public static final byte LAST_ACK = 9;
    public static final byte LISTEN = 10;
    public static final byte OUTBOUND_TOTAL = 11;
    public static final byte SYN_RECV = 12;
    public static final byte TIME_WAIT = 13;
    public static final String[] types = {"bound", "close", "close_wait",
        "closing", "estab", "fin_wait1", "fin_wait2", "idle",
        "inbound", "last_ack", "listen", "outbound", "syn_recv", "time_wait"};
    private int type = -1;

    public TCPStatMetric(SigarProxy aSigar, MetricParams params) {
        super(aSigar);

        if (params.type.isEmpty()) {
            type = ESTAB;
        } else {
            type = Arrays.asList(types).indexOf(params.type);
            if (type < 0) {
                throw new IllegalArgumentException("Unknown TCP type: " + params.type);
            }
        }
    }

    public void getValue(StringBuilder res) throws SigarException {
        NetStat stat = sigarProxy.getNetStat();
        double val;
        switch (type) {
            case BOUND:
                val = stat.getTcpBound();
                break;
            case CLOSE:
                val = stat.getTcpClose();
                break;
            case CLOSE_WAIT:
                val = stat.getTcpCloseWait();
                break;
            case CLOSING:
                val = stat.getTcpClosing();
                break;
            case ESTAB:
                val = stat.getTcpEstablished();
                break;
            case FIN_WAIT1:
                val = stat.getTcpFinWait1();
                break;
            case FIN_WAIT2:
                val = stat.getTcpFinWait2();
                break;
            case IDLE:
                val = stat.getTcpIdle();
                break;
            case INBOUND_TOTAL:
                val = stat.getTcpInboundTotal();
                break;
            case LAST_ACK:
                val = stat.getTcpLastAck();
                break;
            case LISTEN:
                val = stat.getTcpListen();
                break;
            case OUTBOUND_TOTAL:
                val = stat.getTcpOutboundTotal();
                break;
            case SYN_RECV:
                val = stat.getTcpSynRecv();
                break;
            case TIME_WAIT:
                val = stat.getTcpTimeWait();
                break;
            default:
                throw new SigarException("Unknown tcp type " + type);
        }
        res.append(Double.toString(val));
    }
}
