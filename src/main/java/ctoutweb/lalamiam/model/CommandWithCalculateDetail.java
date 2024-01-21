package ctoutweb.lalamiam.model;

import ctoutweb.lalamiam.repository.entity.CommandEntity;

public record CommandWithCalculateDetail(
        CommandEntity command,
        CalculateCommandDetail calculateCommandDetail
) {
}
