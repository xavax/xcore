package com.xavax.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

import org.junit.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import static org.reflections.scanners.Scanners.*;
import static org.reflections.ReflectionUtils.*;

/**
 * Test case for the mapping annotation classes.
 */
@SuppressWarnings("PMD.TooManyMethods")
public class PersistentClassTest {
  private final String COM_XAVAX = "com.xavax";

  private final static Logger logger =
      LoggerFactory.getLogger(PersistentClassTest.class);

  
  /**
   * Test mapping annotations.
   */
  @Test
  public void test() {
//    final Employee employee1 = new HourlyEmployee();
//    showAnnotations(employee1);
//    final Employee employee2 = new SalaryEmployee();
//    showAnnotations(employee2);
  }


  @Test
  public void showPackages() {
    // Package[] packages = Package.getPackages();
    // for ( final Package thePackage : packages ) {
    // final String name = thePackage.getName();
    // if ( name.startsWith(COM_XAVAX) ) {
    // logger.debug("{}", name);
    // System.out.println(name);
    // }
    // }
    Reflections reflections = new Reflections(COM_XAVAX);
    System.out.println("Persistent Classes");
    Set<Class<?>> classes = reflections
	.getTypesAnnotatedWith(PersistentClass.class);
    for ( final Class<?> clazz : classes ) {
      final String name = clazz.getSimpleName();
      logger.debug("{}", name);
      System.out.println(name);
    }
    System.out.println("SubTypes of Employee");
    Set<Class<? extends Employee>> subtypes = reflections
	.getSubTypesOf(Employee.class);
    for ( final Class<? extends Employee> clazz : subtypes ) {
      System.out.println(clazz.getSimpleName());
    }
    System.out.println("Annotated Fields");
    @SuppressWarnings("unchecked")
    Set<Field> fields = getAllFields(HourlyEmployee.class,
	withAnnotation(Mappings.class));
    for ( final Field field : fields ) {
      System.out.println(field.getName());
      Annotation[] annotations = field.getDeclaredAnnotations();
      for ( final Annotation annotation : annotations ) {
	final String name = annotation.annotationType().getSimpleName();
	logger.debug("{}", name);
	System.out.println(name);
      }
    }
  }

  void listClasses(final Set<Class<?>> classes) {
    for ( final Class<?> clazz : classes ) {
      System.out.println(clazz.getSimpleName());
    }
  }
}

