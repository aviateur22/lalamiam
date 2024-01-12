package ctoutweb.lalamiam.util;

public class CommonFunction {
  public static boolean isNullOrEmpty(String word) {
    if(word == null || word.isEmpty() || word.isBlank()) return true;
    return false;
  }

  public static boolean isNumber(String numberText) {
    try
    {
      Integer.parseInt(numberText);
    }
    catch (NumberFormatException e)
    {
      return false;
    }
    return true;
  }
}
