package cz.monitora.elasticsearch;

import cz.monitora.elasticsearch.analyzer.croatian.CroatianStemFilterFactory;
import cz.monitora.elasticsearch.analyzer.czech.CzechStemFilterFactory;
import cz.monitora.elasticsearch.analyzer.lowercase.LowerCaseTokenFilterFactory;
import cz.monitora.elasticsearch.analyzer.slovak.SlovakStemFilterFactory;
import cz.monitora.elasticsearch.analyzer.slovenian.SlovenianStemFilterFactory;
import java.util.HashMap;
import java.util.Map;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

public class MonitoraESPlugin extends Plugin implements AnalysisPlugin {

  @Override
  public Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
    Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> extra = new HashMap<>();
    extra.put(
        "monitora_lowercase",
        AnalysisPlugin.requiresAnalysisSettings(LowerCaseTokenFilterFactory::new));
    extra.put("monitora_czech_stem", CzechStemFilterFactory::new);
    extra.put("monitora_slovak_stem", SlovakStemFilterFactory::new);
    extra.put("monitora_croatian_stem", CroatianStemFilterFactory::new);
    extra.put("monitora_slovenian_stem", SlovenianStemFilterFactory::new);
    return extra;
  }
}
