package com.example.newsAggregator.utils

enum class NewsCategoryEnum(val value: String) {
    GENERAL("general"),
    SCIENCE("science"),
    SPORTS("sports"),
    BUSINESS("business"),
    HEALTH("health"),
    ENTERTAINMENT("entertainment"),
    TECH("tech"),
    POLITICS("politics"),
    FOOD("food"),
    TRAVEL("travel");

    companion object {
        fun from(value: String?): NewsCategoryEnum? =
            entries.find { it.value.equals(value, ignoreCase = true) }
    }
}