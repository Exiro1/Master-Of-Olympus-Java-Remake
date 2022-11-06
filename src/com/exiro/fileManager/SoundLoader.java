package com.exiro.fileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.*;

import com.exiro.moveRelated.Path;
import com.jogamp.openal.AL;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.util.ALut;




public class SoundLoader {


    public enum SoundCategory{
        NULL
        , LAYER1
        , GENERAL_MUSIC
        , COMBAT_MUSIC_SHORT
        , COMBAT_MUSIC_LONG
        , MAINTENANCE_OFFICE
        , COMMON_HOUSING
        , ELITE_HOUSING
        , FARMING
        , ORCHARD
        , SHEEP_FARM
        , GOAT_FARM
        , DOCK
        , URCHIN
        , TRIREME_WHARF
        , HUNTING
        , TIMBER_MILL
        , MARBLE_QUARRY
        , MARBLE
        , MINT
        , FOUNDRY
        , WORKSHOPS
        , STORAGE
        , AGORA_FOOD
        , AGORA_FLEECE
        , AGORA_OIL
        , AGORA_WINE
        , AGORA_ARMS 
        , AGORA_CHARIOT 
        , AGORA_HORSE 
        , ARMORY 
        , ARTISAN 
        , BEACH 
        , BEAUTIFICATION 
        , BIBLIOTHEKE 
        , BOAR 
        , CATTLE 
        , CHARIOT_FACTORY 
        , CORRAL 
        , DEER 
        , DEFENSIVE 
        , DESERT 
        , DRAMA 
        , FARMLAND 
        , FOUNTAIN 
        , FRUIT_GROWERS_LODGE 
        , GOAT 
        , GYMNASIUM 
        , HORSE_RANCH 
        , INFIRMARY 
        , LAVA 
        , MAMMALDROME 
        , MEADOW 
        , MUNICIPAL 
        , MUSEUM 
        , OBSERVATORY 
        , PALACE 
        , PHILOSOPHY 
        , RACETRACK 
        , RESEARCH 
        , ROCKY 
        , SANCTUARY 
        , SHEEP 
        , STADIUM 
        , THEATRE 
        , TRADE 
        , UNIVERSITY 
        , VEGETATION 
        , WATER
        , WOLF
        , LUMBERJACK
        , COPPERMINER
        , SILVERMINER
        , STONECUTTER
    }
    private static final int MUSICBUFSIZE=0;
    private static final int VOICEBUFSIZE=10;
    private static final int AMBIENTBUFSIZE=10;
    public static AL al;
    private static int[] musicBuffer, voiceBuffer, ambientBuffer;

    private static int[] musicBufferUse, voiceBufferUse, ambientBufferUse;

    private static HashMap<Integer, Integer> musicMap, voiceMap, ambientMap;
    private static Map<SoundCategory, ArrayList<String>> soundCategoryPathMap;

