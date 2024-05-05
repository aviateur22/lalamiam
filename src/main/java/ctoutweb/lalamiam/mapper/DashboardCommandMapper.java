package ctoutweb.lalamiam.mapper;

import ctoutweb.lalamiam.factory.Factory;
import ctoutweb.lalamiam.model.dto.DashboardCommandDto;
import ctoutweb.lalamiam.repository.entity.CommandEntity;

import java.util.function.Function;

public class DashboardCommandMapper implements Function<CommandEntity, DashboardCommandDto> {
  @Override
  public DashboardCommandDto apply(CommandEntity command) {
    return Factory.getDashboardCommand(command);
  }
}
