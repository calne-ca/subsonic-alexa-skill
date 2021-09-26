package net.beardbot.subsonic.alexa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
public class PlaybackQueue {
    private int nextSongIndex = 0;
    private final List<String> queue = new ArrayList<>();

    private boolean loop = false;

    @JsonIgnore
    public Optional<String> previousSongId(){
        if (queue.isEmpty()){
            return Optional.empty();
        }

        if (nextSongIndex > 0){
            nextSongIndex--;
        }
        if (nextSongIndex == 0 && loop){
            nextSongIndex = lastIndex();
        }

        return Optional.of(queue.get(nextSongIndex++));
    }

    public Optional<String> currentSongId(){
        if (nextSongIndex <= 0 ){
            return Optional.empty();
        }

        return Optional.of(queue.get(nextSongIndex - 1));
    }

    public Optional<String> nextSongId(){
        if (queue.isEmpty()){
            return Optional.empty();
        }

        if (nextSongIndex >= lastIndex()){
            if (loop){
                nextSongIndex = 0;
            } else {
                return Optional.empty();
            }
        }

        return Optional.of(queue.get(nextSongIndex++));
    }

    public void shuffle(){
        Collections.shuffle(queue);
        nextSongIndex = 0;
    }

    private int lastIndex(){
        return queue.size() - 1;
    }
}
