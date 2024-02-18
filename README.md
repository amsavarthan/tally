[![GitHub license](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)

# Tally 💸
A minimal expense tracker built to demonstrate the use of modern android architecture component with Clean Architecture, primarily developed to explore on Complex Room Implementations

***Get the latest Tally app from Playstore 👇***

[![Tally](https://img.shields.io/badge/Tally-PLAYSTORE-black.svg?style=for-the-badge&logo=android)](https://play.google.com/store/apps/details?id=com.amsavarthan.tally)

## UI Design 🎨

***Click to View Tally app Design from below 👇***

[![Tally](https://img.shields.io/badge/Tally-FIGMA-black.svg?style=for-the-badge&logo=figma)](https://www.figma.com/file/l1qN1GnbMnI5LUymuIImAE/Tally?node-id=0%3A1&t=gevji5o4Ihh9gfk4-1)

## Screenshots 📸
Dashboard | Add Transaction | Wallet | Choose Account
--- | --- | --- |---
![](https://github.com/amsavarthan/tally/blob/main/art/Dashboard.png)|![](https://github.com/amsavarthan/tally/blob/main/art/Add-Edit-Transaction.png)|![](https://github.com/amsavarthan/tally/blob/main/art/Wallet-Screen.png)|![](https://github.com/amsavarthan/tally/blob/main/art/Choose-Account.png)|

## Built With 🛠
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more..
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [Stateflow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) - StateFlow is a state-holder observable flow that emits the current and new state updates to its collectors. 
  - [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) - A flow is an asynchronous version of a Sequence, a type of collection whose values are lazily produced.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes. 
  - [Room](https://developer.android.com/topic/libraries/architecture/room) - SQLite object mapping library.
  - [Jetpack Navigation](https://developer.android.com/guide/navigation) - Navigation refers to the interactions that allow users to navigate across, into, and back out from the different pieces of content within your app
  - [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - Jetpack DataStore is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers. DataStore uses Kotlin coroutines and Flow to store data asynchronously, consistently, and transactionally.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.
- [Figma](https://figma.com/) - Figma is a vector graphics editor and prototyping tool which is primarily web-based.

## Package Structure 📦
    
    
    com.amsavarthan.tally         # Root Package
    ├── di                         # Hilt DI Modules
    ├── data                       
    │   ├── repository             # Implementation classes
    │   ├── source               
    │   │   ├── datastore          
    │   │   └── local              
    │   └── utils                  
    ├── domain                     
    │   ├── entity                 # Model classes
    │   ├── repository             # Interfaces
    │   ├── usecase                
    │   └── utils                
    └── presentation               
        ├── ui                     
        │   ├── components         # Reuseable composables
        │   ├── screens            # Each screen have own directory
        │   └── theme              
        └── utils


## 🧰 Build-tool

- [Android Studio Dolphin 2021.3.1 or above](https://developer.android.com/studio)

## 📩 Contact

DM me at 👇

* Twitter: <a href="https://twitter.com/lvamsavarthan" target="_blank">@lvamsavarthan</a>
* Email: amsavarthan.a@gmail.com

## License 🔖
```
MIT License

Copyright (c) 2023 Amsavarthan Lv

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
