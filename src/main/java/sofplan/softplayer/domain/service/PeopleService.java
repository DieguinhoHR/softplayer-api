package sofplan.softplayer.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sofplan.softplayer.api.v1.dto.PeopleDTO;
import sofplan.softplayer.api.v1.dto.PeopleNewDTO;
import sofplan.softplayer.domain.exception.PeopleNotFoundException;
import sofplan.softplayer.domain.model.People;
import sofplan.softplayer.domain.model.mapper.PeopleMapper;
import sofplan.softplayer.domain.repository.PeopleRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<People> listAll() {
        return peopleRepository.findAll();
    }

    public List<People> findByName(String name) {
        return peopleRepository.findByName(name);
    }

    public People findById(Long id) {
        return peopleRepository.findById(id)
                .orElseThrow(() -> new PeopleNotFoundException("Pessoa não encontrada"));
    }

    @Transactional
    public People save(PeopleNewDTO peoplePostRequestBody) {
        peoplePostRequestBody.setPassword(bCryptPasswordEncoder.encode(peoplePostRequestBody.getPassword()));
        return peopleRepository.save(PeopleMapper.INSTANCE.toPeople(peoplePostRequestBody));
    }

    public void update(PeopleDTO peopleDTO) {
        People savedPeople = findById(peopleDTO.getId());
        People people = PeopleMapper.INSTANCE.toPeople(peopleDTO);
        people.setId(savedPeople.getId());
        peopleRepository.save(people);
    }

    public void delete(long id) {
        peopleRepository.delete(findById(id));
    }
}
