package com.app.ebaebo.util;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.File;

/**
 * Created by liuzwei on 2014/11/20.
 */
public class CommonUtil {
    //判断是否为JSOn格式
    public static boolean isJson(String json) {
        if (StringUtil.isNullOrEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    public static int getMp3TrackLength(File mp3File){
        try {
            MP3File f = (MP3File) AudioFileIO.read(mp3File);
            MP3AudioHeader audioHeader = f.getMP3AudioHeader();
            return audioHeader.getTrackLength();
        } catch (Exception e) {
           return -1;
        }
    }
}
