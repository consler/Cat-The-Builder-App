# Cat-The-Builder-App
App to build .apk from .catrobat(pocket code projects)

## How to use
<img src=art/ctb.jpg alt="screenshot" width="200">

* Download the app [here](https://github.com/consler/Cat-the-Builder-App/releases/latest).
* Open the app.
* Write your desired app name into the first text field.
* Write your desired package name into the second text field.
* Write your desired version into the third text field.
* Write your desired version code into the fourth text field (must be a whole number)
* Click on "Your icon" and select your icon file (.png or .jpeg).
* Click on "Your .catrobat" and select your .catrobat file (exported in pocket code)
* Click "Build" and wait until the build is done.
## Join the community!
* **[EN]** [Telegram](https://t.me/CatTheBuilder)
* **[RU]** [Telegram](https://t.me/WikiPocketCode), [Discord](https://discord.gg/wavBWGudrj)

## Feature comparison
| Features               | Cat The Builder | CBuilder | Manual APK editing |
|------------------------|-----------------|----------|--------------------|
| Newest version support | ✅               | ❌        | ✅                  |
| Open source  client    | ✅               | ❌        | 🤷🏽‍♂️            |
| Fast                   | ✅               | ✅        | ❌                  |
| Easy to use            | ✅               | ✅        | ❌                  |
| Maintained             | ✅               | ❌        | ✅                  |
| Change app name        | ✅               | ✅        | ✅                  |
| Change package         | ✅               | ✅        | ✅                  |
| Change version name    | ✅               | ✅        | ✅                  |
| Change version code    | ✅               | ❌        | ✅                  |
| Change icon            | ✅               | ✅        | ✅                  |
| Use adaptive icon      | ✅               | ❌        | ❌                  |
| Auto resize round icon | ✅               | ❌        | ❌                  |
## TO DO: 
* **Optimize**
* **Option to not use an adaptive icon**

## Contact me
* Telegram: @onsler
* Discord: @consler
* Email: consler2000@gmail.com

## How it works
* When you press "Build", the app copies a folder called CATGAME from the assets folder to the cache directory.
* * CATGAME.apk is a fork of Pocket Code that skips the main menu and loads the project right away (source code can be found at https://github.com/consler/CATGAME).
* * The CATGAME folder is simply an unzipped CATGAME (you can rename any .apk to .zip and extract it like a normal archive).
* The app then copies your .catrobat file and extracts it to CATGAME/assets/CATGAME, because that's the folder CATGAME loads (.catrobat files are also renamed zip archives with your project data).
* After that, Cat The Builder replaces the default icon with your custom icon.
* Then it zips the CATGAME folder and saves it to the cache directory as CATGAME.apk.
* It uses ARSCLib to change the app name, package name, version name, and version code.
* Finally, it signs the apk and exports it onto your phone's storage.

## License
![GNU AGPL v3.0](https://img.shields.io/github/license/consler/Cat-The-Builder-App)