package ru.otus.hibernate.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author sergey
 * created on 08.10.18.
 */

@Entity
@Table(name = "tPhone")
public class Phone {

    @Id
    @GeneratedValue
    private Long id;


    @Column(name = "phone_number", nullable = false)
    private String number;


    @ManyToOne(cascade = CascadeType.ALL) //вариат 1 создания схемы
    @JoinColumn(name = "person_id", nullable = false) //вариат 1 создания схемы
    //@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // вариант 2 создания схемы
    private Person person;

    public Phone() {
    }

    public Phone(String number, Person person) {
        this.number = number;
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", personId=" + person.getId() +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(id, phone.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
