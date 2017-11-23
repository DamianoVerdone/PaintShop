import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PaintShop {


  private static final char MATTE = 'M';
  private static final char GLOSS = 'G';
  private static final String NO_SOLUTION_EXISTS = "No solution exists";
  private List<List<Integer>> affectedUserFromAColorChange;

  private final List<Customer> customers;

  private int nColor;

  private BitSet bestSolution;

  /**
   * Parse the file and create a List of Customer and a List<List<Integer>>
   * that contains the position of the customer for a color change
   */
  public PaintShop(String filePath) {
    this.customers = new LinkedList<>();
    try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(filePath))) {
      nColor = Integer.parseInt(bufferedReader.readLine());
      affectedUserFromAColorChange = new ArrayList<>(nColor);
      bestSolution = new BitSet(nColor);
      for (int i = 0; i < nColor; i++) {
        affectedUserFromAColorChange.add(new LinkedList<>());
      }

      bufferedReader.lines()
          .map(x -> x.split("\\s")).
          forEach(x -> customers.add(createCustomer(x)));

    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }

  }

  /**
   * Check if the customer are all satisfied
   *
   * @return the best solution if exist
   */
  public String areCustomersSatisfied() {
    for (int i = 0; i < customers.size(); i++) {
      if (!customers.get(i).isHappy(bestSolution, nColor)) {
        int matteColor = customers.get(i).getMatteColor();
        if (matteColor >= 0 && !bestSolution.get(matteColor)) {
          bestSolution.set(matteColor);
          if (!areAffectedUsersByColorChangeStillHappy(matteColor, i)) {
            return NO_SOLUTION_EXISTS;
          }
        } else {
          return NO_SOLUTION_EXISTS;
        }
      }
    }
    return printSolution(bestSolution);


  }

  /**
   * Check if a change of a color (G -> M) make unhappy a customer that was happy before
   *
   * @param matteColPosition used to select the users that could be unhappy because the color
   * change
   * @param currentUserPos users that was happy until the change
   * @return true if the previous users are still happy false otherwise
   */
  private boolean areAffectedUsersByColorChangeStillHappy(int matteColPosition,
      int currentUserPos) {
    Queue<Integer> toCheck = new LinkedList<>(affectedUserFromAColorChange.get(matteColPosition));
    while (!toCheck.isEmpty()) {
      int i = toCheck.remove();
      if (i <= currentUserPos && !customers.get(i).isHappy(bestSolution, nColor)) {
        int matteColor = customers.get(i).getMatteColor();
        if (matteColor >= 0 && !bestSolution.get(matteColor)) {
          bestSolution.set(matteColor);
          toCheck.addAll(affectedUserFromAColorChange.get(matteColor));
        } else {
          return false;
        }
      }
    }
    return true;
  }

  private Customer createCustomer(String[] input) {
    BitSet color = new BitSet(nColor);
    int matteIndex = -1;
    for (int i = 0; i < input.length; i++) {
      int pos = Integer.parseInt(input[i]) - 1;
      color.set(pos);
      affectedUserFromAColorChange.get(pos).add(customers.size());
      if (input[++i].charAt(0) == MATTE) {
        matteIndex = pos;
      }
    }
    return new Customer(color, matteIndex);
  }

  private String printSolution(BitSet bestSolution) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < nColor; i++) {
      stringBuilder.append(bestSolution.get(i) ? MATTE : GLOSS).append(' ');
    }
    return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
  }


}
