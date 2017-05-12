package com.xavax.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import static com.xavax.util.JoinerTestConstants.*;

/**
 * Test cases for the Joiner class nesting.
 */
public class JoinerTest {


  private final static String HELLO = "hello";
  private final static String ADDR_NAME = "addresses";
  private final static String FIELD_NAME = "name";
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
  private final static String GADGET1 = "Gadget1";
  private final static String FIELDNAME = "addresses";

  private final static String EXPECT1 =
      STREET1 + SEPARATOR + ATL + SEPARATOR + GEORGIA + SEPARATOR + ZIP1;
  private final static String EXPECT2 =
      STREET2 + SEPARATOR + HSV + SEPARATOR + ALABAMA + SEPARATOR + ZIP2;
  private final static String EXPECT3 =
      FIRST_NAME + SEPARATOR + LAST_NAME + SEPARATOR + 25 + SEPARATOR +
      LBRACE + LPAREN + EXPECT1 + RPAREN + SEPARATOR +
      LPAREN + EXPECT2 + RPAREN + RBRACE;
  private final static String EXPECT4A =
      LBRACKET + LPAREN + EXPECT1 + RPAREN + SEPARATOR +
      LPAREN + EXPECT2 + RPAREN + SEPARATOR + INDICATOR + RBRACKET;
  private final static String EXPECT4B =
      LBRACKET + LPAREN + EXPECT1 + RPAREN + SEPARATOR + LPAREN + EXPECT2 + RPAREN + RBRACKET;
  private final static String EXPECT4C =
      FIELDNAME + SEPARATOR3 + LBRACE + LPAREN + EXPECT1 + RPAREN + SEPARATOR +
      LPAREN + EXPECT2 + RPAREN + SEPARATOR + INDICATOR + RBRACE;
  private final static String EXPECT5 =
      QUOTE + FIRST_NAME + QUOTE + SEPARATOR + QUOTE + LAST_NAME + QUOTE;
  private final static String EXPECT6 =
      STREET1 + SEPARATOR2 + ATL + SEPARATOR2 + GEORGIA + SEPARATOR2 + ZIP1;
  private final static String EXPECT7 =
      INDICATOR2 + SEPARATOR + INDICATOR2 + SEPARATOR + 0 + SEPARATOR + LBRACE + RBRACE;
  private final static String EXPECT8 = "0" + SEPARATOR + LBRACE + RBRACE;
  private final static String EXPECT9 =
      "true, x, 127, 123, 456, 789, false, z, 127, 123, 456, 789";
  private final static String EXPECT10 =
      "true, x, 0, 0, 0, 0, <null>, <null>, <null>, <null>, <null>, <null>";
  private final static String EXPECT11 = "true, x, 0, 0, 0, 0";
  private final static String EXPECT12 = FIELD_NAME + SEPARATOR3 + LAST_NAME;
  private final static String EXPECT13 = GADGET1;
  private final static String EXPECT14 = FIELD_NAME + SEPARATOR3 + INDICATOR;
  private final static String EXPECT15 = "true, 123, 456, 3.14, false, hello";
  private final static String EXPECT15B = EXPECT15 + ", <null>";
  private final static String EXPECT16 = LBRACE + EXPECT15 + RBRACE;
  private final static String EXPECT17 = GADGET1 + SEPARATOR3 + LPAREN + GADGET1 + RPAREN;
  private final static String EXPECT18A = "{<null>: (" + EXPECT1 + 
      "), XX: <null>, GA: (" + EXPECT1 + "), AL: (" + EXPECT2 + ")}";
  private final static String EXPECT18B = "addresses: " + EXPECT18A;
  private final static String EXPECT18C = "{<null>: (" + EXPECT1 +
      "), GA: (" + EXPECT1 + "), AL: (" + EXPECT2 + ")}";

