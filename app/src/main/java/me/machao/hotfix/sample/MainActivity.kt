package me.machao.hotfix.sample

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))

        btn.setOnClickListener { doSomething() }
    }

    private fun doSomething() {
        val result = 10 / 0
        Toast.makeText(this, "result: $result", Toast.LENGTH_SHORT).show()
    }


}
