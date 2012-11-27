package csci498.mcuin.lunchlist;

import android.support.v4.app.Fragment;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DetailFragment extends Fragment {
	
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	RadioGroup types = null;
	RestaurantHelper helper = null;
	String restaurantId = null;
	EditText feed = null;
	TextView location = null;
	LocationManager locMgr = null;
	double latitude = 0.0d;
	double longitude = 0.0d; 
	
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		setHasOptionsMenu( true );
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		helper = new RestaurantHelper( getActivity() );
		restaurantId = getActivity().getIntent().getStringExtra( LunchList.ID_EXTRA );
		
		if ( restaurantId != null ) {
			load();
		}
	}
	
	@Override
	public void onActivityCreated( Bundle savedInstanceState ) {
		super.onActivityCreated( savedInstanceState );
		
	    name = ( EditText )getView().findViewById( R.id.name );
		address = ( EditText )getView().findViewById( R.id.addr );
		types = ( RadioGroup )getView().findViewById( R.id.types );
		notes = ( EditText )getView().findViewById( R.id.notes );
		feed = ( EditText )getView().findViewById( R.id.feed );
		location = ( TextView )getView().findViewById( R.id.location );
		locMgr = ( LocationManager )getActivity().getSystemService( Context.LOCATION_SERVICE ); 
	}
	
	public View onCreateLayout( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		return inflater.inflate( R.layout.detail_form, container, false );
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
	public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
		inflater.inflate( R.menu.details_option, menu ); 
	}
	
	@Override 
	public boolean onOptionsItemSelected( MenuItem item ) {
		if ( item.getItemId() == R.id.feed ) {
			if ( isNetworkAvailable() ) {
				Intent i = new Intent( getActivity(), FeedActivity.class );
				
				i.putExtra( FeedActivity.FEED_URL, feed.getText().toString() );
				startActivity( i );
				
			}
			else {
				Toast.makeText( getActivity(), "Sorry the Internet is not available", 
						Toast.LENGTH_LONG ).show();
			}
			
			return true;
			
		}
		
		else if ( item.getItemId() == R.id.location ) {
			locMgr.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, onLocationChange );
		}
		
		else if ( item.getItemId() == R.id.map ) {
			Intent i = new Intent( getActivity(), RestaurantMap.class );
			
			i.putExtra( RestaurantMap.EXTRA_LATITUDE, latitude );
			i.putExtra( RestaurantMap.EXTRA_LONGITUDE, longitude );
			i.putExtra( RestaurantMap.EXTRA_NAME, name.getText().toString() );
			
			startActivity( i );
			
			return true;
		}
		
		return super.onOptionsItemSelected( item );
		
	}
	
	@Override
	public void onPause() {
		save();
		locMgr.removeUpdates( onLocationChange );
		helper.close();
		
		super.onPause();
	}
	
	@Override
	public void onPrepareOptionsMenu( Menu menu ) {
		if ( restaurantId == null ) {
			menu.findItem( R.id.location ).setEnabled( false );
			menu.findItem( R.id.map ).setEnabled( false );
		}
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager
		cm = ( ConnectivityManager )getActivity().getSystemService( Context.CONNECTIVITY_SERVICE );
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
		
		latitude = helper.getLatitude( c );
		longitude = helper.getLongitude( c );
		
		location.setText( String.valueOf( helper.getLatitude( c ) ) + ", " +
		String.valueOf( helper.getLongitude( c ) ) );
		
		c.close();
	}
	
	LocationListener onLocationChange = new LocationListener() {
		public void onLocationChanged( Location fix ) {
			helper.updateLocation( restaurantId, fix.getLatitude(), fix.getLongitude() );
			location.setText( String.valueOf( fix.getLatitude() ) + ", " + String.valueOf( fix.getLongitude() ) );
			locMgr.removeUpdates( onLocationChange );
			
			Toast.makeText( getActivity(), "Location saved", Toast.LENGTH_LONG ).show();
			
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
