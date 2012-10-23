package csci498.mcuin.lunchlist;

import android.os.Bundle;
import com.google.android.maps.MapActivity;

public class RestaurantMap extends MapActivity {
	@Override
	public void onCreate( Bundle savedInstanceBundle ) {
		super.onCreate( savedInstanceBundle );
		setContentView( R.layout.map );
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return( false );
	}

}
