package it.dzone.aman;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;


@RunWith(JUnit4.class)
public class JacksonBeanNamingCustomTest {

    ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();

        mapper.setPropertyNamingStrategy(new PropertyNamingStrategy() {

            @Override
            public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
                return field.getName();
            }
            
            @Override
            public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
                return convert(method, defaultName);
            }

            @Override
            public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
                return convert(method, defaultName);
            }
            /**
             * get the class from getter/setter methods
             * 
             * @param method
             * @param defaultName - jackson generated name
             * @return the correct property name
             */
            private String convert(AnnotatedMethod method, String defaultName) {
                Class<?> clazz = method.getDeclaringClass();
                List<Field> flds = getAllFields(clazz);
                for (Field fld : flds) {
                    // modify as per your needs
                    if (fld.getName().equalsIgnoreCase(defaultName)) {
                        return fld.getName();
                    }
                }
                return defaultName;
            }

            /**
             * get all fields from class
             * 
             * @param currentClass - should not be null
             * @return fields from the currentClass and its superclass
             */
            private List<Field> getAllFields(Class<?> currentClass) {
                List<Field> flds = new ArrayList<>();
                while (currentClass != null) {
                    Field[] fields = currentClass.getDeclaredFields();
                    Collections.addAll(flds, fields);
                    if (currentClass.getSuperclass() == null)
                        break;
                    currentClass = currentClass.getSuperclass();
                }
                return flds;
            }
        });
    }

    @Test
    public void testBeanNames() throws IOException {
        BeanToTest bt = new BeanToTest();
        bt.setFieldOne("field one data");
        bt.setKFieldTwo("field constant.");
        
        String result = mapper.writeValueAsString(bt);
        String expected = "{\"kFieldTwo\":\"field constant.\",\"fieldOne\":\"field one data\"}";
        
        Assert.assertTrue(result.equals(expected)); 
    }

    // note: no naming strategy
    private class BeanToTest {

        @JsonProperty("fieldOne")
        private String fieldOne;

        @JsonProperty("kFieldTwo")
        private String kFieldTwo;

        public String getFieldOne() {
            return fieldOne;
        }

        public void setFieldOne(String fieldOne) {
            this.fieldOne = fieldOne;
        }

        public String getKFieldTwo() {
            return kFieldTwo;
        }

        public void setKFieldTwo(String kFieldTwo) {
            this.kFieldTwo = kFieldTwo;
        }
    }
}