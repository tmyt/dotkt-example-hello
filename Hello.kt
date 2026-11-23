import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dotktx.compose.ui.winui.Border
import dotktx.compose.ui.winui.Button
import dotktx.compose.ui.winui.ContentDialog
import dotktx.compose.ui.winui.Grid
import dotktx.compose.ui.winui.GridSize
import dotktx.compose.ui.winui.Icon
import dotktx.compose.ui.winui.Modifier
import dotktx.compose.ui.winui.StackPanel
import dotktx.compose.ui.winui.TextBlock
import dotktx.compose.ui.winui.TextBox
import dotktx.compose.ui.winui.TitleBar
import dotktx.compose.ui.winui.Window
import dotktx.compose.ui.winui.application
import dotktx.compose.ui.winui.padding
import dotktx.compose.ui.winui.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Hello DotKt",
        state = rememberWindowState(width = 480.0, height = 320.0)
    ) {
        var name by remember { mutableStateOf("Alice") }
        var isDialogOpen by remember { mutableStateOf(false) }

        Grid(rows = listOf(GridSize.Auto, GridSize.Star())) {
            TitleBar("Hello DotKt", subtitle = "Hi, $name", icon = Icon.Glyph("\uE8A9"))
            StackPanel(spacing = 12.0, modifier = Modifier.cell(1, 0).padding(16.0)) {
                TextBox(name, onTextChange = { name = it })
                Button(onClick = { isDialogOpen = true }) {
                    TextBlock("Hello")
                }
            }
        }

        if (isDialogOpen) {
            ContentDialog(
                onDismissRequest = { isDialogOpen = false },
                title = "Hello",
                closeButtonText = "Close"
            ) {
                TextBlock("Hello, $name!")
            }
        }
    }
}
