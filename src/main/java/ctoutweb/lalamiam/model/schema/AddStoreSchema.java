package ctoutweb.lalamiam.model.schema;

import ctoutweb.lalamiam.repository.entity.ProEntity;

import java.math.BigInteger;

public record AddStoreSchema(BigInteger proId, String name, String Adress, String city, String cp) {
}
