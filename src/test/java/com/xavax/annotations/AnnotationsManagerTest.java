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

import org.junit.Before;
import org.junit.Test;

import static com.xavax.util.Constants.EMPTY_ARGS;
import static com.xavax.util.Constants.EMPTY_STRING;
import static org.junit.Assert.*;

public class AnnotationsManagerTest {
  private final static String ANNOTATION_FIELD_FORMAT = "%s=%s";
  private final static String ANNOTATION_FORMAT = "%s@%s(";
  private final static String CLASS_FORMAT = "class %s {\n";
  private final static String FIELD_FORMAT = "  %s %s;\n";
  private final static String METHOD_FORMAT = "  %s %s();\n";
  
  private AnnotationsManager manager;

  @Before
  public void setup() {
    manager = new AnnotationsManager();
  }

  @Test
  public void test() {
    assertNotNull(manager);
    final AnnotationContext<Object> context =
	new AnnotationContext<>(manager).withIndent("    ").withBuilder();
    manager.add(HourlyEmployee.class);
    manager.add(SalaryEmployee.class);
    manager.scan(context);
    // manager.traverse(context);
    manager.traverse(context, (ctx, element) -> {
      final String msg = String.format("%s%s %s", context.indent(),
	  element.getType().name(), element.getName());
      System.out.println(msg);
      List<Annotation> annotations = element.getAnnotations();
      if ( annotations.size() > 0 ) {
	final StringBuilder builder = context.builder();
	builder.setLength(0);
	showAnnotations(annotations, builder, context.indent());
	System.out.println(builder.toString());
      }
    });
  }

  public static void showAnnotations(final Object object) {
    showAnnotations(object.getClass());
  }

  @SuppressWarnings("PMD.SystemPrintln")
  public static void showAnnotations(final Class<?> clazz) {
    final StringBuilder builder = new StringBuilder();
    showClassAnnotations(clazz, builder);
    builder.append(String.format(CLASS_FORMAT, clazz.getSimpleName()));
    showFields(clazz, builder);
    showMethods(clazz, builder);
    builder.append('}');
    System.out.println(builder.toString());
  }

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public static void showClassAnnotations(final Class<?> clazz, final StringBuilder builder) {
    final LinkedHashMap<String, List<Annotation>> map = new LinkedHashMap<>();
    loadClassAnnotations(map, clazz);
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

  static String formatValue(final Object value) {
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

  static String formatString(final String input) {
    return '"' + input + '"';
  }

  @SuppressWarnings("PMD.UseVarargs")
  static String formatArray(final Object[] array) {
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

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  static void loadClassAnnotations(final Map<String, List<Annotation>> map,
                                  final Class<?> clazz) {
    final Class<?> superClass = clazz.getSuperclass();
    if ( superClass != Object.class ) {
      loadClassAnnotations(map, superClass);
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

  static void showFields(final Class<?> clazz, final StringBuilder builder) {
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

  static void showMethods(final Class<?> clazz, final StringBuilder builder) {
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

  static void showAnnotations(final Annotation[] annotations,
                              final StringBuilder builder, final String prefix) {
    for ( final Annotation annotation : annotations ) {
      showAnnotation(annotation, builder, prefix);
    }
  }

  static void showAnnotations(final List<Annotation> annotations, final StringBuilder builder, final String prefix) {
    for ( final Annotation annotation : annotations ) {
      showAnnotation(annotation, builder, prefix);
    }
  }

  static void showAnnotation(final Annotation annotation, final StringBuilder builder, final String prefix) {
    Class<? extends Annotation> type = annotation.annotationType();
    builder.append(String.format(ANNOTATION_FORMAT, prefix, type.getSimpleName()));
    boolean first = true;
    for ( final Method method : type.getDeclaredMethods() ) {
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

  @SuppressWarnings("PMD.EmptyCatchBlock")
  static Object getMethodValue(final Annotation annotation, final Method method) {
    Object value = "";
    try {
      value = method.invoke(annotation, EMPTY_ARGS);
    }
    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      System.out.println("Unexpected exception: " + e.getClass().getSimpleName());
    }
    return value;
  }

}
