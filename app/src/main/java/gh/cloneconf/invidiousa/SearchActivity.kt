package gh.cloneconf.invidiousa

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import gh.cloneconf.invidiousa.api.Invidious
import gh.cloneconf.invidiousa.databinding.ActivitySearchBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {

    private lateinit var binds : ActivitySearchBinding

    private var job : Job? = null

    val adapter by lazy { ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ArrayList()) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binds = ActivitySearchBinding.inflate(layoutInflater).apply {
            setContentView(root)

            suggestionsLv.adapter = adapter
            suggestionsLv.setOnItemClickListener { _, _, position, _ ->
                search(adapter.getItem(position)!!)
            }


            inputEd.setOnEditorActionListener { _, _, _ ->
                search(inputEd.text.toString()); true
            }

            inputEd.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    lProgress.visibility = View.INVISIBLE
                    job?.cancel()

                    if (s.isNullOrEmpty()) return
                    val q = s.toString()
                    lProgress.visibility = View.VISIBLE

                    job = lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val data = Invidious.suggestions(q)

                            withContext(Dispatchers.Main) {
                                adapter.apply {
                                    clear()
                                }.addAll(data)

                                lProgress.visibility = View.INVISIBLE
                                adapter.notifyDataSetChanged()
                            }
                        }catch (e:Exception) {
                            withContext(Dispatchers.Main) {
                                lProgress.visibility = View.INVISIBLE
                            }
                        }
                    }


                }
                override fun afterTextChanged(s: Editable?) {}
            })



        }

    }


    override fun onResume() {
        super.onResume()
        if(binds.inputEd.requestFocus()) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(binds.inputEd, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onPause() {
        super.onPause()
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(binds.inputEd.windowToken, 0)
    }


    private fun search(q : String) {
        startActivity(Intent(this, ResultsActivity::class.java).apply {
            putExtra("q", q)
        })
    }


}