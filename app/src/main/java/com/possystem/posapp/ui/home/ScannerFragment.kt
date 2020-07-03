package com.possystem.posapp.ui.home

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.Result
import com.possystem.posapp.network.model.Product
import com.possystem.posapp.ui.notifications.ProductsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.dm7.barcodescanner.zxing.ZXingScannerView


class ScannerFragment : Fragment(),ZXingScannerView.ResultHandler {
    private lateinit var mScannerView: ZXingScannerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mScannerView = ZXingScannerView(activity)
        mScannerView.startCamera()
        mScannerView.setResultHandler(this)
        mScannerView.setAspectTolerance(0.5f)
        mScannerView.startCamera()

        return mScannerView
    }

    override fun onResume() {
        super.onResume()
        mScannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {
        Toast.makeText(
            activity, "Contents = " + rawResult.toString() + ", Format = " + rawResult!!.barcodeFormat.name, Toast.LENGTH_SHORT).show()
        d(
            "logloglog", "Contents = " + rawResult.toString() + ", Format = " + rawResult.barcodeFormat.name
        )
        val parameters = mapOf("format" to rawResult.barcodeFormat.name, "code" to rawResult.toString())


        CoroutineScope(Dispatchers.Main).launch {
            val cameraViewModel = ViewModelProvider(this@ScannerFragment.parentFragment as CameraFragment).get(CameraViewModel::class.java)
            cameraViewModel.insert(parameters)
            mScannerView.resumeCameraPreview(this@ScannerFragment);
        }
    }
}