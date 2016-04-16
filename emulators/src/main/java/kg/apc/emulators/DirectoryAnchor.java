package kg.apc.emulators;

public class DirectoryAnchor {

    @Override
    public String toString() {
        String file = this.getClass().getResource("anchor.properties").getPath();
        return file.substring(0, file.lastIndexOf("/"));
    }
}
