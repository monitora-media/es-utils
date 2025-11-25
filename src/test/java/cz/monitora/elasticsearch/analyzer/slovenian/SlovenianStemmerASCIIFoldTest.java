package cz.monitora.elasticsearch.analyzer.slovenian;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SlovenianStemmerASCIIFoldTest {

  @ParameterizedTest
  @CsvSource({
    // ASCII-folded versions (without diacritical marks)
    // Common Slovenian nouns - various cases
    "hisa, his", // hiša -> house (nominative)
    "hise, his", // hiše -> house (genitive)
    "hisi, his", // hiši -> house (dative)
    "hiso, his", // hišo -> house (accusative)
    "hisah, his", // hišah -> house (locative plural)
    "hisami, his", // hišami -> house (instrumental plural)

    // Masculine nouns
    "clovek, clovek", // človek -> person (nominative)
    "cloveka, clovek", // človeka -> person (genitive/accusative)
    "cloveku, clovek", // človeku -> person (dative)
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
    "hisama, hisam", // hišama -> two houses (instrumental dual)
    "knjigama, knjigam", // two books (instrumental dual)

    // Adjectives
    "dobri, dobr", // good
    "dobre, dobr", // good
    "dobrega, dobr", // good (genitive)
    "dobremu, dobr", // good (dative)

    // Possessives
    "ocetov, ocet", // očetov -> father's
    "materina, mater", // mother's - 'in' removed, 'a' removed

    // Verbs (past tense)
    "delal, del", // worked (masculine)
    "delala, delal", // worked (feminine) - 'a' removed
    "delali, del", // worked (plural)
    "delale, del", // worked (feminine plural)

    // Common words
    "vecji, vecj", // večji -> bigger - comparative adjective
    "manjsi, manjs", // manjši -> smaller - comparative adjective
    "lepsi, leps", // lepši -> more beautiful - comparative adjective
    "student, student", // student
    "studenta, student", // student (genitive)
    "studentov, student", // students (genitive plural)
  })
  public void test_stem(String val, String exp) {
    final SlovenianStemmerASCIIFold stemmer = new SlovenianStemmerASCIIFold();
    char[] ch = val.toCharArray();
    assertEquals(exp, new String(Arrays.copyOfRange(ch, 0, stemmer.stem(ch, ch.length))));
  }
}
