# 🎬 MyMoviesApp - Android Frontend

An Android application for movie search and display, utilizing **Volley** for network requests and **Picasso** for image loading. The app allows users to search for movies based on keywords in titles, cast, or extracts, retrieving data from a Spring Boot backend. Results are shown in a list with detailed movie information, including posters, accessible on selection.

## 📜 Description

This repository contains the code for the **MyMoviesApp** Android front-end application. Built with Java, the app includes the following features:
- A **Navigation Drawer** for intuitive navigation.
- **Fragments** to separate different app sections, such as the search and detail views.
- **ListView** to display search results with smooth scrolling.
- **Volley** library for efficient API requests.
- **Picasso** library for seamless image loading.

## 🛠️ Features

- **Search Module**: Users can search for movies based on keywords, retrieving matching results from the backend.
- **Details View**: Selecting a movie from the search results displays detailed information about the movie, including its title, genre, year, and a poster image.
- **Navigation Drawer**: Provides easy access to the search module and other potential app features.

## 🚀 Getting Started

### Prerequisites

- **Android Studio** (latest version recommended)
- **Android Device or Emulator** running API level 24 or higher
- **Backend Server**: Ensure that the Spring Boot backend is running locally on `http://10.0.2.2:8080/api/movies`.

### Installation

1. **Clone the repository**:
   ```bash
    git clone https://github.com/marcusvramos/mymovies-android-app.git
   ```
2. **Open the project** in Android Studio.
3. **Sync dependencies** by going to `File > Sync Project with Gradle Files`.
4. **Run** the application on an emulator or connected device.

### Backend Setup

- Ensure the backend server is running on `http://10.0.2.2:8080/api/movies`.
- This server should provide an endpoint `/search?keyword=` to handle search requests.

## 🖼️ Screenshots

1. **Search View**: Interface for searching movies by keywords.
2. **Movie Details View**: Displays detailed information about a selected movie.

## 📚 Libraries Used

- [Volley](https://developer.android.com/training/volley) - For network operations.
- [Picasso](https://square.github.io/picasso/) - For loading and caching images.
- [Android Navigation Components](https://developer.android.com/guide/navigation) - For smooth navigation handling.

## 🤝 Contributing

Feel free to fork this repository and contribute via pull requests. Any improvements or bug fixes are welcome!
