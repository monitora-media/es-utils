package cz.monitora.elasticsearch.analyzer.czech;

import java.util.Arrays;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class CzechStemmerTest {

    // grep 'ze$' /usr/share/hunspell/cs_CZ.dic | head
    @ParameterizedTest
    @CsvSource({
            "stařenk, stařenk",
            "růžové, růh",

            // from our synonyms
            "liga, lig",
            "lize, lig",
    })
    public void test_stem(String val, String exp) {
        final CzechStemmer stemmer = new CzechStemmer();
        char[] ch = val.toCharArray();
        assertEquals(exp, new String(Arrays.copyOfRange(ch, 0, stemmer.stem(ch, ch.length))));
    }
}
