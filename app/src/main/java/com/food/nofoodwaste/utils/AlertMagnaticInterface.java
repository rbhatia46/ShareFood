package com.food.nofoodwaste.utils;

import android.content.DialogInterface;

/**
 * Created by RamakrishnaAS on 27-Aug-15.
 */
public interface AlertMagnaticInterface {
    public abstract void PositiveMethod(DialogInterface dialog, int id);
    public abstract void NegativeMethod(DialogInterface dialog, int id);
}
