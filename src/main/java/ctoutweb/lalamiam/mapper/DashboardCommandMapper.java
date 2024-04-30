package ctoutweb.lalamiam.mapper;

import ctoutweb.lalamiam.model.dto.DashboardCommandDto;
import ctoutweb.lalamiam.model.dto.RegisterCommandDto;

import java.util.function.Function;

/**
 * Convertion RegisterCommandDto en DashboardCommandDto
 */
public class DashboardCommandMapper implements Function<RegisterCommandDto, DashboardCommandDto> {
  @Override
  public DashboardCommandDto apply(RegisterCommandDto registerCommandDto) {
    return null;
  }
}
