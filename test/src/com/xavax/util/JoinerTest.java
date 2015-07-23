package com.xavax.util;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for the Joiner class.
 */
public class JoinerTest {
  private final static String FIRST_NAME = "Thomas";
  private final static String LAST_NAME = "Jefferson";

  private final static Address[] ADDRESSES = new Address[] {
      new Address("210 Peachtree St NW", "Atlanta", "GA", "30303"),
      new Address("1 Traquility Base", "Huntsville", "AL", "35802")
  };

  private Person person;

  @BeforeMethod
  public void setup() {
    person = new Person(FIRST_NAME, LAST_NAME, 25);
    person.add(ADDRESSES[0]);
    person.add(ADDRESSES[1]);
  }

  @Test
  public void testConstructors() {
  }

  @Test
  public void testJoinableObjects() {
    final Joiner joiner = new Joiner();
    final String string = joiner.append(person).toString();
  }

  @Test
  public void testArrays() {
    final Joiner joiner = new Joiner();
    final String string = joiner.append(ADDRESSES).toString();
  }

  @Test
  public void testReusable() {
    final Joiner joiner = new Joiner().reusable();
    final String string = joiner.append(person).toString();
    final StringBuilder builder = joiner.getBuilder();
    assertEquals(0, builder.length());
  }

  @Test
  public void create() {
  }

  @Test
  public void createint() {
  }

  @Test
  public void endArray() {
  }

  @Test
  public void endCollection() {
  }

  @Test
  public void endEntity() {
  }

  @Test
  public void endObject() {
  }

  @Test
  public void skipNulls() {
  }

  @Test
  public void testToString() {
  }

  @Test
  public void testWithNullIndicator() {
  }

  @Test
  public void testWithQuotedStrings() {
  }

  @Test
  public void testWithSeparator() {
  }

  abstract private static class AbstractJoinableObject implements Joinable {
    public String toString() {
      return Joiner.create().append(this).toString();
    }

    abstract public Joiner join(final Joiner joiner);
  }

  private static class Address extends AbstractJoinableObject {

    private final String street;
    private final String city;
    private final String state;
    private final String postalCode;

    public Address(final String street, final String city, final String state, final String postalCode) {
      this.street = street;
      this.city = city;
      this.state = state;
      this.postalCode = postalCode;
    }

    @Override
    public Joiner join(final Joiner joiner) {
      joiner.append(street)
      	    .append(city)
      	    .append(state)
      	    .append(postalCode);
      return joiner;
    }
  }

  private static class Person extends AbstractJoinableObject {

    private int age;
    private String firstName;
    private String lastName;
    private List<Address> addresses;

    public Person(final String firstName, final String lastName, final int age) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.age = age;
      this.addresses = new ArrayList<>();
    }

    public void add(final Address address) {
      addresses.add(address);
    }

    @Override
    public Joiner join(final Joiner joiner) {
      joiner.append(firstName)
      	    .append(lastName)
      	    .append(addresses);
      return joiner;
    }
  }
}
