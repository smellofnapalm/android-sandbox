import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.geoquiz.R

const val CHEAT_BUTTON_PRESSED_KEY = "CHEAT_BUTTON_PRESSED_KEY"
const val ANSWER_TEXT_KEY = "ANSWER_TEXT_KEY"

class CheatViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    var cheatButtonPressed: Boolean = false
        get() = savedStateHandle[CHEAT_BUTTON_PRESSED_KEY] ?: field
        set(value) { savedStateHandle[CHEAT_BUTTON_PRESSED_KEY] = value }
    var answerText: Int = R.string.empty_text
        get() = savedStateHandle[ANSWER_TEXT_KEY] ?: field
        set(value) { savedStateHandle[ANSWER_TEXT_KEY] = value }
}