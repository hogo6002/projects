
/** Helper class to store a two ints
* @author JF
*/
public class Pair {
    public int x,y;
    public Pair(int x, int y) {
        this.x=x;
        this.y=y;
    }
  
    /**
    * Are two pairs equal?.
    * For pairs p and q, does p.x equal q.x and p.y equal q.y?
    */
    @Override
    public boolean equals(Object o) {
        try {
            Pair p=(Pair)o;
            return ((p.x==this.x) && (p.y==this.y));
        } catch (ClassCastException cce) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int cap=55544;
        return (((x%cap)*111%cap)+(y%cap))%cap;    
    }
};
