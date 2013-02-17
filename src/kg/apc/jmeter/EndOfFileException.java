package kg.apc.jmeter;

import java.io.IOException;

/**
 *
 * @author undera
 */
public class EndOfFileException extends IOException
{

    public EndOfFileException(String string) {
        super(string);
    }

}
