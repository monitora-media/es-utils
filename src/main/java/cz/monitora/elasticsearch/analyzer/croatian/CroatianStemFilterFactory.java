package cz.monitora.elasticsearch.analyzer.croatian;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;

public class CroatianStemFilterFactory extends AbstractTokenFilterFactory {

  /** Creates a new CroatianStemFilterFactory */
  public CroatianStemFilterFactory(
      IndexSettings indexSettings, Environment env, String name, Settings settings) {
    super(name, settings);
  }

  @Override
  public TokenStream create(TokenStream input) {
    return new CroatianStemFilter(input);
  }
}
