# Elasticsearch plugin - Monitora utils

Various utilities enhancing our Elasticsearch installation.

## Release

Release tags are `<es_version>-<major>.<minor>`. CI takes the Elasticsearch version and the
plugin version from the tag, builds against that Elasticsearch version and publishes
`monitora_utils-<es_version>-<major>.<minor>.zip` in the [release](https://github.com/monitora-media/es-utils/releases).
Tags in any other format fail the build.

- New Elasticsearch version, same plugin code: tag the same commit with the new ES version.
  If the build fails because the Elasticsearch API changed, fix the code first.
- Plugin change: bump the minor (or major) version.

```
git tag 8.19.23-1.0
git push origin 8.19.23-1.0
```

Install URL:

    https://github.com/monitora-media/es-utils/releases/download/<es_version>-<major>.<minor>/monitora_utils-<es_version>-<major>.<minor>.zip

Releases up to `1.2.1` use the old `<plugin_version>` tags. They stay available.

## Build

    ES_VERSION=8.19.23
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
