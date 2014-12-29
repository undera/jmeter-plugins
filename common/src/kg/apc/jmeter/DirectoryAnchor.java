package kg.apc.jmeter;

public class DirectoryAnchor {

    @Override
    public String toString() {
        String file = this.getClass().getResource("version.properties").getPath();
        return file.substring(0, file.lastIndexOf("/"));
    }
}
