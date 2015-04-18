package daniel.tiendanaturista;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.provider.BaseColumns._ID;

public class Handler_sqlite extends SQLiteOpenHelper {

	public Handler_sqlite(Context ctx)
    {
            super(ctx, "Naturista", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {


            String query = "CREATE TABLE users ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "password TEXT, user TEXT);";
            db.execSQL(query);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int version_ant, int version_nue)
    {
    	db.execSQL("DROP TABLE IF EXISTS users; DROP TABLE  user");
        onCreate(db);
    }

    public void insertUser(String password, String user)
    {
            ContentValues valores = new ContentValues();
            valores.put("password", password);
            valores.put("user", user);
            this.getWritableDatabase().insert("users",null, valores);
    }





    public void updateClave(int clave, int id){
        Log.e("se va poner ", clave+"");
        ContentValues cv = new ContentValues();
        cv.put("clave",clave);
        //this.getWritableDatabase().update("celular" , cv, null , null);
        this.getWritableDatabase().update("celular",cv, _ID + "=" + id, null);

    }



    @SuppressWarnings("finally")
    public int returnUsers(String user, String password) {
        User User = new User();

        int resultado = 0;
        try {
            SQLiteDatabase db = this.getReadableDatabase();


            String selectQuery = "SELECT * FROM  users where user = '"+user+"' AND password = '"+password+"'";
            Log.e("query", selectQuery+"");




            Cursor cursor = db.rawQuery(selectQuery , null);
            resultado = cursor.getCount();
            db.close();

            Log.e("hay en la db", resultado+"");
            if(resultado >= 1){

                if (cursor.moveToFirst()) {
                    do{
                        Log.e("recorro", cursor.getString(1));

                    }while(cursor.moveToNext());
                }

                Log.e("hay en la lista", "");
            }else{
                Log.e("no hay", "");
            }
        } catch(Exception e) {
            Log.v("Login", e.getMessage());
        } finally {
            return resultado;
        }
    }

/*

    public void updateAlarma(String alarma){
    	Log.e("dato a guardatr", alarma);
    	ContentValues cv = new ContentValues();
    	cv.put("alarma",alarma);
    	this.getWritableDatabase().update("user" , cv, _ID+"="+1, null);
    }


    public void updateAudio(String audio){
    	Log.e("dato a guardatr", audio);
    	ContentValues cv = new ContentValues();
    	cv.put("audio",audio);
    	this.getWritableDatabase().update("user" , cv, _ID+"="+1, null);
    }


    public void updateMove(int move){
        Log.e("dato a guardatr", move+"");
        ContentValues cv = new ContentValues();
        cv.put("move",move);
        this.getWritableDatabase().update("user" , cv, _ID+"="+1, null);
    }




    public com.innova.master.User retunUser() {
    	com.innova.master.User user = new com.innova.master.User();
    	try {
    		SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM user", null);
            int resultado = cursor.getCount();
            db.close();
            if(resultado >= 1){

            	if (cursor.moveToFirst()) {


              	    	user.setId(cursor.getInt(0));
              	    	user.setCelular(cursor.getString(2));
              	    	user.setPassword(cursor.getInt(1));
              	    	user.setType(cursor.getInt(3));
              	    	user.setState(cursor.getInt(4));

              	    	user.setAlarma(cursor.getInt(5));
              	    	user.setVelocidad(cursor.getInt(6));
              	    	user.setAudio(cursor.getInt(7));
                        user.setMover(cursor.getInt(8));


             	   // Log.v("type", cursor.getString(2)+" hola estas");

              	}


            }
    	} catch(Exception e) {
    		Log.v("Login", e.getMessage());
    	} finally {
    		return user;
    	}
    }
*/


    public void abrir()
    {
            this.getWritableDatabase();

    }

    public void cerrar()
    {
            this.close();
    }

}
