package kg.apc.emulators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static org.junit.Assert.*;

/**
 *
 * @author Stephane Hoblingre
 */
public class FilesTestTools {

    /**
     * Compare 2 text files and verify their content is equal
     * Do not take care of newLine
     * Do not take care of empty lines at end of files
     * @param file1
     * @param file2
     * @return true if text content is identical
     */
    public static boolean compareFiles(File file1, File file2)
    {
        if (file1 == null || file2 == null)
        {
            return false;
        }

        FileReader freader1;
        FileReader freader2;
        BufferedReader reader1;
        BufferedReader reader2;

        try
        {
            freader1 = new FileReader(file1);
            freader2 = new FileReader(file2);
            reader1 = new BufferedReader(freader1);
            reader2 = new BufferedReader(freader2);

            String line1 = reader1.readLine();
            String line2 = reader2.readLine();

            while (line1 != null && line2 != null)
            {
                if (!line1.equals(line2))
                {
                    assertEquals(line1, line2);
                    return false;
                }
                line1 = reader1.readLine();
                line2 = reader2.readLine();
            }
            //verify if one of the file has ONLY empty line at end more than
            //the other one.

            BufferedReader reader = null;
            String line = null;

            if (line1 != null)
            {
                line = line1;
                reader = reader1;
            } else if (line2 != null)
            {
                line = line2;
                reader = reader2;
            }

            while (line != null)
            {
                if (!line.trim().equals(""))
                {
                    System.err.println("Empty line");
                    return false;
                }
                line = reader.readLine();
            }

            reader1.close();
            reader2.close();
            freader1.close();
            freader2.close();

            return true;
        } catch (IOException ex)
        {
             System.err.println("IOException:"+ex.getMessage());
            return false;
        }
    }
}
