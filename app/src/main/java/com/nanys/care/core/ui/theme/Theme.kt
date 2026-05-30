package com.nanys.care.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.nanys.care.domain.model.UserRole

private fun lightColorsFor(primaryColor: Color) = lightColorScheme(
    primary = primaryColor,
    onPrimary = Color.White,
    secondary = SecondaryGreen,
    tertiary = TertiaryCoral,
    background = BackgroundLight,
    surface = SurfaceWhite,
    onSurface = OnSurfaceDark,
    error = ErrorRed
)

private fun darkColorsFor(primaryColor: Color) = darkColorScheme(
    primary = primaryColor,
    secondary = SecondaryGreen,
    tertiary = TertiaryCoral
)

@Composable
fun NanysTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    role: UserRole? = null,
    content: @Composable () -> Unit
) {
    val primary = when (role) {
        UserRole.CUIDADOR -> CaregiverPurple
        UserRole.ADMIN -> AdminRed
        UserRole.SUPERVISOR -> SupervisorGreen
        UserRole.TUTOR, null -> PrimaryBlue
    }
    MaterialTheme(
        colorScheme = if (darkTheme) darkColorsFor(primary) else lightColorsFor(primary),
        content = content
    )
}
