package com.possystem.posapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.possystem.posapp.R
import com.possystem.posapp.data.user_preference.UserPreference
import com.possystem.posapp.ui.activities.authentication.log_in.LogInActivity
import com.possystem.posapp.ui.adapters.BottomNavigationViewPagerAdapter
import com.possystem.posapp.ui.dashboard.DashboardFragment
import com.possystem.posapp.ui.camera.CameraFragment
import com.possystem.posapp.ui.checkout.CheckoutFragment
import kotlinx.android.synthetic.main.activity_main.*

class NavigationActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener{

    private val backStack = ArrayList<Int>()
    private var isBackPressEvent = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //temp camera permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),1)
        }
        init()
    }
    private fun init(){

        bottomNavigation.setOnNavigationItemSelectedListener(this)
        val arrayFragments: ArrayList<Fragment> = ArrayList()
        arrayFragments.add(DashboardFragment())
        arrayFragments.add(CameraFragment())
        arrayFragments.add(CheckoutFragment())
        viewPager.adapter = BottomNavigationViewPagerAdapter(this, arrayFragments)
        viewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                bottomNavigation.menu.getItem(position).isChecked = true
                if (!isBackPressEvent)
                    backStack.add(position)
                isBackPressEvent = false
            }
        })
        viewPager.offscreenPageLimit = 3
        viewPager.currentItem = 2
        toolBar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.navigation_dashboard -> {
                viewPager.currentItem = 0
                true
            }
            R.id.navigation_camera -> {
                viewPager.currentItem = 1
                true
            }
            R.id.navigation_notifications -> {
                viewPager.currentItem = 2
                true
            }
            else -> false
        }
    }

    fun logout(view:View){
        UserPreference.removeString(UserPreference.SESSION)
        UserPreference.removeString(UserPreference.USER_ID)
        val intent = Intent(this, LogInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
    override fun onBackPressed() {
       // if (!closeSideMenu()) {
            if (backStack.size > 1) {
                backStack.removeAt(backStack.size - 1)
                isBackPressEvent = true
                viewPager.currentItem = backStack.last()
            } else
                super.onBackPressed()
        }
   // }
}