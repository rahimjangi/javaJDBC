package com.raiseup.service;

import com.raiseup.model.Person;
import com.raiseup.repository.PersonRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PersonService  implements PersonRepository{

    private final Connection connection;

    public PersonService(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Person save(Person person)  {

        String firstName= person.getFirstName();
        String lastName=person.getLastName();
        LocalDate birthDate=person.getBirthDate();
        double salary= person.getSalary();
        String query="INSERT INTO PERSON(FIRST_NAME,LAST_NAME,BIRTH_DATE,SALARY)values(?,?,?,?)";

        PreparedStatement statement= null;
        Integer i=null;
        try {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,firstName);
            statement.setString(2,lastName);
            statement.setDate(3, Date.valueOf(birthDate));
            statement.setDouble(4,salary);
            i = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            while (rs.next()){
                long id = rs.getLong(1);
                person.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("saved person: "+person);
        return person;
    }

    @Override
    public Optional<Person> findPersonById(Long id) {
        Person p =null;
        String findByIdSQL="SELECT ID,FIRST_NAME,LAST_NAME,BIRTH_DATE,SALARY FROM PERSON WHERE ID=?";
        try{

            PreparedStatement ps = connection.prepareStatement(findByIdSQL);
            ps.setLong(1,id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                p= new Person();
                long personId = rs.getLong("ID");
                String firstName = rs.getString("FIRST_NAME");
                String lastName = rs.getString("LAST_NAME");
                LocalDate birthDate = rs.getDate("BIRTH_DATE").toLocalDate();
                double salary = rs.getDouble("SALARY");
                p.setId(personId);
                p.setFirstName(firstName);
                p.setLastName(lastName);
                p.setBirthDate(birthDate);
                p.setSalary(salary);
                System.out.println("Person: "+p);
            }
        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        System.out.println("returned person: "+p);

        return Optional.ofNullable(p);
    }

    @Override
    public List<Person> findAll() {
        return null;
    }

    @Override
    public void delete(Person person) {
        String DELETE_PERSON_SQL="DELETE FROM PERSON WHERE ID=?";
        try{
            PreparedStatement ps = connection.prepareStatement(DELETE_PERSON_SQL);
            ps.setLong(1,person.getId());
            int i = ps.executeUpdate();
        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        String DELETE_PERSON_SQL="DELETE FROM PERSON WHERE ID=?";
        try{
            PreparedStatement ps = connection.prepareStatement(DELETE_PERSON_SQL);
            ps.setLong(1,id);
            int i = ps.executeUpdate();
        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public Person update(Person person) {
        return null;
    }

    @Override
    public Person updateById(Long id) {
        return null;
    }

    public void deleteAll(Person...people) {
        String ids = Arrays.stream(people)
                .map(Person::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String DELETE_ALL_SQL = String.format("DELETE FROM PERSON WHERE ID IN (%s)", ids);
                
        try {
            Statement st = connection.createStatement();
            int i = st.executeUpdate(DELETE_ALL_SQL);
            System.out.println(i+" rows deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
