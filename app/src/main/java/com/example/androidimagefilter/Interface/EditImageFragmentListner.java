package com.example.androidimagefilter.Interface;

public interface EditImageFragmentListner {
    void onBrightnessChanged(int brightness);
    void onSaturationChanged(float saturation);
    void onContrastChanged(float contrast);
    void onEditStarted();
    void onEditCompleted();

}
