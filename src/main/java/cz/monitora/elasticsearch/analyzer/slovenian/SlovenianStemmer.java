/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * *** Source Information ***
 *
 * This implementation is based on the Light Stemmer approach used for
 * Czech and Slovak languages, adapted for Slovenian morphology.
 *
 * The implementation follows the pattern from:
 * - Czech Light Stemmer (Apache Lucene)
 * - Slovak stemmer adaptation
 *
 * Slovenian-specific morphological information is based on:
 * - Popović M. and Willett P., "Processing of documents and queries in
 *   a Slovene language free text retrieval system", Literary and
 *   Linguistic Computing, 5: 182-190 (1990)
 * - General Slovenian morphology (6 cases, 3 genders, 3 numbers including dual)
 */

package cz.monitora.elasticsearch.analyzer.slovenian;

import static org.apache.lucene.analysis.util.StemmerUtil.endsWith;

public class SlovenianStemmer {
  /*
   * Stem an input buffer of Slovenian text.
   *
   * @param s input buffer
   *
   * @param len length of input buffer
   *
   * @return length of input buffer after normalization
   *
   * <p><b>NOTE</b>: Input is expected to be in lowercase,
   * but with diacritical marks (č, š, ž)</p>
   */
  public int stem(char[] s, int len) {
    len = removeCase(s, len);
    len = removePossessives(s, len);
    return len;
  }

  @SuppressWarnings({"NPathComplexity", "CyclomaticComplexity"})
  private int removeCase(char[] s, int len) {
    // Handle longer suffixes first (7+ characters)
    if (len >= 9) {
      if (endsWith(s, len, "osti")) {
        return len - 1;
      }
    }

    // 6+ character suffixes
    if (len > 7) {
      if (endsWith(s, len, "ovoma")
          || endsWith(s, len, "evoma")
          || endsWith(s, len, "ivoma")) {
        return len - 5;
      }
    }

    // 5+ character suffixes
    if (len > 6) {
      if (endsWith(s, len, "osti")
          || endsWith(s, len, "ovih")
          || endsWith(s, len, "evim")
          || endsWith(s, len, "ivim")) {
        return len - 4;
      }
    }

    // 4+ character suffixes
    if (len > 5) {
      if (endsWith(s, len, "oma")
          || endsWith(s, len, "ima")
          || endsWith(s, len, "ema")
          || endsWith(s, len, "ami")
          || endsWith(s, len, "imi")
          || endsWith(s, len, "emi")
          || endsWith(s, len, "ega")
          || endsWith(s, len, "ega")
          || endsWith(s, len, "emu")
          || endsWith(s, len, "ima")
          || endsWith(s, len, "oma")
          || endsWith(s, len, "ova")
          || endsWith(s, len, "eva")
          || endsWith(s, len, "ova")
          || endsWith(s, len, "ove")
          || endsWith(s, len, "eve")
          || endsWith(s, len, "ovi")
          || endsWith(s, len, "evi")
          || endsWith(s, len, "ijo")) {
        return palatalize(s, len - 2);
      }
      if (endsWith(s, len, "ega")
          || endsWith(s, len, "emu")
          || endsWith(s, len, "ega")
          || endsWith(s, len, "emu")
          || endsWith(s, len, "imi")
          || endsWith(s, len, "ich")
          || endsWith(s, len, "ega")
          || endsWith(s, len, "emu")
          || endsWith(s, len, "avi")
          || endsWith(s, len, "aje")
          || endsWith(s, len, "alo")
          || endsWith(s, len, "ali")
          || endsWith(s, len, "ale")) {
        return len - 3;
      }
      if (endsWith(s, len, "ice")) {
        return len - 1;
      }
    }

    // 3+ character suffixes
    if (len > 4) {
      if (endsWith(s, len, "om")
          || endsWith(s, len, "im")
          || endsWith(s, len, "em")) {
        return palatalize(s, len - 1);
      }
      if (endsWith(s, len, "ih")
          || endsWith(s, len, "ah")
          || endsWith(s, len, "om")
          || endsWith(s, len, "ov")
          || endsWith(s, len, "ev")
          || endsWith(s, len, "ih")
          || endsWith(s, len, "eh")
          || endsWith(s, len, "at")
          || endsWith(s, len, "il")
          || endsWith(s, len, "ov")
          || endsWith(s, len, "ev")
          || endsWith(s, len, "ij")
          || endsWith(s, len, "al")
          || endsWith(s, len, "el")) {
        return len - 2;
      }
    }

    // Single character vowel endings
    if (len > 3) {
      switch (s[len - 1]) {
        case 'e':
        case 'i':
          return palatalize(s, len);
        case 'a':
        case 'o':
        case 'u':
          return len - 1;
        default:
      }
    }

    return len;
  }

  private int removePossessives(char[] s, int len) {
    if (len > 5) {
      if (endsWith(s, len, "ov")) {
        return len - 2;
      }
      if (endsWith(s, len, "in")) {
        return palatalize(s, len - 1);
      }
    }

    return len;
  }

  @SuppressWarnings({"CyclomaticComplexity"})
  private int palatalize(char[] s, int len) {
    if (len <= 3) {
      return len - 1;
    }

    // Handle č, ž palatalization (Slovenian specific)
    // c/č + i/e → k (e.g., dec-e → dek)
    if (endsWith(s, len, "ci")
        || endsWith(s, len, "ce")
        || endsWith(s, len, "či")
        || endsWith(s, len, "če")) {
      s[len - 2] = 'k';
    }
    // z/ž + i/e → g (e.g., noz-i → nog)
    else if (endsWith(s, len, "zi")
        || endsWith(s, len, "ze")
        || endsWith(s, len, "ži")
        || endsWith(s, len, "že")) {
      s[len - 2] = 'g';
    }
    // Note: š does not palatalize in Slovenian

    return len - 1;
  }
}
