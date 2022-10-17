package top.kwseeker.developkit.excelutil;

import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class Excel2CollectionConverterTest {

    @Test
    public void testConvert() {
        Excel2CollectionConverter converter = new Excel2CollectionConverter();
        InputStream fis = this.getClass().getResourceAsStream("/test.xlsx");
        List<Person> persons = converter.convert(fis, Person.class);
    }
}