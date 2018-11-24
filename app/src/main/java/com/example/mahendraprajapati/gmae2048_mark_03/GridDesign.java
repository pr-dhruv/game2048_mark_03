package com.example.mahendraprajapati.gmae2048_mark_03;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class GridDesign {

    private static int screenWidth;

    public static int getTileColor(int value, Context context) {
        int colorId = 0;

        if(value == 0)
            colorId = R.color.color_0;
        else if(value == 2)
            colorId = R.color.color_2;
        else if(value == 4)
            colorId = R.color.color_4;
        else if(value == 8)
            colorId = R.color.color_8;
        else if(value == 16)
            colorId = R.color.color_16;
        else if(value == 32)
            colorId = R.color.color_32;
        else if(value == 64)
            colorId = R.color.color_64;
        else if(value == 128)
            colorId = R.color.color_128;
        else if(value == 256)
            colorId = R.color.color_256;
        else if(value == 512)
            colorId = R.color.color_512;
        else if(value == 1024)
            colorId = R.color.color_1024;
        else if(value == 2048)
            colorId = R.color.color_2048;

        return Color.parseColor(context.getResources().getString(colorId));
    }

    public static GradientDrawable getDrawableForTile(int value, Context context) {
        GradientDrawable gradientDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.tile_background);

        int color = getTileColor(value, context);
        gradientDrawable.setColor(color);

        return gradientDrawable;
    }

    public static String saveBitmap(Bitmap image, Context context) {
        String fileName = String.format("%s.%s", String.valueOf(System.currentTimeMillis()/1000),"JPG");

        ContextWrapper contextWrapper = new ContextWrapper(context);

        File folder = contextWrapper.getDir("imageDirectory", Context.MODE_PRIVATE);

        File path = new File(folder, fileName);

        FileOutputStream fileOuputStream = null;
        try {
            fileOuputStream = new FileOutputStream(path);
            image.compress(Bitmap.CompressFormat.PNG, 80, fileOuputStream);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return path.getAbsolutePath();

    }

    public static int setScreenWidthAndGetMinimalTouchEventLength(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();

        //get Screen Width in >= HoneyComb
        Point resolution = new Point();
        windowManager.getDefaultDisplay().getSize(resolution);
        screenWidth = resolution.x;

        return screenWidth / 6;
    }

    public static void updateElement(RelativeLayout relativeLayout, int tile_value, Activity activity) {
        GradientDrawable tile_draw = getDrawableForTile(tile_value, activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            relativeLayout.setBackgroundDrawable(tile_draw);
        else
            relativeLayout.setBackground(tile_draw);

        TextView textView = (TextView) relativeLayout.getChildAt(0);
        textView.setText(String.valueOf(tile_value));

    }

    public static void adjustGrid(View view) {
        int padding_size = (int) (screenWidth * (5.0 / 100.0));
        view.setPadding(padding_size, padding_size, padding_size, padding_size);

        RelativeLayout grid_layout = (RelativeLayout) view.findViewById(R.id.grid);

        padding_size = (int) (screenWidth * (2.0 / 100.0));
        grid_layout.setPadding(padding_size, padding_size, padding_size, padding_size);

        for (int i = 0; i < grid_layout.getChildCount(); i++) {
            FrameLayout child = (FrameLayout)grid_layout.getChildAt(i);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)child.getLayoutParams();
            layoutParams.width = (int) (int) (screenWidth * (20.0/100.0));
            layoutParams.height = (int) (int) (screenWidth * (20.0/100.0));

            if((i+1) % 4 != 0)
                layoutParams.rightMargin = (int) (screenWidth * (2.0/100.0));
            if(i / 12 == 0)
                layoutParams.bottomMargin = (int) (screenWidth * (2.0/100.0));
        }

    }

    public static Bitmap loadBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(687,687,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(canvas);
        return bitmap;
    }

}
