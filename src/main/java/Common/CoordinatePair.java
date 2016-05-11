package Common;

/**
 * @author john
 */
public class CoordinatePair {
    public final int down;
    public final int across;

    public CoordinatePair(int down, int across) {
        this.down = down;
        this.across = across;
    }

    @Override
    public int hashCode() {
        return this.down ^ this.across;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            CoordinatePair castedObj = (CoordinatePair)obj;
            return (castedObj.down == this.down && castedObj.across == this.across);
        }
    }
}
