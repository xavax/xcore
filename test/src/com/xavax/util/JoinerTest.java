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
  private final static String LBRACE   = "{";
  private final static String LBRACKET = "[";
  private final static String LPAREN   = "(";
  private final static String QUOTE    = "\"";
  private final static String RBRACE   = "}";
  private final static String RBRACKET = "]";
  private final static String RPAREN   = ")";
  private final static String SEPARATOR  = ", ";
  private final static String SEPARATOR2 = "; ";
  private final static String INDICATOR  = "<null>";
  private final static String INDICATOR2 = "<<null>>";

  private final static String FIRST_NAME = "Thomas";
  private final static String LAST_NAME = "Jefferson";
  private final static String ATL = "Atlanta";
  private final static String HSV = "Huntsville";
  private final static String STREET1 = "210 Peachtree St NW";
  private final static String STREET2 = "1 Traquility Base";
  private final static String ALABAMA = "AL";
  private final static String GEORGIA = "GA";
  private final static String ZIP1 = "30303";
  private final static String ZIP2 = "35802";

  private final static String EXPECT1 =
      LPAREN + STREET1 + SEPARATOR + ATL + SEPARATOR + GEORGIA + SEPARATOR + ZIP1 + RPAREN;
  private final static String EXPECT2 =
      LPAREN + STREET2 + SEPARATOR + HSV + SEPARATOR + ALABAMA + SEPARATOR + ZIP2 + RPAREN;
  private final static String EXPECT3 =
      LPAREN + FIRST_NAME + SEPARATOR + LAST_NAME + SEPARATOR + 25 + SEPARATOR +
      LBRACE + EXPECT1 + SEPARATOR + EXPECT2 + RBRACE + RPAREN;
  private final static String EXPECT4 =
      LBRACKET + EXPECT1 + SEPARATOR + EXPECT2 + SEPARATOR + INDICATOR + RBRACKET;
  private final static String EXPECT5 =
      LPAREN + QUOTE + FIRST_NAME + QUOTE + SEPARATOR + QUOTE + LAST_NAME + QUOTE;
  private final static String EXPECT6 =
      LPAREN + STREET1 + SEPARATOR2 + ATL + SEPARATOR2 + GEORGIA + SEPARATOR2 + ZIP1 + RPAREN;
  private final static String EXPECT7 =
      LPAREN + INDICATOR2 + SEPARATOR + INDICATOR2 + SEPARATOR + 0 + SEPARATOR + LBRACE + RBRACE + RPAREN;
  private final static String EXPECT8 =
      LPAREN + 0 + SEPARATOR + LBRACE + RBRACE + RPAREN;

  private final static Address[] ADDRESSES = new Address[] {
      new Address(STREET1, ATL, "GA", ZIP1),
      new Address(STREET2, HSV, "AL", ZIP2),
      null
  };

  private Person person;

  /**
   * Common test set up.
   */
  @BeforeMethod
  public void setup() {
    person = new Person(FIRST_NAME, LAST_NAME, 25);
    person.add(ADDRESSES[0]);
    person.add(ADDRESSES[1]);
  }

  /**
   * Test joining objects.
   */
  @Test
  public void testJoinableObjects() {
    Joiner joiner = new Joiner();
    String result = joiner.append(ADDRESSES[0]).toString();
    assertEquals(result, EXPECT1);
    joiner = new Joiner();
    result = joiner.append(ADDRESSES[1]).toString();
    assertEquals(result, EXPECT2);
    joiner = new Joiner();
    result = joiner.append(person).toString();
    assertEquals(result, EXPECT3);
  }

  /**
   * Test joining arrays.
   */
  @Test
  public void testArrays() {
    final Joiner joiner = new Joiner();
    final String result = joiner.append((Object[]) ADDRESSES).toString();
    assertEquals(result, EXPECT4);
  }

  /**
   * Test reusability.
   */
  @Test
  public void testReusable() {
    final Joiner joiner = new Joiner().reusable();
    final String result = joiner.append(person).toString();
    assertEquals(result, EXPECT3);
    final StringBuilder builder = joiner.getBuilder();
    assertEquals(0, builder.length());
  }

  /**
   * Test using quoted strings.
   */
  @Test
  public void testWithQuotedStrings() {
    final Joiner joiner = new Joiner().withQuotedStrings();
    final String result = joiner.append(person).toString();
    assertTrue(result.startsWith(EXPECT5));
  }

  /**
   * Test with custom separator.
   */
  @Test
  public void testWithSeparator() {
    final Joiner joiner = new Joiner().withSeparator(SEPARATOR2);
    final String result = joiner.append(ADDRESSES[0]).toString();
    assertEquals(result, EXPECT6);
  }

  /**
   * Test custom null indicator
   */
  @Test
  public void testWithNullIndicator() {
    final Joiner joiner = new Joiner().withNullIndicator(INDICATOR2);
    final Person nobody = new Person(null, null, 0);
    final String result = joiner.append(nobody).toString();
    assertTrue(result.startsWith(EXPECT7));
  }

  /**
   * Test with skip nulls enabled.
   */
  @Test
  public void skipNulls() {
    final Joiner joiner = new Joiner().skipNulls();
    final Person nobody = new Person(null, null, 0);
    final String result = joiner.append(nobody).toString();
    assertTrue(result.startsWith(EXPECT8));
  }

  @Test
  public void create() {
  }

  @Test
  public void createint() {
  }

  /**
   * AbstractJoinableObject is a base class for objects implementing Joinable.
   */
  abstract private static class AbstractJoinableObject implements Joinable {
    /**
     * Returns a string representation of this object.
     * @return a string representation of this object.
     */
    @Override
    public String toString() {
      return Joiner.create().append(this).toString();
    }

    /**
     * Output this object to the specified joiner.
     *
     * @param joiner  the joiner to use for output.
     * @return this joiner.
     */
    @Override
    abstract public Joiner join(final Joiner joiner);
  }

  /**
   * Address represents a physical mailing address.
   */
  private static class Address extends AbstractJoinableObject {

    private final String street;
    private final String city;
    private final String state;
    private final String postalCode;

    /**
     * Construct an Address.
     */
    public Address(final String street, final String city, final String state, final String postalCode) {
      this.street = street;
      this.city = city;
      this.state = state;
      this.postalCode = postalCode;
    }

    /**
     * Join this address using the supplied joiner.
     *
     * @param joiner  the Joiner to use for output.
     * @return the joiner.
     */
    @Override
    public Joiner join(final Joiner joiner) {
      joiner.append(street)
      	    .append(city)
      	    .append(state)
      	    .append(postalCode);
      return joiner;
    }
  }

  /**
   * Person represents a person with a name and address.
   */
  private static class Person extends AbstractJoinableObject {

    private final int age;
    private final String firstName;
    private final String lastName;
    private final List<Address> addresses;

    /**
     * Construct a Person.
     */
    public Person(final String firstName, final String lastName, final int age) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.age = age;
      this.addresses = new ArrayList<>();
    }

    /**
     * Add an address to this person.
     *
     * @param address  the new address.
     */
    public void add(final Address address) {
      addresses.add(address);
    }

    /**
     * Join this person using the supplied joiner.
     *
     * @param joiner  the Joiner to use for output.
     * @return the joiner.
     */
    @Override
    public Joiner join(final Joiner joiner) {
      joiner.append(firstName)
      	    .append(lastName)
      	    .append(age)
      	    .append(addresses);
      return joiner;
    }
  }

  
}
