## ARCardGame 

This is a repository to create Android applications for Augmented Reality (AR) card games, or any kind of text-based augmented reality. 
The application is self-sufficient and does not require an active internet connection to work. 
This constraint was pushed by the poor wifi/4G network quality that some french schools can experience.

### Project origin

The project was initially created to augment a physical game called Joue Maths Gie, the initial code was created by Bordeaux University students, then updated by Epitech students.

### Structure 

* This repository enables the creation of an Android application. 
* The [Manager](https://github.com/poqudrof/ARCardGameManager) repository is the backend to create augmentations. 
* The [frontend](https://github.com/poqudrof/ARCardGameWebsite) repository contains a website to obtain user and legal information about the application and download it.

### Features to come 

* Card Editor. 
* Card PDF export.

## How to build 

The application requires Adroid Studio and is built using Gradle. 

## AR Card structure 

The AR cards structure is as follows. 

1. The application is built for 1 deck. 
2. Each card belong to a deck. 
3. The cards have a “card role“ which represents its category. 
4. The Lines belong to a card, they represent the textual contents of the cards.
5. Cards can have additional media, currently: Answer, Tip, Illustration (image), preview (image), voiceover (mp3).  

In order to identify a card using the app. The full deck has to be loaded in the app. 


## Build a release 

1. Check / update the cards JSON and audio files.
2. Download them along with the images and MP3.
3. Build the app bundle in Android Studio (Signed build). 
4. Distribute it. 