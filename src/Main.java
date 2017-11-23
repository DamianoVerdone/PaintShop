public class Main {

  public static void main(String[] args) {

    if (args.length != 1) {
      System.err.println("Number of arguments is incorrect. "
          + "Please provide the path for the input file as the first argument!");
      System.exit(1);
    }
    PaintShop paintShop = new PaintShop(args[0]);
    System.out.println(paintShop.areCustomersSatisfied());
  }


}
