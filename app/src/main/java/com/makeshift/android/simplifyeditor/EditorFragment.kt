package com.makeshift.android.simplifyeditor

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.text.*
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.set
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.makeshift.android.simplifyeditor.api.WordFreqApi
import kotlinx.android.synthetic.main.editor_fragment_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.regex.Pattern

private const val TAG = "TAG"
private const val COMPLEXITY = 400

class EditorFragment:Fragment() {
private lateinit var editorEditText: EditText
private lateinit var questionText: TextView
private lateinit var complexWords: MutableList<String>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.editor_fragment_layout, container, false)

        editorEditText = view.findViewById(R.id.answerEditText) as EditText
        questionText = view.findViewById(R.id.questionTextView) as TextView
        complexWords = mutableListOf<String>()

        return view;
    }

    override fun onStart() {
        super.onStart()
        val editorEditTextWatcher = object : TextWatcher{

            override fun afterTextChanged(s: Editable?) {
                colorText(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            fun colorText(charSequence: CharSequence){
                editorEditText.removeTextChangedListener(this)
                val words = charSequence.split(Pattern.compile("\\s+"))
                val notWords = charSequence.split(Pattern.compile("\\S+"))
                var note = SpannableStringBuilder("")

               for(word in words){

                   val wordFreqLiveData: LiveData<List<Term>> =
                       WordFreqFetchr().fetchWords(word)
                   wordFreqLiveData.observe(
                       viewLifecycleOwner,
                       androidx.lifecycle.Observer { terms->

                           if(terms.isNotEmpty() && terms[0].frequency < COMPLEXITY && !complexWords.contains(terms[0].word)){
                               complexWords.add(terms[0].word)
                           }
                       }
                   )

                }

                var prevLength = 0


                for(i in 0 until words.size){
                    var curLength = note.length
                    note.append(words[i])
                    curLength += words[i].length
                    if(complexWords.contains(words[i].toLowerCase())){
                        note.setSpan(BackgroundColorSpan(Color.RED),
                            prevLength,
                            curLength,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }

                        if(i<notWords.size-1){
                            note.append(notWords[i+1])
                            curLength += notWords[i+1].length
                        }
                        prevLength = curLength

                }


                editorEditText.setText(note, TextView.BufferType.SPANNABLE)
                editorEditText.setSelection(note.length)

                editorEditText.addTextChangedListener(this)


            }

        }

        editorEditText.addTextChangedListener(editorEditTextWatcher)
    }



}