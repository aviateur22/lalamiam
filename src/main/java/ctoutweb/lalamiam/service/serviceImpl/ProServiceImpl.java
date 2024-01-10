package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.mapper.ProInformationMapper;
import ctoutweb.lalamiam.model.schema.AddProfessionalSchema;
import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.repository.ProRepository;
import ctoutweb.lalamiam.repository.entity.ProEntity;
import ctoutweb.lalamiam.service.ProService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class ProServiceImpl implements ProService {

  private final ProRepository proRepository;
  private final ProInformationMapper proInformationMapper;

  public ProServiceImpl(ProRepository proRepository, ProInformationMapper proInformationMapper) {
    this.proRepository = proRepository;
    this.proInformationMapper = proInformationMapper;
  }

  @Override
  public ProInformationDto addProfessional(AddProfessionalSchema addProfessionalInfo) {

    String password = addProfessionalInfo.password();
    String email = addProfessionalInfo.email();
    if(password == null || password.isEmpty() || email == null || email.isEmpty())
      throw new RuntimeException("Données Professionnelles incomplete");

    ProEntity addPro = proRepository.save(new ProEntity(addProfessionalInfo));
    return proInformationMapper.apply(addPro);
  }

  @Override
  public ProInformationDto getProfessionalInformation(BigInteger professionalId) {
    ProEntity proEntity = proRepository.findById(professionalId).orElseThrow(()-> new RuntimeException("pas connu"));
    return proInformationMapper.apply(proEntity);
  }
}
