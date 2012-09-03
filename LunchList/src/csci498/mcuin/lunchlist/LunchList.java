package csci498.mcuin.lunchlist;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LunchList extends Activity {
	
	Resturant r = new Resturant();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        
        Button save = ( Button )findViewById( R.id.save );
        save.setOnClickListener( onSave );
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			EditText name = ( EditText )findViewById( R.id.name );
			EditText address = ( EditText )findViewById( R.id.addr );
					
			r.setName( name.getText().toString() );
			r.setAddress( address.getText().toString() );
		}
	};
}    
