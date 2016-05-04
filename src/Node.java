import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Dainiuss on 2016-04-20.
 */
public class Node {

    public String name;
    public Integer fathersCount;
    public ArrayList<Integer> fathers = new ArrayList<>();
    public ArrayList<BigDecimal> probabilities = new ArrayList<>();
    public boolean state;
    public BigDecimal probability;

}
