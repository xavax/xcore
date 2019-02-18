package com.xavax.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.annotations.Test;

/**
 * Test case for the mapping annotation classes.
 */
public class PersistentClassTest {
  private final static String ANNOTATION_FIELD_FORMAT = "%s=%s";
  private final static String ANNOTATION_FORMAT = "%s@%s(";
  private final static String CLASS_FORMAT = "class %s {\n";
  private final static String EMPTY_STRING = "";
  private final static String FIELD_FORMAT = "  %s %s;\n";
  private final static String METHOD_FORMAT = "  %s %s();\n";

  /**
   * Test mapping annotations.
   */
  @Test
  public void test() {
    final Employee employee1 = new HourlyEmployee();
    showAnnotations(employee1);
    final Employee employee2 = new SalaryEmployee();
    showAnnotations(employee2);
  }

  void showAnnotations(final Object object) {
    final StringBuilder builder = new StringBuilder();
    showClassAnnotations(object.getClass(), builder);
    final Class<? extends Object> clazz = object.getClass();
    builder.append(String.format(CLASS_FORMAT, clazz.getSimpleName()));
    showFields(clazz, builder);
    showMethods(clazz, builder);
    builder.append('}');
    System.out.println(builder.toString());
  }

  void showClassAnnotations(final Class<?> clazz, final StringBuilder builder) {
    final LinkedHashMap<String, List<Annotation>> map = new LinkedHashMap<>();
    getClassAnnotations(map, clazz);
    for ( final Entry<String, List<Annotation>> entry : map.entrySet() ) {
      builder.append(String.format(ANNOTATION_FORMAT, "", entry.getKey()));
      final Map<String, Object> parameters = new LinkedHashMap<>();
      for ( final Annotation annotation : entry.getValue() ) {
	for ( final Method method : annotation.annotationType().getDeclaredMethods() ) {
	  final String name = method.getName();
	  final Object value = getMethodValue(annotation, method);
	  if ( value != null ) {
	    final Object existing = parameters.get(name);
	    if ( existing == null || !value.equals(EMPTY_STRING) ) {
	      parameters.put(name, value);
	    }
	  }
	}
      }
      boolean first = true;
      for ( final Entry<String, Object> parameter : parameters.entrySet() ) {
	if ( first ) {
	  first = false;
	}
	else {
	  builder.append(", ");
	}
	builder.append(String.format(ANNOTATION_FIELD_FORMAT,
	    			     parameter.getKey(),
	    			     formatValue(parameter.getValue())));
      }
      builder.append(")\n");
    }
  }

  String formatValue(final Object value) {
    String result = null;
    if ( value instanceof String ) {
      result = formatString((String) value);
    }
    else if ( value.getClass().isArray() ) {
      result = formatArray((Object[]) value);
    }
    else {
      result = value.toString();
    }
    return result;
  }

  String formatString(final String input) {
    return '"' + input + '"';
  }

  @SuppressWarnings("PMD.UseVarargs")
  String formatArray(final Object[] array) {
    final StringBuilder builder = new StringBuilder("{ ");
    boolean first = true;
    for ( final Object item : array ) {
      if ( first ) {
	first = false;
      }
      else {
	builder.append(", ");
      }
      builder.append(formatValue(item));
    }
    builder.append(" }");
    return builder.toString();
  }

  void getClassAnnotations(final Map<String, List<Annotation>> map,
                                  final Class<?> clazz) {
    final Class<?> superClass = clazz.getSuperclass();
    if ( superClass != Object.class ) {
      getClassAnnotations(map, superClass);
    }
    for ( final Annotation annotation : clazz.getDeclaredAnnotations() ) {
      final String name = annotation.annotationType().getSimpleName();
      List<Annotation> list = map.get(name);
      if ( list == null ) {
	list = new ArrayList<>();
	map.put(name, list);
      }
      list.add(annotation);
    }
  }

  void showFields(final Class<?> clazz, final StringBuilder builder) {
    final Class<?> superClass = clazz.getSuperclass();
    if ( superClass != Object.class ) {
      showFields(superClass, builder);
    }
    for ( final Field field : clazz.getDeclaredFields() ) {
      showAnnotations(field.getAnnotations(), builder, "  ");
      builder.append(String.format(FIELD_FORMAT,
	  			   field.getType().getSimpleName(), field.getName()));
    }
  }

  void showMethods(final Class<?> clazz, final StringBuilder builder) {
    final Class<?> superClass = clazz.getSuperclass();
    if ( superClass != Object.class ) {
      showMethods(superClass, builder);
    }
    for ( final Method method : clazz.getDeclaredMethods() ) {
	showAnnotations(method.getAnnotations(), builder, "  ");
	final String typeName = method.getReturnType().getSimpleName();
	builder.append(String.format(METHOD_FORMAT, typeName, method.getName()));
    }
  }

  void showAnnotations(final Annotation[] annotations, final StringBuilder builder, final String prefix) {
    for ( final Annotation annotation : annotations ) {
      showAnnotation(annotation, builder, prefix);
    }
  }

  void showAnnotation(final Annotation annotation, final StringBuilder builder, final String prefix) {
    final Class<? extends Annotation> clazz = annotation.annotationType();
    builder.append(String.format(ANNOTATION_FORMAT, prefix, clazz.getSimpleName()));
    boolean first = true;
    for ( final Method method : clazz.getDeclaredMethods() ) {
	final String name = method.getName();
	final Object value = getMethodValue(annotation, method);
      	if ( value instanceof Annotation[] ) {
      	  builder.append("{\n");
      	  showAnnotations((Annotation[]) value, builder, prefix);
      	  builder.append(prefix).append('}');
      	}
      	else {
      	  if ( first ) {
      	    first = false;
      	  }
      	  else {
      	    builder.append(", ");
      	  }
      	  builder.append(String.format(ANNOTATION_FIELD_FORMAT, name,
	    			       formatValue(value)));
      	}
    }
    builder.append(")\n");  
  }

  Object getMethodValue(final Annotation annotation, final Method method) {
    Object value = "";
    try {
      value = method.invoke(annotation);
    }
    catch (IllegalAccessException e) {
    }
    catch (IllegalArgumentException e) {
    }
    catch (InvocationTargetException e) {
    }
    return value;
  }

  /**
   * EmployeeType defines all possible employee types.
   */
  enum EmployeeType {
    HOURLY,
    SALARY
  }

  /**
   * Employee is an abstract base class example using inheritance.
   */
  @PersistentClass(name="Employee", variantField="employeeType", variantEnum="EmployeeType")
  static class Employee {
    @PersistentField(name="employeeType")
    @Mapping(channel="json", name="type")
    @Mapping(channel="mongo", name="employee_type")
    EmployeeType employeeType;

    @PersistentField(name="firstName")
    @Mapping(channel="json", name="first")
    @Mapping(channel="mongo", name="first_name")
    String firstName;

    @PersistentField(name="lastName")
    @Mapping(channel="json", name="last")
    @Mapping(channel="mongo", name="last_name")
    String lastName;

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

  /**
   * SalaryEmployee is an example using inheritance.
   */
  @PersistentClass(name="SalaryEmployee", variant="SALARY")
  static class SalaryEmployee extends Employee {
  }

  /**
   * HourlyEmployee is an example using inheritance.
   */
  @PersistentClass(name="HourlyEmployee", variant="HOURLY")
  static class HourlyEmployee extends Employee {
  }
}
 
