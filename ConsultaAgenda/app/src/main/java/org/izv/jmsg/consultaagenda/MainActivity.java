package org.izv.jmsg.consultaagenda;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private final int CONTACTS_PERMISSION = 1;
    private final String TAG = "xyzxz";

    private Button btSearch;
    private EditText etPhone;
    private TextView tvContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG, "onCreate");
        initalize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            viewSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void viewSettings() {
        //intent = intencion
        //Intunciones explicitas o explicitas
        //Explicita: Definir que quiero ir desde el contexto actual a un contexto que se crea con la clase SettingActivity.
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v(TAG, "onRequestPermission");

        switch (requestCode){
            case CONTACTS_PERMISSION:
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    search();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
    }

    private void explain() {
        showRationalDialog(getString(R.string.title),getString(R.string.message),
                Manifest.permission.READ_CONTACTS, CONTACTS_PERMISSION);
    }

    private void initalize() {
        btSearch = findViewById(R.id.btSearch);
        etPhone = findViewById(R.id.etPhoneContact);
        tvContact = findViewById(R.id.tvContact);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchIfPermitted();
            }
        });
        //etPhone.setOnClickListener();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        requestPermissions(new String []{Manifest.permission.READ_CONTACTS}, CONTACTS_PERMISSION);
    }

    private void search() {

        Uri uri2 = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String proyeccion2[] = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        String seleccion2 = null;
        String argumentos2[] = null;
        String orden2 = null;

        Cursor cursor2 = getContentResolver().query(uri2, proyeccion2, seleccion2, argumentos2, orden2);
        String columnas2[] = cursor2.getColumnNames();

        for (String s: columnas2){
            Log.v(TAG, s);
        }

        int columnaNombre = cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int columnaNum = cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        String nombre = "";
        String numero;

        while (cursor2.moveToNext()){
            numero = cursor2.getString(columnaNum);
            numero = numero.replaceAll("[^0-9]", "");
            if (numero.equals(etPhone.getText().toString())){
                nombre += cursor2.getString(columnaNombre) + " ";
                Log.v(TAG, nombre + ": " + numero);
                tvContact.setText(nombre);
            }

        }
    }

    private void searchIfPermitted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //la version de android es posterior a la 6 incluido
            if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED) {
                //ya tengo el permiso
                search();
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                    explain(); //2?? ejecucion de
            } else {
                requestPermission();//1ra ejecucion
            }
        } else {
            //la version de android es anterior a la 6
            search();
        }
    }

    private void showRationalDialog (String title, String message, String permission, int requestCode){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermission();
                    }
                });
        builder.create().show();
    }

    //Enunciado: Terminar la app, y que muestre el nombre de los contactos con el numero que se pone en el editext
    //La entrega se har?? por github, y se le mandar?? el enlace a trav??s de moodle.
}