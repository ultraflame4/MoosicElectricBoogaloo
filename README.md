# MoosicAppElectricBoogalo

A music app created for a school project

## The Plan (revised)
### basic features (the very bare minimum and hard coded stuff)
- [x] Implement the login screen
- [x] Implement the basic library ui
- [x] Implement playing of songs
- [x] Implement playlists
- [x] Implement basic playlist ui
- 
### core features (must have)
- [x] Implement searching of songs in lib 
- [ ] Implement removal songs
- [ ] Implement removal of playlists
- [ ] basic settings page (for logout, changing password & clearing of data)
- [x] Implement adding of songs through links (web audio links only for now) 
- [ ] Implement adding of songs through local files [reference](https://developer.android.com/training/data-storage/shared/documents-files)
- [ ] Implement creation of playlist

#### data stuff
- [ ] Implement local data storage
- [ ] Implement cloud data storage for logged in users

### common features pt.2
- [ ] Implement editing of song information
- [ ] Implement editing of playlist information
- [ ] Implement editing of playlist songs
- [ ] recent searches

### advanced features
- [ ] Implement recommendations and suggestions for search menu
- [ ] Shareable playlist link


<strike>
## The Plan
- [ ] Implement the library ui
- [ ] Implement adding of songs from links to the library
- [ ] Implement creation of playlist
- [ ] Implement playing of songs/playlists (and the ui)
- [ ] Implement the login screen
- [ ] Implement the search bar (and the ui)
- [ ] Implement recommendations and suggestions for search menu
- [ ] Attempt ~~stealing~~ retrieval of songlist from youtube/spotify/soundcloud/etc.
</strike>

#### Done
- [x] Do the bottom navigation bar

### Fragments
[Tutorial](https://www.youtube.com/watch?v=PiExmkR3aps)


### Search function - > check for similarity.
Regex? [This](https://github.com/tdebatty/java-string-similarity) ?

### Login/Signin:

##### Server:
- Firebase
##### Normal:
 - Magic
##### Google:
 - https://developers.google.com/identity/one-tap/android/get-started

### Adding of songs from links:
 - This library [here](https://github.com/Litarvan/vget)
 - Download Album covers [here](https://github.com/square/picasso)

### Advanced playing of songs and playlist.
 1. New class to wrap around media player and playlists
 2. Called SongPlayer
 3. When created, it takes in a playlist
 4. It copies the playlist songs to a new array
 5. SongPlayer class keeps tracked of played songs and next songs