    public static ArrayList<String> getSoundPaths(SoundCategory soundCategory){
        return soundCategoryPathMap.getOrDefault(soundCategory, new ArrayList<>());
    }
    private static void loadMap(){
        soundCategoryPathMap = new HashMap<>();
        String path = "Assets/Audio/sound_fxU.txt";
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            String currentCat = "";
            SoundCategory currCat = null;
            soundCategoryPathMap.put(SoundCategory.LAYER1, new ArrayList<>());
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.contains("CATEGORY")) {
                    currentCat = data.split(" ")[1];
                    currCat = SoundCategory.valueOf(currentCat);
                    soundCategoryPathMap.put(currCat, new ArrayList<>());
                }else if(!currentCat.equals("") && data.contains(".wav")) {
                    soundCategoryPathMap.get(currCat).add(data.replaceAll(" ", "").replaceAll("\t",""));
                }else if(!currentCat.equals("") && data.contains(".ogg")) {
                    soundCategoryPathMap.get(currCat).add(data.replaceAll(" ", ""));
                }else if(data.contains(".wav")){
                    soundCategoryPathMap.get(SoundCategory.LAYER1).add(data.split(".wav")[0]+".wav");
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    static Random random;
    public static int requestSoundBuffer(SoundCategory s){
        if(s != SoundCategory.NULL && soundCategoryPathMap.get(s).size() > 0) {
            int r = random.nextInt(soundCategoryPathMap.get(s).size());
            String filename = soundCategoryPathMap.get(s).get(r);
            if (s == SoundCategory.LAYER1) {
                filename = "Assets/Audio/Ambient/Layer1/" + filename;
                if (ambientMap.containsKey(filename.hashCode())) {
                    ambientBufferUse[ambientMap.get(filename.hashCode())]++;
                    return ambientBuffer[ambientMap.get(filename.hashCode())];
                }
                System.out.println(filename);
                return SoundLoader.loadSound(filename, SoundBuffer.AMBIENT);
            } else {
                filename = "Assets/Audio/Ambient/Layer2/" + filename;
                if (ambientMap.containsKey(filename.hashCode())) {
                    ambientBufferUse[ambientMap.get(filename.hashCode())]++;
                    return ambientBuffer[ambientMap.get(filename.hashCode())];
                }
                return SoundLoader.loadSound(filename, SoundBuffer.AMBIENT);
            }
        }
        return -1;
    }

    public static int requestSoundBuffer(String filename, SoundBuffer type) {
        switch (type){
            case MUSIC -> {
                if(musicMap.containsKey(filename.hashCode())){
                    musicBufferUse[musicMap.get(filename.hashCode())]++;
                    return musicBuffer[musicMap.get(filename.hashCode())];
                }
            }
            case VOICE -> {
                if(voiceMap.containsKey(filename.hashCode())){
                    voiceBufferUse[voiceMap.get(filename.hashCode())]++;
                    return voiceBuffer[voiceMap.get(filename.hashCode())];
                }
            }
            case AMBIENT -> {
                if(ambientMap.containsKey(filename.hashCode())){
                    ambientBufferUse[ambientMap.get(filename.hashCode())]++;
                    return ambientBuffer[ambientMap.get(filename.hashCode())];
                }
            }
        }
        return SoundLoader.loadSound(filename,type);
    }





    private static int getIndexFromBufferID(int bufferID, SoundBuffer type){
        int idx = 0;
        switch (type){
            case MUSIC -> {
                for(Integer i : musicBuffer){
                    if(i == bufferID)
                        return idx;
                    idx++;
                }
                return -1;
            }
            case VOICE -> {
                for(Integer i : voiceBuffer){
                    if(i == bufferID)
                        return idx;
                    idx++;
                }
                return -1;
            }
            case AMBIENT -> {
                for(Integer i : ambientBuffer){
                    if(i == bufferID)
                        return idx;
                    idx++;
                }
                return -1;
            }
        }
        return -1;
    }

    public static void processedSoundBuffer(int buffer) {
        int idx;
        if((idx = getIndexFromBufferID(buffer, SoundBuffer.MUSIC)) >= 0){
            musicBufferUse[idx]--;
        }else if((idx = getIndexFromBufferID(buffer, SoundBuffer.VOICE)) >= 0){
            voiceBufferUse[idx]--;
        }else if((idx = getIndexFromBufferID(buffer, SoundBuffer.AMBIENT)) >= 0){
            ambientBufferUse[idx]--;
        }
    }

    public enum SoundBuffer
    {
        MUSIC,VOICE,AMBIENT
    }



    public static void init(){
        musicBuffer = new int[MUSICBUFSIZE];
        voiceBuffer = new int[VOICEBUFSIZE];
        ambientBuffer = new int[AMBIENTBUFSIZE];
        
        musicBufferUse = new int[MUSICBUFSIZE];
        voiceBufferUse = new int[VOICEBUFSIZE];
        ambientBufferUse = new int[AMBIENTBUFSIZE];
        
        musicMap = new HashMap<>();
        voiceMap = new HashMap<>();
        ambientMap = new HashMap<>();

        random = new Random();
        loadMap();

        al = ALFactory.getAL();

        // Initialize OpenAL and clear the error bit

        ALut.alutInit();
        al.alGetError();
        al.alGenBuffers(VOICEBUFSIZE, voiceBuffer, 0);
        //al.alGenBuffers(MUSICBUFSIZE, musicBuffer, 0);
        al.alGenBuffers(AMBIENTBUFSIZE, ambientBuffer, 0);
    }

    public static void exit() {
        al.alDeleteBuffers(VOICEBUFSIZE, voiceBuffer, 0);
        //al.alDeleteBuffers(MUSICBUFSIZE, musicBuffer, 0);
        al.alDeleteBuffers(AMBIENTBUFSIZE, ambientBuffer, 0);
    }

    private static int unloadSound(String filename, SoundBuffer type){
        switch (type){
            case MUSIC -> {
                int idx = musicMap.get(filename.hashCode());
                musicMap.remove(filename.hashCode());
            }
            case VOICE -> {
                int idx = voiceMap.get(filename.hashCode());
                voiceMap.remove(filename.hashCode());
            }
            case AMBIENT -> {
                int idx = ambientMap.get(filename.hashCode());
                ambientMap.remove(filename.hashCode());
            }
        }
        return 0;
    }

    private static boolean unusedBuffer(int idx, SoundBuffer type){
        switch (type){

            case MUSIC -> {
                return musicBufferUse[idx]==0;
            }
            case VOICE -> {
                return voiceBufferUse[idx]==0;
            }
            case AMBIENT -> {
                return ambientBufferUse[idx]==0;
            }
        }
        return false;
    }

    private static int loadSound(String filename, SoundBuffer type){
        int[] format = new int[1];
        int[] size = new int[1];
        ByteBuffer[] data = new ByteBuffer[1];
        int[] freq = new int[1];
        int[] loop = new int[1];

        if (al.alGetError() != AL.AL_NO_ERROR) {
            return AL.AL_FALSE;
        }

        WavReader.alutLoadWAVFile(
                filename,
                format,
                data,
                size,
                freq,
                loop);

        switch (type){

            case MUSIC -> {
                int idx = 0;
                for(int b : musicBuffer){
                    if(unusedBuffer(idx, type))
                    {
                        break;
                    }
                    idx++;
                }
                if(idx >= MUSICBUFSIZE)
                    return -1;
                if(musicBuffer[idx] != -1){
                    int toDelete = -1;
                    for(Map.Entry<Integer, Integer> kvp : musicMap.entrySet()){
                        if(kvp.getValue() == idx)
                            toDelete = kvp.getKey();
                    }
                    musicMap.remove(toDelete);
                }
                al.alGetError();
                al.alBufferData(
                        musicBuffer[idx],
                        format[0],
                        data[0],
                        size[0],
                        freq[0]);


                musicMap.put(filename.hashCode(),idx);
                musicBufferUse[idx]=1;
                return musicBuffer[idx];
            }
            case VOICE -> {
                int idx = 0;
                for(int b : voiceBuffer){
                    if(unusedBuffer(idx, type))
                    {
                        break;
                    }
                    idx++;
                }
                if(idx >= VOICEBUFSIZE)
                    return -1;
                if(voiceBuffer[idx] != -1){
                    int toDelete = -1;
                    for(Map.Entry<Integer, Integer> kvp : voiceMap.entrySet()){
                        if(kvp.getValue() == idx)
                            toDelete = kvp.getKey();
                    }
                    voiceMap.remove(toDelete);
                }
                al.alBufferData(
                        voiceBuffer[idx],
                        format[0],
                        data[0],
                        size[0],
                        freq[0]);
                voiceMap.put(filename.hashCode(),idx);
                voiceBufferUse[idx]=1;
                return voiceBuffer[idx];
            }
            case AMBIENT -> {
                int idx = 0;
                for(int b : ambientBuffer){
                    if(unusedBuffer(idx, type))
                    {
                        break;
                    }
                    idx++;
                }
                if(idx >= AMBIENTBUFSIZE)
                    return -1;
                if(ambientBuffer[idx] != -1){
                    int toDelete = -1;
                    for(Map.Entry<Integer, Integer> kvp : ambientMap.entrySet()){
                        if(kvp.getValue() == idx)
                            toDelete = kvp.getKey();
                    }
                    ambientMap.remove(toDelete);
                }
                al.alBufferData(
                        ambientBuffer[idx],
                        format[0],
                        data[0],
                        size[0],
                        freq[0]);
                ambientMap.put(filename.hashCode(),idx);
                ambientBufferUse[idx]=1;

                if (al.alGetError() != AL.AL_NO_ERROR) {
                    return AL.AL_FALSE;
                }
                return ambientBuffer[idx];
            }
        }
        return -1;
    }






}
