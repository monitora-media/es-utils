package cz.monitora.elasticsearch.analyzer.czech;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CzechStemmerASCIIFoldTest {

  // grep 'ze$' /usr/share/hunspell/cs_CZ.dic | head
  @ParameterizedTest
  @CsvSource({
    "starenka, starenk",
    "ruzove, ruh",

    // from our synonyms
    "liga, lig",
    "lize, lig",

    // iva
    "iva, iva",
    "ivy, iva",
    "ive, iva",
    "ivou, iva",
    "invektiva, invektiv",
    "invektivy, invektiv",
    "invektive, invektiv",
    "invektivou, invektiv",
  })
  public void test_stem(String val, String exp) {
    final CzechStemmerASCIIFold stemmer = new CzechStemmerASCIIFold();
    char[] ch = val.toCharArray();
    assertEquals(exp, new String(Arrays.copyOfRange(ch, 0, stemmer.stem(ch, ch.length))));
  }
}
