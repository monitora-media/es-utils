package cz.monitora.elasticsearch.analyzer.slovak;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;


public class SlovakStemFilterFactory extends AbstractTokenFilterFactory {
    private final boolean withASCIIFold;

    public SlovakStemFilterFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(name, settings);
        withASCIIFold = settings.getAsBoolean("with_asciifold", false);
	}

	@Override
	public TokenStream create(TokenStream input) {
		return new SlovakStemFilter(input, withASCIIFold);
	}
}
