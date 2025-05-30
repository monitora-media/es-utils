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
package cz.monitora.elasticsearch.analyzer.croatian;

import java.io.IOException;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.SetKeywordMarkerFilter; // for javadoc
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;

/**
 * A {@link TokenFilter} that applies {@link CroatianStemmer} to stem Croatian words.
 *
 * <p>To prevent terms from being stemmed use an instance of {@link SetKeywordMarkerFilter} or a
 * custom {@link TokenFilter} that sets the {@link KeywordAttribute} before this {@link
 * TokenStream}.
 *
 * <p><b>NOTE</b>: Input is expected to be in lowercase, but with diacritical marks
 *
 * @see SetKeywordMarkerFilter
 */
public final class CroatianStemFilter extends TokenFilter {
  private final CroatianStemmer stemmer = new CroatianStemmer();
  private final CharTermAttribute termAttr = addAttribute(CharTermAttribute.class);
  private final KeywordAttribute keywordAttr = addAttribute(KeywordAttribute.class);

  public CroatianStemFilter(TokenStream input) {
    super(input);
  }

  @Override
  public boolean incrementToken() throws IOException {
    if (input.incrementToken()) {
      if (!keywordAttr.isKeyword()) {
        final int newlen = stemmer.stem(termAttr.buffer(), termAttr.length());
        termAttr.setLength(newlen);
      }
      return true;
    } else {
      return false;
    }
  }
}
