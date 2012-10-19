package csci498.mcuin.lunchlist;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DetailForm extends Activity {
	
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	RadioGroup types = null;
	RestaurantHelper helper = null;
	String restaurantId = null;
	EditText feed = null;
	TextView location = null;
	LocationManager locMgr = null;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.detail_form );
		
		helper = new RestaurantHelper( this );
	    name = ( EditText )findViewById( R.id.name );
		address = ( EditText )findViewById( R.id.addr );
		types = ( RadioGroup )findViewById( R.id.types );
		notes = ( EditText )findViewById( R.id.notes );
		feed = ( EditText )findViewById( R.id.feed );
		location = ( TextView )findViewById( R.id.location );
		locMgr = ( LocationManager )getSystemService( LOCATION_SERVICE ); 
        
        restaurantId = getIntent().getStringExtra( LunchList.ID_EXTRA );
        if( restaurantId != null) {
        	load();
        }
        
	}
	
	@Override
	public void onSaveInstanceState( Bundle state ) {
		super.onSaveInstanceState( state );
		
		name.setText( state.getString( "name" ) );
		address.setText( state.getString( "address" ) );
		notes.setText( state.getString( "notes" ) );
		types.check( state.getInt( "type" ) );
		
	}
	
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		new MenuInflater( this ).inflate( R.menu.details_option, menu );
		
		return super.onCreateOptionsMenu( menu );
		
	}
	
	@Override 
	public boolean onOptionsItemSelected( MenuItem item ) {
		if ( item.getItemId() == R.id.feed ) {
			if ( isNetworkAvailable() ) {
				Intent i = new Intent( this, FeedActivity.class );
				
				i.putExtra( FeedActivity.FEED_URL, feed.getText().toString() );
				startActivity( i );
				
			}
			else {
				Toast.makeText( this, "Sorry the Internet is not available", 
						Toast.LENGTH_LONG ).show();
			}
			
			return true;
			
		}
		
		else if ( item.getItemId() == R.id.location ) {
			locMgr.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, onLocationChange );
		}
		
		return super.onOptionsItemSelected( item );
		
	}
	
	@Override
	public void onPause() {
		save();
		locMgr.removeUpdates( onLocationChange );
		super.onPause();
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager cm = ( ConnectivityManager ).getSystemService( CONNECTIVITY_SERVICE );
		NetworkInfo info = cm.getActiveNetworkInfo();
		
		return info != null;
		
	}
	
	private void load() {
		Cursor c = helper.getById( restaurantId );
		
		c.moveToFirst();
		name.setText( helper.getName( c ) );
		address.setText( helper.getAddress( c ) );
		notes.setText( helper.getNotes( c ) );
		feed.setText( helper.getFeed( c ) );
		
		if( helper.getType( c ).equals( "sit_down") ) {
			types.check( R.id.sit_down );
		} else if ( helper.getType( c ).equals( "take_out ") ) {
			types.check( R.id.take_out );
		} else {
			types.check( R.id.delivery );
		}
		
		location.setText( String.valueOf( helper.getLatitude( c ) ) + ", " +
		String.valueOf( helper.getLongitute( c ) ) );
		
		c.close();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		helper.close();
	}
	
	LocationListener onLocationChange = new LocationListener() {
		public void onLocationChanged( Location fix ) {
			helper.updateLocation( restaurantId, fix.getLatitude(), fix.getLongitude() );
			location.setText( String.valueOf( fix.getLatitude() ) + ", " + String.valueOf( fix.getLongitude() ) );
			locMgr.removeUpdates( onLocationChange );
			
			Toast.makeText( DetailForm.this, "Location saved", Toast.LENGTH_LONG ).show();
			
		}
		
		public void onProviderDisabled( String providor ) {
			
		}
		
		public void onProviderEnabled( String providor ) {
			
		}
		
		public void onStatusChanged( String providor, int status, Bundle extras ) {
			
		}
	};
	
	private void save() {
		if ( name.getText().toString().length() > 0 ) {
			String type = null;
			
				
				switch ( types.getCheckedRadioButtonId() ) {
				  case R.id.sit_down:
					  type = "sit_down";
					  break;
				  case R.id.take_out:
					  type = "take_out";
					  break;
				  case R.id.delivery:
					  type = "delivery";
					  break;
				}
				
				if( restaurantId == null ) {
					helper.insert( name.getText().toString(), address.getText().toString(),
							type, notes.getText().toString(), feed.getText().toString() );
				} else {
					helper.update( restaurantId, name.getText().toString(), address.getText().toString(), 
							type, notes.getText().toString(), feed.getText().toString() );
				}
				
		}
	};

}
