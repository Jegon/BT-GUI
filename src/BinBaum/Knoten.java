package BinBaum;

/**
 * Created by Jegoni on 24.01.2016.
 */
public class Knoten implements BE {

    private static Ende ende = new Ende();

    private String index;
    private String info;
    private BE links;
    private BE rechts;

    public Knoten(String index, String info) {
        this.index = index;
        this.info = info;
        links = Knoten.ende;
        rechts = Knoten.ende;
    }

    @Override
    public BE einfuegen(String index, String info) throws IndexAlreadyExistsException {
        int k = this.index.compareToIgnoreCase(index);
        if(k == 0)
            throw new IndexAlreadyExistsException(index);
        if(k > 0){
            links = links.einfuegen(index, info);
        }else
            rechts = rechts.einfuegen(index, info);
        return this;
    }

    @Override
    public BE loeschen(String index) throws IndexNotFoundException {
        int k = this.index.compareToIgnoreCase(index);
        if(k == 0){
            try{
                links = links.einfuegen(rechts);
            }catch(IndexAlreadyExistsException ex){
                System.err.println("Fehler biem loeschen von \"" + index + "\"!");
                ex.printStackTrace();
            }
            return links;
        }
        if(k > 0){
            links = links.loeschen(index);
        }else
            rechts = rechts.loeschen(index);
        return this;
    }

    @Override
    public int knotenanzahl() {
        return links.knotenanzahl() + rechts.knotenanzahl() + 1;
    }

    @Override
    public int tiefe() {
        int links = this.links.tiefe();
        int rechts = this.rechts.tiefe();
        return links > rechts ? links + 1 : rechts + 1;
    }

    @Override
    public BE suchen(String index) throws IndexNotFoundException {
        return null;
    }

    @Override
    public String getIndex() {
        return index;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public BE getLinks() {
        return links;
    }

    @Override
    public BE getRechts() {
        return rechts;
    }

    @Override
    public BE einfuegen(BE be) throws IndexAlreadyExistsException {
        return einfuegen(be.getIndex(), be.getInfo());
    }
}
