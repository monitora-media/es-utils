// source:
// https://gerrit.wikimedia.org/r/#/c/search/extra/+/423043/3/src/main/java/org/wikimedia/search/extra/analysis/filters/SlovakStemmer.java
/*
 * The WMF licenses this file to you under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * *** Source Information ***
 *
 * This code combines implementation details and linguistic information
 * from two main sources.
 *
 * ** Light Stemmer for Czech **
 *
 * The implementation is based on the lucene-solr "Light Stemmer for
 * Czech", which is licensed from ASF under the Apache License, Version
 * 2.0. Source code is available here:
 * https://github.com/apache/lucene-solr/blob/master/lucene/analysis/common/src/java/org/apache/lucene/analysis/cz/CzechStemmer.java
 *
 * ** stemm-sk **
 *
 * The Slovak-specific suffix information is adapted from stemm-sk, which
 * is Copyright (c) 2015 Marek Suppa and licensed under the MIT
 * License (included below, as required). Source code is available here:
 * https://github.com/mrshu/stemm-sk/
 *
 * | Slovak-specific suffix information Copyright (c) 2015 Marek Suppa
 * |
 * | Permission is hereby granted, free of charge, to any
 * | person obtaining a copy of this software and associated
 * | documentation files (the "Software"), to deal in the
 * | Software without restriction, including without limitation
 * | the rights to use, copy, modify, merge, publish,
 * | distribute, sublicense, and/or sell copies of the
 * | Software, and to permit persons to whom the Software is
 * | furnished to do so, subject to the following conditions:
 * |
 * | The above copyright notice and this permission notice
 * | shall be included in all copies or substantial portions of
 * | the Software.
 *
 * ** Additional Sources **
 *
 * The stemm-sk source code includes its own additional sources. The
 * Light Stemmer for Czech source code references the paper "Indexing
 * and stemming approaches for the Czech language" by Dolamic and Savoy
 * (2009), which is also the ultimate source of the main Czech
 * implementation that stemm-sk is based on. The paper is available
 * here: http://portal.acm.org/citation.cfm?id=1598600 .
 *
 * ** Additional Changes **
 *
 * - Updates to conform to findbugs/spotbugs/checkstyle errors.
 *
 * - Added prefix stripping based on review of Slovak morphology and
 * comparison to Polish.
 */

package cz.monitora.elasticsearch.analyzer.slovak;

import static org.apache.lucene.analysis.util.StemmerUtil.deleteN;
import static org.apache.lucene.analysis.util.StemmerUtil.endsWith;
import static org.apache.lucene.analysis.util.StemmerUtil.startsWith;

public class SlovakStemmerASCIIFold {
  /*
   * Stem an input buffer of Slovak text.
   *
   * @param s input buffer
   *
   * @param len length of input buffer
   *
   * @return length of input buffer after normalization
   *
   * <p><b>NOTE</b>: Input is expected to be in lowercase,
   * but with diacritical marks</p>
   */
  public int stem(char[] s, int len) {
    len = removeCase(s, len);
    len = removePossessives(s, len);
    // len = removePrefixes(s, len);
    return len;
  }

  private int removePrefixes(char[] s, int len) {
    if (len > 5 && startsWith(s, len, "naj")) {
      return deleteN(s, 0, len, 3);
    }
    return len;
  }

  @SuppressWarnings({"NPathComplexity", "CyclomaticComplexity"})
  private int removeCase(char[] s, int len) {
    if (len >= 9) {
      if (endsWith(s, len, "osti")) {
        return len - 1;
      }
      if (endsWith(s, len, "ostou")) {
        return len - 2;
      }
      if (endsWith(s, len, "ostami")) {
        return len - 3;
      }
      if (endsWith(s, len, "ostiach")) {
        return len - 4;
      }
    }

    if (len > 7 && endsWith(s, len, "atoch")) {
      return len - 5;
    }

    if (len > 6 && endsWith(s, len, "atom")) {
      return palatalize(s, len - 3);
    }

    if (len > 5) {
      if (endsWith(s, len, "och")
          || endsWith(s, len, "ich")
          || endsWith(s, len, "ich")
          || endsWith(s, len, "eho")
          || endsWith(s, len, "ami")
          || endsWith(s, len, "emi")
          || endsWith(s, len, "emu")
          || endsWith(s, len, "ete")
          || endsWith(s, len, "eti")
          || endsWith(s, len, "iho")
          || endsWith(s, len, "iho")
          || endsWith(s, len, "imi")
          || endsWith(s, len, "imu")
          || endsWith(s, len, "ata")) {
        return palatalize(s, len - 2);
      }
      if (endsWith(s, len, "ach")
          || endsWith(s, len, "ata")
          || endsWith(s, len, "aty")
          || endsWith(s, len, "ych")
          || endsWith(s, len, "ami")
          || endsWith(s, len, "ove")
          || endsWith(s, len, "ovi")
          || endsWith(s, len, "ymi")) {
        return len - 3;
      }
      if (endsWith(s, len, "ice")) {
        return len - 1;
      }
      if (endsWith(s, len, "ciam")) {
        return len - 3;
      }
    }

    if (len > 4) {
      if (endsWith(s, len, "om")) {
        return palatalize(s, len - 1);
      }
      if (endsWith(s, len, "es") || endsWith(s, len, "em") || endsWith(s, len, "im")) {
        return palatalize(s, len - 2);
      }
      if (endsWith(s, len, "um")
          || endsWith(s, len, "at")
          || endsWith(s, len, "am")
          || endsWith(s, len, "os")
          || endsWith(s, len, "us")
          || endsWith(s, len, "ym")
          || endsWith(s, len, "mi")
          || endsWith(s, len, "ou")
          || endsWith(s, len, "ej")) {
        return len - 2;
      }
    }

    if (len > 3) {
      switch (s[len - 1]) {
        case 'e':
        case 'i':
          // case 'i':
          return palatalize(s, len);
        case 'u':
        case 'y':
        case 'a':
        case 'o':
          // case 'a':
          // case 'e':
          // case 'y':
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
    assert len > 3;

    if (endsWith(s, len, "ci")
        || endsWith(s, len, "ce")
        || endsWith(s, len, "ci")
        || endsWith(s, len, "ce")) { // [cc][ie] -> k
      s[len - 2] = 'k';
    } else if (endsWith(s, len, "zi")
        || endsWith(s, len, "ze")
        || endsWith(s, len, "zi")
        || endsWith(s, len, "ze")) { // [zz][ie] -> h
      s[len - 2] = 'h';
    } else if (endsWith(s, len, "cte")
        || endsWith(s, len, "cti")
        || endsWith(s, len, "cti")) { // ct[eii] -> ck
      s[len - 3] = 'c';
      s[len - 2] = 'k';
    } else if (endsWith(s, len, "ste")
        || endsWith(s, len, "sti")
        || endsWith(s, len, "sti")) { // st[eii] -> sk
      s[len - 3] = 's';
      s[len - 2] = 'k';
    }

    return len - 1;
  }
}
