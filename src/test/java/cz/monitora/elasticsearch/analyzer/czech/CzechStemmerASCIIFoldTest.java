package cz.monitora.elasticsearch.analyzer.czech;

import java.util.Arrays;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class CzechStemmerASCIIFoldTest {

    // grep 'ze$' /usr/share/hunspell/cs_CZ.dic | head
    @ParameterizedTest
    @CsvSource({
            "starenka, starenk",
            "ruzove, ruh",

            // from our synonyms
            "liga, lig",
            "lize, lig",
    })
    public void test_stem(String val, String exp) {
        final CzechStemmerASCIIFold stemmer = new CzechStemmerASCIIFold();
        char[] ch = val.toCharArray();
        assertEquals(exp, new String(Arrays.copyOfRange(ch, 0, stemmer.stem(ch, ch.length))));
    }
}
