/*
* Copyright (C) 2013 The OmniROM Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package com.lineageos.device.DeviceSettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.PreferenceManager;

import com.lineageos.device.DeviceSettings.audio.*;
import com.lineageos.device.DeviceSettings.ModeSwitch.*;

public class Startup extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";
   
    @Override
    public void onReceive(final Context context, final Intent bootintent) {
        boolean enabled = false;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        enabled = sharedPrefs.getBoolean(DeviceSettings.KEY_DC_SWITCH, false);
        restore(DCModeSwitch.getFile(), enabled);

        enabled = sharedPrefs.getBoolean(DeviceSettings.KEY_FAST_CHARGE, false);
        restore(FastChargeSwitch.getFile(), enabled);
        
        enabled = sharedPrefs.getBoolean(DeviceSettings.KEY_FPS_INFO, false);
        if (enabled) {
            context.startService(new Intent(context, FPSInfoService.class));
        }

        VibratorStrengthPreference.restore(context);
        context.startService(new Intent(context, KeyHandler.class));

        enabled = sharedPrefs.getBoolean(DeviceSettings.KEY_BUTTON_SWAP, false);
        restore(ButtonSwap.getFile(), enabled);

        EarpieceGainPreference.restore(context);
        HeadphoneGainPreference.restore(context);
        MicGainPreference.restore(context);
        SpeakerGainPreference.restore(context);

        if (Build.DEVICE.equals("OnePlus5")) {
            restore("/proc/flicker_free/min_brightness", "66");
        } else if (Build.DEVICE.equals("OnePlus5T")) {
            restore("/proc/flicker_free/min_brightness", "302");
        }
    }

    private void restore(String file, boolean enabled) {
        if (file == null) {
            return;
        }
        if (enabled) {
            Utils.writeValue(file, "1");
        }
    }

    private void restore(String file, String value) {
        if (file == null) {
            return;
        }
        Utils.writeValue(file, value);
    }
}
