package ctoutweb.lalamiam.mapper;

import ctoutweb.lalamiam.model.dto.ProInformation;
import ctoutweb.lalamiam.repository.entity.ProEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
public class ProInformationMapper implements Function<ProEntity, ProInformation> {
  @Override
  public ProInformation apply(ProEntity proEntity) {
    return new ProInformation(proEntity);
  }
}
