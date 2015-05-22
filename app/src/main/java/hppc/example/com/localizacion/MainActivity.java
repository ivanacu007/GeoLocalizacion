package hppc.example.com.localizacion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;


public class MainActivity extends Activity implements LocationListener{
    LocationManager handle; //Gestor del servicio de localización
    private boolean servicioActivo;

    private Button botonActivar;
    public TextView longitud;
    public TextView latitud;
    private TextView proveedor;
    private String provider,visible;
    private TextView precision;
    private Button mapa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonActivar = (Button)findViewById(R.id.BotonActivar);
        latitud = (TextView)findViewById(R.id.latitud);
        longitud = (TextView)findViewById(R.id.longitud);
        proveedor = (TextView)findViewById(R.id.proveedor);
        precision = (TextView) findViewById(R.id.precision);
        mapa = (Button) findViewById(R.id.mapa);
        servicioActivo = false;
        //El botón activar permitirá activar y desactivar el servicio.
        botonActivar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (servicioActivo) {
                    pararServicio();
                } else {
                    iniciarServicio();
                }
            }
        });

    }
    public void mapa(View v){
        try {
            if(servicioActivo==true) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                startActivity(intent);
            }
            else{
                if (servicioActivo == false) {
                    Toast.makeText(this, "Proveedor desactivado", Toast.LENGTH_SHORT).show();
                }
            }

        }
        catch (Exception e) {

        }
    }
    public void pararServicio(){
        //Se para el servicio de localización
        servicioActivo = false;
        botonActivar.setText(R.string.activar);
        //Se desactivan las notificaciones
        handle.removeUpdates(this);
        proveedor.setText("Proveedor Off");
        longitud.setText("Desconocida");
        latitud.setText("Desconocida");
        precision.setText("Desconocida");

    }
    public void iniciarServicio(){
        //Se activa el servicio de localización
        servicioActivo = true;
        botonActivar.setText(R.string.desactivar);
        //Crea el objeto que gestiona las localizaciones
        handle = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        //obtiene el mejor proveedor en función del criterio asignado
        //(la mejor precisión posible)
        provider = handle.getBestProvider(c, true);
        proveedor.setText(provider+" On");
        //Se activan las notificaciones de localización con los parámetros:
        //proveedor, tiempo mínimo de actualización, distancia mínima, Locationlistener
        handle.requestLocationUpdates(provider, 20000, 0, this);
        //Obtenemos la última posición conocida dada por el proveedor
        Location loc = handle.getLastKnownLocation(provider);
        muestraPosicionActual(loc);
    }
    public void muestraPosicionActual(Location loc){
        if(loc == null){//Si no se encuentra localización, se mostrará "Desconocida"
            longitud.setText("Desconocida");
            latitud.setText("Desconocida");
            precision.setText("Desconocida");

        }else{//Si se encuentra, se mostrará la latitud y longitud
            latitud.setText("Latitud: "+String.valueOf(loc.getLatitude()));
            longitud.setText("Longitud: "+String.valueOf(loc.getLongitude()));
            precision.setText(String.valueOf(loc.getAccuracy()));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        muestraPosicionActual(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
