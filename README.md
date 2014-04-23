# tourney-map

A webapp for viewing USCF-rated chess tournaments on a map.

The USCF listings for chess tournaments leave a lot to be desired. It's not possible to search filter by location, other than state, which is not reasonable for most states due to how large they are. It's not possible to search at all.

Personally, I created this for the following reasons:

1. I live in Pennsylvania and don't enjoy scrolling past tournaments that are more than a four hour drive away.
2. I learned that I wasn't aware of tournaments happening in NJ just across the river.
3. Similar to (1), I am only interested in tournaments in NJ that are close to Philadelphia.

## Quickstart

1. Clone this repo.
2. Run `lein run`
3. Run `lein cljsbuild once`
4. Share `static/` with your favorite webserver

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Project Details

The `dev_data` folder contains web data that needs to processed. Used to store things that are too large to download dynamically, or to assist in offline development.

The `resources` folder contains edn data.

When you run `-main`, it will produce a fully-deployable directory called `static/` that contains everything needed to run the site with a webserver like nginx or apache. If source data is missing, you may need to run setup functions in `ops.clj`.

## License

Copyright © 2014 Steve Lamb
GNU GPL v3

## Todo

* handle the CA-N CA-S anomolly
* make main/main runnable from `lein run`.
