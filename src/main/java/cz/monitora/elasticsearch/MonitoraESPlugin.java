package cz.monitora.elasticsearch;

import cz.monitora.elasticsearch.analyzer.czech.CzechStemFilterFactory;
import cz.monitora.elasticsearch.analyzer.lowercase.LowerCaseTokenFilterFactory;
import cz.monitora.elasticsearch.analyzer.slovak.SlovakStemFilterFactory;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.HashMap;
import java.util.Map;

public class MonitoraESPlugin extends Plugin implements AnalysisPlugin {

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
        Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> extra = new HashMap<>();
        extra.put("monitora_lowercase", AnalysisPlugin.requiresAnalysisSettings(LowerCaseTokenFilterFactory::new));
        extra.put("czech_stem", CzechStemFilterFactory::new);
        extra.put("slovak_stem", SlovakStemFilterFactory::new);
        return extra;
    }
}
