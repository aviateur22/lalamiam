package ctoutweb.lalamiam.model.dto;

import ctoutweb.lalamiam.model.PersistCommand;

import java.math.BigInteger;

public class PersistUpdatedCommandDto extends PersistCommand {
  private BigInteger commandId;

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////


  public BigInteger getCommandId() {
    return commandId;
  }

  public void setCommandId(BigInteger commandId) {
    this.commandId = commandId;
  }
}
