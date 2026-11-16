package com.Qapfoxo9.Diknos.presentation.menu

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.core.di.ServiceLocator
import com.Qapfoxo9.Diknos.core.theme.AccentStripe
import com.Qapfoxo9.Diknos.core.theme.AccentStripeEnd
import com.Qapfoxo9.Diknos.core.theme.AccentTrail
import com.Qapfoxo9.Diknos.core.theme.BgBase
import com.Qapfoxo9.Diknos.core.theme.BgDeep
import com.Qapfoxo9.Diknos.core.theme.Gold
import com.Qapfoxo9.Diknos.core.theme.SurfaceBase
import com.Qapfoxo9.Diknos.core.theme.SurfaceRaised
import com.Qapfoxo9.Diknos.core.theme.TextMuted
import com.Qapfoxo9.Diknos.core.theme.TextPrimary
import com.Qapfoxo9.Diknos.core.theme.TextSecondary
import com.Qapfoxo9.Diknos.core.ui.AppBackground
import com.Qapfoxo9.Diknos.core.ui.ClayCard
import com.Qapfoxo9.Diknos.core.ui.EmptyStateCard
import com.Qapfoxo9.Diknos.core.ui.StatCard

@Composable
fun MenuRoute(
    onPlay: (Int) -> Unit,
    onLevels: () -> Unit,
    onJournal: () -> Unit,
    onExpedition: () -> Unit,
    onOptions: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.factory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()
    MenuScreen(
        state = state,
        onPlay = { onPlay(state.currentLevel) },
        onLevels = onLevels,
        onJournal = onJournal,
        onExpedition = onExpedition,
        onOptions = onOptions
    )
}

@Composable
fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onLevels: () -> Unit,
    onJournal: () -> Unit,
    onExpedition: () -> Unit,
    onOptions: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgBase)
    ) {
        AppBackground(
            overlay = listOf(
                BgBase.copy(alpha = 0.55f),
                BgBase.copy(alpha = 0.72f),
                BgDeep.copy(alpha = 0.92f)
            ),
            showClaws = true
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
        ) {
            MenuHeader(journalPages = state.journalPages)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(184.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PlayTile(
                    levelLine = stringResource(
                        R.string.menu_level_line,
                        state.currentLevel,
                        stringResource(state.currentRegionRes)
                    ),
                    onClick = onPlay,
                    modifier = Modifier
                        .weight(1.6f)
                        .fillMaxHeight()
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (state.hasProgress) {
                        StatCard(
                            label = stringResource(R.string.pawprints),
                            value = state.totalPawprints.toString(),
                            accent = Gold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                        StatCard(
                            label = stringResource(R.string.best_streak),
                            value = state.bestStreak.toString(),
                            accent = AccentTrail,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    } else {
                        EmptyStateCard(
                            message = stringResource(R.string.menu_empty),
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NavTile(
                    label = stringResource(R.string.levels),
                    counter = stringResource(
                        R.string.fraction,
                        state.currentLevel,
                        GameConfig.LEVEL_COUNT
                    ),
                    iconRes = R.drawable.ic_levels,
                    accent = AccentStripe,
                    onClick = onLevels,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                NavTile(
                    label = stringResource(R.string.collection),
                    counter = stringResource(
                        R.string.fraction,
                        state.journalPages,
                        GameConfig.LEVEL_COUNT
                    ),
                    iconRes = R.drawable.ic_collection,
                    accent = Gold,
                    onClick = onJournal,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                NavTile(
                    label = stringResource(R.string.progress),
                    counter = stringResource(R.string.percent_value, state.clearRate),
                    iconRes = R.drawable.ic_progress,
                    accent = AccentTrail,
                    onClick = onExpedition,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            OptionsTile(
                hint = stringResource(
                    R.string.safe_first_state,
                    stringResource(
                        if (state.safeFirstStep) R.string.state_on else R.string.state_off
                    )
                ),
                onClick = onOptions
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.menu_footer_hint),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }
    }
}

@Composable
private fun MenuHeader(journalPages: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.cd_logo),
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(R.string.title_app),
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        ClayCard(
            fill = SurfaceRaised,
            border = BorderStroke(1.5.dp, AccentStripe.copy(alpha = 0.27f)),
            corner = 18.dp,
            elevation = 6.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = stringResource(R.string.cd_journal),
                    modifier = Modifier.size(20.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(
                        R.string.fraction,
                        journalPages,
                        GameConfig.LEVEL_COUNT
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = Gold
                )
            }
        }
    }
}

@Composable
private fun PlayTile(
    levelLine: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = GameConfig.PRESS_DAMPING,
            stiffness = GameConfig.PRESS_STIFFNESS
        ),
        label = "playTile"
    )
    val shape = RoundedCornerShape(28.dp)
    Button(
        onClick = onClick,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (pressed) 8.dp else 16.dp,
                shape = shape,
                ambientColor = AccentStripe,
                spotColor = AccentStripe
            )
            .background(
                Brush.linearGradient(listOf(AccentStripe, AccentStripeEnd)),
                shape
            ),
        shape = shape,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = BgBase
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.sprite_1),
                contentDescription = stringResource(R.string.cd_tiger),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp, bottom = 10.dp)
                    .size(64.dp),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(18.dp)
            ) {
                Text(
                    text = stringResource(R.string.play),
                    style = MaterialTheme.typography.headlineMedium,
                    color = BgBase
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = levelLine,
                    style = MaterialTheme.typography.labelSmall,
                    color = BgBase.copy(alpha = 0.62f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun NavTile(
    label: String,
    counter: String,
    iconRes: Int,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)
    Button(
        onClick = onClick,
        modifier = modifier
            .shadow(elevation = 8.dp, shape = shape)
            .background(SurfaceBase, shape),
        shape = shape,
        contentPadding = PaddingValues(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextPrimary
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = counter,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun OptionsTile(
    hint: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .shadow(elevation = 8.dp, shape = shape)
            .background(SurfaceBase, shape),
        shape = shape,
        contentPadding = PaddingValues(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextPrimary
        )
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_settings),
            contentDescription = null,
            tint = AccentTrail,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(R.string.settings),
            style = MaterialTheme.typography.labelLarge,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = hint,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
