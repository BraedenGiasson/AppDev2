package com.example.invoiceapp_braedengiasson.settings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.invoiceapp_braedengiasson.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        try {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            // Gets the edit texts for the settings
            EditTextPreference companyName =
                    (EditTextPreference) findPreference(getString(R.string.company_name_key));
            EditTextPreference numberOfEmployees =
                    (EditTextPreference) findPreference(getString(R.string.number_of_employees_key));

            // Set the settings summary to the text result
            companyName.setSummaryProvider(
                    (Preference.SummaryProvider<EditTextPreference>) preference ->
                            companyName.getText());

            numberOfEmployees.setSummaryProvider(
                    (Preference.SummaryProvider<EditTextPreference>) preference ->
                            numberOfEmployees.getText());
        }
        catch (Exception ex){
            return;
        }
    }
}
