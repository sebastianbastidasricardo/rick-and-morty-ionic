package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.CharacterDetailDialog
import com.example.ui.components.CharacterItemCard
import com.example.ui.theme.PortalCyan
import com.example.ui.theme.PortalGreen
import com.example.ui.theme.SpaceBackground
import com.example.ui.theme.SpaceCardBorder
import com.example.ui.theme.SpaceSurface
import com.example.ui.theme.SpaceSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.CharacterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    viewModel: CharacterViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // Detect when user scrolled to bottom to load more
    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleIndex >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && uiState.hasMorePages && !uiState.isLoading && !uiState.isLoadingMore) {
            viewModel.loadCharacters(reset = false)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SpaceBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(PortalGreen.copy(alpha = 0.2f))
                                .border(1.5.dp, PortalGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "RM",
                                fontWeight = FontWeight.Black,
                                color = PortalGreen,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SpaceSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_characters_placeholder),
                        color = TextSecondary
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = PortalCyan
                    )
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = TextSecondary
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PortalGreen,
                    unfocusedBorderColor = SpaceCardBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = SpaceSurfaceVariant,
                    unfocusedContainerColor = SpaceSurfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("search_text_field")
            )

            // Status Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusFilterChip(
                    label = stringResource(id = R.string.filter_all),
                    selected = uiState.selectedStatus == null,
                    onClick = { viewModel.onStatusSelected(null) },
                    testTag = "filter_chip_all"
                )
                StatusFilterChip(
                    label = stringResource(id = R.string.filter_alive),
                    selected = uiState.selectedStatus == "alive",
                    onClick = { viewModel.onStatusSelected("alive") },
                    testTag = "filter_chip_alive"
                )
                StatusFilterChip(
                    label = stringResource(id = R.string.filter_dead),
                    selected = uiState.selectedStatus == "dead",
                    onClick = { viewModel.onStatusSelected("dead") },
                    testTag = "filter_chip_dead"
                )
                StatusFilterChip(
                    label = stringResource(id = R.string.filter_unknown),
                    selected = uiState.selectedStatus == "unknown",
                    onClick = { viewModel.onStatusSelected("unknown") },
                    testTag = "filter_chip_unknown"
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Main Content Area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when {
                    // Initial Loading
                    uiState.isLoading && uiState.characters.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(
                                    color = PortalGreen,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Viajando por el multiverso...",
                                    color = TextSecondary,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    // Error Screen
                    uiState.errorMessage != null && uiState.characters.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "🌀",
                                    fontSize = 48.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = stringResource(id = R.string.error_loading_characters),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextSecondary,
                                        textAlign = TextAlign.Center
                                    )
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.loadCharacters(reset = true) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PortalGreen,
                                        contentColor = SpaceBackground
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.testTag("retry_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = stringResource(id = R.string.retry_button),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Empty results
                    !uiState.isLoading && uiState.characters.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "🛸",
                                    fontSize = 44.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = stringResource(id = R.string.no_characters_found),
                                    color = TextSecondary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // List of characters
                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("characters_list"),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = uiState.characters,
                                key = { it.id }
                            ) { character ->
                                CharacterItemCard(
                                    character = character,
                                    onClick = { viewModel.selectCharacter(character) }
                                )
                            }

                            // Load more spinner at bottom
                            if (uiState.isLoadingMore) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            color = PortalCyan,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Detail Dialog
    uiState.selectedCharacter?.let { character ->
        CharacterDetailDialog(
            character = character,
            onDismiss = { viewModel.selectCharacter(null) }
        )
    }
}

@Composable
fun StatusFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = PortalGreen,
            selectedLabelColor = SpaceBackground,
            containerColor = SpaceSurface,
            labelColor = TextSecondary
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = if (selected) PortalGreen else SpaceCardBorder,
            selectedBorderColor = PortalGreen
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.testTag(testTag)
    )
}
