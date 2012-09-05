package csci498.mcuin.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioButton;

public class LunchList extends Activity {
	
	List<Resturant> model = new ArrayList<Resturant>();
	ArrayAdapter<Resturant> adapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        
        Button save = ( Button )findViewById( R.id.save );
        save.setOnClickListener( onSave );
        
        ListView list= (ListView)findViewById( R.id.resturants );
        adapter = new ArrayAdapter<Resturant>( this, android.R.layout.simple_list_item_1, model );
        
        list.setAdapter( adapter );
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			Resturant r = new Resturant();
			EditText name = ( EditText )findViewById( R.id.name );
			EditText address = ( EditText )findViewById( R.id.addr );
					
			r.setName( name.getText().toString() );
			r.setAddress( address.getText().toString() );
			
			RadioGroup types = ( RadioGroup )findViewById( R.id.types );
				
				switch ( types.getCheckedRadioButtonId() ) {
				  case R.id.sit_down:
					  r.setType( "sit_down" );
					  break;
				  case R.id.take_out:
					  r.setType( "take_out" );
					  break;
				  case R.id.delivery:
					  r.setType( "delivery" );
					  break;
				}
				adapter.add( r );
			}
	};
}
