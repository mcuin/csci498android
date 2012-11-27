package csci498.mcuin.lunchlist;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

public class LunchList extends FragmentActivity implements LunchFragment.OnRestaurantListener {
    public final static String ID_EXTRA = "csci498.mcuin.lunchlist._ID";
    LunchFragment lunch = ( LunchFragment )getSupportFragmentManager().findFragmentById( R.id.lunch );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        
        lunch.setOnRestaurantListener( this );
    }
    
    public void onRestaurantSelected( long id ) {
    	if ( findViewById( R.id.details ) == null ) {
    	Intent i = new Intent( this, DetailForm.class );
    	
    	i.putExtra( ID_EXTRA, String.valueOf( id ) );
    	startActivity( i );
    } else {
    	
    }
    }
}
