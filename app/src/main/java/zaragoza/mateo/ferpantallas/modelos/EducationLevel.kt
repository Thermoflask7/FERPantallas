package zaragoza.mateo.ferpantallas.modelos

// Names are saved on the device. slug is the Strapi level used by the feed.
enum class EducationLevel(val slug: String) {
    PREPARATORIA("prepa"),
    UNIVERSIDAD("uni"),
    POSGRADO("posgrado"),
}
