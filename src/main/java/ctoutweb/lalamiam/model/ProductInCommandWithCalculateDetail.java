package ctoutweb.lalamiam.model;

import ctoutweb.lalamiam.repository.entity.CommandProductEntity;

public record ProductInCommandWithCalculateDetail(CommandProductEntity productInCommand, CalculateCommandDetail calculateCommandDetail) {
}
