package cz.monitora.elasticsearch.analyzer.croatian;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class CroatianStemmerTest {

  @ParameterizedTest
  @MethodSource("provideData")
  public void test_stem(String val, String exp) {
    final CroatianStemmer stemmer = new CroatianStemmer();
    char[] ch = val.toCharArray();
    assertEquals(exp, new String(Arrays.copyOfRange(ch, 0, stemmer.stem(ch, ch.length))));
  }

  private static Stream<Arguments> provideData() {
    /*
    	In [11]: for one in ElasticSynonym.objects.filter(language='hr'):
    	...:     for s in one.synonyms:
    	...:         print(f'Arguments.of("{s.lower()}", "{one.word.lower()}"),')
    	...:     print()
    */

    return Stream.of(
        Arguments.of("zlatko", "zlatk"),
        Arguments.of("zlatka", "zlatk"),
        Arguments.of("zlatku", "zlatk"),
        Arguments.of("zlatkom", "zlatk"),
        Arguments.of("hrkaća", "hrkać"),
        Arguments.of("hrkaću", "hrkać"),
        Arguments.of("hrkaćem", "hrkać"),
        Arguments.of("ivana", "ivan"),
        Arguments.of("ivanu", "ivan"),
        Arguments.of("ivanom", "ivan"),
        Arguments.of("bešlića", "bešlić"),
        Arguments.of("bešliću", "bešlić"),
        Arguments.of("bešlićem", "bešlić"),
        Arguments.of("zorana", "zoran"),
        Arguments.of("zoranu", "zoran"),
        Arguments.of("zoranom", "zoran"),
        Arguments.of("milanoviću", "milanović"),
        Arguments.of("milanovićem", "milanović"),
        Arguments.of("milanovića", "milanović"),
        Arguments.of("dobroslava", "dobroslav"),
        Arguments.of("dobroslavu", "dobroslav"),
        Arguments.of("dobroslavom", "dobroslav"),
        Arguments.of("dobar", "dobr"),
        Arguments.of("dobro", "dobr"),
        Arguments.of("dobra", "dobr"),
        Arguments.of("zao", "zlo"),
        Arguments.of("zla", "zlo"),
        Arguments.of("zlo", "zlo"));
  }
}
