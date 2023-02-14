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
 */
package cz.monitora.elasticsearch.analyzer.czech;


import static org.apache.lucene.analysis.util.StemmerUtil.*;

/**
 * Light Stemmer for Czech.
 * <p>
 * Implements the algorithm described in:
 * <i>
 * Indexing and stemming approaches for the Czech language
 * </i>
 * http://portal.acm.org/citation.cfm?id=1598600
 * </p>
 */
public class CzechStemmerASCIIFold {

  /**
   * Stem an input buffer of Czech text.
   *
   * @param s input buffer
   * @param len length of input buffer
   * @return length of input buffer after normalization
   *
   * <p><b>NOTE</b>: Input is expected to be in lowercase,
   * but with diacritical marks</p>
   */
  public int stem(char s[], int len) {
    len = removeCase(s, len);
    len = removePossessives(s, len);
    if (len > 0) {
      len = normalize(s, len);
    }
    return len;
  }

  private int removeCase(char s[], int len) {
    if (len > 7 && endsWith(s, len, "atech"))
      return len - 5;

    if (len > 6 &&
        (endsWith(s, len,"etem") ||
        endsWith(s, len,"etem") ||
        endsWith(s, len,"atum")))
      return len - 4;

    if (len > 5 &&
        (endsWith(s, len, "ech") ||
        endsWith(s, len, "ich") ||
        endsWith(s, len, "ich") ||
        endsWith(s, len, "eho") ||
        endsWith(s, len, "emi") ||
        endsWith(s, len, "emi") ||
        endsWith(s, len, "emu") ||
        endsWith(s, len, "ete") ||
        endsWith(s, len, "ete") ||
        endsWith(s, len, "eti") ||
        endsWith(s, len, "eti") ||
        endsWith(s, len, "iho") ||
        endsWith(s, len, "iho") ||
        endsWith(s, len, "imi") ||
        endsWith(s, len, "imu") ||
        endsWith(s, len, "imu") ||
        endsWith(s, len, "ach") ||
        endsWith(s, len, "ata") ||
        endsWith(s, len, "aty") ||
        endsWith(s, len, "ych") ||
        endsWith(s, len, "ama") ||
        endsWith(s, len, "ami") ||
        endsWith(s, len, "ove") ||
        endsWith(s, len, "ovi") ||
        endsWith(s, len, "ymi")))
      return len - 3;

    if (len > 4 &&
        (endsWith(s, len, "em") ||
        endsWith(s, len, "es") ||
        endsWith(s, len, "em") ||
        endsWith(s, len, "im") ||
        endsWith(s, len, "um") ||
        endsWith(s, len, "at") ||
        endsWith(s, len, "am") ||
        endsWith(s, len, "os") ||
        endsWith(s, len, "us") ||
        endsWith(s, len, "ym") ||
        endsWith(s, len, "mi") ||
        endsWith(s, len, "ou")))
      return len - 2;

		/*
				special case for "liga" local (6th case - "lize")
				which we want to be stemmed as "lig" as the rest of the cases
				if we move this further, we might have been demaging more words
				so we just do it here
		*/
    if (len == 4 && endsWith(s, len, "lize")) {
      s[len - 2] = 'g';
      return len - 1;
    }

    if (len > 3) {
      switch (s[len - 1]) {
        case 'a':
        case 'e':
        case 'i':
        case 'o':
        case 'u':
        //case 'u':
        case 'y':
        //case 'a':
        //case 'e':
        //case 'i':
        //case 'y':
        //case 'e':
          return len - 1;
      }
    }

    return len;
  }

  private int removePossessives(char s[], int len) {
    if (len > 5 &&
        (endsWith(s, len, "ov") ||
        endsWith(s, len, "in") ||
        endsWith(s, len, "uv")))
      return len - 2;

    return len;
  }

  private int normalize(char s[], int len) {
    if (endsWith(s, len, "ct")) { // ct -> ck
      s[len - 2] = 'c';
      s[len - 1] = 'k';
      return len;
    }

    if (endsWith(s, len, "st")) { // st -> sk
      s[len - 2] = 's';
      s[len - 1] = 'k';
      return len;
    }

    switch(s[len - 1]) {
      case 'c': // [cc] -> k
      //case 'c':
        s[len - 1] = 'k';
        return len;
      case 'z': // [zz] -> h
      //case 'z':
        s[len - 1] = 'h';
        return len;
    }

    if (len > 1 && s[len - 2] == 'e') {
      s[len - 2] = s[len - 1]; // e* > *
      return len - 1;
    }

    if (len > 2 && s[len - 2] == 'u') {
      s[len - 2] = 'o'; // *u* -> *o*
      return len;
    }

    return len;
  }
}
