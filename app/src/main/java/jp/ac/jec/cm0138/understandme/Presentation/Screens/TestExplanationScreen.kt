package jp.ac.jec.cm0138.understandme.Presentation.Screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm0138.understandme.Presentation.Screens.Components.ArcTimerButton
import jp.ac.jec.cm0138.understandme.customTheme.CustomTypography
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * TestExplanationScreen - Displays a step-by-step explanation before starting the test
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TestExplanationScreen(
    onDismiss: () -> Unit,
    onStartTest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val totalSteps = 5
    val pagerState = rememberPagerState(pageCount = { totalSteps })
    var dontShowAgain by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Red
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Cancel",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("キャンセル")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MyAppTheme.colors.background
                )
            )
        },
        containerColor = MyAppTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 30.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "テストの説明",
                    style = CustomTypography.header.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MyAppTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "始める前に重要な情報を確認してください",
                    style = CustomTypography.body,
                    color = MyAppTheme.colors.primary.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }

            // Progress indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .padding(bottom = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(totalSteps) { step ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(
                                if (step <= pagerState.currentPage)
                                    MyAppTheme.colors.accent
                                else
                                    Color.Gray.copy(alpha = 0.3f)
                            )
                    )
                }
            }

            // Content pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> StepMainTimer()
                    1 -> StepArcTimer()
                    2 -> StepArcTimerAction()
                    3 -> StepNoReturn()
                    4 -> StepFinalConfirmation()
                }
            }

            // Bottom controls
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .padding(bottom = 30.dp)
            ) {
                // Don't show again toggle (only on last page)
                AnimatedVisibility(visible = pagerState.currentPage == totalSteps - 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = dontShowAgain,
                            onCheckedChange = { dontShowAgain = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MyAppTheme.colors.accent
                            )
                        )
                        Text(
                            text = "次回から表示しない",
                            style = CustomTypography.body
                        )
                    }
                }

                // Navigation buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Back button
                    if (pagerState.currentPage > 0) {
                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                width = 2.dp,
                                brush = androidx.compose.ui.graphics.SolidColor(MyAppTheme.colors.accent)
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MyAppTheme.colors.accent
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("戻る", style = CustomTypography.label)
                        }
                    }

                    // Next/Start button
                    Button(
                        onClick = {
                            if (pagerState.currentPage < totalSteps - 1) {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            } else {
                                if (dontShowAgain) {
                                    TestExplanationPreference.setDontShowAgain(context, true)
                                }
                                onStartTest()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (pagerState.currentPage == totalSteps - 1)
                                Color(0xFF4CAF50)
                            else
                                MyAppTheme.colors.accent
                        )
                    ) {
                        Text(
                            text = if (pagerState.currentPage < totalSteps - 1) "次へ" else "テストを開始",
                            style = CustomTypography.label
                        )
                        if (pagerState.currentPage < totalSteps - 1) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}

// Step 1: Main Timer Explanation
@Composable
private fun StepMainTimer() {
    var animatedTimerValue by remember { mutableIntStateOf(60) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            animatedTimerValue = if (animatedTimerValue > 0) animatedTimerValue - 1 else 60
        }
    }

    StepContainer {
        // Illustration
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MyAppTheme.colors.background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.Red.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color.Red)
                ) {
                    Text(
                        text = "残り時間: ${animatedTimerValue}秒",
                        style = CustomTypography.label.copy(fontWeight = FontWeight.Bold),
                        color = Color.Red,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Explanation
        ExplanationCard(
            stepNumber = 1,
            icon = Icons.Default.Timer,
            iconColor = MyAppTheme.colors.accent,
            title = "メインタイマー",
            backgroundColor = Color(0xFF2196F3).copy(alpha = 0.05f)
        ) {
            ExplanationText("左上の赤いタイマーは質問ごとの制限時間です。")
            ExplanationText("時間切れになると自動的に次の質問に進みます。")
        }
    }
}

// Step 2: Arc Timer Explanation
@Composable
private fun StepArcTimer() {
    val arcProgress = remember { mutableFloatStateOf(0f) }

    StepContainer {
        // Illustration
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MyAppTheme.colors.background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "画面下部",
                    style = CustomTypography.body.copy(fontSize = 12.sp),
                    color = MyAppTheme.colors.primary.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(30.dp))
                ArcTimerButton(
                    progress = arcProgress,
                    durationSeconds = 10,
                    size = 80.dp,
                    strokeWidth = 10.dp,
                    label = "PUSH",
                    accentColor = Color(0xFF2196F3),
                    warningColor = Color.Red,
                    onComplete = null
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Explanation
        ExplanationCard(
            stepNumber = 2,
            icon = Icons.Default.Refresh,
            iconColor = Color(0xFF2196F3),
            title = "アークタイマー",
            backgroundColor = Color(0xFFFF9800).copy(alpha = 0.05f)
        ) {
            ExplanationText("画面下部の円形タイマーは10秒ごとにリセットされます。")
            ExplanationText("このタイマーは常に動いています。")
        }
    }
}

