package com.Qapfoxo9.Diknos.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val SCREEN_FADE_IN_MS = 320
    const val SCREEN_FADE_OUT_MS = 260
    const val TILE_FLIP_MS = 260
    const val FLOOD_STAGGER_MS = 28
    const val RESOLVE_DELAY_MS = 700L
    const val MESSAGE_DURATION_MS = 2000L

    const val PRESS_DAMPING = 0.5f
    const val PRESS_STIFFNESS = 500f
    const val POP_DAMPING = 0.45f
    const val POP_STIFFNESS = 420f

    const val COLS = 5
    const val ROWS = 6
    const val TILE_COUNT = COLS * ROWS
    const val LEVEL_COUNT = 12

    const val MAX_SENSE = 3
    const val SENSE_PER_PAWPRINTS = 3
    const val GENERATION_ATTEMPTS = 20
    const val MAX_OCCUPIED_TILES = 18
    const val MIN_OPENING_REGION = 3

    const val STRIPE_THREE_RATIO = 0.4f
    const val STRIPE_TWO_RATIO = 0.2f

    const val SCORE_PER_PAWPRINT = 100
    const val SCORE_PER_TREK_LEFT = 25
    const val SCORE_PER_STREAK = 50
    const val SCORE_UNASSISTED_MULTIPLIER = 1.5f
    const val SCORE_HARD_TRAIL_BONUS = 150

    const val JOURNAL_LOCKED = 0
    const val JOURNAL_RECOVERED = 1
    const val JOURNAL_GOLDEN = 2
}
