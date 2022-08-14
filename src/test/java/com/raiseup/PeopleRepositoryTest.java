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
    public void canSave() {
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
}
