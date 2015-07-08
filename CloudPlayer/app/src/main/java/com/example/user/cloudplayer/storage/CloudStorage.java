package com.example.user.cloudplayer.storage;


import android.content.res.Resources;

import com.example.user.cloudplayer.R;
import com.example.user.cloudplayer.model.Comment;
import com.example.user.cloudplayer.model.Like;
import com.example.user.cloudplayer.model.PlayList;
import com.example.user.cloudplayer.model.Song;
import com.example.user.cloudplayer.transport.NetworkEventListener;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CloudStorage {
    private NetworkEventListener listener;
    private Resources resources;

    public void setListener(NetworkEventListener listener) {
        this.listener = listener;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public void getSearchResult(String keyword){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(resources.getString(R.string.play_table));
        query.whereMatches(resources.getString(R.string.name_col),"("+keyword+")", "i");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null){
                    ArrayList<PlayList> playLists = new ArrayList<PlayList>();
                    for (ParseObject object: parseObjects){
                        PlayList playList =new PlayList(
                                        object.getString(resources.getString(R.string.name_col)),
                                        object.getInt(resources.getString(R.string.numlikes_col)));
                        playList.setID(object.getObjectId());
                        playList.setUserID(object.getParseUser(resources.getString(R.string.key_user)).
                                getObjectId());
                        playLists.add(playList);
                    }
                    listener.onSearchResultDownloaded(playLists);
                } else
                    listener.onSearchResultDownloaded(null);
            }
        });
    }

    public void getTopTen(){
        final int limit = 10;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(resources.getString(R.string.play_table));
        query.addDescendingOrder(resources.getString(R.string.numlikes_col));
        query.setLimit(limit);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null){
                    ArrayList<PlayList> playLists = new ArrayList<PlayList>();
                    for (ParseObject object: parseObjects){
                        PlayList playList =new PlayList(
                                object.getString(resources.getString(R.string.name_col)),
                                object.getInt(resources.getString(R.string.numlikes_col)));
                        playList.setID(object.getObjectId());
                        playList.setUserID(object.getParseUser(resources.getString(R.string.key_user)).
                                getObjectId());
                        playLists.add(playList);

                    }
                    listener.onTopTenDownloaded(playLists);
                } else
                    listener.onTopTenDownloaded(null);
            }
        });
    }

    public void addNewPlaylist(final PlayList playList){
        final ParseObject object = new ParseObject(resources.getString(R.string.play_table));
        object.put(resources.getString(R.string.name_col),playList.getName());
        object.put(resources.getString(R.string.numlikes_col),playList.getNumLikes());
        object.put(resources.getString(R.string.key_user), ParseUser.getCurrentUser());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    playList.setID(object.getObjectId());
                    playList.setUserID(ParseUser.getCurrentUser().getObjectId());
                    listener.onPlayListAdded(playList);
                }else
                    listener.onPlayListAdded(null);
            }
        });
    }

    public void deletePlayList(final PlayList playList){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(resources.getString(
                R.string.play_table));
        query.getInBackground(playList.getID(),new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null){
                    parseObject.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null)
                                listener.onPlayListDeleted(playList);
                            else
                                listener.onPlayListDeleted(null);
                        }
                    });
                } else
                    listener.onPlayListDeleted(null);
            }
        });
        // kaskaduri ambebi
    }

    public void downloadUsersPlaylists(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(resources.getString(R.string.play_table));
        query.whereEqualTo(resources.getString(R.string.key_user),ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null){
                    ArrayList<PlayList> playLists = new ArrayList<PlayList>();
                    for (ParseObject object: parseObjects){
                        PlayList playList =new PlayList(
                                object.getString(resources.getString(R.string.name_col)),
                                object.getInt(resources.getString(R.string.numlikes_col)));
                        playList.setID(object.getObjectId());
                        playList.setUserID(object.getParseUser(resources.getString(R.string.key_user)).
                                getObjectId());
                        playLists.add(playList);

                    }
                    listener.onUsersPlayListsDownloaded(playLists);
                } else
                    listener.onUsersPlayListsDownloaded(null);
            }
        });
    }

    public void getComments(final String playlistID){
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery(resources.getString(
                R.string.play_table));
        query1.getInBackground(playlistID,new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e==null){
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(resources.getString(R.string.
                            comment_table));
                    query.whereEqualTo(resources.getString(R.string.parent_col),parseObject);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            if (e==null){
                                ArrayList<Comment> comments = new ArrayList<Comment>();
                                for (ParseObject object: parseObjects){
                                    ParseUser user=object.getParseUser(resources.getString(R.string.
                                            key_user));
                                    String name = user.getString(resources.getString(R.string.name_col));
                                    String text = user.getString(resources.getString(R.string.text_col));
                                    comments.add(new Comment(playlistID,name,text));
                                    listener.onCommentsDownloaded(comments);
                                }
                            } else
                                listener.onCommentsDownloaded(null);
                        }
                    });
                } else
                    listener.onCommentsDownloaded(null);
            }
        });
    }

    public void getLikes(final String playlistID){
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery(resources.getString(
                R.string.play_table));
        query1.getInBackground(playlistID,new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e==null){
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(resources.getString(R.string.
                            like_table));
                    query.whereEqualTo(resources.getString(R.string.parent_col),parseObject);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            if (e==null){
                                ArrayList<Like> likes = new ArrayList<Like>();
                                for (ParseObject object: parseObjects){
                                    ParseUser user=object.getParseUser(resources.getString(R.string.
                                            key_user));
                                    String name = user.getString(resources.getString(R.string.name_col));
                                    likes.add(new Like(playlistID,name));
                                }
                                listener.onLikesDownloaded(likes);
                            } else
                                listener.onLikesDownloaded(null);
                        }
                    });
                } else
                    listener.onLikesDownloaded(null);
            }
        });
    }

    public void addComment(final Comment comment){
        final ParseObject object = new ParseObject(resources.getString(R.string.comment_table));
        object.put(resources.getString(R.string.parent_col),ParseObject.createWithoutData(resources
        .getString(R.string.play_table),comment.getPlayListID()));
        object.put(resources.getString(R.string.text_col), comment.getText());
        object.put(resources.getString(R.string.key_user), ParseUser.getCurrentUser());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    listener.onCommentAdded(comment);
                } else
                    listener.onCommentAdded(null);
            }
        });
    }

    public void getSongs(final String playlistID){
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery(resources.getString(
                R.string.play_table));
        query1.getInBackground(playlistID,new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e==null){
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(resources.getString(R.string.
                            song_table));
                    query.whereEqualTo(resources.getString(R.string.parent_col),parseObject);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            if (e==null){
                                ArrayList<Song> songs = new ArrayList<Song>();
                                for (ParseObject object: parseObjects){
                                    String name = object.getString(resources.getString(R.string.
                                            name_col));
                                    String url = object.getParseFile(resources.getString(R.string.
                                            file_col)).getUrl();
                                    songs.add(new Song(name,object.getObjectId(),playlistID,url));
                                }
                                listener.onSongsDownloaded(songs);
                            } else
                                listener.onSongsDownloaded(null);
                        }
                    });
                } else
                    listener.onSongsDownloaded(null);
            }
        });
    }

    public void addSong(String path,final String playlistID,final String name){
        byte[] byteArray = getSongBytes(path);
        final ParseFile file = new ParseFile(name,byteArray);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    final ParseObject object = new ParseObject(resources.getString(R.string.song_table));
                    object.put(resources.getString(R.string.parent_col),ParseObject.createWithoutData
                            (resources.getString(R.string.play_table),playlistID));
                    object.put(resources.getString(R.string.name_col),name);
                    object.put(resources.getString(R.string.file_col),file);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null){
                                listener.onSongAdded(new Song(name,object.getObjectId(),playlistID,
                                        file.getUrl()));
                            } else
                                listener.onSongAdded(null);
                        }
                    });
                } else
                    listener.onSongAdded(null);
            }
        });

    }

    private byte[] getSongBytes(String path){
        File file = new File(path);
        byte[] byteArray = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(byteArray);
        } catch (FileNotFoundException  e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return byteArray;
    }

    // lishnebi envokes gareshe pirveli

    public void addLike(Like like){
        final ParseObject object = new ParseObject(resources.getString(R.string.like_table));
        object.put(resources.getString(R.string.parent_col),ParseObject.createWithoutData(resources
                .getString(R.string.play_table),like.getPlayListID()));
        object.put(resources.getString(R.string.key_user), ParseUser.getCurrentUser());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){

                } else
                    listener.onCommentAdded(null);// onLiked envoke-it chanacvleba
            }
        });
    }

    public void addSongFromOtherUser(final Song song,final String playListID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(resources.getString(
                R.string.song_table));
        query.getInBackground(song.getID(),new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e==null) {
                    final ParseObject object = new ParseObject(resources.getString(R.string.song_table));
                    object.put(resources.getString(R.string.parent_col), ParseObject.createWithoutData(resources
                            .getString(R.string.play_table), playListID));
                    object.put(resources.getString(R.string.name_col),song.getName());
                    final ParseFile file = parseObject.getParseFile(resources.getString(R.string.
                            file_col));
                    object.put(resources.getString(R.string.file_col),file);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null){
                                listener.onSongAdded(new Song(song.getName(),object.getObjectId(),
                                        playListID,file.getUrl()));
                            } else
                                listener.onSongAdded(null);
                        }
                    });
                } else
                    listener.onSongAdded(null);
            }
        });
    }
}
