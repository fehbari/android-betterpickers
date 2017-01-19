package com.doomonafireball.betterpickers.handler;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Handler to deal with in-app sounds.
 *
 * @author Fernanda Bari
 */
public class SoundHandler {

    private static final String LOG_TAG = SoundHandler.class.getSimpleName();

    private static SoundPool sSoundPool;
    private static int sSoundId;
    private static boolean sIsLoaded;

    public static void load(Context context, int sound) {
        sSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sSoundPool.setOnLoadCompleteListener(sCompleteListener);
        sSoundId = sSoundPool.load(context, sound, 1);
    }

    public static void playSound(Context context) {
        try {
            if (sIsLoaded && areSoundsEnabled(context)) {
                float volume = getVolume(context);
                sSoundPool.play(sSoundId, volume, volume, 1, 0, 1);
            }
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "SoundPool not loaded. Make sure to call SoundHandler.load()", e);
        }
    }

    private static SoundPool.OnLoadCompleteListener sCompleteListener = new SoundPool.OnLoadCompleteListener() {
        @Override
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            sIsLoaded = true;
        }
    };

    private static float getVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        return actualVolume / maxVolume;
    }

    private static boolean areSoundsEnabled(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean("settings_in_app_sounds", true);
    }

}
