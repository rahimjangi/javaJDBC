package com.raiseup;

import com.raiseup.model.Person;
import com.raiseup.service.PersonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class PeopleRepositoryTest {
    PersonService repository;
    Connection connection;

    @BeforeEach
    public void connectionDB() throws SQLException {
        connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb",
                "root","rahmin4101");
        repository=new PersonService(connection);
        System.out.println("connection opened!");
        connection.setAutoCommit(false);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if(connection!=null){
            connection.close();
        }
        System.out.println("connection closed!");
    }

    @Test
    public void canSavePersonTest() {
        Person p1= new Person("rahim","jangi",
                LocalDate.of(1982,9,23),
                5700.00);

        Person savedPerson=repository.save(p1);
        assertThat(savedPerson.getId()).isGreaterThan(0);
        assertThat(p1).isEqualTo(savedPerson);
    }

    @Test
    public void findPersonByIdTest(){
        Person p1=new Person("rahim","jangi",LocalDate.now(),5600.00);
        p1=repository.save(p1);
        Person personById = repository.findPersonById(p1.getId()).get();
        assertThat(personById).isEqualTo(p1);
    }

    @Test
    public void notFoundPersonTest(){
        Optional<Person> personById = repository.findPersonById(-1L);
        assertThat(personById).isEmpty();
    }

    @Test
    public void canDeletePersonObjectTest(){
        Person p = new Person("rahim","jangi",LocalDate.now(),5700);
        p=repository.save(p);
        repository.delete(p);
        Optional<Person> personById = repository.findPersonById(p.getId());
        assertThat(personById).isEmpty();
    }

    @Test
    public void canDeletePersonByIdTest(){
        Person p = new Person("rahim","jangi",LocalDate.now(),5700);
        p=repository.save(p);
        repository.deleteById(p.getId());
        Optional<Person> personById = repository.findPersonById(p.getId());
        assertThat(personById).isEmpty();
    }

    @Test
    public void canDeleteAllPersonTest(){
        Person p1 = new Person("rahim","jangi",LocalDate.now(),5700);
        Person p2 = new Person("hoshi","kheiri",LocalDate.now(),5700);
        p1=repository.save(p1);
        p2=repository.save(p2);
        repository.deleteAll(p1,p2);
    }
}
