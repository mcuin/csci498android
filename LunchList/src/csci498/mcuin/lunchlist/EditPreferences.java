package csci498.mcuin.lunchlist;

import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class EditPreferences extends PreferenceActivity {
	SharedPreferences prefs = null;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		addPreferencesFromResource( R.xml.preferences );
	}
	
	@Override 
	public void onResume() {
		super.onResume();
		
		prefs = PreferenceManager.getDefaultSharedPreferences( this );
		prefs.registerOnSharedPreferenceChangeListener( onChange );
	}
	
	@Override
	public void onPause() {
		prefs.unregisterOnSharedPreferenceChangeListener( onChange );
		
		super.onPause();
	}
	
	SharedPreferences.OnSharedPreferenceChangeListener onChange = new 
			SharedPreferences.OnSharedPreferenceChangeListener() {
		public void onSharedPreferenceChanged( SharedPreferences prefs, String key ) {
			if ( "alarm".equals( key ) ) {
				boolean enabled = prefs.getBoolean( key, false );
				int flag = ( enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED:
					PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
				ComponentName component = new ComponentName( EditPreferences.this, 
						OnBootReciever.class );
				
				getPackageManager().setComponentEnabledSetting( component, flag, 
						PackageManager.DONT_KILL_APP );
				
				if ( enabled ) {
					OnBootReciever.setAlarm( EditPreferences.this );
				}
				else {
					OnBootReciever.cancelAlarm( EditPreferences.this );
				}
			}
			else if ( "alram_time".equals( key ) ) {
				OnBootReciever.cancelAlarm( EditPreferences.this );
				OnBootReciever.setAlarm( EditPreferences.this );
			}
			
		}
	};
}
