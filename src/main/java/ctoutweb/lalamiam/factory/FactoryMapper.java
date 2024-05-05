package ctoutweb.lalamiam.factory;

import ctoutweb.lalamiam.mapper.DashboardCommandMapper;

public class FactoryMapper {

  public static DashboardCommandMapper getDashboardCommandMapper() {
    return new DashboardCommandMapper();
  }
}
