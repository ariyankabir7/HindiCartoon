package com.webnexahindicartoon

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.webnexahindicartoon.constant.Constants


class BottomSheetFragment() : BottomSheetDialogFragment() {
    private var mainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mainActivity?.callHomeScreen()
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shareAppLayout = view.findViewById<LinearLayout>(R.id.shareApp)
        val closeBtn = view.findViewById<ImageView>(R.id.buttonClose)
        val privacyPolicyLayout = view.findViewById<LinearLayout>(R.id.privacyPolicy)
        val rewardLayout = view.findViewById<LinearLayout>(R.id.reward)
        val rateUsLayout = view.findViewById<LinearLayout>(R.id.rateUs)
        val exitAppLayout = view.findViewById<LinearLayout>(R.id.exitApp)

        shareAppLayout.setOnClickListener {
            val sharingText = "Watch Hindi Cartoon Free From this App: " +
                    "https://play.google.com/store/apps/details?id=com.webnexahindicartoon"

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, sharingText)

            startActivity(Intent.createChooser(shareIntent, "Share Raksha Bandhan Status"))
        }
        closeBtn.setOnClickListener {
            dialog?.dismiss()
        }
        privacyPolicyLayout.setOnClickListener {
            val uri =
                Uri.parse("https://www.app-privacy-policy.com/live.php?token=114xQngq7buVTwVcVJDFQk6IVILv4xfi") // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        rewardLayout.setOnClickListener {

           val intent=Intent(mainActivity,EarnHomeActivity::class.java)
            startActivity(intent)
        }

        rateUsLayout.setOnClickListener {
            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${Constants.APP_PACKAGE}")
                )
            startActivity(intent)
        }

        exitAppLayout.setOnClickListener {
            (context as MainActivity).finish()
        }
    }

}