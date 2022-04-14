package dev.m13d.rickandmorty.features.episodes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import dev.m13d.rickandmorty.R

class EpisodesDialogFragment : DialogFragment() {

    private lateinit var nameF: EditText
    private lateinit var seasonF: Spinner
    private lateinit var buttonOk: Button

    interface DialogCallback {
        fun onFilter()
    }

    private var callback: DialogCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        callback = targetFragment as DialogCallback?
        return inflater.inflate(R.layout.dialog_filter_episodes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        nameF = view.findViewById(R.id.name)
        seasonF = view.findViewById(R.id.season)

        nameF.setText(EpisodesFragment.filter.name)
        seasonF.setSelection(
            requireContext().resources.getStringArray(R.array.seasons)
                .indexOf(EpisodesFragment.filter.season?.split("0")?.last())
        )

        buttonOk = view.findViewById(R.id.buttonOk)
        buttonOk.setOnClickListener {
            EpisodesFragment.filter.apply {
                name = if (nameF.text.toString().isNotEmpty())
                    nameF.text.toString()
                else null
                season = if (seasonF.selectedItem.toString() != "All") {
                    "S0${seasonF.selectedItem}"
                } else null
            }
            callback?.onFilter()
            dialog?.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }
}
