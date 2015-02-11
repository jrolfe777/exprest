package exprest.test.core;

import java.io.File;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.Path;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

import com.google.common.collect.Maps;

import exprest.module.system.auth.AuthParam;


public class RestInterfaceGenerator {

   public static final String BASE_PACKAGE = "exprest";
   public static final String GENERATED_TEST_LOCATION = "src/test/java/test/";
   
   public static void main(String... args) {
      
      ((ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Reflections.class)).setLevel(Level.WARN);
      
      ConfigurationBuilder configBuilder = new ConfigurationBuilder();
      FilterBuilder filterBuilder = new FilterBuilder();
      configBuilder.addUrls(ClasspathHelper.forPackage(BASE_PACKAGE));
      filterBuilder.include(FilterBuilder.prefix(BASE_PACKAGE));
      
      configBuilder.filterInputsBy(filterBuilder).setScanners(new SubTypesScanner(), new TypeAnnotationsScanner());
      Reflections r = new Reflections(configBuilder);    
      
      Set<Class<?>> resourceClasses = r.getTypesAnnotatedWith(Path.class);
      
      resourceClasses.stream().forEach(e -> {
         generateInterface(e, true);
      });
   }
   
   public static void generateInterface(Class<?> clazz, Boolean overwrite) {      
      if (!clazz.isInterface()) {
         writeFile(clazz, generateInterfaceCode(clazz), overwrite);   
      }      
   }
   
   public static void writeFile(Class<?> clazz, String code, Boolean overwrite) {
      String packageDir = clazz.getPackage().getName().replace(".", "/");
      File f = new File(GENERATED_TEST_LOCATION + packageDir + "/" + clazz.getSimpleName() + ".java");
      if (!f.getParentFile().exists()) {
         f.getParentFile().mkdirs();
      }
      
      if (f.exists() && !overwrite) {
         System.out.println("Skipping writing of generated interface at " + f.getAbsolutePath() + 
                  ": a file already exists and overwrite is false.");
      } else {
         
         boolean writeFile = true;
         
         if (f.exists()) {
            try {
               String existingContent = new String(Files.readAllBytes(f.toPath())).trim();
               writeFile = !existingContent.trim().equals(code.trim());
            } catch (Throwable t) {
               System.out.println("Could not write " + f.getName() + ": " + t.getMessage());
            } 
         }
         
         if (writeFile) {
            try(PrintWriter fileOut = new PrintWriter(f);) {
               Files.write(f.toPath(), code.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
               fileOut.write(code);
            } catch (Throwable t) {
               System.out.println("Could not write " + f.getName() + ": " + t.getMessage());
            }
         }
      }
   }
   
   public static String generateInterfaceCode(Class<?> clazz) {      
      
      String resourceAnnos = getAnnotations(clazz.getAnnotations(), true, false);      
      StringBuilder methodBuilder = new StringBuilder();
      
      Arrays.asList(clazz.getMethods()).stream()
            .sorted((m1, m2) -> m1.toGenericString().compareTo(m2.toGenericString()))
            .filter(e ->  e.isAnnotationPresent(Path.class))
            .forEach(e -> {
               
         String methodAnnos = getAnnotations(e.getAnnotations(), true, true);
         methodBuilder.append(methodAnnos.toString());
         methodBuilder.append("\tpublic ").append(e.getReturnType().getName()).append(" ").append(e.getName()).append("(");
         
         methodBuilder.append(Arrays.asList(e.getParameters())
               .stream()
               .filter(p -> !p.isAnnotationPresent(AuthParam.class))
               .map(p -> getAnnotations(p.getAnnotations(), false, false) + p.getType().getName() + " " + p.getName())
               .collect(Collectors.joining(", ")));
         
         
         methodBuilder.append(");\n");
         methodBuilder.append("\n");

      });
      
     return MessageFormat.format(TEMPLATE, clazz.getPackage().getName(), resourceAnnos, clazz.getSimpleName(), methodBuilder.toString());
   }
   
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public static String getAnnotations(Annotation[] annotations, boolean lineBreak, boolean tab) {
      StringBuilder annos = new StringBuilder();
      Arrays.asList(annotations).stream().filter(e -> e.annotationType().toString().contains("javax.ws.rs")).forEach(e -> {
         if (tab) {
            annos.append("\t");
         }
         annos.append("@").append(e.annotationType().getName());       
         Arrays.asList(e.annotationType().getDeclaredMethods()).stream().forEach(m -> {
            try {
               Object o = m.invoke(e);
               AnnotationValueAdapater adapter = VALUE_ADAPTERS.get(o.getClass());
               String value = adapter != null ? adapter.adapt(o) : o.toString();               
               annos.append("(" + m.getName() + "=\"" + value + "\")");
               
            } catch (Throwable t) {
               t.printStackTrace();
            }            
         });         
         if (lineBreak) {
            annos.append("\n");
         } else {
            annos.append(" ");
         }
      });
      
      return annos.toString();
   }
   
   protected static final String TEMPLATE =
   "package test.{0};" +                  "\n" +
                                          "\n" +
   "{1}" +                                "\n" +
   "public interface {2} '{'" +           "\n" +
                                          "\n" +
      "{3}"                                    +                            
   "'}'";
   
   public static interface AnnotationValueAdapater<T> {
      public String adapt(T value);
   }
   
   public static Map<Class<?>,AnnotationValueAdapater<?>> VALUE_ADAPTERS = Maps.newHashMap();
    
   static {
      VALUE_ADAPTERS.put(String[].class, (String[] v) -> Arrays.asList(v).stream().collect(Collectors.joining(", ")));
   }
}