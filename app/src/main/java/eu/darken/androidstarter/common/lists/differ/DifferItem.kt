package eu.darken.androidstarter.common.lists.differ

import eu.darken.androidstarter.common.lists.ListItem

interface DifferItem : ListItem {
    val stableId: Long

    val payloadProvider: ((DifferItem, DifferItem) -> DifferItem?)?
        get() = null
}