  private final static Address[] ADDRESSES = new Address[] {
      new Address(STREET1, ATL, GEORGIA, ZIP1),
      new Address(STREET2, HSV, ALABAMA, ZIP2),
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
   * Test the fluent interface.
   */
  @Test
  public void testInterface() {
    final Joiner joiner = Joiner.create();
    assertEquals(joiner.withMaxDepth(5).getMaxDepth(), 5);
    assertEquals(joiner.withMaxDepth(-5).getMaxDepth(), 0);
    assertEquals(joiner.withPrefix(LBRACE).getPrefix(), LBRACE);
    assertTrue(joiner.withPrefix(null).getPrefix().isEmpty());
    assertEquals(joiner.withSuffix(RBRACE).getSuffix(), RBRACE);
    assertTrue(joiner.withSuffix(null).getSuffix().isEmpty());
    assertEquals(joiner.withPrefix(LBRACE).getPrefix(), LBRACE);
    assertTrue(joiner.withPrefix(null).getPrefix().isEmpty());
    assertEquals(joiner.withSeparator(SEPARATOR).getSeparator(), SEPARATOR);
    assertTrue(joiner.withSeparator(null).getSeparator().isEmpty());
    assertEquals(joiner.withItemSeparator(SEPARATOR).getItemSeparator(), SEPARATOR);
    assertTrue(joiner.withItemSeparator(null).getItemSeparator().isEmpty());
    assertEquals(joiner.withFieldNameSeparator(SEPARATOR).getFieldNameSeparator(), SEPARATOR);
    assertEquals(joiner.withFieldNameSeparator(null).getFieldNameSeparator(), SEPARATOR3);
    assertTrue(joiner.withFieldNames(true).hasFieldNames());
    assertFalse(joiner.withFieldNames(false).hasFieldNames());
  }

  /**
   * Test the join method.
   */
  @Test
  public void testJoin() {
    String result =
	Joiner.create()
	      .skipNulls()
	      .withSeparator(SEPARATOR)
	      .join(true, 123, 456, 3.14, false, HELLO, null);
    assertEquals(result, EXPECT15);
    result =
	Joiner.create()
	      .withSeparator(SEPARATOR)
	      .join(true, 123, 456, 3.14, false, HELLO, null);
    assertEquals(result, EXPECT15B);
    result =
	Joiner.create()
	      .withSeparator(SEPARATOR)
	      .withPrefix(LBRACE)
	      .withSuffix(RBRACE)
	      .skipNulls()
	      .join(true, 123, 456, 3.14, false, HELLO, null);
    assertEquals(result, EXPECT16);
  }

  /**
   * Test joining objects.
   */
  @Test
  public void testObjects() {
    String result = Joiner.create().append(ADDRESSES[0]).toString();
    assertEquals(result, EXPECT1);
    result = Joiner.create().append(ADDRESSES[1]).toString();
    assertEquals(result, EXPECT2);
    result = Joiner.create().append(person).toString();
    assertEquals(result, EXPECT3);
    final Gadget gadget = new Gadget(GADGET1);
    result = Joiner.create().append(gadget).toString();
    assertEquals(result, EXPECT13);
    result = Joiner.create().append(GADGET1, gadget).toString();
    assertTrue(EXPECT17.equals(result));
    result = Joiner.create().append((Object) null).toString();
    assertEquals(result, INDICATOR);
    result = Joiner.create().append(FIELD_NAME, (Object) null).toString();
    assertEquals(result, EXPECT14);
    result = ADDRESSES[0].join(null).toString();
    assertEquals(result, EXPECT1);
  }

  /**
   * Test joining arrays.
   */
  @Test
  public void testArrays() {
    String result = Joiner.create().append((Object[]) ADDRESSES).toString();
    assertEquals(result, EXPECT4A);
    result = Joiner.create().skipNulls().append((Object[]) ADDRESSES).toString();
    assertEquals(result, EXPECT4B);
    result = Joiner.create().append((Object[]) null).toString();
    assertEquals(result, INDICATOR);
    result = Joiner.create().skipNulls().append((Object[]) null).toString();
    assertTrue(result.isEmpty());
  }

  /**
   * Test collections.
   */
  @Test
  public void testCollections() {
    String result = Joiner.create().append((Collection<?>) null).toString();
    assertEquals(result, INDICATOR);
    result = Joiner.create().skipNulls().append((Collection<?>) null).toString();
    assertEquals(result, EMPTY);
    final List<Address> list = Arrays.asList(ADDRESSES);
    result = Joiner.create().append(ADDR_NAME, list).toString();
    assertEquals(result, EXPECT4C);
  }

  /**
   * Test maps.
   */
  @Test
  public void testMaps() {
    final Map<String, Address> map = new HashMap<>();
    map.put(GEORGIA, ADDRESSES[0]);
    map.put(ALABAMA, ADDRESSES[1]);
    map.put(null, ADDRESSES[0]);
    map.put("XX", null);
    String result = Joiner.create().append(map).toString();
    assertEquals(result, EXPECT18A);
    result = Joiner.create().append(null, map).toString();
    assertEquals(result, EXPECT18A);
    result = Joiner.create().append(ADDR_NAME, map).toString();
    assertEquals(result, EXPECT18B);
    result = Joiner.create().skipNulls().append(null, map).toString();
    assertEquals(result, EXPECT18C);
  }

  /**
   * Test appending primitives and wrappers.
   */
  @Test
  public void testAppenders() {
    Widget widget =
	new Widget(true, 'x', (byte) 0x7F, (short) 123, 456, 789L,
	    	   false, 'z', new Byte((byte) 0x7F),
	    	   new Short((short) 123), 456, 789L);
    String result = widget.toString();
    assertEquals(result, EXPECT9);
    widget = new Widget(true, 'x', (byte) 0, (short) 0, 0, 0L, null, null, null, null, null, null);
    result = widget.toString();
    assertEquals(result, EXPECT10);
    result = Joiner.create().append(null, new Integer(ZIP1)).toString();
    assertEquals(result, ZIP1);
  }

  /**
   * Test reusability.
   */
  @Test
  public void testReusable() {
    final Joiner joiner = Joiner.create().reusable();
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
    final Joiner joiner = Joiner.create().withQuotedStrings();
    final String result = joiner.append(person).toString();
    assertTrue(result.startsWith(EXPECT5));
  }

  /**
   * Test with custom separator.
   */
  @Test
  public void testWithSeparator() {
    final Joiner joiner = Joiner.create().withSeparator(SEPARATOR2);
    final String result = joiner.append(ADDRESSES[0]).toString();
    assertEquals(result, EXPECT6);
  }

  /**
   * Test custom null indicator
   */
  @Test
  public void testWithNullIndicator() {
    Joiner joiner = Joiner.create().withNullIndicator(null);
    assertTrue(joiner.getNullIndicator().isEmpty());
    joiner = Joiner.create().withNullIndicator(INDICATOR2);
    assertEquals(joiner.getNullIndicator(), INDICATOR2);
    final Person nobody = new Person(null, null, 0);
    final String result = joiner.append(nobody).toString();
    assertTrue(result.startsWith(EXPECT7));
  }

  /**
   * Test with skip nulls enabled.
   */
  @Test
  public void testSkipNulls() {
    final Person nobody = new Person(null, null, 0);
    String result = Joiner.create().skipNulls().append(nobody).toString();
    assertTrue(result.startsWith(EXPECT8));
    final Widget widget = new Widget(true, 'x', (byte) 0, (short) 0, 0, 0L, null, null, null, null, null, null);
    result = Joiner.create().skipNulls().append(widget).toString();
    assertEquals(result, EXPECT11);
    result = Joiner.create().append(FIELD_NAME, (Object) null).toString();
    assertEquals(result, EXPECT14);
    result = Joiner.create().skipNulls().append(FIELD_NAME, (Object) null).toString();
    assertEquals(result, EMPTY);
  }

  /**
   * Test field names.
   */
  @Test
  public void testFieldNames() {
    Joiner joiner = Joiner.create().appendField(FIELD_NAME, LAST_NAME);
    assertEquals(joiner.toString(), EXPECT12);
    joiner = Joiner.create().withFieldNames(false).appendField(FIELD_NAME, LAST_NAME);
    assertEquals(joiner.toString(), LAST_NAME);
  }

  /**
   * Test creating a joiner with a specified buffer size.
   */
  @Test
  public void testCreate() {
    final Joiner joiner = Joiner.create(CAPACITY);
    final StringBuilder builder = joiner.getBuilder();
    assertEquals(builder.capacity(), CAPACITY);
  }

  /**
   * Test appendRaw.
   */
  @Test
  public void testAppendRaw() {
    assertEquals(Joiner.create().appendRaw(null).appendRaw(LAST_NAME).toString(), LAST_NAME);
  }

  /**
   * Test the Tracker.
   */
  @Test
  public void testTracker() {
    final Joiner joiner = Joiner.create();
    final Joiner.Tracker tracker = joiner.getTracker();
    tracker.setLevel(0);
    tracker.pop();
    assertEquals(tracker.getLevel(), 0);
    tracker.setLevel(Long.SIZE - 1);
    tracker.push(null);
    assertEquals(tracker.getLevel(), Long.SIZE - 1);
    
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
    protected Joiner doJoin(final Joiner joiner) {
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
    protected Joiner doJoin(final Joiner joiner) {
      joiner.append(firstName)
      	    .append(lastName)
      	    .append(age)
      	    .append(addresses);
      return joiner;
    }
  }

  /**
   * A simple object for testing.
   */
  private static class Widget extends AbstractJoinableObject {
    private final boolean field0;
    private final char    field1;
    private final byte    field2;
    private final short   field3;
    private final int     field4;
    private final long    field5;
    private final Boolean   field6;
    private final Character field7;
    private final Byte      field8;
    private final Short     field9;
    private final Integer   field10;
    private final Long      field11;

    /**
     * Construct a Widget.
     */
    @SuppressWarnings("PMD.ExcessiveParameterList")
    public Widget(final boolean arg0, final char arg1, final byte arg2, final short arg3,
                  final int arg4, final long arg5, final Boolean arg6, final Character arg7,
                  final Byte arg8, final Short arg9, final Integer arg10, final Long arg11) {
      this.field0  = arg0;
      this.field1  = arg1;
      this.field2  = arg2;
      this.field3  = arg3;
      this.field4  = arg4;
      this.field5  = arg5;
      this.field6  = arg6;
      this.field7  = arg7;
      this.field8  = arg8;
      this.field9  = arg9;
      this.field10 = arg10;
      this.field11 = arg11;
    }

    /**
     * Join this Widget using the supplied joiner.
     *
     * @param joiner  the Joiner to use for output.
     * @return the joiner.
     */
    @Override
    protected Joiner doJoin(final Joiner joiner) {
      joiner.append(field0)
      	    .append(field1)
      	    .append(field2)
      	    .append(field3)
      	    .append(field4)
      	    .append(field5)
      	    .append(field6)
      	    .append(field7)
      	    .append(field8)
      	    .append(field9)
      	    .append(field10)
      	    .append(field11);
      return joiner;
    }
  }

  /**
   * A simple class for testing.
   */
  private static class Gadget {
    private final String name;

    /**
     * Construct a Gadget.
     */
    public Gadget(final String name) {
      this.name = name;
    }

    /**
     * Returns a string representation of this object.
     * @return a string representation of this object.
     */
    @Override
    public String toString() {
      return name;
    }
  }
}
