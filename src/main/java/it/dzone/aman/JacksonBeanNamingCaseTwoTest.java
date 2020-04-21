package it.dzone.aman;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@RunWith(JUnit4.class)
public class JacksonBeanNamingCaseTwoTest {

    ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testBeanNames() throws IOException {
        BeanToTest bt = new BeanToTest();
        bt.setFieldOne("field one data.");
        bt.setKFieldTwo("field two data.");

        String expected = "{\"kfield_two\":\"field two data.\",\"field_one\":\"field one data.\"}";
        
        // will generate  {"kfield_two":"field two data.","fieldOne":"field one data.","kFieldTwo":"field two data."}
        // skipping PropertyNamingStrategy.SnakeCaseStrategy and adding extra class field
        String result = mapper.writeValueAsString(bt); 
        
        Assert.assertTrue(result.equals(expected));
    }

    // Setting PropertyNamingStrategy per-class should include @JsonNaming
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class BeanToTest {
        @JsonProperty("fieldOne")
        private String fieldOne;

        @JsonProperty("kFieldTwo")
        private String kFieldTwo;

        public BeanToTest() {
            //
        }

        public String getFieldOne() {

            return fieldOne;

        }

        public void setFieldOne(String fieldOne) {

            this.fieldOne = fieldOne;

        }

        public String getKFieldTwo() { //notice the K here

            return kFieldTwo;

        }

        public void setKFieldTwo(String kFieldTwo) {

            this.kFieldTwo = kFieldTwo;

        }

    }

}