package BinBaum;

/**
 * Created by Jegoni on 24.01.2016.
 */
public class BinBaum {

    private BE root = new Ende();

    public void einfuegen(String index, String info) throws IndexAlreadyExistsException {
        root = root.einfuegen(index, info);
    }

    public BE getRoot() {
        return root;
    }

    public void loeschen(String index) throws IndexNotFoundException {
        root = root.loeschen(index);
    }

    public int knotenanzahl(){
        return root.knotenanzahl();
    }

    public int tiefe(){
        return root.tiefe();
    }

    BE suchen(String index){
        try {
            return root.suchen(index);
        } catch (IndexNotFoundException e) {
            System.err.println("\"" + index + "\" konnte nicht gefunden werden!");
            return null;
        }
    }
}
