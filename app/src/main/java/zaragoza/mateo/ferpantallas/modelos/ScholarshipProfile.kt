package zaragoza.mateo.ferpantallas.modelos

// Names are saved on the device. slug is the Strapi audience used by the feed.
enum class ScholarshipProfile(val slug: String) {
    ASPIRANTE("aspirantes"),
    BECADO("becados"),
    OTROS("otros"),
}
