package kg.apc.io;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author direvius
 */
public class FileSystemTest {
    
    public FileSystemTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of checkFileExistByPattern method, of class FileSystem.
     */
    @Test
    public void testCheckFileExistByPattern() {
        System.out.println("checkFileExistByPattern");
        String pattern = "he*.?orld";
        String dir = ".";
        File file = new File("hello.world");
        try {
            file.createNewFile();
        } catch (IOException ex) {
            fail("Could not create file for test");
        }
        boolean result = FileSystem.checkFileExistByPattern(dir, pattern);
        assert result;
        file.delete();
    }
    /**
     * Test of checkFileExistByPattern method, of class FileSystem. Unhappy scenario
     */
    @Test
    public void testCheckFileExistByPatternUnhappy() {
        System.out.println("checkFileExistByPattern unhappy");
        String pattern = "ge*.?orld";
        String dir = ".";
        File file = new File("hello.world");
        try {
            file.createNewFile();
        } catch (IOException ex) {
            fail("Could not create file for test");
        }
        boolean result = FileSystem.checkFileExistByPattern(dir, pattern);
        assert !result;
        file.delete();
    }
}
