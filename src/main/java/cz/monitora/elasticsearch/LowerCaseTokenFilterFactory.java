package cz.monitora.elasticsearch;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;

public class LowerCaseTokenFilterFactory extends AbstractTokenFilterFactory {
    private final boolean preserveOriginal;

    public LowerCaseTokenFilterFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(name, settings);

        preserveOriginal = settings.getAsBoolean("preserve_original", true);
    }

    @Override
    public TokenStream create(TokenStream in) {
        return new LowerCaseFilter(in, preserveOriginal);
    }
}
