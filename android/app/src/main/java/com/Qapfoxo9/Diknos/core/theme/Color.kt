package com.Qapfoxo9.Diknos.core.theme

import androidx.compose.ui.graphics.Color

val BgBase = Color(0xFF0D2818)
val BgDeep = Color(0xFF0A1F12)
val BgDeepest = Color(0xFF060F09)
val SurfaceBase = Color(0xFF132D1E)
val SurfaceRaised = Color(0xFF1A3A28)
val SurfaceSunken = Color(0xFF0F3024)
val SurfaceCleared = Color(0xFF0B2217)
val SurfaceMuted = Color(0xFF2A4A36)
val TileTop = Color(0xFF16412B)

val AccentTrail = Color(0xFF00E676)
val AccentStripe = Color(0xFFFF6D00)
val AccentStripeEnd = Color(0xFFFF9100)
val Danger = Color(0xFFFF3B5C)
val Gold = Color(0xFFF5D77A)
val GoldDeep = Color(0xFFD4AF37)

val TextPrimary = Color(0xFFF2FFF7)
val TextSecondary = Color(0xFFA8C4B4)
val TextMuted = Color(0xFF6E8C7C)

val Hairline = Color(0x14FFFFFF)
val TopHighlight = Color(0x1AFFFFFF)
val ShadowTint = Color(0xFF000000)
val ScrimDim = Color(0xA6000000)
val ClawSlash = Color(0x08FFFFFF)

val SignOne = AccentTrail
val SignTwo = Color(0xFF7FE8C5)
val SignThree = Color(0xFFFFD54F)
val SignFour = AccentStripeEnd
val SignFive = Danger

fun trailSignColor(sign: Int): Color = when (sign) {
    1 -> SignOne
    2 -> SignTwo
    3 -> SignThree
    4 -> SignFour
    else -> SignFive
}
