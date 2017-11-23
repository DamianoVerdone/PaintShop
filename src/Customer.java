import java.util.BitSet;


public class Customer {

  private int matteIndex;

  private final BitSet color;


  public Customer(BitSet color, int mattePos) {
    this.matteIndex = mattePos;
    this.color = color;
  }


  public boolean isHappy(final BitSet currentSolution, int nColor) {
    BitSet bitSet = BitSet.valueOf(currentSolution.toByteArray());
    if (matteIndex >= 0) {
      bitSet.flip(matteIndex);
    }
    bitSet.flip(0, nColor);
    return bitSet.intersects(color);

  }


  public int getMatteColor() {
    return matteIndex;
  }


}
