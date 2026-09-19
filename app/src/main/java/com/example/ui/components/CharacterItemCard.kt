package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.CharacterDto
import com.example.ui.theme.PortalCyan
import com.example.ui.theme.PortalGreen
import com.example.ui.theme.SpaceCardBorder
import com.example.ui.theme.SpaceSurface
import com.example.ui.theme.StatusAlive
import com.example.ui.theme.StatusDead
import com.example.ui.theme.StatusUnknown
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CharacterItemCard(
    character: CharacterDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("character_card_${character.id}")
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, SpaceCardBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = SpaceSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Character Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character.image)
                    .crossfade(true)
                    .build(),
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.5.dp, PortalGreen.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Status Indicator
                StatusBadge(status = character.status)

                Spacer(modifier = Modifier.height(6.dp))

                // Species & Gender
                Text(
                    text = "${character.species} • ${character.gender}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 12.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Location preview
                if (character.location.name.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = PortalCyan,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = character.location.name,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = PortalCyan.copy(alpha = 0.9f),
                                fontSize = 11.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSecondary.copy(alpha = 0.5f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun StatusBadge(status: String, modifier: Modifier = Modifier) {
    val (statusColor, statusTextRes) = when (status.lowercase()) {
        "alive" -> StatusAlive to R.string.status_alive
        "dead" -> StatusDead to R.string.status_dead
        else -> StatusUnknown to R.string.status_unknown
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(statusColor, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = stringResource(id = statusTextRes),
            style = MaterialTheme.typography.bodySmall.copy(
                color = statusColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        )
    }
}
