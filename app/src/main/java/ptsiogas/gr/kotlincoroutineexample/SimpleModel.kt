package ptsiogas.gr.kotlincoroutineexample

class SimpleModel(private var result: String?, private var code: String?, private var resultText: String?) {

    fun getResult(): String? {
        return result
    }

    fun setResult(result: String) {
        this.result = result
    }

    fun getCode(): String? {
        return code
    }

    fun setCode(code: String) {
        this.code = code
    }

    fun getResultText(): String? {
        return resultText
    }

    fun setResultText(resultText: String) {
        this.resultText = resultText
    }
}