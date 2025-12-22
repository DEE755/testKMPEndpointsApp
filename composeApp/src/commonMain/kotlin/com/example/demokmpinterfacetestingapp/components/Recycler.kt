package com.example.demokmpinterfacetestingapp.components

import androidx.compose.foundation.background
import com.example.demokmpinterfacetestingapp.Model.models.recycler.ListItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun RecyclerScreen(
    items: List<ListItem>,
    modifier: Modifier = Modifier,
    selectedIds: MutableSet<String> = mutableSetOf(),
    onItemClick: (ListItem) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items, key = { it.id }) { item ->
            if (selectedIds.contains(item.id)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Cyan)
                        .padding(2.dp)
                ) {
                    RecyclerRow(item = item, onItemClick = onItemClick)
                }
            } else {
                RecyclerRow(item = item, onItemClick = onItemClick)
            }
        }
    }
}



@Composable
fun ModuleRecyclerScreen(
    items: List<ListItem>,
    modifier: Modifier = Modifier,
    selectedIds: Set<String> = emptySet(),
    onCheckedChange: (String, Boolean) -> Unit,
    onItemClick: (ListItem) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items, key = { it.id }) { item ->
            val isSelected = selectedIds.contains(item.title)
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Cyan)
                        .padding(2.dp)
                ) {
                    ModuleRecyclerRow(
                        item = item,
                        isChecked = true,
                        onCheckedChange = { checked -> onCheckedChange(item.title, checked) },
                        onItemClick = onItemClick
                    )
                }
            } else {
                ModuleRecyclerRow(
                    item = item,
                    isChecked = false,
                    onCheckedChange = { checked -> onCheckedChange(item.title, checked) },
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
private fun RecyclerRow(
    item: ListItem,
    onItemClick: (ListItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
            .padding(horizontal = 2.dp, vertical = 1.dp)
            .background(color = Color.LightGray)//item.color.)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val resource = item.thumbnailUrl?.takeIf { it.isNotBlank() }?.let { asyncPainterResource(it) }
            if (resource != null) {
                KamelImage(
                    resource = resource,
                    contentDescription = item.title,
                    modifier = Modifier.size(64.dp),
                    onLoading = { Box(Modifier.size(64.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(Modifier.size(20.dp)) } },
                    onFailure = { Box(Modifier.size(64.dp)) }
                )
            } else {
                Box(modifier = Modifier.size(64.dp))
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Description..", style = MaterialTheme.typography.bodyMedium) //TODO add description to ListItem
            }
        }
    }
}



@Composable
private fun ModuleRecyclerRow(
    item: ListItem,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onItemClick: (ListItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
            .padding(horizontal = 2.dp, vertical = 1.dp)
            .background(color = Color.LightGray)//item.color.)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val resource = item.thumbnailUrl?.takeIf { it.isNotBlank() }?.let { asyncPainterResource(it) }
            if (resource != null) {
                KamelImage(
                    resource = resource,
                    contentDescription = item.title,
                    modifier = Modifier.size(64.dp),
                    onLoading = { Box(Modifier.size(64.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(Modifier.size(20.dp)) } },
                    onFailure = { Box(Modifier.size(64.dp)) }
                )
            } else {
                Box(modifier = Modifier.size(64.dp))
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Description..", style = MaterialTheme.typography.bodyMedium) //TODO add description to ListItem
            }

            Checkbox(
                onCheckedChange = { onCheckedChange(it) },
                checked = isChecked,
            )
        }
    }
}
