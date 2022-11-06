package com.exiro.soundManager;

import com.exiro.buildingList.Building;
import com.exiro.fileManager.OggStreamer;
import com.exiro.fileManager.SoundLoader;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.layout.GameWindow;
import com.exiro.systemCore.GameThread;
import com.exiro.terrainList.Terrain;
import com.jogamp.openal.AL;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class SoundManager {

    public int secBeforeNextAmbient;

    public int secBeforeNextLayer1;
    public static int secBeforeNextMusic;
    Random random;
    ArrayList<Integer> sources;
    public int ambientCount = 0;
    HashMap<Integer, SoundLoader.SoundCategory> sourcesMap;

    public SoundManager(){
        sources = new ArrayList<>();
        sourcesMap = new HashMap<>();
        random = new Random();
        float[] listenerPos = { 0.0f, 0.0f, 0.0f };

        // Velocity of the listener.
        float[] listenerVel = { 0.0f, 0.0f, 0.0f };

        // Orientation of the listener. (first 3 elements are "at", second 3 are "up")
        float[] listenerOri = { 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f };

        SoundLoader.al.alDistanceModel(AL.AL_LINEAR_DISTANCE_CLAMPED);
        //SoundLoader.al.alListenerf(AL.AL_GAIN, 0.9f);

        SoundLoader.al.alListenerfv(AL.AL_POSITION, listenerPos, 0);
        SoundLoader.al.alListenerfv(AL.AL_VELOCITY, listenerVel, 0);
        SoundLoader.al.alListenerfv(AL.AL_ORIENTATION, listenerOri, 0);

    }

    public boolean isAvailable(int src){
        int[] status = new int[1];
        SoundLoader.al.alGetSourcei(src, AL.AL_SOURCE_STATE, status,0);
        return (status[0] != AL.AL_PLAYING && status[0] != AL.AL_PAUSED);
    }

    public Integer requestSource(){
        for (int i : sources){
            if(isAvailable(i))
                return i;
        }
        Integer i = generateSource();
        sources.add(i);
        return i;
    }


    public void playSound(SoundLoader.SoundCategory soundCategory){
       playSound(soundCategory, 0, 0);
    }
    public void playSound(SoundLoader.SoundCategory soundCategory, float x, float y){
        if(soundCategory == SoundLoader.SoundCategory.GENERAL_MUSIC || soundCategory == SoundLoader.SoundCategory.COMBAT_MUSIC_LONG|| soundCategory == SoundLoader.SoundCategory.COMBAT_MUSIC_SHORT) {
            int r = random.nextInt(SoundLoader.getSoundPaths(soundCategory).size());
            Thread t = new Thread(new OggStreamer(SoundLoader.getSoundPaths(soundCategory).get(r)));
            t.start();
            return;
        }
        int buffer = SoundLoader.requestSoundBuffer(soundCategory);
        if(buffer == -1) {
            System.out.println("Error playing sound");
            return;
        }
        int source = requestSource();
        if(source == -1) {
            System.out.println("Error playing sound");
            return;
        }
        SoundLoader.al.alSourcei (source, AL.AL_BUFFER, buffer);

        int[] test = new int[1];
        SoundLoader.al.alSourcefv(source, AL.AL_POSITION, new float[]{x,y,0}, 0);
        if(soundCategory == SoundLoader.SoundCategory.LAYER1){
            updateModel(source, SoundLoader.SoundBuffer.MUSIC);
        }else {
            updateModel(source, SoundLoader.SoundBuffer.AMBIENT);
            ambientCount++;
        }
        SoundLoader.al.alSourcePlay(source);
        sourcesMap.put(source, soundCategory);
        System.out.println("play "+soundCategory.toString());

    }

    public void playAmbientSound(){
        synchronized (GameWindow.syncSound) {
            if(GameWindow.currentRandomCase != null) {
                System.out.println("ambient : "+GameWindow.currentRandomCase.getSoundCategory().toString());
                if (GameWindow.currentRandomCase instanceof Building)
                    playSound(GameWindow.currentRandomCase.getSoundCategory(), ((Building) GameWindow.currentRandomCase).getxPos(), ((Building) GameWindow.currentRandomCase).getyPos());
                else if (GameWindow.currentRandomCase instanceof Terrain)
                    playSound(GameWindow.currentRandomCase.getSoundCategory(), ((Terrain) GameWindow.currentRandomCase).getxPos(), ((Terrain) GameWindow.currentRandomCase).getyPos());
            }else{
                playSound(SoundLoader.SoundCategory.VEGETATION);
            }
        }
    }
    public void updateModel(int source, SoundLoader.SoundBuffer type){
        if(type == SoundLoader.SoundBuffer.MUSIC || true){
            SoundLoader.al.alSourcef (source, AL.AL_REFERENCE_DISTANCE, 1000f);
            SoundLoader.al.alSourcef(source,AL.AL_ROLLOFF_FACTOR, 0f);
            SoundLoader.al.alSourcef(source,AL.AL_MAX_DISTANCE, 1000F);
        }else{
            SoundLoader.al.alSourcef (source, AL.AL_REFERENCE_DISTANCE,1000.0f);
            SoundLoader.al.alSourcef(source,AL.AL_ROLLOFF_FACTOR, 1f);
            SoundLoader.al.alSourcef(source,AL.AL_MAX_DISTANCE, 3000f);
        }
    }

    public void stopAll(){
        for(int s : sourcesMap.keySet()){
            SoundLoader.al.alSourceStop(s);
        }
    }

    public void stopMusic(){
        for(int s : sourcesMap.keySet()){
            if(sourcesMap.get(s) == SoundLoader.SoundCategory.GENERAL_MUSIC){
                SoundLoader.al.alSourceStop(s);
            }
        }
    }

    public void exit()
    {
        // Release all buffer

        for (Integer source : sources) {
            SoundLoader.al.alDeleteSources(1, IntBuffer.wrap(new int[]{source}));
        }
        sources.clear();
    }

    private int generateSource(){
        int[] source = new int[1];
        int result;
        SoundLoader.al.alGenSources(1, source, 0);

        if ((result = SoundLoader.al.alGetError()) != AL.AL_NO_ERROR)
            return -1;

        SoundLoader.al.alSourcef (source[0], AL.AL_PITCH,    1.0f      );

        SoundLoader.al.alSourcef (source[0], AL.AL_GAIN,     0.5f      );
        SoundLoader.al.alSourcefv(source[0], AL.AL_POSITION, new float[]{0,-3136,0}, 0);
        SoundLoader.al.alSourcefv(source[0], AL.AL_VELOCITY, new float[]{0,0,0}, 0);
        SoundLoader.al.alSourcei (source[0], AL.AL_LOOPING,  AL.AL_FALSE );

        SoundLoader.al.alSourcef (source[0], AL.AL_REFERENCE_DISTANCE,    1000.0f      );
        SoundLoader.al.alSourcef(source[0],AL.AL_ROLLOFF_FACTOR, 1f);
        SoundLoader.al.alSourcef(source[0],AL.AL_MAX_DISTANCE, 3000f);
        if (SoundLoader.al.alGetError() != AL.AL_NO_ERROR) {
            System.out.println("error");
            return AL.AL_FALSE;
        }
        return source[0];
    }

    public void updateListener(float x, float y){
        float[] listenerPos = { x, y, 0.0f };

        // Velocity of the listener.
        float[] listenerVel = { 0.0f, 0.0f, 0.0f };

        SoundLoader.al.alListenerfv(AL.AL_POSITION, listenerPos, 0);
        //SoundLoader.al.alListenerfv(AL.AL_VELOCITY, listenerVel, 0);
    }
    public void updateSources(){
        int[] processed = new int[1];
        int[] buffer = new int[1];
        for(Integer s : sources){
            if(!sourcesMap.containsKey(s))
                continue;
            SoundLoader.al.alGetSourcei(s, AL.AL_SOURCE_STATE, processed,0);
            if(processed[0]==AL.AL_STOPPED){

                SoundLoader.al.alSourceStop(s);
                SoundLoader.al.alGetSourcei(s, AL.AL_BUFFER, buffer,0);
                SoundLoader.al.alSourcei(s, AL.AL_BUFFER, AL.AL_NONE);
                SoundLoader.processedSoundBuffer(buffer[0]);

                if(sourcesMap.get(s) == SoundLoader.SoundCategory.GENERAL_MUSIC || sourcesMap.get(s) == SoundLoader.SoundCategory.COMBAT_MUSIC_LONG|| sourcesMap.get(s) == SoundLoader.SoundCategory.COMBAT_MUSIC_SHORT)
                    secBeforeNextMusic = random.nextInt(60) + 30;
                else if(sourcesMap.get(s) == SoundLoader.SoundCategory.LAYER1)
                    secBeforeNextLayer1 = random.nextInt(10);
                else {
                    System.out.println("fdddddd");
                    sourcesMap.remove(s);
                    playAmbientSound();
                    continue;
                }
                sourcesMap.remove(s);
            }
        }
    }
    public void playNewMusic(){
        playSound(SoundLoader.SoundCategory.GENERAL_MUSIC);
    }

    public void playNewLayer1(){
        playSound(SoundLoader.SoundCategory.LAYER1);
    }


}
