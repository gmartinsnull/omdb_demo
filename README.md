# OMDB Demo

This Android application displays a list of movies which can be retrieved either from local database or 
public OMDB API. Each item in the list represents compiled data from the aforementioned API, containing
attributes such as title, year and poster image. The goal of this project is to display how an android
architecture should look like in regards to performance, scalability and code cleanliness/readability.

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Install](#install)
- [Focus and Trade-offs](#focus-and-trade-offs)
- [Libraries](#libraries)
- [Outro](#outro)

## Features
- Movie search: Retrieves a list of movies based on the title input.  If the data does not 
exist in the database, the application fetches it from the API, otherwise, it retrieves from the
database. If no title is in the search field, it retrieves all movies saved in the database.
- Movie list: Displays a list of movies sorted by title then year of debut. The data is 
retrieved from either the local database or OMDB API.
- Movie item button: Displays a toast message containing the title of the movie upon click.

## Architecture
The architecture pattern applied in this project was a combination of CLEAN architecture and MVVM. The 
project structure is organized into layers according to CLEAN architecture pattern whereas the implementation
falls into Model-View-ViewModel pattern.

## Install
### GitHub
Since it's a public repository, it can simply be cloned from develop branch into your local directory 
using Git. Once cloned, Open Android Studio, on the welcome screen click on "Open" button. Select the 
directory where the project was cloned to.

### Zip
Unzip the file in a directory (ideally in the designated android studio folder: AndroidStudioProjects).
Open Android Studio and, on the welcome screen click on "Open" button. Select the directory where the project
was unzipped to.

## Focus and Trade-offs

### Focus
The primary focus of this project was the base architecture, data layer and the main Jetpack Compose 
elements. With a solid foundation, applications are more likely to be scalable and maintainable in the
foreseeable future.

### Trade-offs
Some trade-offs were in the realm of UX/UI and unit testing. Priorities were shifted in order to optimize 
architecture and data layer over better UX/UI such as animations/transitions, a detailed screen for each
movie and overall material design practices such as theming.

# Libraries
The following are the primary third party libraries used in this project:
- Hilt
- Jetpack Compose
- Coroutines
- Retrofit2
- Moshi
- OkHttp
- Room
- Mockito
- Coil

# Outro
I'd like to share the resources I used during the development of this project:
- StackOverflow
- Medium articles
- Material3 Components, theming and typography (https://m3.material.io)
- Android docs (https://developer.android.com/)
- Kotlin docs (https://kotlinlang.org)