package com.Qapfoxo9.Diknos.presentation.collection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.core.theme.Gold
import com.Qapfoxo9.Diknos.core.theme.Hairline
import com.Qapfoxo9.Diknos.core.theme.SurfaceBase
import com.Qapfoxo9.Diknos.core.theme.SurfaceRaised
import com.Qapfoxo9.Diknos.core.theme.TextMuted
import com.Qapfoxo9.Diknos.core.theme.TextPrimary
import com.Qapfoxo9.Diknos.core.theme.TextSecondary
import com.Qapfoxo9.Diknos.domain.model.JournalPage
import com.Qapfoxo9.Diknos.domain.model.JournalState

@Composable
fun JournalCard(
    page: JournalPage,
    width: Dp,
    onOpen: () -> Unit,
    onLockedTap: () -> Unit
) {
    val unlocked = page.state != JournalState.LOCKED
    val golden = page.state == JournalState.GOLDEN
    val shape = RoundedCornerShape(20.dp)
    val lockedLabel = stringResource(R.string.sd_locked)
    Button(
        onClick = { if (unlocked) onOpen() else onLockedTap() },
        modifier = Modifier
            .size(width = width, height = 104.dp)
            .shadow(elevation = if (unlocked) 10.dp else 2.dp, shape = shape)
            .background(
                if (unlocked) SurfaceRaised else SurfaceBase.copy(alpha = 0.55f),
                shape
            )
            .semantics {
                if (!unlocked) stateDescription = lockedLabel
            },
        shape = shape,
        border = BorderStroke(
            width = if (golden) 2.dp else 1.5.dp,
            color = if (golden) Gold else if (unlocked) Hairline else Color.Transparent
        ),
        contentPadding = PaddingValues(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextPrimary
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (unlocked) {
                    Image(
                        painter = painterResource(R.drawable.sprite_4),
                        contentDescription = stringResource(R.string.cd_journal),
                        modifier = Modifier.size(26.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(page.titleRes),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (golden) Gold else TextPrimary,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(page.regionRes),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_lock),
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.page_number, page.index),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    Text(
                        text = stringResource(R.string.locked),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }
        }
    }
}
