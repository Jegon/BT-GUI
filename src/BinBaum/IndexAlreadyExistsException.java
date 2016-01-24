package BinBaum;

/**
 * Created by Jegoni on 24.01.2016.
 */
public class IndexAlreadyExistsException extends Exception {
    private String index;

    public IndexAlreadyExistsException(String index){
        super("The index does already exist!");
        this.index = index;
    }

    public IndexAlreadyExistsException(String index, String message){
        super(message);
        this.index = index;
    }

    public String getIndex(){
        return index;
    }
}