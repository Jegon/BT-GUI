package BinBaum;

/**
 * Created by Jegoni on 24.01.2016.
 */
public interface BE {

    BE einfuegen(String index, String info) throws IndexAlreadyExistsException;

    BE einfuegen(BE be) throws IndexAlreadyExistsException;

    BE loeschen(String index) throws IndexNotFoundException;

    int knotenanzahl();

    int tiefe();

    BE suchen(String index) throws IndexNotFoundException;

    String getIndex();

    String getInfo();

    boolean isLeaf();

    BE getLinks();

    BE getRechts();
}
