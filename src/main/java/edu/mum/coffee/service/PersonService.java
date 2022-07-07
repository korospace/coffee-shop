package edu.mum.coffee.service;

import edu.mum.coffee.domain.Person;
import edu.mum.coffee.domain.Role;
import edu.mum.coffee.exception.EmailTakenException;
import edu.mum.coffee.exception.UsernameTakenException;
import edu.mum.coffee.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PersonService {
    @Autowired
    PersonRepository adminRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public List<Person> getAllPersons() {
        return adminRepository.findAll();
    }

    public Person savePerson(Person person) {
        if (person.getPassword() != null && !person.getPassword().isEmpty()) {
            person.setEncryptedPassword(passwordEncoder.encode(person.getPassword()));
        }
        else {
            Person personDb = adminRepository.findOne(person.getId());
            person.setEncryptedPassword(personDb.getEncryptedPassword());
        }
        
        return adminRepository.save(person);
    }

    public Person findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public List<Person> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Person findById(Long id) {
        return adminRepository.findOne(id);
    }

    public void removePerson(Person person) {
        adminRepository.delete(person);
    }

    public void removePerson(Long id) {
        adminRepository.delete(id);
    }

    public Person registerNewPerson(Person person) throws EmailTakenException, UsernameTakenException {
        String username = person.getUsername();
        String email = person.getEmail();

        if (findByUsername(username) != null) {
            throw new UsernameTakenException("Username is already taken: " + username);
        }

        if (!findByEmail(email).isEmpty()) {
            throw new EmailTakenException("Email is already exists: " + email);
        }

        person.addRole(new Role("ROLE_USER"));

        return savePerson(person);
    }
}
