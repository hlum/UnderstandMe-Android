package jp.ac.jec.cm0138.understandme.Presentation.Screens.Components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.max

@Composable
fun ArcTimerButton(
    progress: MutableState<Float>,
    durationSeconds: Int,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    strokeWidth: Dp = 12.dp,
    timerRunning: Boolean,
    label: String? = null,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    warningColor: Color = Color.Red,
    warningThreshold: Int = 3,
    onTick: ((Int) -> Unit)? = null,
    onComplete: (() -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    var timerJob by remember { mutableStateOf<Job?>(null) }
    var lastTickedSecond by remember { mutableIntStateOf(-1) }

    val remainingSeconds by remember {
        derivedStateOf {
            max(0, ceil((1f - progress.value) * durationSeconds).toInt())
        }
    }



    // Trigger onTick when remainingSeconds changes
    LaunchedEffect(remainingSeconds) {
        if (remainingSeconds != lastTickedSecond) {
            lastTickedSecond = remainingSeconds
            onTick?.invoke(remainingSeconds)
        }
    }

    val isWarning = remainingSeconds in 1..warningThreshold
    val currentColor = if (isWarning) warningColor else accentColor

    /* ---------- Breathing Animation ---------- */
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathOffsetPx = with(LocalDensity.current) {
        if (isWarning) 15.dp.toPx() else 5.dp.toPx()
    }
    val sizePx = with(LocalDensity.current) { size.toPx() }
    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.0f + (breathOffsetPx / sizePx),
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    /* ---------- Timer Logic ---------- */
    fun stopTimer() {
        timerJob?.cancel()
    }

    fun startTimer() {
        stopTimer()

        timerJob = scope.launch {
            val totalMillis = durationSeconds * 1000L
            val startTime = withFrameNanos { it / 1_000_000L }

            while (isActive && timerRunning) {
                val currentTime = withFrameNanos { it / 1_000_000L }
                val elapsed = currentTime - startTime
                val newProgress = (elapsed / totalMillis.toFloat()).coerceIn(0f, 1f)

                progress.value = newProgress

                if (newProgress >= 1f) {
                    delay(100) // Match SwiftUI's 0.1s delay
                    onComplete?.invoke()
                    progress.value = 0f
                    startTimer()
                    break
                }
            }
        }
    }

    LaunchedEffect(timerRunning) {
        if (!timerRunning) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    LaunchedEffect(progress.value) {
        if (progress.value == 0f) {
            startTimer()
        }
    }

    
    // Auto-start timer on first composition
    LaunchedEffect(Unit) {
        if (progress.value == 0f) {
            startTimer()
        }
    }

    // Cleanup on disposal
    DisposableEffect(Unit) {
        onDispose {
            stopTimer()
        }
    }

    /* ---------- UI ---------- */
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(size)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                progress.value = 0f
                startTimer()
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()

            // Background circle
            drawArc(
                color = currentColor.copy(alpha = 0.12f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(strokePx, cap = StrokeCap.Round)
            )

            // Progress arc (matches SwiftUI's trim and rotation)
            drawArc(
                color = currentColor,
                startAngle = -90f,
                sweepAngle = 360f * (1f - progress.value),
                useCenter = false,
                style = Stroke(strokePx, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (label != null) {
                Text(
                    text = label,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalContentColor.current
                )
            } else {
                Text(
                    text = remainingSeconds.toString(),
                    fontSize = if (isWarning) 28.sp else 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isWarning) warningColor
                    else LocalContentColor.current.copy(alpha = 0.8f)
                )
            }
        }
    }
}
