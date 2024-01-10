package ctoutweb.lalamiam.mapper;

import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.repository.entity.ProEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
public class ProInformationMapper implements Function<ProEntity, ProInformationDto> {
  @Override
  public ProInformationDto apply(ProEntity proEntity) {
    return new ProInformationDto(proEntity);
  }
}
