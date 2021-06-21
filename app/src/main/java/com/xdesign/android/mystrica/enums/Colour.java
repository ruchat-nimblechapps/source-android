package com.xdesign.android.mystrica.enums;


import com.xdesign.android.mystrica.R;

/**
 * @author keithkirk
 */
public enum Colour {
    RED(    R.color.myst_background_light_red,    R.color.myst_red,     "R"),
    GREEN(  R.color.myst_background_light_green,  R.color.myst_green,   "G"),
    BLUE(   R.color.myst_background_light_blue,   R.color.myst_blue,    "B");

    private final int backgroundResource;
    private final int lineResource;
    private final String name;

    Colour(int backgroundResource, int lineResource, String name) {
        this.backgroundResource = backgroundResource;
        this.lineResource = lineResource;
        this.name = name;
    }

    public static Colour forName(String name) {
        for (Colour colour : values()) {
            if (colour.name.equals(name)) {
                return colour;
            }
        }

        return null;
    }

    public int getBackgroundResource() {
        return backgroundResource;
    }

    public int getLineResource() {
        return lineResource;
    }

    @Override
    public String toString() {
        return name;
    }
}
