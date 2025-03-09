package com.example.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.designsystem.icons.AppIcon
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.MeterAppTheme
import com.example.designsystem.theme.ValueChanged
import com.example.navigation.BLEMeterTopLevelDestination

enum class AppNavItem(
    val destination: BLEMeterTopLevelDestination,
    val icon: AppIcon,
    val label: String
)

@Composable
fun AppBottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    navItems: List<AppNavItem>,
    selectedItem: AppNavItem,
    onItemSelect: ValueChanged<AppNavItem>
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    

    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(
            items = navItems,
            key = { it.destination.destination }
        ) { navItem ->
            AppBottomNavBarItem(
                modifier = Modifier
                    .clickable { onItemSelect(navItem) },
                navItem = navItem,
                isSelected = navItem.destination.route == selectedItem.destination.route
            )
        }
    }
}

@Composable
private fun AppBottomNavBarItem(
    modifier: Modifier = Modifier,
    navItem: AppNavItem,
    isSelected: Boolean
) {
    val color = if (isSelected) AppTheme.colors.onBackground else AppTheme.colors.textHighlighted

    Column(
        modifier = modifier
            .size(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppIcon(
            modifier = modifier
                .padding(bottom = 6.dp),
            icon = navItem.icon,
            tint = color
        )

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = navItem.label,
            maxLines = 1,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Preview
@Composable
private fun AppBottomNavBarPreview() {
    MeterAppTheme {
        AppSurface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            /*val selectedItem = AppNavItem(
                destination = AuthenticationDestination,
                icon = AppIcon.DrawableResourceIcon(AppIcons.Valve),
                label = "Setting",
            )

            val navItems = listOf(
                AppNavItem(
                    destination = AuthenticationDestination,
                    icon = AppIcon.DrawableResourceIcon(AppIcons.Valve),
                    label = "User",
                ),
                AppNavItem(
                    destination = AuthenticationDestination,
                    icon = AppIcon.DrawableResourceIcon(AppIcons.Valve),
                    label = "Setting",
                ),
                AppNavItem(
                    destination = AuthenticationDestination,
                    icon = AppIcon.DrawableResourceIcon(AppIcons.Valve),
                    label = "Profile",
                ),
                AppNavItem(
                    destination = AuthenticationDestination,
                    icon = AppIcon.DrawableResourceIcon(AppIcons.Valve),
                    label = "Scan",
                ),
                AppNavItem(
                    destination = AuthenticationDestination,
                    icon = AppIcon.DrawableResourceIcon(AppIcons.Valve),
                    label = "Recharge",
                )
            )


            AppBottomNavBar(
                navItems = navItems,
                selectedItem = selectedItem
            ) {

            }*/
        }
    }
}
