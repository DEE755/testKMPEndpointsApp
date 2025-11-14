// kotlin
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun GoogleSignInButton(
    serverClientId: String,
    backendUrl: String,
    modifier: Modifier = Modifier,
    onResult: (Boolean, String?) -> Unit = { _, _ -> }
) {
    Button(
        onClick = { onResult(false, "Google Sign-In non pris en charge sur JVM") },
        modifier = modifier
    ) {
        Text(text = "Sign in with Google (JVM fallback)")
    }
}