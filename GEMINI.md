# GEMINI.md – DeskPad Code Generation Specification
Version: 2.0  
Language: Kotlin  
UI Framework: Jetpack Compose  
Target OS: Android 14+ (minSdk 34)

---

# 0. Overview
DeskPad は Pixel のデスクトップモード利用時に、  
**スマホ本体をタッチパッドとして使えるようにするアプリ**。

- DeskPad は IME ではない  
- 文字入力はユーザーが選択した IME（Gboard / ATOK / Pixel IME）に任せる  
- DeskPad は **タッチパッド機能のみ**を提供  
- IME が表示されると OS によって押し上げられた領域で動作する  
- 2本指スクロールは最初から実装する  
- UI は Jetpack Compose で構築する（XML 不使用）

---


# 1. Project Structure (to be generated)
app/
 ├─ java/com.example.deskpad/
 │    ├─ MainActivity.kt
 │    ├─ DeskPadForegroundService.kt
 │    ├─ VirtualMouseManager.kt
 │    ├─ TouchpadComposable.kt
 │    └─ GestureInterpreter.kt
 └─ res/ └─ values/themes.xml


---

# 2. Required Components

## 2.1 MainActivity.kt
### Responsibilities
- Compose UI のルート
- タッチパッド UI の表示
- 画面回転ボタンの実装（Compose の `Modifier.graphicsLayer` で 180° 回転）
- スリープ制御（常時 ON）
- 輝度最小設定
- Foreground Service の起動
- TouchpadComposable からのジェスチャイベントを VirtualMouseManager に渡す

### Key Functions
- `override fun onCreate()`
- `@Composable fun DeskPadScreen()`
- `private fun rotateView180()`
- `private fun setScreenBrightnessMin()`
- `private fun startForegroundService()`

---

## 2.2 TouchpadComposable.kt
### Responsibilities
- Compose ベースのタッチパッド UI
- PointerInput を使ったジェスチャ検出
- GestureInterpreter にイベントを渡す

### Required Behavior
- **1本指ドラッグ → カーソル移動**
- **タップ → 左クリック**
- **ダブルタップ → ダブルクリック**
- **長押し＋ドラッグ → ドラッグ操作**
- **2本指ドラッグ → スクロール（最初から実装）**

### Key APIs
- `Modifier.pointerInput`
- `awaitPointerEventScope`
- `detectTapGestures`
- `detectDragGestures`
- `detectTransformGestures`（2本指スクロール用）

---

## 2.3 GestureInterpreter.kt
### Responsibilities
- Compose の PointerInput から得た生データを解釈し、
  VirtualMouseManager が扱いやすい形式に変換する。

### Required Behavior
- 相対移動量の計算
- 2本指スクロール量の計算
- クリック・ダブルクリックの判定
- 長押しドラッグの状態管理

### Key Functions
- `fun onSingleFingerMove(dx: Float, dy: Float)`
- `fun onTwoFingerScroll(dy: Float)`
- `fun onClick()`
- `fun onDoubleClick()`
- `fun onDragStart()`
- `fun onDragEnd()`

---

## 2.4 VirtualMouseManager.kt
### Responsibilities
- 仮想マウスデバイスの生成
- MotionEvent を OS に送信
- カーソル移動・クリック・スクロールの実装

### Required Behavior
- 相対移動イベントの送信
- 左クリックイベントの送信
- スクロールイベントの送信（2本指スクロール対応）
- 長押しドラッグのサポート

### Key Functions
- `fun moveCursor(dx: Float, dy: Float)`
- `fun scroll(dy: Float)`
- `fun click()`
- `fun doubleClick()`
- `fun startDrag()`
- `fun endDrag()`

---

## 2.5 DeskPadForegroundService.kt
### Responsibilities
- アプリの常駐維持
- スリープ制御の補助
- 通知チャンネルの作成

### Key Functions
- `override fun onStartCommand()`
- `private fun createNotificationChannel()`
- `private fun buildNotification()`

---

# 3. Compose UI Specification

## 3.1 DeskPadScreen()
### Requirements
- 黒背景
- 全画面タッチパッド
- 右上に回転ボタン（Compose IconButton）
- 回転は `Modifier.graphicsLayer(rotationZ = 180f)` で実装

### Structure
Box( modifier = Modifier .fillMaxSize() .background(Color.Black) ) {
 TouchpadComposable(...)
 IconButton( modifier = Modifier.align(TopEnd), onClick = { rotateView180() } ) { Icon(...) }
 }


---

# 4. Behavior Rules

## 4.1 Screen / Power
- `FLAG_KEEP_SCREEN_ON` を設定
- `screenBrightness = 0.01f`
- 背景は黒 (`Color.Black`)

## 4.2 Rotation
- Activity の orientation は固定（Portrait）
- Compose のルートに `rotationZ = 180f` を適用

## 4.3 IME Interaction
- DeskPad は IME ではない
- IME が表示されると OS によって押し上げられる
- 押し上げられた領域でタッチパッドとして動作

---

# 5. Permissions
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

6. Gemini-cli Code Generation Rules
Gemini-cli は以下のルールに従ってコードを生成すること：
- Kotlin + Jetpack Compose で記述すること
- XML レイアウトは一切生成しないこと
- Android 14+ の API に準拠すること
- 仮想マウスデバイスの実装は InputManager を使用すること
- 2本指スクロールを最初から実装すること
- UI は黒背景でシンプルにすること
- 画面回転は Compose の View 回転で実装すること
- IME との競合を避けるため、SYSTEM_ALERT_WINDOW は使用しないこと
- Foreground Service は必須とすること
- タッチパッドの感度・加速度は定数化し調整可能にすること

7. Output Format (Gemini-cli must follow)
Gemini-cli は以下の形式で出力する：
# File: MainActivity.kt
<code>

# File: TouchpadComposable.kt
<code>

# File: GestureInterpreter.kt
<code>

# File: VirtualMouseManager.kt
<code>

# File: DeskPadForegroundService.kt
<code>

# File: themes.xml
<code>

8. Notes
- 日本語入力は他IMEに任せるため、DeskPad は IME を実装しない。
- Compose の PointerInput は複雑なため、GestureInterpreter を分離して保守性を高める。
- 2本指スクロールは detectTransformGestures または awaitPointerEventScope の raw pointer 解析で実装する。

	
