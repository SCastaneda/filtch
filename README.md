# filtch
It's a webserver that uses the twitch API to filter streams by their title for a specific game (specified by the gameId in `config.edn`).
You'll need to get a `clientId` and `clientSecret` by signing up at https://dev.twitch.tv .

Only tested on gameId 2748 - for Magic: The Gathering.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## Build

To build the jar run `lein ring jar` and run it with `java -jar filtch.jar`.
It will start up on the port specified in `project.clj`.
