package ctoutweb.lalamiam.service.serviceImpl;

import ctoutweb.lalamiam.exception.ProException;
import ctoutweb.lalamiam.mapper.ProInformationMapper;
import ctoutweb.lalamiam.model.dto.AddProfessionalDto;
import ctoutweb.lalamiam.model.dto.ProInformationDto;
import ctoutweb.lalamiam.repository.UserRepository;
import ctoutweb.lalamiam.repository.entity.UserEntity;
import ctoutweb.lalamiam.repository.transaction.UserTransactionSession;
import ctoutweb.lalamiam.service.ProService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProServiceImpl implements ProService {
  private final UserTransactionSession userTransactionSession;
  private final UserRepository userRepository;
  private final ProInformationMapper proInformationMapper;

  public ProServiceImpl(
          UserTransactionSession userTransactionSession,
          UserRepository userRepository,
          ProInformationMapper proInformationMapper
  ) {
    this.userTransactionSession = userTransactionSession;
    this.userRepository = userRepository;
    this.proInformationMapper = proInformationMapper;
  }

  @Override
  public ProInformationDto addProfessional(AddProfessionalDto addProfessionalInfo) {

    String password = addProfessionalInfo.password();
    String email = addProfessionalInfo.email();

    if(password == null || password.isEmpty() || email == null || email.isEmpty())
      throw new RuntimeException("Données Professionnelles incomplete");

    UserEntity addPro = userRepository.save(new UserEntity(addProfessionalInfo));
    return proInformationMapper.apply(addPro);
  }

  @Override
  public ProInformationDto getProfessionalInformation(Long professionalId) {
    UserEntity proEntity = userTransactionSession.getUserInformationById(professionalId);
    if(proEntity == null) throw new ProException("Professionel non trouvé", HttpStatus.BAD_REQUEST);
    return proInformationMapper.apply(proEntity);
  }
}
