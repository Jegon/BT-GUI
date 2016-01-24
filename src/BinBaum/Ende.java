package BinBaum;

/**
 * Created by Jegoni on 24.01.2016.
 */
public class Ende implements BE {
    @Override
    public BE einfuegen(String index, String info) throws IndexAlreadyExistsException {
        return new Knoten(index, info);
    }

    @Override
    public BE loeschen(String index) throws IndexNotFoundException {
        throw new IndexNotFoundException(index);
    }

    @Override
    public int knotenanzahl() {
        return 0;
    }

    @Override
    public int tiefe() {
        return 0;
    }

    @Override
    public BE suchen(String index) throws IndexNotFoundException {
        throw new IndexNotFoundException(index);
    }

    @Override
    public String getIndex() {
        return null;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public BE getLinks() {
        return this;
    }

    @Override
    public BE getRechts() {
        return this;
    }

    @Override
    public BE einfuegen(BE be) throws IndexAlreadyExistsException {
        return be;
    }
}
