import dotktx.ui.state
import dotktx.ui.winui.*
import dotktx.ui.winui.coroutines.async
import Microsoft.UI.Xaml.Controls.ContentDialog
import Microsoft.UI.Xaml.Controls.FontIconSource
import Microsoft.UI.Xaml.Controls.StackPanel
import System.await

fun main() = winUiApp {
    // textBox と双方向に結ぶので State は差し替えず、同じインスタンスを使い続ける
    val name = state("Alice")
    
    title("Hello DotKt")
    windowSize(480.0, 320.0)
    extendContentIntoTitleBar()

    grid(rows = listOf(auto(), star())) {
        // 上段: WinUI の TitleBar コントロール (DotKtx.Ui.WinUI 0.9.2 の titleBar)
        // setTitleBarElement で置いた TitleBar がウィンドウのドラッグ領域になる
        cell(0, 0) {
            setTitleBarElement {
                titleBar(
                    title = { "Hello DotKt" },
                    subtitle = { "Hi, ${name.value}" },   // name が変わるたびに Subtitle が更新される
                    configure = { IconSource = FontIconSource().apply { Glyph = "\uE8A9" } }
                )
            }
        }

        // 下段: 本体
        cell(1, 0) {
            stackPanel(spacing = 12.0, configure = { Padding = thickness(16.0) }) {
                textBox(name, configure = { Header = "Name" })
                button("Hello") async {
                    // クリックハンドラは非 suspend なので、UI スレッドのコルーチンで await する
                    val dialog = ContentDialog().apply {
                        XamlRoot = sender.XamlRoot
                        Title = "Hello"
                        Content = "Hello, ${name.value}!"
                        CloseButtonText = "Close"
                    }
                    dialog.ShowAsync().await()
                }
            }
        }
    }
}
