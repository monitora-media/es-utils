/* MIT License
 *
 * Copyright (c) 2025
 * Port of a Snowball-style Slovenian stemmer (conservative).
 * Based on community implementations and Snowball design principles.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software ...
 */

package cz.monitora.elasticsearch.analyzer.slovenian;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Arrays;
import static org.apache.lucene.analysis.util.StemmerUtil.endsWith;
import org.apache.lucene.analysis.CharArraySet;


public final class SlovenianStemmer {
  private static final String[] suffixes2 = {
    "ih","im","om","am","em","ov","ev","in","mi","eh","ah"
  };
  private static final String[] suffixes3 = {
    "ega","emu","ima","imi","ami","oma","ama","ove","ova","ovs","ina","ino","ini"
  };
  private static final String[] suffix3_remove2 = {
    "rja", "rje", "rju", "rjo"
  };
  private static final String[] suffix4_remove3 = {
    "rjem"
  };
  //private static final CharArraySet dont_stem = new CharArraySet(
  //  Arrays.asList("skupina", "telekom"),
  //  false
  //);

  public int stem(char[] s, int len) {
    //if (dont_stem.contains(s, 0, len)) return len;

    int r1 = calculateR1(s, len);
    if (r1 >= len) return len;

    if (len - 4 >= r1) {
      for (String suf : suffix4_remove3) {
        if (endsWith(s, len, suf)) {
          return len - suf.length() + 1;
        }
      }
    }

    if (len - 3 >= r1) {
      for (String suf : suffixes3) {
        if (endsWith(s, len, suf)) {
          return len - suf.length();
        }
      }
      for (String suf : suffix3_remove2) {
        if (endsWith(s, len, suf)) {
          return len - suf.length() + 1;
        }
      }
    }

    if (len - 2 >= r1) {
      for (String suf : suffixes2) {
        if (endsWith(s, len, suf)) {
          // protect very short stems
          if (len - suf.length() >= 2) return len - suf.length();
        }
      }
    }

    if (len - 1 >= r1) {
      char last = s[len - 1];
      if (isVowel(last)) {
        if (len - 1 >= 2) return len - 1;
      }
    }

    return len;
  }

  // R1: first region after the first non-vowel following a vowel.
  private int calculateR1(char[] s, int len) {
    boolean foundVowel = false;
    for (int i = 0; i < len; i++) {
      if (isVowel(s[i])) {
        foundVowel = true;
      } else if (foundVowel) {
        return i + 1;
      }
    }
    return len;
  }

  // Conservative vowel test for Slovenian. Note: syllabic 'r' isn't treated as vowel here.
  private boolean isVowel(char c) {
    // includes Slovene-specific vowels (č/š/ž are consonants, preserved)
    return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
  }
}
