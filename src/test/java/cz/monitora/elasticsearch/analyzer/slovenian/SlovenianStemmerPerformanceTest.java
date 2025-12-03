package cz.monitora.elasticsearch.analyzer.slovenian;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Performance analysis test for Slovenian stemmer.
 * This test evaluates the stemmer's precision across comprehensive datasets
 * and provides detailed failure reports.
 *
 * Evaluation metric: All inflected forms within a group should stem to the same string.
 * The stemmed result does not have to match the base form (Map key), it just needs to be
 * consistent across all forms in the Set. Success = all forms produce identical stem.
 *
 * Run manually (not part of regular test suite) to analyze stemmer quality.
 * Use: ./gradlew performanceTest
 */
public class SlovenianStemmerPerformanceTest {
    static class DatasetResult {
        String name;
        int totalEntries;
        int successfulGroupings;
        List<FailureReport> failures;

        DatasetResult(String name, int totalEntries, int successfulGroupings, List<FailureReport> failures) {
            this.name = name;
            this.totalEntries = totalEntries;
            this.successfulGroupings = successfulGroupings;
            this.failures = failures;
        }

        double getPrecision() {
            return totalEntries > 0 ? (double) successfulGroupings / totalEntries * 100 : 0;
        }
    }

    static class FailureReport {
        String expectedBase;
        Set<String> uniqueStems;
        Map<String, String> formToStem;

        FailureReport(String expectedBase, Set<String> uniqueStems, Map<String, String> formToStem) {
            this.expectedBase = expectedBase;
            this.uniqueStems = uniqueStems;
            this.formToStem = formToStem;
        }
    }

