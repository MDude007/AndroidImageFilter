package com.example.androidimagefilter;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.androidimagefilter.Interface.EditImageFragmentListner;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditImageFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private EditImageFragmentListner listner;
    SeekBar seekbar_brightness, seekbar_contrast, seekbar_saturation;

    public void setListner(EditImageFragmentListner listner) {
        this.listner = listner;
    }

    public EditImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_edit_image, container, false);
        seekbar_brightness = (SeekBar)itemView.findViewById(R.id.seekbar_brightness);
        seekbar_contrast = (SeekBar)itemView.findViewById(R.id.seekbar_contrast);
        seekbar_saturation = (SeekBar)itemView.findViewById(R.id.seekbar_saturation);

        seekbar_brightness.setMax(200);
        seekbar_brightness.setProgress(100);

        seekbar_contrast.setMax(20);
        seekbar_contrast.setProgress(0);

        seekbar_saturation.setMax(30);
        seekbar_saturation.setProgress(10);

        seekbar_saturation.setOnSeekBarChangeListener(this);
        seekbar_contrast.setOnSeekBarChangeListener(this);
        seekbar_brightness.setOnSeekBarChangeListener(this);

        return itemView;

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(listner != null){
            if(seekBar.getId() == R.id.seekbar_brightness){
                listner.onBrightnessChanged(progress-100);
            }
            else if(seekBar.getId() == R.id.seekbar_contrast){
                progress += 10;
                float value = .10f*progress;
                listner.onContrastChanged(value);
            }
            else if(seekBar.getId() == R.id.seekbar_saturation){
                float value = .10f*progress;
                listner.onSaturationChanged(value);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if(listner != null){
            listner.onEditStarted();
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(listner != null){
            listner.onEditCompleted();
        }
    }

    public void resetControls(){
        seekbar_brightness.setProgress(100);
        seekbar_contrast.setProgress(0);
        seekbar_saturation.setProgress(10);
    }
}
