package kg.apc.io;

import java.io.*;
import java.nio.channels.FileChannel;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class FileSystem {

    public static boolean checkFileExistByPattern(String path, String pattern) {
        if (path == null) {
            path = ".";
        }
        File dir = new File(path);
        FileFilter ff = new WildcardFileFilter(pattern);
        File[] found = dir.listFiles(ff);
        return found != null && found.length > 0;
    }

    public static void copyFile(String source, String destination) throws IOException {
        FileChannel out = null;
        try {
            FileChannel in = new FileInputStream(source).getChannel();
            File outFile = new File(destination);
            out = new FileOutputStream(outFile).getChannel();
            in.transferTo(0, in.size(), out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
