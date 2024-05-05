package ctoutweb.lalamiam.model.dto;

import java.time.LocalDate;
import java.util.List;

public record DashboardDto(
  List<DashboardCommandDto> dashboardCommands,
  LocalDate commandDate
) {}
