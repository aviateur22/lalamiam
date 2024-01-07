package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.mapper.ProInformationMapper;
import ctoutweb.lalamiam.model.AddProfessionalSchema;
import ctoutweb.lalamiam.model.dto.ProInformation;
import ctoutweb.lalamiam.repository.ProRepository;
import ctoutweb.lalamiam.repository.entity.ProEntity;
import ctoutweb.lalamiam.service.ProService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
public class ProServiceImpl implements ProService {

  private final ProRepository proRepository;
  private final ProInformationMapper proInformationMapper;

  public ProServiceImpl(ProRepository proRepository, ProInformationMapper proInformationMapper) {
    this.proRepository = proRepository;
    this.proInformationMapper = proInformationMapper;
  }

  @Override
  public void addProfessional(AddProfessionalSchema addProfessionalSchema) {

  }

  @Override
  public ProInformation getProfessionalInformation(BigInteger professionalId) {
    ProEntity proEntity = proRepository.findById(professionalId).orElseThrow(()-> new RuntimeException("pas connu"));

    return proInformationMapper.apply(proEntity);
  }
}
