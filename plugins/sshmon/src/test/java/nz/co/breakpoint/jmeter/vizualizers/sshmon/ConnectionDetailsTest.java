package nz.co.breakpoint.jmeter.vizualizers.sshmon;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ConnectionDetailsTest {

    @Test
    public void testEquals() {
        assertEquals(new ConnectionDetails("host"),
                     new ConnectionDetails("HOST"));

        assertEquals(new ConnectionDetails("host"),
                     new ConnectionDetails("host", 22));

        assertEquals(new ConnectionDetails("host", 123),
                     new ConnectionDetails("host", 123));

        assertEquals(new ConnectionDetails("username", "host"),
                     new ConnectionDetails("USERNAME", "host"));

        assertEquals(new ConnectionDetails("username", "host", "password"),
                     new ConnectionDetails("username", "host", "otherpassword"));

        assertEquals(new ConnectionDetails("username", "host", 22, "password"),
                     new ConnectionDetails("username", "host", 22, "password", new byte[]{1,2,3}));
    }

    @Test
    public void testNotEquals() {
        assertNotEquals(new ConnectionDetails("host"),
                        new ConnectionDetails("otherhost"));

        assertNotEquals(new ConnectionDetails("host", 22),
                        new ConnectionDetails("host", 23));
    }

    @Test
    public void testSameHashCode() {
        assertEquals(new ConnectionDetails("host").hashCode(),
                     new ConnectionDetails("HOST").hashCode());

        assertEquals(new ConnectionDetails("host").hashCode(),
                     new ConnectionDetails("host", 22).hashCode());

        assertEquals(new ConnectionDetails("host", 123).hashCode(),
                     new ConnectionDetails("host", 123).hashCode());

        assertEquals(new ConnectionDetails("username", "host").hashCode(),
                     new ConnectionDetails("USERNAME", "host").hashCode());

        assertEquals(new ConnectionDetails("username", "host", "password").hashCode(),
                     new ConnectionDetails("username", "HOST", "otherpassword").hashCode());

        assertEquals(new ConnectionDetails("username", "host", 22, "password").hashCode(),
                     new ConnectionDetails("username", "host", 22, "password", new byte[]{1,2,3}).hashCode());
    }

    @Test
    public void testDifferentHashCode() {
        assertNotEquals(new ConnectionDetails("host").hashCode(),
                        new ConnectionDetails("otherhost").hashCode());

        assertNotEquals(new ConnectionDetails("host", 22).hashCode(),
                        new ConnectionDetails("host", 23).hashCode());
    }

       @Test
    public void testToString() {
        assertEquals(new ConnectionDetails("host").toString(),
            "host:22");

        assertEquals(new ConnectionDetails("username", "host").toString(),
            "username@host:22");

        assertEquals(new ConnectionDetails("username", "host", 123).toString(),
            "username@host:123");

        assertEquals(new ConnectionDetails("username", "host", 123, "password").toString(),
            "username@host:123");

        assertEquals(new ConnectionDetails("username", "host", 123, "password", new byte[]{1,2,3}).toString(),
            "username@host:123");
    }
}
