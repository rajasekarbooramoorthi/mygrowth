package com.raj.mygrowth

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.google.gson.Gson
import com.raj.mygrowth.domain.MasterRoadmap

@Composable
fun FullStackMasterScreen(jsonString: String) {
    val context = LocalContext.current
    val roadmap = remember(jsonString) { Gson().fromJson(jsonString, MasterRoadmap::class.java) }

    var selectedPhase by remember { mutableStateOf(roadmap.data.firstOrNull()) }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {

        // 1. Top Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Search topics...") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_search_24),
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Gray
                )
            },
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        // 2. Main Content: 30/70 Split
        Row(modifier = Modifier.fillMaxSize()) {

            // LEFT SIDE: Phase Navigation (30%)
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .background(Color.White)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(roadmap.data) { phase ->
                        val isSelected = selectedPhase == phase
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                                .background(
                                    color = if (isSelected) Color(0xFF6200EE) else Color.Transparent,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { selectedPhase = phase },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = phase.phase.split("—").last().trim(),
                                color = if (isSelected) Color.White else Color.Black,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }

            // RIGHT SIDE: Content Details (70%)
            LazyColumn(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight()
                    .padding(start = 12.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                selectedPhase?.categories?.forEach { category ->
                    item {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF37474F),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    items(category.subcategories) { sub ->
                        val filteredTopics = sub.topics.filter {
                            it.contains(searchQuery, ignoreCase = true) ||
                                    sub.technology.contains(searchQuery, ignoreCase = true)
                        }

                        if (filteredTopics.isNotEmpty()) {
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                    Text(
                                        text = sub.technology,
                                        color = Color(0xFF6200EE),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 4.dp
                                        )
                                    )

                                    filteredTopics.forEach { topic ->
                                        TopicRow(topic = topic) { clickedTopic ->
                                            val query =
                                                Uri.encode("${sub.technology} $clickedTopic documentation tutorial")
                                            val url = "https://www.google.com/search?q=$query"
                                            println("Search Intent ---> $url")
                                            context.startActivity(
                                                Intent(Intent.ACTION_VIEW).apply {
                                                    data = url.toUri()
                                                    // Adding this flag is best practice when starting activity from non-activity context
                                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}

@Composable
fun TopicRow(topic: String, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            // Moving clickable here ensures the ripple covers the whole area including the divider space
            .clickable { onClick(topic) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Bullet Point
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(Color(0xFFBDBDBD), RoundedCornerShape(50))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 2. Topic Text
            Text(
                text = topic,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF424242), // Darker gray for better readability
                modifier = Modifier.weight(1f)
            )
        }

        // 4. The Divider Line
        // This is your "drawer line" that separates each topic
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp), // Matches row padding
            thickness = 0.8.dp,
            color = Color.LightGray.copy(alpha = 0.4f)
        )
    }
}