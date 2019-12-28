package com.creation.apps.mada.noteo.Service;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Andrianina_pc on 01/09/2018.
 */

public class UtilService {
    @SuppressWarnings({"JavaReflectionMemberAccess", "deprecation"})
    public static void setCursorDrawableColor(EditText editText, int color) {

        try {
            setCursorPointerColor(editText, color);
            Field cursorDrawableResField = TextView.class.getDeclaredField("mCursorDrawableRes");
            cursorDrawableResField.setAccessible(true);
            int cursorDrawableRes = cursorDrawableResField.getInt(editText);
            Field editorField = TextView.class.getDeclaredField("mEditor");
            editorField.setAccessible(true);
            Object editor = editorField.get(editText);
            Class<?> clazz = editor.getClass();
            Resources res = editText.getContext().getResources();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Field drawableForCursorField = clazz.getDeclaredField("mDrawableForCursor");
                drawableForCursorField.setAccessible(true);
                Drawable drawable = res.getDrawable(cursorDrawableRes);
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                drawableForCursorField.set(editor, drawable);
            } else {
                Field cursorDrawableField = clazz.getDeclaredField("mCursorDrawable");
                cursorDrawableField.setAccessible(true);
                Drawable[] drawables = new Drawable[2];
                drawables[0] = res.getDrawable(cursorDrawableRes);
                drawables[1] = res.getDrawable(cursorDrawableRes);
                drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
                drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
                cursorDrawableField.set(editor, drawables);
            }
        } catch (Throwable t) {
        }
    }

    @SuppressWarnings({"JavaReflectionMemberAccess", "deprecation"})
    private static void setCursorPointerColor(EditText view, int color) throws Exception {
        try {
            //get the pointer resource id
            Field field = TextView.class.getDeclaredField("mTextSelectHandleRes");
            field.setAccessible(true);
            int drawableResId = field.getInt(view);

            //get the editor
            field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(view);

            //tint drawable
            Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableResId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);

            //set the drawable
            field = editor.getClass().getDeclaredField("mSelectHandleCenter");
            field.setAccessible(true);
            field.set(editor, drawable);

        } catch (Exception ex) {
        }
    }


    public static void setRadioButtonTint(RadioButton radio, int enabledColor){
        if(Build.VERSION.SDK_INT>=21) {
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{

                            new int[]{-android.R.attr.state_enabled}, //disabled
                            new int[]{android.R.attr.state_enabled} //enabled
                    },
                    new int[]{

                            Color.BLACK //disabled
                            , enabledColor//enabled
                    }
            );
            radio.setButtonTintList(colorStateList);//set the color tint list
            radio.invalidate(); //could not be necessary
        }
    }
}
