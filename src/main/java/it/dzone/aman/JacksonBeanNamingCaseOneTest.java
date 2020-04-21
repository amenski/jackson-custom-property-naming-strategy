package it.dzone.aman;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@RunWith(JUnit4.class)
public class JacksonBeanNamingCaseOneTest {

    ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testBeanNames() throws IOException {
        BeanToTest bt = new BeanToTest();
        bt.setFieldOne("field one data.");
        bt.setFieldTwo("field two data.");

        String result = mapper.writeValueAsString(bt);
        String expected = "{\"field_one\":\"field one data.\",\"field_two\":\"field two data.\"}";
        
        Assert.assertTrue(result.equals(expected));
    }

    // Setting PropertyNamingStrategy per-class should include @JsonNaming
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class BeanToTest {
        private String fieldOne;
        private String fieldTwo;

        public BeanToTest() {
            //
        }

        public String getFieldOne() {
            return fieldOne;
        }

        public void setFieldOne(String fieldOne) {
            this.fieldOne = fieldOne;
        }

        public String getFieldTwo() {
            return fieldTwo;
        }

        public void setFieldTwo(String kFieldTwo) {
            this.fieldTwo = kFieldTwo;
        }

    }

}