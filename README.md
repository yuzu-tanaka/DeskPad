# DeskPad

[English]
A specialized Android application designed to transform your smartphone into a high-precision touchpad when using Pixel's desktop mode. Unlike traditional input methods, DeskPad focuses purely on pointing device functionality, allowing you to use your preferred IME (Gboard, ATOK, etc.) for text input while maintaining control over the cursor.

[日本語]
Pixel のデスクトップモード利用時に、スマホ本体を高精度なタッチパッドとして使えるようにする Android アプリです。DeskPad は IME（入力法）ではなく、ポインティングデバイス機能に特化しています。文字入力は使い慣れたキーボードアプリ（Gboard / ATOK 等）に任せつつ、端末をタッチパッドとしてフル活用できます。

---

## Features / 特徴

- **Full-Screen Touchpad / フルスクリーン・タッチパッド**:
  - Entire screen acts as a dedicated touch area.
  - 画面全体を広大な操作領域として使用。
- **Intuitive Gestures / 直感的なジェスチャ**:
  - **Single-finger drag**: Cursor movement. (1本指ドラッグ: カーソル移動)
  - **Single tap**: Left click. (1本指タップ: 左クリック)
  - **Double tap**: Double click. (ダブルタップ: ダブルクリック)
  - **Long-press + Drag**: Drag-and-drop. (長押し + ドラッグ: ドラッグ操作)
  - **Two-finger drag**: Vertical scrolling. (2本指ドラッグ: 垂直スクロール)
- **Desktop Mode Optimizations / デスクトップモード向け最適化**:
  - **180° Rotation**: Invert the UI to accommodate cable connections. (180度回転: ケーブルの向きに合わせて UI を反転可能)
  - **Screen Always-On & Low Brightness**: Prevents screen timeout while minimizing battery consumption. (画面常時点灯 & 輝度最小化: 操作中のスリープを防ぎ、電池消費を抑制)
- **Foreground Service / フォアグラウンド動作**:
  - Ensures stable operation even when switching apps.
  - Foreground Service により、バックグラウンドでも安定して動作。

## Requirements / 必要条件

- **OS**: Android 14+ (minSdk 34)
- **Device**: Desktop mode compatible devices (e.g., Google Pixel series).
- **Note**: Uses `InputManager.injectInputEvent` for mouse events. This may require specific system permissions or a debug environment to function correctly.
- **注意**: マウスイベントの注入（`InputManager.injectInputEvent`）を使用しているため、動作には適切な権限設定、またはデバッグ環境での実行が必要になる場合があります。

## Project Structure / プロジェクト構造

```text
app/src/main/java/com/example/deskpad/
├── MainActivity.kt               # UI root, rotation/brightness control, service lifecycle.
├── TouchpadComposable.kt        # Touch input detection using Jetpack Compose.
├── GestureInterpreter.kt        # Converts raw touch data into meaningful gestures.
├── VirtualMouseManager.kt       # System-level virtual mouse event injection.
└── DeskPadForegroundService.kt  # Maintains application persistence.
```

## Development / 開発者向け

Adjust sensitivity and acceleration in `VirtualMouseManager.kt`:
感度や加速度は `VirtualMouseManager.kt` 内の定数で調整可能です：

```kotlin
private var sensitivity = 1.5f
private var acceleration = 1.1f
```

## License / ライセンス

Please refer to the LICENSE file for details.
詳細は LICENSE ファイルをご確認ください。
