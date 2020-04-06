package sa.ksu.gpa.saleem.exercise

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import sa.ksu.gpa.saleem.R

class SettingsActivityFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}