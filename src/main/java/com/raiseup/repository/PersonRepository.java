package com.raiseup.repository;

import com.raiseup.model.Person;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PersonRepository {

    Person save(Person person) throws SQLException;
    Optional<Person>findPersonById(Long id);
    List<Person>findAll();
    void delete(Person person);
    void deleteById(Long id);
    Person update(Person person);
    Person updateById(Long id);

}
