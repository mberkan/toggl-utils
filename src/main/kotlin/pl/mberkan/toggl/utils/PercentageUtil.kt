package pl.mberkan.toggl.utils

fun percentage(value: Long?, sum: Long?): Long? {
    if (value == null || sum == null) {
        return null
    }
    return value*100/sum
}
