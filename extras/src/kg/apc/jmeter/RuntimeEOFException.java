package kg.apc.jmeter;

public class RuntimeEOFException extends RuntimeException
{

    public RuntimeEOFException(String string) {
        super(string);
    }

    public RuntimeEOFException(String string, Throwable ex) {
        super(string, ex);
    }

}
