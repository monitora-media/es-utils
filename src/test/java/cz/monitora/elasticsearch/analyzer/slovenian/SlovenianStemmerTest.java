package cz.monitora.elasticsearch.analyzer.slovenian;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SlovenianStemmerTest {

  @ParameterizedTest
  @CsvSource({
    // Common Slovenian nouns - various cases
    "hiša, hiš", // house (nominative)
    "hiše, hiš", // house (genitive)
    "hiši, hiš", // house (dative)
    "hišo, hiš", // house (accusative)
    "hišah, hiš", // house (locative plural)
    "hišami, hiš", // house (instrumental plural)

    // Masculine nouns
    "človek, človek", // person (nominative)
    "človeka, človek", // person (genitive/accusative)
    "človeku, človek", // person (dative)
    "ljudje, ljudj", // people
    "ljudi, ljud", // people (genitive)

    // Feminine nouns
    "knjiga, knjig", // book
    "knjige, knjig", // book (genitive/plural)
    "knjigi, knjig", // book (dative)
    "knjigo, knjig", // book (accusative)
    "knjigah, knjig", // book (locative plural)

    // Neuter nouns
    "mesto, mest", // city
    "mesta, mest", // city (genitive/plural)
    "mestu, mest", // city (dative)
    "mestih, mest", // city (locative plural)

    // Dual forms (distinctive Slovenian feature)
    "hišama, hišam", // two houses (instrumental dual)
    "knjigama, knjigam", // two books (instrumental dual)

    // Adjectives
    "dobri, dobr", // good
    "dobre, dobr", // good
    "dobrega, dobr", // good (genitive)
    "dobremu, dobr", // good (dative)

    // Possessives
    "očetov, očet", // father's
    "materina, mater", // mother's - 'in' removed, 'a' removed

    // Verbs (past tense)
    "delal, del", // worked (masculine)
    "delala, delal", // worked (feminine) - 'a' removed
    "delali, del", // worked (plural)
    "delale, del", // worked (feminine plural)

    // Common words
    "večji, večj", // bigger - comparative adjective
    "manjši, manjš", // smaller - comparative adjective
    "lepši, lepš", // more beautiful - comparative adjective
    "študent, študent", // student
    "študenta, študent", // student (genitive)
    "študentov, študent", // students (genitive plural)
  })
  public void test_stem(String val, String exp) {
    final SlovenianStemmer stemmer = new SlovenianStemmer();
    char[] ch = val.toCharArray();
    assertEquals(exp, new String(Arrays.copyOfRange(ch, 0, stemmer.stem(ch, ch.length))));
  }
}
