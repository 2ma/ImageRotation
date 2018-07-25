package hu.am2.imagerotation

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.scalpel.ScalpelFrameLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ScalpelFrameLayout.RotationListener {
    override fun onRotation(rotationX: Float, rotationY: Float) {
        rX = rotationX
        rY = rotationY
        rotationXText.text = getString(R.string.rotationX, rotationX)
        rotationYText.text = getString(R.string.rotationY, rotationY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scalpelView.isLayerInteractionEnabled = true
        scalpelView.setRotationListener(this)
        resetButton.setOnClickListener { scalpelView.resetRotation() }
        imageSelectButton.setOnClickListener{
            loadImage()
        }
        img?.let {
            image.setImageBitmap(it)
            scalpelView.setRotation(rX, rY)
            rotationXText.text = getString(R.string.rotationX, rX)
            rotationYText.text = getString(R.string.rotationY, rY)
        }
    }

    private fun loadImage() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.type = "image/*"
        startActivityForResult(Intent.createChooser(i, "Complete action using"), 1337)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1337) {
            data?.let {
                img = MediaStore.Images.Media.getBitmap(contentResolver, it.data)
                image.setImageBitmap(img)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (it.itemId == R.id.menu_about) {
                val web = WebView(this)
                web.loadUrl("file:///android_asset/license.html")
                val dialog = AlertDialog.Builder(this)
                        .setTitle(R.string.license)
                        .setView(web)
                        .setPositiveButton(android.R.string.ok) {
                            dialogInterface, _ -> dialogInterface.dismiss()
                        }
                dialog.create().show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //dirty solution for rotation
    companion object {
        var img: Bitmap? = null
        var rX = 0f
        var rY = 0f
    }
}
