import dotktx.ui.state
import dotktx.ui.winui.*
import Microsoft.UI.Xaml.Controls.ContentDialog
import Microsoft.UI.Xaml.Controls.FontIconSource
import Microsoft.UI.Xaml.Controls.StackPanel
import System.AsTask

fun main() = winUiApp {
    title("Hello DotKt")
    windowSize(480.0, 320.0)
    extendContentIntoTitleBar()

    // textBox と双方向に結ぶので State は差し替えず、同じインスタンスを使い続ける
    val name = state("Alice")

    // build ラムダはレシーバを持たないので、StackPanel は戻り値で受け取る
    lateinit var panel: StackPanel

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
            panel = stackPanel(spacing = 12.0, configure = { Padding = thickness(16.0) }) {
                textBox(name, configure = { Header = "Name" })
                button("Hello") {
                    // クリックハンドラは非 suspend なので、UI スレッドのコルーチンで await する
                    launch {
                        val dialog = ContentDialog().apply {
                            XamlRoot = panel.XamlRoot
                            Title = "Hello"
                            Content = "Hello, ${name.value}!"
                            CloseButtonText = "Close"
                        }
                        // ShowAsync() は WinRT の IAsyncOperation なので Task に変換してから待つ
                        // (GetAwaiter が拡張メソッドなので、SDK 0.9.18 の .await() は直接は付かない)
                        dialog.ShowAsync().AsTask().await()
                    }
                }
            }
        }
    }
}
