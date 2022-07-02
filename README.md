# MoosicAppElectricBoogalo

A music app created for a school project


## The Plan
- [ ] Do the bottom navigation bar ( Done by template )
- [ ] Implement the library user interface
- [ ] Implement adding of songs from links to the library
- [ ] Implement creation of playlist
- [ ] Implement playing of songs/playlists (and the ui)
- [ ] Implement the search bar (and the ui)
- [ ] Implement recommendations and suggestions for search menu
- [ ] Attempt ~~stealing~~ retrieval of songlist from youtube/spotify/soundcloud/etc. 

### Fragments
[Tutorial](https://www.youtube.com/watch?v=PiExmkR3aps)


### Search function - > check for similarity.
Regex? [This](https://github.com/tdebatty/java-string-similarity) ?

### Login/Signin:

##### Server?: (do i hate myself?)
- Heroku fo hosting? or firebase?
- Use nodejs with express. Passport.js? for oauth authentication and Yes Definately Typescript. Whose idea was it to not type javascript?
##### Normal:
 - Magic
##### Google:
 - https://developers.google.com/identity/one-tap/android/get-started

### Adding of songs from links:
 - This library [here](https://github.com/Litarvan/vget)

### Advanced playing of songs and playlist.
 1. New class to wrap around media player and playlists
 2. Called SongPlayer
 3. When created, it takes in a playlist
 4. It copies the playlist songs to a new array
 5. SongPlayer class keeps tracked of played songs and next songs