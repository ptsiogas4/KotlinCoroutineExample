package ptsiogas.gr.kotlincoroutineexample

import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.item_main_state1.*
import kotlinx.android.synthetic.main.item_progress_bars.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

    val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRegistry.addObserver(mainScope)
        setContentView(R.layout.activity_main)
        coroutineMultipleButton.setOnClickListener {
            runMultipleTasks()
        }
    }

    private var result1: Deferred<SimpleModel>? = null
    private var result2: Deferred<SimpleModel>? = null

    /*
    * This method runs multiple tasks and await them all to finish before presenting their results in the UI thread.
    * */
    private fun runMultipleTasks() = mainScope.launch {
        checkCoroutines(result1)
        checkCoroutines(result2)
        showText("")
        Log.d("START", "Coroutine: START")
        result1 = async { longProcessMethod(3) }
        result2 = async { longProcessMethod(5) }

        val simpleModel1 = result1?.await()
        val simpleModel2 = result2?.await()
        if (simpleModel1 == null || simpleModel2 == null) {
            showText("Error!")
        } else {
            //Setup a result text, combining all results and present it.
            val data = "${simpleModel1.getResult()}\n${simpleModel1.getCode()}\n" + "${simpleModel2.getResult()}\n${simpleModel2.getCode()}"
            showText(data)
        }
        Log.d("END", "Coroutine: END")
    }

    /*
    * Check if a coroutine task is already running and cancel it.
    * This is very useful in cases that we have to support only the latest call to a webservice (e.g get bank commission)
    * */
    private fun checkCoroutines(result: Deferred<SimpleModel>?) {
        if (result != null && result.isActive) {
            result.cancel()
        }
    }

    /*
    * Show/Hide loader and async method results
    * */
    private fun showText(data: String) {
        val constraintSet = ConstraintSet()
        if (data.isEmpty()) {
            progressBarLayout.visibility = View.VISIBLE
            resultText.visibility = View.GONE
            constraintSet.clone(this, R.layout.item_main_state1)
        } else {
            progressBarLayout.visibility = View.GONE
            resultText.visibility = View.VISIBLE
            constraintSet.clone(this, R.layout.item_main_state2)
        }
        resultText.text = data
        TransitionManager.beginDelayedTransition(mainConstraintLayout)
        constraintSet.applyTo(mainConstraintLayout)
    }

    /*
    * We perform a long running task in a background thread. This method returns a SimpleModel result.
    * */
    private suspend fun longProcessMethod(seconds: Long): SimpleModel = withContext(dispatcher) {
        var resultText = "res"
        for (i in 0..(1000 * TimeUnit.SECONDS.toMillis(seconds))) {
            if (resultText.length < 100) {
                resultText += i.toString()
            }
            resultText.compareTo("res")
            resultText.capitalize()
        }
        Log.d("END task", "Coroutine: task" + seconds.toString())
        SimpleModel("Coroutine: task" + seconds.toString(), seconds.toString(), resultText)
    }
}
