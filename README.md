# spotify-access-token-generator
Simple GUI tool to generate the access tokens needed when working on the SpotifyWrapper

**Note:** Issues on this repository is unlikely to be dealt with swiftly as this is meant to be a rough tool to continually get
access tokens from the API.

**Note:** We are not going to use the latest versions of the API from maven central because we might want to test minute
changes to the library, especially if this is done locally.

## Usage
1. Clone this repository
2. Add the wrapper library to your class path
3. Launch `Main.java`
4. Select the required scopes to cycle and fill in all text fields
5. Authorize the application
6. Copy the access tokens to your clipboard

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