package com.example.demokmpinterfacetestingapp.components

import com.example.demokmpinterfacetestingapp.Model.models.recycler.ListItem
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun RecyclerScreen(
    items: List<ListItem>,
    modifier: Modifier = Modifier,
    onItemClick: (ListItem) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items, key = { it.id }) { item ->
            RecyclerRow(item = item, onItemClick = onItemClick)
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
                Text(text = item.id, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
class Recycler {
}