    public static final Map<String, Set<String>> slovenian_keywords_dataset = Map.ofEntries(
        // --- Companies & Brands ---
        Map.entry("nomago", Set.of("nomago", "nomaga", "nomagu", "nomagom")),
        Map.entry("nomago bikes", Set.of("nomago bikes", "nomaga bikes", "nomagu bikes")),
        Map.entry("Nomago travel", Set.of("nomago travel", "nomaga travel", "nomagu travel")),
        Map.entry("nova ljubljanska banka", Set.of("nove ljubljanske banke", "novi ljubljanski banki", "novo ljubljansko banko")),
        Map.entry("halcom ca", Set.of("halcom ca", "halcoma ca", "halcomu ca", "halcomom ca")),
        Map.entry("telekom slovenija", Set.of("telekom slovenije", "telekoma slovenije", "telekomu slovenije", "telekomom slovenije")),
        Map.entry("lidl slovenija", Set.of("lidl slovenija", "lidla slovenija", "lidlu slovenija", "lidlom slovenija")),
        Map.entry("skupina triglav", Set.of("skupine triglav", "skupini triglav", "skupino triglav")),
        Map.entry("triglav lab", Set.of("triglav lab", "triglav laba", "triglav labu", "triglav labom")),
        Map.entry("skupina gen", Set.of("skupine gen", "skupini gen", "skupino gen")),
        Map.entry("agencija taktik", Set.of("agencija taktik", "agencije taktik", "agenciji taktik", "agencijo taktik")),

        // --- Personal Names (Male) ---
        Map.entry("Miha tavčar", Set.of("miha tavčar", "mihe tavčarja", "mihi tavčarju", "miho tavčarjem")),
        Map.entry("Blaž Brodnjak", Set.of("blaž brodnjak", "blaža brodnjaka", "blažu brodnjaku", "blažem brodnjakom")),
        Map.entry("Archibald Kremser", Set.of("archibald kremser", "archibalda kremserja", "archibaldu kremserju", "archibaldom kremserjem")),
        Map.entry("Andreas Burkhardt", Set.of("andreas burkhardt", "andreasa burkhardta", "andreasu burkhardtu", "andreasom burkhardtom")),
        Map.entry("Andrej Lasič", Set.of("andrej lasič", "andreja lasiča", "andreju lasiču", "andrejem lasičem")),
        Map.entry("Antonio Argir", Set.of("antonio argir", "antonia argirja", "antoniu argirju", "antoniem argirjem")),
        Map.entry("Reinhard Höll", Set.of("reinhard höll", "reinharda hölla", "reinhardu höllu", "reinhardom höllom")),
        Map.entry("gregor pelhan", Set.of("gregor pelhan", "gregorja pelhana", "gregorju pelhanu", "gregorjem pelhanom")),
        Map.entry("Aleksandar Spremić", Set.of("aleksandar spremić", "aleksandra spremića", "aleksandru spremiću", "aleksandrom spremićem")),
        Map.entry("tadej pogačar", Set.of("tadej pogačar", "tadeja pogačarja", "tadeju pogačarju", "tadejem pogačarjem")),
        Map.entry("luka dončič", Set.of("luka dončič", "luke dončiča", "luki dončiču", "luko dončičem")),

        // --- Personal Names (Female) ---
        Map.entry("Hedvika Usenik", Set.of("hedvika usenik", "hedvike usenik", "hedviki usenik", "hedviko usenik")),
        Map.entry("andreja pongračič", Set.of("andreja pongračič", "andreje pongračič", "andreji pongračič", "andrejo pongračič")),
        Map.entry("saška rihtaršič", Set.of("saška rihtaršič", "saške rihtaršič", "saški rihtaršič", "saško rihtaršič")),
        Map.entry("Irena Ilešič Čujovič", Set.of("irena ilešič čujovič", "irene ilešič čujovič", "ireni ilešič čujovič", "ireno ilešič čujovič")),

        // --- Institutions & Political ---
        Map.entry("socialni demokrati", Set.of("socialni demokrati", "socialnih demokratov", "socialnim demokratom", "socialne demokrate", "socialnimi demokrati")),
        Map.entry("nova slovenija", Set.of("nova slovenija", "nove slovenije", "novi sloveniji", "novo slovenijo")),
        Map.entry("slovenska demokratska stranka", Set.of("slovenska demokratska stranka", "slovenske demokratske stranke", "slovenski demokratski stranki", "slovensko demokratsko stranko")),
        Map.entry("mestna občina kranj", Set.of("mestna občina kranj", "mestne občine kranj", "mestni občini kranj", "mestno občino kranj")),
        Map.entry("mok", Set.of("mok", "moka", "moku", "mokom")),
        Map.entry("zavod za zdravstveno zavarovanje", Set.of("zavod za zdravstveno zavarovanje", "zavoda za zdravstveno zavarovanje", "zavodu za zdravstveno zavarovanje", "zavodom za zdravstveno zavarovanje")),
        Map.entry("Nuklearna elektrarna Krško", Set.of("nuklearna elektrarna krško", "nuklearne elektrarne krško", "nuklearni elektrarni krško", "nuklearno elektrarno krško")),
        Map.entry("Savske elektrarne", Set.of("savske elektrarne", "savskih elektrarn", "savskim elektrarnam", "savskimi elektrarnami")),
        Map.entry("Termoelektrarna Brestanica", Set.of("termoelektrarna brestanica", "termoelektrarne brestanica", "termoelektrarni brestanica", "termoelektrarno brestanica")),
        Map.entry("mestno gledališče ljubljansko", Set.of("mestno gledališče ljubljansko", "mestnega gledališča ljubljanskega", "mestnemu gledališču ljubljanskemu", "mestnim gledališčem ljubljanskim")),

        // --- General Services & Concepts ---
        Map.entry("avtobusni prevozi", Set.of("avtobusni prevozi", "avtobusnih prevozov", "avtobusnim prevozom", "avtobusne prevoze", "avtobusnimi prevozi")),
        Map.entry("avtobusne vozovnice", Set.of("avtobusne vozovnice", "avtobusnih vozovnic", "avtobusnim vozovnicam", "avtobusnimi vozovnicami")),
        Map.entry("osebni kredit", Set.of("osebni kredit", "osebnega kredita", "osebnemu kreditu", "osebni krediti", "osebnih kreditov", "osebnim kreditom")),
        Map.entry("stanovanjski kredit", Set.of("stanovanjski kredit", "stanovanjskega kredita", "stanovanjskemu kreditu", "stanovanjski krediti", "stanovanjskim kreditom")),
        Map.entry("kvalificirano digitalno potrdilo", Set.of("kvalificirano digitalno potrdilo", "kvalificiranega digitalnega potrdila", "kvalificiranemu digitalnemu potrdilu", "kvalificiranim digitalnim potrdilom", "kvalificirana digitalna potrdila")),
        Map.entry("zdravstveno zavarovanje", Set.of("zdravstveno zavarovanje", "zdravstvenega zavarovanja", "zdravstvenemu zavarovanju", "zdravstvenim zavarovanjem")),
        Map.entry("hemofilia", Set.of("hemofilia", "hemofilije", "hemofiliji", "hemofilijo")),
        Map.entry("Semaglutid", Set.of("semaglutid", "semaglutida", "semaglutidu", "semaglutidom")),
        Map.entry("piščancem prijazna reja", Set.of("piščancem prijazna reja", "piščancem prijazne reje", "piščancem prijazni reji", "piščancem prijazno rejo")),
        Map.entry("italijanska moda", Set.of("italijanska moda", "italijanske mode", "italijanski modi", "italijansko modo")),
        Map.entry("Mesec italijanske mode", Set.of("mesec italijanske mode", "meseca italijanske mode", "mesecu italijanske mode", "mesecem italijanske mode")),

        // --- Phrases & Local ---
        Map.entry("podkast iz kranja", Set.of("podkast iz kranja", "podkasta iz kranja", "podkastu iz kranja", "podkastom iz kranja", "podkasti iz kranja")),
        Map.entry("kranjske novice", Set.of("kranjske novice", "kranjskih novic", "kranjskim novicam", "kranjskimi novicami")),
        Map.entry("nori na poli", Set.of("nori na poli", "norih na poli", "norim na poli", "nore na poli", "norimi na poli")));

