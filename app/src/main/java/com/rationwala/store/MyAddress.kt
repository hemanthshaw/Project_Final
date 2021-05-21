package com.rationwala.store

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rationwala.store.adapters.LocationAdapter
import com.rationwala.store.classes.LocationClass
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.activity_my_address.*
import kotlinx.android.synthetic.main.activity_my_address.back
import java.util.*
import kotlin.collections.HashMap

@Suppress("DEPRECATION")
class MyAddress : AppCompatActivity(),AddAddress1 ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    ResultCallback<LocationSettingsResult> {
    private lateinit var mMap: GoogleMap
    protected var mGoogleApiClient: GoogleApiClient? = null
    protected var locationRequest: LocationRequest? = null
    var REQUEST_CHECK_SETTINGS = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_address)
        var hm= LinearLayoutManager(this@MyAddress, LinearLayoutManager.VERTICAL,false)
        rview.layoutManager = hm
        addAdreess.setOnClickListener {
                startActivity(Intent(this,AddAddress::class.java).putExtra("lati","0")
                    .putExtra("longi","0").putExtra("activity","MyAddress")
                    )
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);

        }
        addaddress.setOnClickListener {
            startActivity(Intent(this,AddAddress::class.java).putExtra("lati","0")
                .putExtra("longi","0").putExtra("activity","MyAddress")
            )
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        }
        back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);

        }
        choosead.setOnClickListener {
            var status = ContextCompat.checkSelfPermission(
                this@MyAddress,
                Manifest.permission.ACCESS_FINE_LOCATION)
            if(status== PackageManager.PERMISSION_GRANTED){
                if(isLocationEnabled(this@MyAddress))
                {
                    setLocation()
                }
                else
                {
                    initializeLocation()
                }
                /*startActivity(Intent(this@MyAddress,MapsActivity::class.java).putExtra("lati","0").putExtra("longi","0")
                    .putExtra("activity","MyAddress"))
                overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);*/


            }else{
                ActivityCompat.requestPermissions(this@MyAddress,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    111)
            }
        }

        loadrview()

    }

    private fun setLocation() {
        val colors = intArrayOf(R.color.gradStart,R.color.gradEnd)
        val pd = SimpleArcDialog(this)
        var ar = ArcConfiguration(this)
        ar.colors = colors
        pd.setConfiguration(ar)
        pd.setTitle("Loading..")
        pd.setCanceledOnTouchOutside(false)
        pd.setCancelable(false)
        pd.show()
        var lManager: LocationManager = getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager
        /*lManager.getLastKnownLocation(
            LocationManager.NETWORK_PROVIDER
        )*/
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        lManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000.toLong(), 1.toFloat(),
            object : LocationListener {
                override fun onLocationChanged(l: Location) {
                    pd.dismiss()
                    var lati = l.latitude
                    var longi = l.longitude
                    // Add a marker in Sydney and move the camera


                    //Toast.makeText(this@MapsActivity, "" + lati + longi, Toast.LENGTH_LONG).show()

                        var g = Geocoder(this@MyAddress, Locale.getDefault())
                        var a=g.getFromLocation(l.latitude,l.longitude,2)
                        var address = a.get(0)
                        var i = Intent(this@MyAddress, AddAddress::class.java)
                        i.putExtra("lati", lati.toString())
                        i.putExtra("longi", longi.toString())
                        i.putExtra("activity","MyAddress")
                        Toast.makeText(this@MyAddress,address.getAddressLine(0)+"\n"+address.locality,Toast.LENGTH_LONG).show()
                        startActivity(i)
                        finish()

                        overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);


                    lManager.removeUpdates(this)

                }

                override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
                }

                override fun onProviderEnabled(provider: String) {
                }

                override fun onProviderDisabled(provider: String) {
                }

            })
    }

    private fun initializeLocation() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this).build()
        mGoogleApiClient!!.connect()
        locationRequest = LocationRequest.create()
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest!!.setInterval(30 * 1000)
        locationRequest!!.setFastestInterval(5 * 1000)
    }
    private fun loadrview() {
        var uid =FirebaseAuth.getInstance().uid
        var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation").child(uid.toString()).child("mylocation")
        db.addValueEventListener(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists()) {
                        l1.visibility = View.GONE
                        var chi = p0.children
                        var lis = mutableListOf<LocationClass>()
                        chi.forEach {
                            var k = it.key.toString()
                            var chi1 = it.children
                            var map = HashMap<String, String>()
                            chi1.forEach {
                                map.put(it.key.toString(), it.value.toString())
                            }
                            var l = LocationClass(
                                map.getValue("locationname"),
                                map.getValue("address"),
                                map.getValue("area"),
                                map.getValue("landmark"),
                                map.getValue("pincode"),
                                map.getValue("lati"),
                                map.getValue("longi")
                                ,
                                map.getValue("mobile"),
                                k
                            )
                            lis.add(l)
                        }
                        // Toast.makeText(this@SelectAddress,""+lis.size,Toast.LENGTH_LONG).show()
                        rview.adapter = LocationAdapter(this@MyAddress, lis)
                        rview.visibility = View.VISIBLE

                    }
                    else
                    {
                        l1.visibility = View.VISIBLE
                        rview.visibility = View.GONE
                    }
                }

            })


    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            if(isLocationEnabled(this@MyAddress))
            {
                setLocation()

            }
            else
            {
                initializeLocation()
            }

        }else{
            Toast.makeText(this@MyAddress,
                "App Can't read Location info...",
                Toast.LENGTH_LONG).show()
        }
    }


    override fun addAddress(l: LocationClass) {
        var uid = FirebaseAuth.getInstance().uid
        var db =  FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation").child(uid.toString()).child("address")
        db.setValue(l)
        Toast.makeText(this,"default address is changed", Toast.LENGTH_LONG).show()
        finish()
    }

    override fun clearcart() {

    }

    override fun onStart() {
        super.onStart()
        var cm=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(cm.activeNetworkInfo==null)
        {
            startActivity(Intent(this,NetConnection::class.java))
        }
        var user = FirebaseAuth.getInstance().currentUser
        if(user==null)
        {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        }
    }
    private fun isLocationEnabled(mContext: Context): Boolean {
        val lm = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && lm.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }


    override fun onConnected(p0: Bundle?) {

        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
        builder.setAlwaysShow(true)
        val result = LocationServices.SettingsApi.checkLocationSettings(
            mGoogleApiClient,
            builder.build()
        )

        result.setResultCallback(this)
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onResult(@NonNull locationSettingsResult: LocationSettingsResult) {
        val status: Status = locationSettingsResult.getStatus()
        when (status.getStatusCode()) {
            LocationSettingsStatusCodes.SUCCESS -> {
            }
            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                 //  Location settings are not satisfied. Show the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(
                        this@MyAddress,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (e: IntentSender.SendIntentException) {

                    //failed to show
                }
            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {
                setLocation()
                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
                finish()
            }

        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_left,
            R.anim.slide_out_right);
    }



}