// Step 3: Arc Timer Action (Interactive)
@Composable
private fun StepArcTimerAction() {
    val arcProgress = remember { mutableFloatStateOf(0f) }

    StepContainer {
        // Interactive Illustration
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MyAppTheme.colors.background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "タップしてリセット!",
                    style = CustomTypography.label.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF2196F3)
                )
                Spacer(modifier = Modifier.height(30.dp))
                ArcTimerButton(
                    progress = arcProgress,
                    durationSeconds = 10,
                    size = 80.dp,
                    strokeWidth = 10.dp,
                    label = "PUSH",
                    accentColor = Color(0xFF2196F3),
                    warningColor = Color.Red,
                    onComplete = {}
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Explanation
        ExplanationCard(
            stepNumber = 3,
            icon = Icons.Default.Warning,
            iconColor = Color.Red,
            title = "重要: 10秒ごとにタップ!",
            titleColor = Color.Red,
            backgroundColor = Color.Red.copy(alpha = 0.05f)
        ) {
            ExplanationText("アークタイマーが一周する前(10秒以内)に必ずタップしてください。")
            ExplanationText(
                text = "タップし忘れると、自動的に次の質問に進んでしまいます。",
                color = Color.Red,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// Step 4: No Return Warning
@Composable
private fun StepNoReturn() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 30.dp)
            .padding(top = 20.dp)
    ) {
        // Warning Illustration
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MyAppTheme.colors.background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color(0xFFFF9800)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "注意!",
                    style = CustomTypography.header.copy(fontSize = 28.sp, fontWeight = FontWeight.Bold),
                    color = Color(0xFFFF9800)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Warnings
        ExplanationCard(
            stepNumber = 4,
            icon = Icons.Default.Block,
            iconColor = Color(0xFFFF9800),
            title = "戻ることができません",
            backgroundColor = Color(0xFFFF9800).copy(alpha = 0.05f)
        ) {
            WarningItem(
                icon = Icons.Default.Cancel,
                iconColor = Color.Red,
                text = "テストを開始すると、途中で戻ることはできません。"
            )
            WarningItem(
                icon = Icons.Default.Cancel,
                iconColor = Color.Red,
                text = "アプリを閉じたり、戻るボタンを押すと、再受験できなくなります。"
            )
            WarningItem(
                icon = Icons.Default.CheckCircle,
                iconColor = Color.Green,
                text = "集中できる環境で、最後まで完了する準備をしてから始めてください。"
            )
        }
    }
}

// Step 5: Final Confirmation
@Composable
private fun StepFinalConfirmation() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 30.dp)
            .padding(top = 20.dp)
    ) {
        // Ready Illustration
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MyAppTheme.colors.background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "準備完了!",
                    style = CustomTypography.header.copy(fontSize = 28.sp, fontWeight = FontWeight.Bold),
                    color = MyAppTheme.colors.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Summary
        ExplanationCard(
            stepNumber = 5,
            icon = Icons.Default.Description,
            iconColor = Color(0xFF4CAF50),
            title = "テスト概要",
            backgroundColor = Color(0xFF4CAF50).copy(alpha = 0.05f)
        ) {
            SummaryRow(
                icon = Icons.Default.Timer,
                color = Color.Red,
                text = "メインタイマー(左上)で各質問の時間管理"
            )
            SummaryRow(
                icon = Icons.Default.Refresh,
                color = Color(0xFFFF9800),
                text = "アークタイマー(下部)を10秒ごとにタップ"
            )
            SummaryRow(
                icon = Icons.Default.PanTool,
                color = Color(0xFF9C27B0),
                text = "タイマーを忘れると次の質問へ自動移動"
            )
            SummaryRow(
                icon = Icons.Default.Lock,
                color = Color(0xFF2196F3),
                text = "開始後は戻れません・再受験不可"
            )
        }
    }
}

// Helper Composables
@Composable
private fun StepContainer(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
            .padding(top = 20.dp),
        content = content
    )
}

@Composable
private fun ExplanationCard(
    stepNumber: Int,
    icon: ImageVector,
    iconColor: Color,
    title: String,
    titleColor: Color = MyAppTheme.colors.primary,
    backgroundColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = iconColor
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    style = CustomTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = titleColor
                )
                content()
            }
        }
    }
}

@Composable
private fun ExplanationText(
    text: String,
    color: Color = MyAppTheme.colors.primary.copy(alpha = 0.7f),
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        style = CustomTypography.body.copy(fontWeight = fontWeight),
        color = color
    )
}

@Composable
private fun WarningItem(icon: ImageVector, iconColor: Color, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = iconColor
        )
        Text(
            text = text,
            style = CustomTypography.body,
            color = MyAppTheme.colors.primary.copy(alpha = 0.7f),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SummaryRow(icon: ImageVector, color: Color, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = color
        )
        Text(
            text = text,
            style = CustomTypography.body,
            color = MyAppTheme.colors.primary.copy(alpha = 0.7f),
            modifier = Modifier.weight(1f)
        )
    }
}

// Preference Manager for "Don't show again"
object TestExplanationPreference {
    private const val PREFS_NAME = "test_explanation_prefs"
    private const val KEY_DONT_SHOW = "dont_show_test_explanation"

    fun shouldShowExplanation(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return !prefs.getBoolean(KEY_DONT_SHOW, false)
    }

    fun setDontShowAgain(context: Context, value: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_DONT_SHOW, value).apply()
    }

    fun reset(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_DONT_SHOW).apply()
    }
}
