package daniel.tiendanaturista;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Login extends ActionBarActivity {

    public Handler_sqlite helper = new Handler_sqlite(this);
    public User objUser;
    public String urlUsers = "http://192.168.0.106:3000/employers.json";
    public EditText user, password;
    Context context;
    public String Message = "";
    public boolean error= false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        Log.e("Json", "bien json");
        helper.returnUsers("user1","123456");





        user = (EditText)findViewById(R.id.txtUsuario);
        password   = (EditText)findViewById(R.id.txtClave);



        final Button button = (Button) findViewById(R.id.btnIngresar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                //helper.returnUsers("user1","1234566");

               Message = "";
               error = false;
               if(user.getText().toString().equals("") && password.getText().toString().equals("")){
                   Message = "Ingrese su usuario y clave" ;
                   error = true;
               }else if(user.getText().toString().equals("") && !password.getText().toString().equals("") ){
                    Message = "Ingrese su usuario" ;
                   error = true;
               }else if(!user.getText().toString().equals("") && password.getText().toString().equals("") ){
                   Message = "Ingrese su clave" ;
                   error = true;
               }


                if(error){
                    Toast toast = Toast.makeText(context, Message, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else{
                    if(helper.returnUsers(user.getText().toString(),password.getText().toString())> 0){
                        Log.e("Hay Usuario", "");
                    }else{


                        Toast toast = Toast.makeText(context, "Usuario o Clave incorrecta", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    }
                }


            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sincronizar) {
            loadJson loaderTask = new loadJson();
            loaderTask.execute();
        }

        return super.onOptionsItemSelected(item);
    }



    public String dataUser() {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet("http://192.168.0.106:3000/employers.json");



        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);

                }

            } else {
                //Log.e("no se pudo bajar", "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }



    public class loadJson extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        String json = "";
        public JSONObject p;
        public String mensajeAlUsuario = "";
        public Handler_sqlite helper = new Handler_sqlite(getApplicationContext());


        @Override
        protected void onPreExecute(){
            dialog = new ProgressDialog(context);
            dialog.setTitle("Cargando");
            dialog.setMessage("Espere mientras actualizamos registros");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            json = dataUser();
            try {
                JSONArray dataArray = new JSONArray(json);



                for(int i=0; i<dataArray.length(); i++) {
                    JSONObject jsonData = dataArray.getJSONObject(i);

                    Log.e("Json Bien", jsonData.getString("name")+"bien json");
                    helper.insertUser(jsonData.getString("password"), jsonData.getString("name"));

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(Void result){
            dialog.dismiss();

            Toast toast = Toast.makeText(context, "Se realizo la actualización", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            super.onPostExecute(result);
        }


    }
}
