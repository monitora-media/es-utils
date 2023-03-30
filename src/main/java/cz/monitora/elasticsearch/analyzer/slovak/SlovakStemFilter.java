package cz.monitora.elasticsearch.analyzer.slovak;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;

public final class SlovakStemFilter extends TokenFilter {
	private final SlovakStemmer stemmer = new SlovakStemmer();
	private final SlovakStemmerASCIIFold stemmerASCIIFold = new SlovakStemmerASCIIFold();
	private final CharTermAttribute termAttr = addAttribute(CharTermAttribute.class);
	private final KeywordAttribute keywordAttr = addAttribute(KeywordAttribute.class);
	private final boolean withASCIIFold;

	public SlovakStemFilter(TokenStream input, boolean withASCIIFold) {
		super(input);
		this.withASCIIFold = withASCIIFold;
	}

	@Override
	public boolean incrementToken() throws IOException {
		if (input.incrementToken()) {
			if (!keywordAttr.isKeyword()) {
				final int newlen = (withASCIIFold ? stemmerASCIIFold.stem(termAttr.buffer(), termAttr.length())
						: stemmer.stem(termAttr.buffer(), termAttr.length()));
				termAttr.setLength(newlen);
			}
			return true;
		} else {
			return false;
		}
	}
}
