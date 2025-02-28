package dicomp.debateit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import SharedModels.*;

import dicomp.debateit.R;

public class RegisterActivity extends AppCompatActivity implements DataReceivable {
    EditText inusername, inpassword, inconfirmPassword;
    ServerBridge sb = new ServerBridge(this);
    ArrayList<Serializable> coming;
    TextView warning, successful;
    String username, password, confirmPassword;
    Button register;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        warning = (TextView)findViewById(R.id.warning);
        warning.setVisibility(View.GONE);
        successful = (TextView)findViewById(R.id.successful);
        successful.setVisibility(View.GONE);
        register = (Button)findViewById(R.id.register);
        inusername   = (EditText)findViewById(R.id.username);
        inpassword   = (EditText)findViewById(R.id.password);
        inconfirmPassword   = (EditText)findViewById(R.id.confirmPassword);
    }
    public void register(View view){
        register.setClickable(false);
        username = inusername.getText().toString();
        password = inpassword.getText().toString();
        confirmPassword = inconfirmPassword.getText().toString();
        System.out.println("asd1");
        if(!password.equals(confirmPassword)){
            System.out.println("asd2");
            warning.setVisibility(View.VISIBLE);
            register.setClickable(true);
            System.out.println("asd3");
        }
        else{
            successful.setVisibility(View.VISIBLE);
        }
    }

    public void goToLogin(View view){
        finish();
    }
    public boolean receiveAndUpdateUI(int responseId,ArrayList<Serializable> responseData) {
        return false;
    }

    @Override
    public void updateRetrieveProgress(int progress) {

    }

    private class Helper extends AsyncTask<Void, Void, User> {
        protected User doInBackground(Void... arg0) {
            coming = sb.getLeastRecentlyReceivedData();
            if(coming == null)
                register.setClickable(true);
            else{
                user = (User)coming.get(0);
                System.out.println(user.getUsername());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
            return null;
        }
    }
}
