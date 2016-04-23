package kg.apc.jmeter;

import java.io.IOException;

public class EndOfFileException extends IOException
{

    public EndOfFileException(String string) {
        super(string);
    }

}
