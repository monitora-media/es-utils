# Elasticsearch plugin - Monitora utils

Various utilities enhancing our Elasticsearch installation.

## Release

To create a release, just tag the commit with version number. The built files are available
in the [release](https://github.com/monitora-media/es-utils/releases/latest).

    git tag 1.1.0
    git push --tags

## Build

    ./gradlew build -Pelasticsearch.version=8.5.3

## Install

    elasticsearch-plugin install file://$(pwd)/build/distributions/monitora_utils-1.0.0-SNAPSHOT-8.5.3.zip

## Usage

### Lowercase filter

Analysis filter that converts to lowercase but keeps the originally-cased token in the stream as
well.

Index settings:

```json
{
  "analysis": {
    "filter": {
      "lowercase": {
        "type": "monitora_lowercase",
        "preserve_original": "true"
      },
      "analyzer": {
        "index": {
          "type": "custom",
          "filter": [
            "lowercase"
          ]
        }
      }
    }
  }
}
```
