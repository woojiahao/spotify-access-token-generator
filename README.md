# spotify-access-token-generator
Simple GUI tool to generate the access tokens needed when working on the SpotifyWrapper

**Note:** Issues on this repository is unlikely to be dealt with swiftly as this is meant to be a rough tool to continually get
access tokens from the API.

**Note:** We are not going to use the latest versions of the API from maven central because we might want to test minute
changes to the library, especially if this is done locally.

## Usage
1. Download the distributed `.jar` file
2. Run the file
   
   ```bash
   $ java -jar SpotifyAccessTokenGenerator.jar
   ```
3. Fill in your credentials and press "Start" to begin the authorization process

## Compiling
1. Clone this repository
   
   ```bash
   $ git clone https://github.com/woojiahao/spotify-access-token-generator.git
   $ cd spotify-access-token-generator
   ```
2. Create a local copy of the compiled version of the wrapper
   
   ```bash
   $ git clone https://github.com/woojiahao/java-spotify-wrapper.git
   $ cd java-spotify-wrapper
   $ mvn clean install
   $ cd target
   ```
3. Then, add the compiled jar to your local maven repository
   
   ```bash
   $ mvn install:install-file -Dfile=JavaSpotifyWrapper-1.0-SNAPSHOT-jar-with-dependencies.jar -DgroupId=me.chill -DartifactId=JavaSpotifyWrapper -Dversion=1.0-SNAPSHOT -Dpackaging=jar
   ```
4. Then navigate back into the access token generator repository location and perform:
   
   ```bash
   $ mvn clean package
   $ cd target
   ```
5. Execute the newly created compiled jar inside the target folder
   
   ```bash
   $ java -jar SpotifyAccessTokenGenerator.jar
   ```
6. Fill in the fields and begin using

## Configuration
If you wish to supply a default set of configurations every time the credentials window shows up, do the following:

1. Add a folder to the root of the project and call it `config`
2. Add a file inside this folder and call it `config.json`
3. Add the following to `config.json`, for example:

```json
{
	"id": "cea6a21eeb874d1d91dbaaccce0996f3",
	"secret": "96729e5f290e496d8115d9e0bf27e515",
	"url": "https://woojiahao.github.io"
}
```