package com.example.appdev.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.appdev.ui.theme.SkyBlue
import com.example.appdev.ui.theme.White

@Composable
fun QrResultDialog(
    content: String,
    isUrl: Boolean,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = androidx.compose.ui.platform.LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Use theme surface
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (isUrl) "Website Detected" else "QR Content",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isUrl) {
                        Button(
                            onClick = {
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(content))
                                context.startActivity(intent)
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SkyBlue, contentColor = White)
                        ) {
                            Text("Open")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    
                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(content))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if(isUrl) MaterialTheme.colorScheme.secondary else SkyBlue, 
                                                             contentColor = White)
                    ) {
                        Text("Copy")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest, contentColor = MaterialTheme.colorScheme.onSurface)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}