    public static final Map<String, Set<String>> slovenian_common_words_dataset = Map.ofEntries(
        // -- NOUNS --
        Map.entry("hiš", Set.of("hiša", "hiše", "hiši", "hišo", "hišah", "hišama")),
        Map.entry("knjig", Set.of("knjiga", "knjigami")),
        Map.entry("mest", Set.of("mesto", "mestu")),

        // -- MASCULINE & IRREGULAR --
        Map.entry("človek", Set.of("človek", "človeka")),
        Map.entry("ljud", Set.of("ljudje", "ljudi", "ljudmi")),

        // -- ADJECTIVES --
        Map.entry("dobr", Set.of("dobri", "dobrega", "dobremu")),
        Map.entry("lepš", Set.of("lepši")),

        // -- POSSESSIVES --
        Map.entry("očet", Set.of("očetov", "očetova")),
        Map.entry("mater", Set.of("materin", "materina")),

        // -- VERBS --
        Map.entry("delal", Set.of("delal", "delala", "delali")),

        // -- SHORT WORDS --
        Map.entry("most", Set.of("most")),
        Map.entry("tri", Set.of("tri")),
        Map.entry("pes", Set.of("pes")),
        Map.entry("vse", Set.of("vse")),

        // -- NAMES --
        Map.entry("gregor", Set.of("gregorja", "gregorju", "gregorjem"))
    );

