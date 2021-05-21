package com.rationwala.store

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.rationwala.dukana.MyOrdersActivity
import com.rationwala.store.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.app_bar_main.cart
import kotlinx.android.synthetic.main.app_bar_main.ccount
import kotlinx.android.synthetic.main.maintainance_frag.view.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.updatefrag.view.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val bottomNavMethod =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    var fragment = HomeFragment()
                    supportFragmentManager.beginTransaction().detach(fragment).attach(fragment)
                        .replace(R.id.contentPanel, fragment!!).addToBackStack(null).commit()
                }
                R.id.category->{
                    //fragment = CatogoriesFrag()
                    var fragment = CategoriesFrag()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contentPanel, fragment!!).addToBackStack(null).commit()
                }
                R.id.search->{
                    var fragment = SearchFrag()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contentPanel, fragment!!).addToBackStack(null).commit()
                }
                R.id.notification->{
                    //fragment = BasketFrag()
                    var fragment = NotificationFrag()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contentPanel, fragment!!).addToBackStack(null).commit()
                }
                R.id.profile->{
                    var fragment = MyAccountfrag()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contentPanel, fragment!!).addToBackStack(null).commit()
                }

            }
            true
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomnv.setOnNavigationItemSelectedListener(bottomNavMethod)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        sliderr.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }
        val colors = intArrayOf(R.color.gradStart,R.color.gradEnd)
        val pd = SimpleArcDialog(this)
        var ar = ArcConfiguration(this)
        ar.colors = colors
        pd.setConfiguration(ar)
        pd.setCanceledOnTouchOutside(false)
        pd.setTitle("Loading..")
        var fragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.contentPanel, fragment!!).commit()
        loadUserDetails()

        cart.setOnClickListener {
            startActivity(Intent(this@MainActivity,CartActivity::class.java)
                .putExtra("ac","sc"))
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        }
        navView.setNavigationItemSelectedListener(this)

        var uid = FirebaseAuth.getInstance().uid
        var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation").child(uid.toString()).child("cart")

        db.addValueEventListener(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists())
                    {
                        var c=0
                        var chi = p0.children
                        chi.forEach {
                            c++
                        }
                        ccount.text = c.toString()
                    }
                    else
                    {
                        ccount.setText("0")
                    }
                }

            }
        )
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_container)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var user = FirebaseAuth.getInstance().currentUser
        when(item.itemId)
        {
            R.id.sign->{
                //Toast.makeText(this,item.title,Toast.LENGTH_LONG).show()
                if(user==null)
                    startActivity(Intent(this,LoginActivity::class.java))
                else
                    Toast.makeText(this,"Already logged in",Toast.LENGTH_LONG).show()
            }
            R.id.nav_aboutus->{
                var bundle = Bundle()
                bundle.putString("title","about")
                var fragment = AboutUs()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fragment).addToBackStack(null).commit()

            }
            R.id.nav_policy->{
                var bundle = Bundle()
                bundle.putString("title","terms")
                var fragment = AboutUs()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fragment).addToBackStack(null).commit()
            }
            R.id.nav_contact->{
                var fragment = Contactus()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fragment).addToBackStack(null).commit()
            }
            R.id.nav_policy1->{
                var bundle = Bundle()
                bundle.putString("title","privacy")
                var fragment = AboutUs()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fragment).addToBackStack(null).commit()
            }
            R.id.nav_order->{
                var fragment = OrderonPhone()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fragment).addToBackStack(null).commit()
            }
            R.id.nav_share->{
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage =
                    """
                ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                
                
                """.trimIndent()

                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            }
            R.id.nav_rate->{
                val rateIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + this.getPackageName())
                )
                startActivity(rateIntent)
            }
            R.id.nav_logout->{
                if(user!=null) {
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(this,"You have logged out ",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,LoginActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);

                }
                else
                    Toast.makeText(this,"no user is logged in ",Toast.LENGTH_LONG).show()

            }
            R.id.myorders->{
                var fragment = MyOrdersActivity()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fragment!!).addToBackStack(null).commit()
            }


        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadUserDetails() {
        var uid = FirebaseAuth.getInstance().uid
        var dbase = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation").child(uid.toString())
        dbase.addValueEventListener(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists())
                    {
                        var chi = p0.children
                        chi.forEach{
                            when(it.key)
                            {
                                "fullname"->mname.text = "Hi, "+it.value.toString()


                            }
                        }
                    }

                }

            }
        )
    }

    override fun onStart() {
        super.onStart()
        var cm=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(cm.activeNetworkInfo==null)
        {
            startActivity(Intent(this,NetConnection::class.java))
        }

    }

    override fun onBackPressed() {

        if(intent.getStringExtra("activity")!=null && intent.getStringExtra("activity").equals("orderplaced"))
        {
            /*startActivity(Intent(this,MainActivity::class.java)
                .putExtra("activity","orderplaced"))
            overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
            Toast.makeText(this,"You cant go back",Toast.LENGTH_LONG).show()*/
            var alert= android.app.AlertDialog.Builder(this)
            alert.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("yes",
                    DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
                        startActivity(Intent(this,MainActivity::class.java))
                        moveTaskToBack(true)
                    })
                .setNegativeButton("no",
                    DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.cancel()
                    })
            var al = alert.create()
            al.show()

        }
        else
        {
            super.onBackPressed()
        }
    }

}