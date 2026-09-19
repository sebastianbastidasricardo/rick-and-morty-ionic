package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.CharacterDto
import com.example.ui.theme.PortalCyan
import com.example.ui.theme.PortalGreen
import com.example.ui.theme.SpaceCardBorder
import com.example.ui.theme.SpaceSurface
import com.example.ui.theme.SpaceSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CharacterDetailDialog(
    character: CharacterDto,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(0.92f)
                .testTag("character_detail_dialog")
                .clip(RoundedCornerShape(24.dp))
                .border(1.5.dp, PortalGreen.copy(alpha = 0.8f), RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = SpaceSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top row with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.character_details_title),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = PortalGreen,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_detail_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.close_button),
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Avatar Image
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(character.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = character.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(2.dp, PortalCyan, RoundedCornerShape(20.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Character Name
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Status Badge
                StatusBadge(status = character.status)

                Spacer(modifier = Modifier.height(16.dp))

                Divider(color = SpaceCardBorder, thickness = 1.dp)

                Spacer(modifier = Modifier.height(16.dp))

                // Detail Attributes
                DetailRowItem(
                    icon = Icons.Default.Person,
                    label = stringResource(id = R.string.species_label),
                    value = "${character.species}${if (character.type.isNotBlank()) " (${character.type})" else ""}"
                )

                Spacer(modifier = Modifier.height(10.dp))

                DetailRowItem(
                    icon = Icons.Default.Person,
                    label = stringResource(id = R.string.gender_label),
                    value = character.gender
                )

                Spacer(modifier = Modifier.height(10.dp))

                DetailRowItem(
                    icon = Icons.Default.Explore,
                    label = stringResource(id = R.string.origin_label),
                    value = character.origin.name.ifBlank { "Desconocido" }
                )

                Spacer(modifier = Modifier.height(10.dp))

                DetailRowItem(
                    icon = Icons.Default.Place,
                    label = stringResource(id = R.string.location_label),
                    value = character.location.name.ifBlank { "Desconocido" }
                )

                Spacer(modifier = Modifier.height(10.dp))

                DetailRowItem(
                    icon = Icons.Default.Movie,
                    label = stringResource(id = R.string.episodes_count_label),
                    value = "${character.episode.size} episodios"
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Close Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dismiss_dialog_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PortalGreen,
                        contentColor = SpaceSurface
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.close_button),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRowItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SpaceSurfaceVariant.copy(alpha = 0.5f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PortalCyan,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.weight(1f)
        )
    }
}
