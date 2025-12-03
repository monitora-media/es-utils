# Elasticsearch plugin - Monitora utils

Various utilities enhancing our Elasticsearch installation.

## Release

To create a release, just tag the commit with version number. The built files are available
in the [release](https://github.com/monitora-media/es-utils/releases/latest).

    git tag 1.1.0
    git push --tags

## Build

    ES_VERSION=8.15.2
    ./gradlew build -Pelasticsearch.version=$ES_VERSION

    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
    export PATH=$JAVA_HOME/bin:$PATH
    JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 PATH=$JAVA_HOME/bin:$PATH ./gradlew build -Pelasticsearch.version=8.15.2


## Testing

    gradle test --info --tests "Croatian*"

## Install

    elasticsearch-plugin install file://$(pwd)/build/distributions/monitora_utils-*-SNAPSHOT-$ES_VERSION.zip

## Usage

### Lowercase filter

Analysis filter that converts to lowercase but keeps the originally-cased token in the stream as
well.

### Czech, Slovak, Croatian and Slovenian stemmers

Specialized stemmers for Slavic languages

### Example index settings

```json
{
    "filter": {
        "lowercase": {
            "type": "monitora_lowercase",
            "preserve_original": "true"
        },
        "mczech_stem": {
            "type": "monitora_czech_stem",
            "with_asciifold": "true"
        },
        "mslovak_stem": {
            "type": "monitora_slovak_stem",
            "with_asciifold": "true"
        },
        "mslovenian_stem": {
            "type": "monitora_slovenian_stem",
            "with_asciifold": "true"
        },
        "mcroatian_stem": {
            "type": "monitora_croatian_stem"
        }
    },
    "analyzer": {
        "index": {
            "type": "custom",
            "tokenizer": "whitespace",
            "filter": [
                "lowercase",
                "mczech_stem"
            ]
        }
    }
}
```


Test all tests:

./gradlew test

Test only Slovenian stemmer:

./gradlew test --tests "Slovenian*"

Test with detailed output:

./gradlew test --info --tests "Slovenian*"

Test specific language stemmers:

# Croatian tests
./gradlew test --tests "Croatian*"

# Slovak tests
./gradlew test --tests "Slovak*"

# Czech tests
./gradlew test --tests "Czech*"

Build Commands

Full build (compiles + tests + creates plugin):

./gradlew build

Build with specific Elasticsearch version:

ES_VERSION=8.15.2
./gradlew build -Pelasticsearch.version=$ES_VERSION

Build without running tests:

./gradlew build -x test

Clean and rebuild:

./gradlew clean build

After Building

The built plugin will be located at:
build/distributions/monitora_utils-<version>-<ES_VERSION>.zip

Install the plugin:

elasticsearch-plugin install file://$(pwd)/build/distributions/monitora_utils-*-SNAPSHOT-8.15.2.zip

View Test Reports

After running tests, view the HTML report:
# Open in browser
xdg-open build/reports/tests/test/index.html

# Or just view the file location
echo "file://$(pwd)/build/reports/tests/test/index.html"
