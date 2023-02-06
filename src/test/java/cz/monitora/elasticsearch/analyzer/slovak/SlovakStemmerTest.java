package cz.monitora.elasticsearch.analyzer.slovak;

import java.util.Arrays;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class SlovakStemmerTest {

    // grep 'ze$' /usr/share/hunspell/sk_SK.dic | head
    @ParameterizedTest
    @CsvSource({
            "vlastenci, vlastenk",
            "kurence, kurenk",
            "popiči, popik",
            "náruče, náruk",

            "medzi, medh",
            "holomraze, holomrah",
            "slíži, slíh",
            "odkiaľže, odkiaľh",

            "zmočte, zmock",
            "nezvedečti, nezvedeck",
            "nevimdalčtí, nevimdalck",

            "kušte, kusk",
            "počešti, počesk",
            "klieští, kliesk",
    })
    public void test_stem(String val, String exp) {
        final SlovakStemmer stemmer = new SlovakStemmer();
        char[] ch = val.toCharArray();
        assertEquals(exp, new String(Arrays.copyOfRange(ch, 0, stemmer.stem(ch, ch.length))));
    }
}
