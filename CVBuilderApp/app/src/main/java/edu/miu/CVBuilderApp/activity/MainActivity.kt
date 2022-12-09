package edu.miu.CVBuilderApp.activity

import CVBuilderApp.R
import CVBuilderApp.databinding.ActivityMainBinding
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import edu.miu.CVBuilderApp.adapter.MyViewAdapter
import edu.miu.CVBuilderApp.data.Work
import edu.miu.CVBuilderApp.workdialogs.WorkDialogCommunicator
import edu.miu.CVBuilderApp.utility.AppUtils


class MainActivity : AppCompatActivity(), WorkDialogCommunicator {

    private lateinit var binding: ActivityMainBinding
    private var sharedPref: SharedPreferences = AppUtils.getSharedPref()
    private lateinit var adapter: MyViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val theme = AppUtils.getPref(getString(R.string.saved_theme))
        val user = AppUtils.getPref(getString(R.string.login_user_key))
        if (theme != null) AppUtils.decideTheme(theme)

        showWorkDialog()
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.home_menu)
                1 -> tab.text = getString(R.string.about_me_menu)
                2 -> tab.text = getString(R.string.work_menu)
                3 -> tab.text = getString(R.string.contact_menu)
            }
        }.attach()


        user?.apply { binding.toolbar.title = "$user's CV" }
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.google -> {
                    onGoogleClicked()
                    return@setOnMenuItemClickListener true
                }
                R.id.linkedIn -> {
                    onLinkedInClicked()
                    return@setOnMenuItemClickListener true
                }
                R.id.menu_main_logout -> {
                    finish()
                    return@setOnMenuItemClickListener true
                }
                else -> false
            }
        }
    }
    private fun onLinkedInClicked() {
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra(getString(R.string.intent_key), getString(R.string.my_linkedin))
        startActivity(intent)
    }
    private fun onGoogleClicked() {
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra(getString(R.string.intent_key), "https://www.google.com/")
        startActivity(intent)
    }
    private fun showWorkDialog() {
        adapter = MyViewAdapter(supportFragmentManager, lifecycle)
        binding.pager.adapter = adapter
    }

    override fun onAddWOrk(work: Work) {
        if (::adapter.isInitialized) {
            adapter.addWork(work)
        } else {
            showWorkDialog()
            adapter.addWork(work)
        }
    }

}