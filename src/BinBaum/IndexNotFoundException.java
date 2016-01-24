package BinBaum;

/**
 * Created by Jegoni on 24.01.2016.
 */
public class IndexNotFoundException extends Exception {
    private String index;

    public IndexNotFoundException(String index) {
        super("The index could not been found");
        this.index = index;
    }

    public IndexNotFoundException(String index, String message) {
        super(message);
        this.index = index;
    }

    public String getIndex() {
        return index;
    }
}