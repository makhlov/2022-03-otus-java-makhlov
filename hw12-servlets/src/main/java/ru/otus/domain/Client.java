package ru.otus.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

import static java.util.List.copyOf;
import static java.util.Objects.nonNull;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(cascade = ALL, fetch = EAGER, mappedBy = "client")
    private List<Phone> phones;

    public Client() {
    }

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;

        this.phones = phones;
        setOwnerForPhones();
    }

    private void setOwnerForPhones() {
        if (nonNull(phones)) {
            for (Phone p : phones) {
                p.setClient(this);
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public Client clone() {
        Address addressCopy = nonNull(this.address) ? this.address.clone() : null;
        List<Phone> phonesCopy = nonNull(phones) ? copyOf(this.phones) : null;

        return new Client(
                this.id,
                this.name,
                addressCopy,
                phonesCopy
        );
    }

}
