/*
 * Copyright (C) 2010-2011 Mike Novak <michael.novakjr@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.droidkit.demos.preference;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import org.droidkit.demos.R;

public class SampleSeekBarPreference extends PreferenceActivity {
    PreferenceManager mPreferenceManager;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mPreferenceManager = getPreferenceManager();
        mPreferenceManager.setSharedPreferencesName("droidkit.demos.prefs");
        mPreferenceManager.setSharedPreferencesMode(MODE_PRIVATE);
        
        addPreferencesFromResource(R.xml.seekbar_prefs);
    }
}
