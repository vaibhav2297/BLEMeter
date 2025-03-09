package com.example.designsystem.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.example.designsystem.theme.AppTheme

@Composable
fun ColumnScope.VerticalSpacer(height: Dp = AppTheme.padding.medium) = Spacer(Modifier.height(height))

@Composable
fun ColumnScope.SpacerFillVertically(weight: Float = 1f) = Spacer(Modifier.weight(weight))

@Composable
fun RowScope.HorizontalSpacer(width: Dp = AppTheme.padding.medium) = Spacer(Modifier.width(width))

@Composable
fun RowScope.SpacerFillHorizontally(weight: Float = 1f) = Spacer(Modifier.weight(weight))
