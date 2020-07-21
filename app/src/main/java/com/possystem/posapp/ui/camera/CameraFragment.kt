package com.possystem.posapp.ui.camera

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.internal.Objects
import com.google.android.material.chip.Chip
import com.possystem.posapp.mlkit.barcodedetection.BarcodeProcessor
import com.possystem.posapp.mlkit.camera.CameraSource
import com.possystem.posapp.mlkit.camera.CameraSourcePreview
import com.possystem.posapp.mlkit.camera.GraphicOverlay
import com.possystem.posapp.mlkit.camera.WorkflowModel
import com.possystem.posapp.mlkit.settings.SettingsActivity
import com.possystem.posapp.R
import kotlinx.android.synthetic.main.camera_preview_overlay_kotlin.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class CameraFragment : Fragment(), View.OnClickListener {

    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var rootView:View

    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var settingsButton: View? = null
    private var flashButton: View? = null
    private var promptChip: Chip? = null
    private var promptChipAnimator: AnimatorSet? = null
    private var workflowModel: WorkflowModel? = null
    private var currentWorkflowState: WorkflowModel.WorkflowState? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cameraViewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
        rootView = inflater.inflate(R.layout.fragment_camera, container, false)
        init()
        setUpWorkflowModel()
        return rootView
    }

    private fun init() {
        preview = rootView.findViewById(R.id.camera_preview)
        graphicOverlay = rootView.camera_preview_graphic_overlay.apply {
            setOnClickListener(this@CameraFragment)
            cameraSource = CameraSource(this)
        }

        promptChip = rootView.findViewById(R.id.bottom_prompt_chip)
        promptChipAnimator =
            (AnimatorInflater.loadAnimator(
                this.context,
                R.animator.bottom_prompt_chip_enter
            ) as AnimatorSet).apply {
                setTarget(promptChip)
            }

        flashButton = rootView.findViewById<View>(R.id.flash_button).apply {
            setOnClickListener(this@CameraFragment)
        }
        settingsButton = rootView.findViewById<View>(R.id.settings_button).apply {
            setOnClickListener(this@CameraFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        workflowModel?.markCameraFrozen()
        settingsButton?.isEnabled = true
        currentWorkflowState = WorkflowModel.WorkflowState.NOT_STARTED
        cameraSource?.setFrameProcessor(BarcodeProcessor(graphicOverlay!!, workflowModel!!))
        workflowModel?.setWorkflowState(WorkflowModel.WorkflowState.DETECTING)
    }


    override fun onPause() {
        super.onPause()
        currentWorkflowState = WorkflowModel.WorkflowState.NOT_STARTED
        stopCameraPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
        cameraSource = null
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.flash_button -> {
                flashButton?.let {
                    if (it.isSelected) {
                        it.isSelected = false
                        cameraSource?.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF)
                    } else {
                        it.isSelected = true
                        cameraSource!!.updateFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
                    }
                }
            }
            R.id.settings_button -> {
                settingsButton?.isEnabled = false
                startActivity(Intent(this.activity, SettingsActivity::class.java))
            }
        }
    }

    private fun startCameraPreview() {
        val workflowModel = this.workflowModel ?: return
        val cameraSource = this.cameraSource ?: return
        if (!workflowModel.isCameraLive) {
            try {
                workflowModel.markCameraLive()
                preview?.start(cameraSource)
            } catch (e: IOException) {
                Log.e("barcodescanner", "Failed to start camera preview!", e)
                cameraSource.release()
                this.cameraSource = null
            }
        }
    }

    private fun stopCameraPreview() {
        val workflowModel = this.workflowModel ?: return
        if (workflowModel.isCameraLive) {
            workflowModel.markCameraFrozen()
            flashButton?.isSelected = false
            preview?.stop()
        }
    }

    private fun setUpWorkflowModel() {
        workflowModel = ViewModelProvider(this).get(WorkflowModel::class.java)
        // Observes the workflow state changes, if happens, update the overlay view indicators and
        // camera preview state.
        workflowModel!!.workflowState.observe(viewLifecycleOwner, Observer { workflowState ->
            if (workflowState == null || Objects.equal(currentWorkflowState, workflowState)) {
                return@Observer
            }
            currentWorkflowState = workflowState
            Log.d(
                "workflow",
                "Current workflow state: ${currentWorkflowState!!.name}"
            )
            val wasPromptChipGone = promptChip?.visibility == View.GONE
            when (workflowState) {
                WorkflowModel.WorkflowState.DETECTING -> {
                    promptChip?.visibility = View.VISIBLE
                    promptChip?.setText(R.string.prompt_point_at_a_barcode)
                    startCameraPreview()
                }
                WorkflowModel.WorkflowState.CONFIRMING -> {
                    promptChip?.visibility = View.VISIBLE
                    promptChip?.setText(R.string.prompt_move_camera_closer)
                    startCameraPreview()
                }
                WorkflowModel.WorkflowState.SEARCHING -> {
                    promptChip?.visibility = View.VISIBLE
                    promptChip?.setText(R.string.prompt_searching)
                    stopCameraPreview()
                }
                WorkflowModel.WorkflowState.DETECTED, WorkflowModel.WorkflowState.SEARCHED -> {
                    promptChip?.visibility = View.GONE
                    stopCameraPreview()
                }
                else -> promptChip?.visibility = View.GONE
            }

            val shouldPlayPromptChipEnteringAnimation =
                wasPromptChipGone && promptChip?.visibility == View.VISIBLE
            promptChipAnimator?.let {
                if (shouldPlayPromptChipEnteringAnimation && !it.isRunning) it.start()
            }
        })

        workflowModel?.detectedBarcode?.observe(viewLifecycleOwner, Observer { barcode ->
            if (barcode != null) {
                Log.d("loglogbarcode", barcode.rawValue.toString())
                Toast.makeText(this.context,"Scanned: ${barcode.rawValue.toString()}",Toast.LENGTH_SHORT).show()
                val parameters = mapOf( "barcode" to barcode.rawValue.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    val response = cameraViewModel.insert(parameters)
                    Toast.makeText(this@CameraFragment.context,response.message,Toast.LENGTH_SHORT).show()
                    startCameraPreview()
                }
            }
        })
    }
}