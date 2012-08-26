package csci498.hello;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import java.util.Date;

public class HelloAndroid extends Activity implements View.OnClickListener {
	Button hello;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hello = new Button(this);
        hello.setOnClickListener(this);
        updateHello();
        setContentView(hello);
    }
    
    public void onClick(View view) {
    	updateHello();
    }
    
    private void updateHello() {
    	hello.setText(new Date().toString());
    }

}
