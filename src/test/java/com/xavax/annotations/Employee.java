package com.xavax.annotations;

/**
 * Employee is an abstract base class example using inheritance.
 */
@PersistentClass(name="Employee", variantField="employeeType", variantEnum="EmployeeType")
public class Employee {
  @PersistentField(name="employeeType")
  @Mapping(channel="json", name="type")
  @Mapping(channel="mongo", name="employee_type")
  protected EmployeeType employeeType;

  @PersistentField(name="firstName")
  @Mapping(channel="json", name="first")
  @Mapping(channel="mongo", name="first_name")
  protected String firstName;

  @PersistentField(name="lastName")
  @Mapping(channel="json", name="last")
  @Mapping(channel="mongo", name="last_name")
  protected String lastName;

  @PersistentField(name="manager")
  protected Employee manager;

  @AutoWire(resource=Employee.class)
  Employee resource;

  /**
   * Construct an Employee.
   */
  protected Employee() {
    // Protected constructor to prevent instantiation of this base class.
  }

  public String getFirstName() {
    return firstName;
  }

  @Modifier
  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  @Modifier
  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }
}