package ctoutweb.lalamiam.model;

import org.w3c.dom.stylesheets.LinkStyle;

import java.util.Arrays;
import java.util.List;

public enum CommandStatus {
  New_ORDER(1),
  NOT_STARTED(2),
  IN_PROGRESS(3),
  FINISH(4),
  PAID(5);

  private final int value;

  CommandStatus(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  /**
   * Vérification si statut valide
   * @param value int - Status a vérifier
   * @return boolean
   */
  public static boolean isStatusValid(int value) {
    return Arrays.stream(CommandStatus.values())
            .anyMatch(statusValue -> statusValue.getValue() == value);
  }

  /**
   * Verification validité d'une liste de statut
   * @param statusIdList List<Integer>
   * @return boolean
   */
  public static boolean areListOfStatusValid(List<Integer> statusIdList) {
    return statusIdList.stream().allMatch(statusId -> isStatusValid(statusId));
  }
}
