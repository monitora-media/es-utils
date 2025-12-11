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

    export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
    export PATH=$JAVA_HOME/bin:$PATH
    JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 PATH=$JAVA_HOME/bin:$PATH ./gradlew build -Pelasticsearch.version=$ES_VERSION


## Testing

Unit tests:

    ./gradlew test --info --tests "Croatian*"

Performance tests:

    ./gradlew performanceTest

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
            "type": "monitora_slovenian_stem"
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
