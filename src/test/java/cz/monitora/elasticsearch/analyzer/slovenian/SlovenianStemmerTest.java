package cz.monitora.elasticsearch.analyzer.slovenian;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SlovenianStemmerTest {

    @ParameterizedTest
    @CsvSource({
        // hiš - house
        "hiša, hiš",
        "hiše, hiš",
        "hiši, hiš",
        "hišo, hiš",
        "hišah, hiš",
        "hišama, hiš",
        // knjig - book
        "knjiga, knjig",
        "knjigami, knjig",
        // mest - city
        "mesto, mest",
        "mestu, mest",
        // človek - person/human
        "človek, človek",
        "človeka, človek",
        // ljud - people
        "ljudje, ljudj",
        "ljudi, ljud",
        "ljudmi, ljud",
        // dobr - good
        "dobri, dobr",
        "dobrega, dobr",
        "dobremu, dobr",
        // lepš - beautiful/nicer
        "lepši, lepš",
        // očet - father's (possessive)
        "očetov, očet",
        "očetova, očet",
        // mater - mother's (possessive)
        "materin, mater",
        "materina, mater",
        // delal - worked
        "delal, delal",
        "delala, delal",
        "delali, delal",
        // most - bridge
        "most, most",
        // tri - three
        "tri, tri",
        // pes - dog
        "pes, pes",
        // vse - all/everything
        "vse, vse",
        // gregor - name Gregor
        "gregorja, gregor",
        "gregorju, gregor",
        "gregorjem, gregor"
    })
    public void test_stem(String input, String expectedStem) {
        final SlovenianStemmer stemmer = new SlovenianStemmer();
        char[] ch = input.toCharArray();
        assertEquals(expectedStem, new String(Arrays.copyOfRange(ch, 0, stemmer.stem(ch, ch.length))));
    }
}
