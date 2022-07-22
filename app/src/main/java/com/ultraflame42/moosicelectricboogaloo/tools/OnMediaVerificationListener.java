package com.ultraflame42.moosicelectricboogaloo.tools;

public interface OnMediaVerificationListener {
    /**
     * Called when the media has been verified after setSongInfo() is called.
     * @param playable whether the media is playable by media player
     */
    void onMediaVerified(boolean playable);

    /**
     * Called when the media has been verified. And the media is playable.
     * @param songDuration
     */
    void setSongInfo(int songDuration);
}
