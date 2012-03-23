/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.io;

import java.io.File;
import java.io.FileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 *
 * @author direvius
 */
public class FileSystem {
    public static boolean checkFileExistByPattern(String path, String pattern){
        if(path == null) path = ".";
        File dir = new File(path);
        FileFilter ff = new WildcardFileFilter(pattern);
        File [] found = dir.listFiles(ff);
        return found!=null ? found.length > 0 : false;
    }
}
