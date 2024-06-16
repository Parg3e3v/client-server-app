# Client-Server App

When you tap Start on the client app, it opens Google Chrome on your device and notifies the server. The server then starts sending instructions for different swipe gestures (swiping up or down with varying distances) to the client. Using the Android Accessibility Service, the client executes these gestures in Chrome and sends back the results to the server, which logs everything into a local database. This interaction continues until the client presses Pause. Pressing Start again resumes the process. The server can handle multiple clients simultaneously using coroutines, and communication between the server and clients is done via WebSocket.

## Teck Stack
- Kotlin 
- Jetpack Compose
- Корутины
- Flow 
- Dagger Hilt
- MVVM
- Clean Architecture
- Многомодульность 
- Ktor Server
- Ktor Client
- Room
- Proto DataStore
