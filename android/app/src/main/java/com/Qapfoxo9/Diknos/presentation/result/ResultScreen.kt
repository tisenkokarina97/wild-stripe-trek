package com.Qapfoxo9.Diknos.presentation.result

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.core.di.ServiceLocator
import com.Qapfoxo9.Diknos.core.navigation.Screen
import com.Qapfoxo9.Diknos.core.theme.AccentTrail
import com.Qapfoxo9.Diknos.core.theme.BgBase
import com.Qapfoxo9.Diknos.core.theme.BgDeep
import com.Qapfoxo9.Diknos.core.theme.Danger
import com.Qapfoxo9.Diknos.core.theme.Gold
import com.Qapfoxo9.Diknos.core.theme.SurfaceBase
import com.Qapfoxo9.Diknos.core.theme.TextMuted
import com.Qapfoxo9.Diknos.core.theme.TextPrimary
import com.Qapfoxo9.Diknos.core.theme.TextSecondary
import com.Qapfoxo9.Diknos.core.ui.AppBackground
import com.Qapfoxo9.Diknos.core.ui.ClayCard
import com.Qapfoxo9.Diknos.core.ui.PrimaryButton
import com.Qapfoxo9.Diknos.core.ui.SecondaryButton
import com.Qapfoxo9.Diknos.core.ui.StatCard
import com.Qapfoxo9.Diknos.core.ui.StripeRow
import com.Qapfoxo9.Diknos.data.sample.SampleData
import com.Qapfoxo9.Diknos.domain.model.RoundOutcome

@Composable
fun ResultRoute(
    result: Screen.Result,
    onPlayAgain: () -> Unit,
    onNextTrail: () -> Unit,
    onMenu: () -> Unit,
    onJournal: () -> Unit
) {
    val resultViewModel: ResultViewModel = viewModel(factory = ServiceLocator.factory)
    val state by resultViewModel.state.collectAsStateWithLifecycle()
    ResultScreen(
        result = result,
        state = state,
        onPlayAgain = onPlayAgain,
        onNextTrail = onNextTrail,
        onMenu = onMenu,
        onJournal = onJournal
    )
}

@Composable
fun ResultScreen(
    result: Screen.Result,
    state: ResultUiState,
    onPlayAgain: () -> Unit,
    onNextTrail: () -> Unit,
    onMenu: () -> Unit,
    onJournal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val won = result.outcome == RoundOutcome.COMPLETE
    val accent = if (won) AccentTrail else Danger
    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(result) {
        revealed = true
    }
    val stripeScale by animateFloatAsState(
        targetValue = if (revealed) 1f else 0.6f,
        animationSpec = spring(
            dampingRatio = GameConfig.POP_DAMPING,
            stiffness = GameConfig.POP_STIFFNESS
        ),
        label = "stripePop"
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgBase)
    ) {
        AppBackground(
            overlay = if (won) {
                listOf(
                    BgBase.copy(alpha = 0.70f),
                    BgDeep.copy(alpha = 0.88f),
                    BgDeep.copy(alpha = 0.95f)
                )
            } else {
                listOf(
                    Danger.copy(alpha = 0.18f),
                    BgDeep.copy(alpha = 0.90f),
                    BgDeep.copy(alpha = 0.96f)
                )
            },
            showClaws = true
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ClayCard(
                modifier = Modifier.fillMaxWidth(),
                fill = SurfaceBase,
                border = BorderStroke(2.dp, accent),
                corner = 32.dp,
                elevation = 20.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(
                            if (won) R.drawable.sprite_1 else R.drawable.sprite_3
                        ),
                        contentDescription = stringResource(
                            if (won) R.string.cd_tiger else R.string.cd_nest
                        ),
                        modifier = Modifier.size(44.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(outcomeTitleRes(result.outcome)),
                        style = MaterialTheme.typography.displaySmall,
                        color = accent,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(
                            R.string.menu_level_line,
                            result.levelIndex,
                            stringResource(SampleData.level(result.levelIndex).regionRes)
                        ),
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    if (won) {
                        Spacer(modifier = Modifier.height(14.dp))
                        StripeRow(
                            earned = result.stripes,
                            modifier = Modifier.graphicsLayer {
                                scaleX = stripeScale
                                scaleY = stripeScale
                            }
                        )
                    }
                    if (result.journalAward != GameConfig.JOURNAL_LOCKED) {
                        Spacer(modifier = Modifier.height(16.dp))
                        JournalReward(
                            golden = result.journalAward == GameConfig.JOURNAL_GOLDEN,
                            levelIndex = result.levelIndex
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.score),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    Text(
                        text = result.score.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                    if (result.hardTrailBonus && won) {
                        Text(
                            text = stringResource(R.string.hard_trail_bonus),
                            style = MaterialTheme.typography.labelSmall,
                            color = Gold
                        )
                    }
                    ResultStats(result = result, lifetimeBest = state.lifetimeBestStreak)
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = onPlayAgain,
                modifier = Modifier.fillMaxWidth()
            )
            if (won && result.unlockedNext) {
                Spacer(modifier = Modifier.height(10.dp))
                SecondaryButton(
                    text = stringResource(R.string.next_trail),
                    onClick = onNextTrail,
                    accent = AccentTrail,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = onMenu) {
                    Text(
                        text = stringResource(R.string.menu),
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                TextButton(onClick = onJournal) {
                    Text(
                        text = stringResource(R.string.collection),
                        style = MaterialTheme.typography.labelLarge,
                        color = Gold
                    )
                }
            }
        }
    }
}

@Composable
private fun JournalReward(golden: Boolean, levelIndex: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(R.drawable.sprite_4),
            contentDescription = stringResource(R.string.cd_journal),
            modifier = Modifier.size(44.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = if (golden) {
                stringResource(R.string.golden_page_recovered)
            } else {
                stringResource(R.string.journal_page_recovered, levelIndex)
            },
            style = MaterialTheme.typography.labelLarge,
            color = if (golden) Gold else TextPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ResultStats(result: Screen.Result, lifetimeBest: Int) {
    val cards = ArrayList<Triple<String, String, Color>>(3)
    if (result.found > 0) {
        cards.add(
            Triple(
                stringResource(R.string.pawprints),
                stringResource(R.string.fraction, result.found, result.total),
                Gold
            )
        )
    }
    if (result.treksLeft > 0) {
        cards.add(
            Triple(
                stringResource(R.string.treks_left),
                result.treksLeft.toString(),
                AccentTrail
            )
        )
    }
    if (lifetimeBest > 0) {
        cards.add(
            Triple(
                stringResource(R.string.best_streak),
                lifetimeBest.toString(),
                TextPrimary
            )
        )
    }
    if (cards.size < 2) return
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for (card in cards) {
            StatCard(
                label = card.first,
                value = card.second,
                accent = card.third,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun outcomeTitleRes(outcome: RoundOutcome): Int = when (outcome) {
    RoundOutcome.COMPLETE -> R.string.outcome_complete
    RoundOutcome.NEST -> R.string.outcome_nest
    RoundOutcome.COLD -> R.string.outcome_cold
}
