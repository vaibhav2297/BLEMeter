package com.example.designsystem.theme

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

typealias VoidCallback = () -> Unit
typealias ValueChanged<T> = (T) -> Unit

typealias VoidComposable = @Composable () -> Unit
typealias PaddingComposable = @Composable (PaddingValues) -> Unit
typealias BoxScopeComposable = @Composable BoxScope.() -> Unit
typealias RowScopeComposable = @Composable RowScope.() -> Unit
typealias ColumnScopeComposable = @Composable ColumnScope.() -> Unit


