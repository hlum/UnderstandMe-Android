package jp.ac.jec.cm0138.understandme.Presentation.Screens.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberEnd
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shape.dashedShape
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import jp.ac.jec.cm0138.understandme.Entity.ResultData
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme
import java.time.LocalDateTime

data class MonthlyAverageResult(
    val month: LocalDateTime, // 1-12
    val averageScore: Int
) {


    constructor(resultsOfOneMonth: List<ResultData>) : this(
        month = resultsOfOneMonth.firstOrNull()?.evaluatedAtDate
            ?: LocalDateTime.now(),

        averageScore = resultsOfOneMonth
            .map { it.score }
            .average()
            .toInt()
    )
}

@Composable
fun MonthlyAverageScoreChart(
    currentSelectedYear: Int,
    nextYearButtonClicked: () -> Unit,
    previousYearButtonClicked: () -> Unit,
    monthlyData: List<MonthlyAverageResult>,
    modifier: Modifier = Modifier
) {

    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(monthlyData) {
        modelProducer.runTransaction {
            columnSeries {
                // Main data series
                val scores = monthlyData.map { it.averageScore.toDouble() }
                series(scores)
            }
            lineSeries {
                // Invisible line series at 100 to force Y-axis max
                series(monthlyData.indices.map { 100.0 })
            }
        }
    }

    val accentColor = MyAppTheme.colors.accent

    Column(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(2.dp, Color.Gray.copy(alpha = 0.2f)),
            color = Color.Transparent
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Chart with padding for year navigation
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
                ) {
                    CartesianChartHost(
                        chart = rememberCartesianChart(
                            rememberColumnCartesianLayer(),
                            rememberLineCartesianLayer(
                                lineProvider = LineCartesianLayer.LineProvider.series(
                                    LineCartesianLayer.Line(
                                        fill = LineCartesianLayer.LineFill.single(
                                            fill(Color.Transparent)
                                        )
                                    )
                                )
                            ),
                            endAxis = VerticalAxis.rememberEnd(
                                itemPlacer = VerticalAxis.ItemPlacer.count({ 6 }),
                                label = rememberTextComponent(color = Color.Gray),
                                valueFormatter = { _, value, _ ->
                                    value.toInt().toString()
                                },
                                line = rememberAxisLineComponent(
                                    fill = fill(Color.Gray)
                                ),
                                guideline = rememberAxisGuidelineComponent(
                                    fill = fill(Color.Gray.copy(alpha = 0.5f)),
                                    shape = dashedShape()
                                )
                            ),
                            bottomAxis = HorizontalAxis.rememberBottom(
                                label = rememberTextComponent(color = Color.Gray),
                                valueFormatter = { _, value, _ ->
                                    val monthIndex = value.toInt()
                                    if (monthIndex in monthlyData.indices) {
                                        "${monthlyData[monthIndex].month.month.value}月"
                                    } else ""
                                },
                                guideline = null
                            )
                        ),
                        modelProducer = modelProducer,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }


                // Year navigation overlay at top
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = previousYearButtonClicked,
                        modifier = Modifier.size(60.dp, 40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Previous Year",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = "${currentSelectedYear}年",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    IconButton(
                        onClick = nextYearButtonClicked,
                        modifier = Modifier.size(60.dp, 40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Next Year",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