    @Test
    @Tag("performance")
    public void analyze_stemmer_performance() {
        final SlovenianStemmer stemmer = new SlovenianStemmer();

        System.out.println("================================================================================");
        System.out.println("Slovenian Stemmer Performance Analysis");
        System.out.println("================================================================================\n");

        Map<String, DatasetResult> datasetResults = new LinkedHashMap<>();

        // Analyze common words dataset
        DatasetResult commonWordsResult = analyzeDataset(
            stemmer,
            "slovenian_common_words_dataset",
            slovenian_common_words_dataset
        );
        datasetResults.put("Common Words", commonWordsResult);

        // Analyze keywords dataset
        DatasetResult keywordsResult = analyzeDataset(
            stemmer,
            "slovenian_keywords_dataset",
            slovenian_keywords_dataset
        );
        datasetResults.put("Keywords", keywordsResult);

        // Print per-dataset results
        for (Map.Entry<String, DatasetResult> entry : datasetResults.entrySet()) {
            DatasetResult result = entry.getValue();
            System.out.printf("Dataset: %s%n", entry.getKey());
            System.out.printf("  Total entries: %d%n", result.totalEntries);
            System.out.printf("  Successful groupings: %d%n", result.successfulGroupings);
            System.out.printf("  Precision: %.1f%%%n%n", result.getPrecision());
        }

        // Calculate overall precision
        int totalEntries = datasetResults.values().stream().mapToInt(r -> r.totalEntries).sum();
        int totalSuccesses = datasetResults.values().stream().mapToInt(r -> r.successfulGroupings).sum();
        double overallPrecision = totalEntries > 0 ? (double) totalSuccesses / totalEntries * 100 : 0;

        System.out.printf("Overall Precision: %.1f%% (%d/%d)%n%n", overallPrecision, totalSuccesses, totalEntries);

        // Print failures
        System.out.println("================================================================================");
        System.out.println("FAILURES");
        System.out.println("================================================================================\n");

        int failureCount = 0;
        for (Map.Entry<String, DatasetResult> entry : datasetResults.entrySet()) {
            if (!entry.getValue().failures.isEmpty()) {
                System.out.printf("=== %s ===%n%n", entry.getKey());
                for (FailureReport failure : entry.getValue().failures) {
                    failureCount++;
                    System.out.printf("[%d] Expected: \"%s\"%n", failureCount, failure.expectedBase);
                    System.out.printf("    Got %d different stems: %s%n", failure.uniqueStems.size(), failure.uniqueStems);
                    System.out.println("    Examples:");
                    for (Map.Entry<String, String> e : failure.formToStem.entrySet()) {
                        System.out.printf("      \"%s\" → \"%s\"%n", e.getKey(), e.getValue());
                    }
                    System.out.println();
                }
            }
        }

        if (failureCount == 0) {
            System.out.println("No failures! All word forms stem correctly. ✓");
        }
    }

    private DatasetResult analyzeDataset(
        SlovenianStemmer stemmer,
        String datasetName,
        Map<String, Set<String>> dataset
    ) {
        int totalEntries = 0;
        int successfulGroupings = 0;
        List<FailureReport> failures = new ArrayList<>();

        for (Map.Entry<String, Set<String>> entry : dataset.entrySet()) {
            totalEntries++;
            String expectedBase = entry.getKey();
            Set<String> inflectedForms = entry.getValue();

            // Stem all forms, track unique stems
            Set<String> uniqueStems = new HashSet<>();
            Map<String, String> formToStem = new LinkedHashMap<>();

            for (String form : inflectedForms) {
                String stemmed = stemTokens(stemmer, form);
                uniqueStems.add(stemmed);
                formToStem.put(form, stemmed);
            }

            if (uniqueStems.size() == 1) {
                successfulGroupings++;
            } else {
                failures.add(new FailureReport(expectedBase, uniqueStems, formToStem));
            }
        }

        return new DatasetResult(datasetName, totalEntries, successfulGroupings, failures);
    }

    private String stemTokens(SlovenianStemmer stemmer, String text) {
        String[] tokens = text.trim().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            char[] chars = tokens[i].toCharArray();
            int newLen = stemmer.stem(chars, chars.length);
            if (i > 0) result.append(" ");
            result.append(new String(chars, 0, newLen));
        }
        return result.toString();
    }
}
