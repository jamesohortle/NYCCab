# NYC Cab trip data

Clone this repository. You may need Git LFS since I've included large files.

We will refer to it as `./` in the rest of the readme.

I was developing and testing on MacOS 10.13.6.

For ease's sake, I've put passwords etc in plaintext here. They are also in the source history [fixed with BFG], so you can see how I worked. Obviously I wouldn't do this in practice.

## Set up the database
1. Install MySQL 8.0.21.
1. Create a database called `nyccab` as user `root` with password `rootroot`.
1. Import the SQL file. The table will be called `nyc_cab_trip_data`.

## Build and run the server
1. Navigate to `./NYCCab/`.
1. Export environment variables:
```
export JASYPT_ENCRYPTOR_PASSWORD=jamesohortle
export JASYPT_ENCRYPTOR_ALGORITHM=PBEWithMD5AndDES
export JASYPT_ENCRYPTOR_IV_GENERATOR_CLASSNAME=org.jasypt.iv.NoIvGenerator
```
Jasypt will read from the environment variables to decrypt database passwords etc.

### Build with Gradle, run with Java
1. Build with Gradle 6.6.1 using script: `./gradlew build`.
1. Run the generated JAR file with Java 14.0.1: `java -jar build/libs/NYCCab-0.0.1-SNAPSHOT.jar`.

### Alternatively, run with Gradle
1. Run with Gradle 6.6.1 using script: `./gradlew bootRun`.

## Install the Python CLI
1. Navigate to `./cabCLI/`.
1. With Python 3.6+, execute `python3 setup.py install`.
1. Navigate somewhere else to test: `python3 -m cabCLI -h` should show the help.

## Test the CLI
1. The basic format is
```
python3 -m cabCLI -d yyyy-mm-dd -m med1 med2 ... medN
```
1. `--date` and `--medallion` flags can occur in any order.
1. All arguments after `--medallion` are interpreted as medallions until a `--date` is encountered.
1. Multiple medallions can be specified; only a single date is allowed.
1. If `--clear_cache` is set anywhere in the command, a request to clear the cache will be sent before any other requests for data are sent, meaning any other users will also receive fresh data.
1. If `--no_cache` is set anywhere in the command, the data will be requested fresh, with cached data for those medallion-date pairs evicted and recalculated before being returned.

## Issues
1. The Springboot caching abstraction is in place (the annotations are there), but the Ehcache backend was too fiddly to get running in time. There is a branch `ehcache` that you can have a look at to get an idea of my progress. [Done! ]
1. Logging is pretty minimal, with simple `System.out.println()`s. 
1. Currently the validation for inputs is pretty strict; things will simply fail or quit if dates aren't correctly formatted and only valid medallions will be processed. We would want to fail a bit more gracefully in reality.
1. The CLI is simple but with a little effort could be made prettier and have a few more options.
1. The API does not accept anything other than GET requests, since this is a publicly available dataset and so random people shouldn't be PUSHing, PATCHing, DELETEing, etc.
1. No unit tests in the interest of time. However, I did have a few Postman requests that I tested with.
