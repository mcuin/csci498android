package csci498.mcuin.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.app.ListActivity;


public class LunchList extends ListActivity {
	
	Cursor model = null;
	RestaurantAdapter adapter = null;
	RestaurantHelper helper = null;
	EditText name = null;
    EditText address = null;
    RadioGroup types = null;
    EditText notes = null;
    Restaurant current = null;
	
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        
        helper = new RestaurantHelper( this );
        model = helper.getAll();
        startManagingCursor( model );
        adapter = new RestaurantAdapter( model );
        setListAdapter( adapter );
        
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	helper.close();
    }

    AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick( AdapterView<?> parent, View view, int position, long id) {
		    Intent i = new Intent( LunchList.this, DetailForm.class );
		    
		    startActivity( i );
		}
	};
	
	class RestaurantAdapter extends CursorAdapter {
		RestaurantAdapter( Cursor c ) {
			super( LunchList.this, c );
		}
		
		@Override
		public void bindView( View row, Context ctxt, Cursor c ) {
			RestaurantHolder holder = (RestaurantHolder)row.getTag();
			
			holder.populateFrom( c, helper );
		}
		
		@Override
		public View newView( Context ctxt, Cursor c, ViewGroup parent ) {
			
				LayoutInflater inflater = getLayoutInflater();
				View row = inflater.inflate( R.layout.row, parent, false);
				RestaurantHolder holder = new RestaurantHolder( row );
				
				row.setTag( holder );
				
				holder = ( RestaurantHolder )row.getTag();
			
			
			
			return( row );
			}		
		}
	
	static class RestaurantHolder {
		private TextView name = null;
		private TextView address = null;
		private ImageView icon = null;
		
		RestaurantHolder( View row ) {
			name = ( TextView )row.findViewById( R.id.title );
			address = ( TextView )row.findViewById( R.id.address );
			icon = ( ImageView )row.findViewById( R.id.icon );
		}
		
		void populateFrom( Cursor c, RestaurantHelper helper ) {
			name.setText( helper.getName( c ) );
			address.setText( helper.getAddress( c ) );
			
			if( helper.getType( c ).equals( "sit_down") ) {
				icon.setImageResource( R.drawable.ball_red );
			} else if ( helper.getType( c ).equals( "take_out ") ) {
				icon.setImageResource( R.drawable.ball_yellow );
			} else {
				icon.setImageResource( R.drawable.ball_green );
			}
		}
	}
	}
