package ctoutweb.lalamiam.mapper;

import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.repository.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
public class ProInformationMapper implements Function<UserEntity, ProInformationDto> {
  @Override
  public ProInformationDto apply(UserEntity proEntity) {
    return new ProInformationDto(proEntity);
  }
}
