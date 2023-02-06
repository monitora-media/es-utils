package cz.monitora.elasticsearch.analyzer.slovak;

import java.util.Arrays;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class SlovakStemmerASCIIFoldTest {

    // grep 'ze$' /usr/share/hunspell/sk_SK.dic | head
    @ParameterizedTest
    @CsvSource({
                "vlastenci, vlastenk",
                "kurence, kurenk",
                "popici, popik",
                "naruce, naruk",

                "medzi, medh",
                "holomraze, holomrah",
                "slizi, slih",
                "odkialze, odkialh",
                
                "zmocte, zmock",
                "nezvedecti, nezvedeck",
                "nevimdalcti, nevimdalck",

                "kuste, kusk",
                "pocesti, pocesk",
                "kliesti, kliesk",
                })
    public void test_stem(String val, String exp) {
        final SlovakStemmerASCIIFold stemmer = new SlovakStemmerASCIIFold();
        char[] ch = val.toCharArray();
        assertEquals(new String(Arrays.copyOfRange(ch, 0, stemmer.stem(ch, ch.length))), exp);
    }
}
