package com.sandrlab.drawapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static int COLOR_SELECTED;
    private static int MODE_SELECTED = 1;
    private List<Integer> figuresList = Arrays.asList(R.drawable.krivayi_line_48px, R.drawable.straight_line_48px, R.drawable.black_color_24dp, R.drawable.black_rect_48_26, R.drawable.rect_48px);

    private static Map<Integer, Integer> colorsMap;
    private static Map<Integer, Integer> figuresMap;
    private static boolean isSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initMaps();
        Log.i("желтый", String.valueOf(R.color.yellow));

    }

    private void initViews() {
        final ImageButton colorButton = findViewById(R.id.colorButton);
        final ImageButton button = findViewById(R.id.button_multi);
        final ImageButton bin = findViewById(R.id.clear);
        final CheckBox scale = findViewById(R.id.scale);
        button.setSelected(isSelected);
        final MultiView multiView = findViewById(R.id.draw_view);
        final Spinner figuresSpinner = findViewById(R.id.lines);
        PicturesAdapter figuresAdapter = new PicturesAdapter(figuresList);
        figuresSpinner.setAdapter(figuresAdapter);


        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog choose_color = ColorPickerDialogBuilder
                        .with(MainActivity.this)
                        .setTitle("Choose color")
                        .initialColor(Color.GREEN)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                COLOR_SELECTED = selectedColor;
                                MultiView.setCOLOR(COLOR_SELECTED);
                            }
                        }).build();
                choose_color.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
                choose_color.show();
            }
        });

        scale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                multiView.setmIsZooming(b);
                Log.i("опа", String.valueOf(multiView.ismIsZooming()));
            }
        });

        bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiView.clear();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = !isSelected;
                button.setSelected(flag);
                isSelected = flag;
                if (flag) {
                    MODE_SELECTED = 5;
                    MultiView.setSelectedMode(MODE_SELECTED);
                } else {
                    MODE_SELECTED = figuresMap.get(figuresList.get(figuresSpinner.getSelectedItemPosition()));
                    MultiView.setSelectedMode(MODE_SELECTED);
                }
            }
        });

        figuresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Mode_selected in line", String.valueOf(MODE_SELECTED));
                MODE_SELECTED = figuresMap.get(figuresList.get(i));
                MultiView.setSelectedMode(MODE_SELECTED);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initMaps () {
        colorsMap = new HashMap<>();
        colorsMap.put(R.drawable.black_color_24dp, getResources().getColor(R.color.black));
        colorsMap.put(R.drawable.blue_color_24dp, getResources().getColor(R.color.blue));
        colorsMap.put(R.drawable.green_color_24dp, getResources().getColor(R.color.green));
        colorsMap.put(R.drawable.purple_color_24dp, getResources().getColor(R.color.purple));
        colorsMap.put(R.drawable.red_color_24dp, getResources().getColor(R.color.red));
        colorsMap.put(R.drawable.sky_blue_color_24dp, getResources().getColor(R.color.sky_blue));
        colorsMap.put(R.drawable.yellow_color_24dp, getResources().getColor(R.color.yellow));

        figuresMap = new HashMap<>();
        figuresMap.put(R.drawable.krivayi_line_48px, 2);
        figuresMap.put(R.drawable.straight_line_48px, 1);
        figuresMap.put(R.drawable.black_color_24dp, 0);
        figuresMap.put(R.drawable.rect_48px, 3);
        figuresMap.put(R.drawable.black_rect_48_26, 4);

    }
}
