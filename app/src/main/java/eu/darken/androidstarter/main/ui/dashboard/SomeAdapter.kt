package eu.darken.androidstarter.main.ui.dashboard

import android.view.ViewGroup
import eu.darken.androidstarter.R
import eu.darken.androidstarter.common.lists.BindableVH
import eu.darken.androidstarter.common.lists.differ.AsyncDiffer
import eu.darken.androidstarter.common.lists.differ.DifferItem
import eu.darken.androidstarter.common.lists.differ.HasAsyncDiffer
import eu.darken.androidstarter.common.lists.differ.setupDiffer
import eu.darken.androidstarter.common.lists.modular.ModularAdapter
import eu.darken.androidstarter.common.lists.modular.mods.DataBinderMod
import eu.darken.androidstarter.common.lists.modular.mods.SimpleVHCreatorMod
import eu.darken.androidstarter.databinding.SomeItemLineBinding
import javax.inject.Inject


class SomeAdapter @Inject constructor() : ModularAdapter<SomeAdapter.ItemVH>(),
    HasAsyncDiffer<SomeAdapter.Item> {

    override val asyncDiffer: AsyncDiffer<*, Item> = setupDiffer()

    override fun getItemCount(): Int = data.size

    init {
        addMod(DataBinderMod(data))
        addMod(SimpleVHCreatorMod { ItemVH(it) })
    }

    data class Item(
        val label: String,
        val number: Long,
        val onClickAction: (Long) -> Unit
    ) : DifferItem {
        override val stableId: Long = label.hashCode().toLong()
    }

    class ItemVH(parent: ViewGroup) : VH(R.layout.some_item_line, parent),
        BindableVH<Item, SomeItemLineBinding> {

        override val viewBinding: Lazy<SomeItemLineBinding> = lazy {
            SomeItemLineBinding.bind(itemView)
        }

        override val onBindData: SomeItemLineBinding.(
            item: Item,
            payloads: List<Any>
        ) -> Unit = { item, _ ->
            numberDisplay.text = "${item.label} #${item.number}"

            itemView.setOnClickListener { item.onClickAction(item.number) }
        }
    }
}