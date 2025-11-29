package com.aravind.parva.data.local

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter
import com.aravind.parva.data.model.*
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.LocalDate

/**
 * Custom Gson TypeAdapter for LocalDate
 */
class LocalDateTypeAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    override fun serialize(src: LocalDate?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.toString() ?: "")
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate {
        return LocalDate.parse(json?.asString ?: "1970-01-01")
    }
}

/**
 * Custom Gson TypeAdapter for Compose Color
 */
class ColorTypeAdapter : JsonSerializer<Color>, JsonDeserializer<Color> {
    override fun serialize(src: Color?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.toArgb() ?: 0)
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Color {
        return Color(json?.asInt ?: 0)
    }
}

/**
 * Custom Gson TypeAdapter for ThemeWithColor
 */
class ThemeWithColorTypeAdapter : JsonSerializer<ThemeWithColor>, JsonDeserializer<ThemeWithColor> {
    override fun serialize(src: ThemeWithColor?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        if (src == null) return JsonNull.INSTANCE
        val obj = JsonObject()
        obj.addProperty("theme", src.theme.name)
        obj.addProperty("color", src.color.toArgb())
        return obj
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ThemeWithColor {
        val obj = json?.asJsonObject ?: throw JsonParseException("Invalid ThemeWithColor")
        val themeName = obj.get("theme").asString
        val colorInt = obj.get("color").asInt
        return ThemeWithColor(
            theme = CycleTheme.valueOf(themeName),
            customColor = Color(colorInt)
        )
    }
}

/**
 * Type converters for Room database
 */
class Converters {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
        .registerTypeAdapter(Color::class.java, ColorTypeAdapter())
        .registerTypeAdapter(ThemeWithColor::class.java, ThemeWithColorTypeAdapter())
        .create()

    // LocalDate converters (for entity fields)
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it) }
    }

    // Note: No Color converter needed - entity uses Int (ARGB) for colors
    // Color TypeAdapter above is for Gson JSON serialization only

    // MandalaStyle converter
    @TypeConverter
    fun fromMandalaStyle(style: MandalaStyle): String {
        return style.name
    }

    @TypeConverter
    fun toMandalaStyle(styleName: String): MandalaStyle {
        return MandalaStyle.valueOf(styleName)
    }

    // CycleTheme converter
    @TypeConverter
    fun fromCycleTheme(theme: CycleTheme): String {
        return theme.name
    }

    @TypeConverter
    fun toCycleTheme(themeName: String): CycleTheme {
        return CycleTheme.valueOf(themeName)
    }

    // DinaTheme converter
    @TypeConverter
    fun fromDinaTheme(theme: DinaTheme): String {
        return theme.name
    }

    @TypeConverter
    fun toDinaTheme(themeName: String): DinaTheme {
        return DinaTheme.valueOf(themeName)
    }

    // List<Parva> converter
    @TypeConverter
    fun fromParvaList(parvas: List<Parva>): String {
        return gson.toJson(parvas)
    }

    @TypeConverter
    fun toParvaList(parvasJson: String): List<Parva> {
        val type = object : TypeToken<List<Parva>>() {}.type
        return gson.fromJson(parvasJson, type)
    }

    // List<Saptaha> converter
    @TypeConverter
    fun fromSaptahaList(saptahas: List<Saptaha>): String {
        return gson.toJson(saptahas)
    }

    @TypeConverter
    fun toSaptahaList(saptahasJson: String): List<Saptaha> {
        val type = object : TypeToken<List<Saptaha>>() {}.type
        return gson.fromJson(saptahasJson, type)
    }

    // List<Dina> converter
    @TypeConverter
    fun fromDinaList(dinas: List<Dina>): String {
        return gson.toJson(dinas)
    }

    @TypeConverter
    fun toDinaList(dinasJson: String): List<Dina> {
        val type = object : TypeToken<List<Dina>>() {}.type
        return gson.fromJson(dinasJson, type)
    }
}

