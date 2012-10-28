package csci498.mcuin.lunchlist;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimePreference extends DialogPreference {
	private int lastHour = 0;
	private int lastMin = 0;
	private TimePicker picker = null;
	
	public static int getHour( String time ) {
		String[] pieces = time.split( ":" );
		
		return Integer.parseInt( pieces[0] );
	}
	
	public static int getMinute( String time ) {
		String[] pieces = time.split( ":" );
		
		return Integer.parseInt( pieces[1] );
	}
	
	public TimePreference( Context ctxt, AttributeSet attrs ) {
		super( ctxt, attrs );
		setPositiveButtonText( "Set" );
		setNegativeButtonText( "Cancel" );
	}
	
	@Override
	protected View onCreateDialogView() {
		picker = new TimePicker( getContext() );
		
		return picker;
	}
	
	@Override
	protected void onBindDialogView( View v ) {
		super.onBindDialogView( v );
		
		picker.setCurrentHour( lastHour );
		picker.setCurrentMinute( lastMin );
	}
	
	@Override
	protected void onDialogClosed( boolean positiveResult ) {
		super.onDialogClosed( positiveResult );
		
		if ( positiveResult ) {
			lastHour = picker.getCurrentHour();
			lastMin = picker.getCurrentMinute();
			
			String time = String.valueOf( lastHour ) + ":" + String.valueOf( lastMin );
			
			if ( callChangeListener( time ) ) {
				persistString( time );
			}
		}
	}
	
	@Override
	protected Object onGetDefaultValue( TypedArray a, int index ) {
		return a.getString( index );
	}
	
	protected void onSetIntialValue( boolean restoreValue, Object defaultValue ) {
		String time = null;
		
		if ( restoreValue ) {
			if ( defaultValue == null ) {
				time = getPersistedString( "00:00" );
			}
			else {
				time = getPersistedString( defaultValue.toString() );
			}
		} 
		else {
			time = defaultValue.toString();
		}
		
		lastHour = getHour( time );
		lastMin = getMinute( time );
	}

}
