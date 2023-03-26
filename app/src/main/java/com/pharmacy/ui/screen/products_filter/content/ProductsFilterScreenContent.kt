//@file:OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)

package com.pharmacy.ui.screen.products_filter.content

//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
//import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
//import androidx.compose.foundation.lazy.staggeredgrid.items
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.material.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.pharmacy.ui.model.CategoryItem
//import com.pharmacy.ui.model.SelectableItem
//import com.pharmacy.ui.screen.products_filter.ProductsFilterViewModel
//import com.pharmacy.ui.screen.products_filter.model.PriceRange
//import com.pharmacy.ui.screen.products_filter.model.mvi.ProductsFilterViewState
//import org.orbitmvi.orbit.compose.collectAsState
//
//@Composable
//fun ProductsFilterScreenContent(viewModel: ProductsFilterViewModel) {
//    val state: ProductsFilterViewState by viewModel.collectAsState()
//    ProductsFilterContent(
//        state = state,
//        onBack = viewModel::back,
//        onTryAgainLoading = {},
//        onPriceRangeChanged = viewModel::onPriceRangeChanged,
//        onCategoryClicked = viewModel::onCategoryClicked,
//        onReset = viewModel::reset,
//        onApply = viewModel::apply,
//    )
//}
//
//@Composable
//private fun ProductsFilterContent(
//    state: ProductsFilterViewState,
//    onBack: () -> Unit,
//    onTryAgainLoading: () -> Unit,
//    onPriceRangeChanged: (Int, Int) -> Unit,
//    onCategoryClicked: (CategoryItem) -> Unit,
//    onReset: () -> Unit,
//    onApply: () -> Unit,
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        when {
//            state.isLoading -> ProductsFilterLoadingContent()
//            else -> ProductsAllFiltersContent(
//                isSearchLoading = state.isSearchLoading,
//                priceRange = state.priceRange,
//                categories = state.categories,
//                productCount = state.productCount,
//                onPriceRangeChanged = onPriceRangeChanged,
//                onCategoryClicked = onCategoryClicked,
//                onApply = onApply,
//            )
//        }
//    }
//}
//
//@Composable
//private fun ProductsFilterLoadingContent() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.TopCenter
//    ) {
//        CircularProgressIndicator()
//    }
//}
//
//@Composable
//private fun ProductsAllFiltersContent(
//    isSearchLoading: Boolean,
//    productCount: Int,
//    priceRange: PriceRange,
//    categories: List<SelectableItem<CategoryItem>>,
//    onPriceRangeChanged: (Int, Int) -> Unit,
//    onCategoryClicked: (CategoryItem) -> Unit,
//    onApply: () -> Unit,
//) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//    ) {
//        PriceRangeItemComponent(
//            priceRange = priceRange,
//            onValueChange = onPriceRangeChanged,
//        )
//        CategoriesListContent(
//            modifier = Modifier.weight(1f),
//            categories = categories,
//            onClick = onCategoryClicked,
//        )
//        Text(text = "Найдено $productCount")
//        Button(onClick = onApply::invoke) {
//            if (isSearchLoading) {
//                CircularProgressIndicator()
//            } else {
//                Text(text = "Применить")
//            }
//        }
//    }
//}
//
//
//@Composable
//private fun CategoriesListContent(
//    modifier: Modifier,
//    categories: List<SelectableItem<CategoryItem>>,
//    onClick: (CategoryItem) -> Unit,
//) {
//    LazyVerticalStaggeredGrid(
//        columns = StaggeredGridCells.Adaptive(56.dp),
//        modifier = modifier,
//        contentPadding = PaddingValues(16.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        items(categories) { order ->
//            CategoryItemComponent(
//                category = order,
//                onClick = { onClick.invoke(order.value) },
//            )
//        }
//    }
//}
//
//@Composable
//private fun CategoryItemComponent(
//    category: SelectableItem<CategoryItem>,
//    onClick: () -> Unit,
//) {
//    Card(
//        onClick = onClick,
//        elevation = 3.dp,
//        border = BorderStroke(
//            width = if (category.isSelected) 1.dp else 0.dp,
//            color = MaterialTheme.colors.primary
//        )
//    ) {
//        Row(
//            modifier = Modifier
//                .wrapContentSize()
//                .padding(horizontal = 4.dp, vertical = 4.dp)
//        ) {
//            Text(text = category.value.name)
//        }
//    }
//}
//
//@Composable
//private fun PriceRangeItemComponent(
//    priceRange: PriceRange,
//    onValueChange: (Int, Int) -> Unit,
//) {
//    var valueRange: ClosedFloatingPointRange<Float>? = null
//    Column {
//        Text(text = "Цена")
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//        ) {
//            PriceRangeInputValueComponent(
//                modifier = Modifier.weight(1f),
//                value = priceRange.currentStart,
//                onValueChange = { value ->
//                    onValueChange.invoke(value, priceRange.currentEnd)
//                },
//                label = "От",
//            )
//            PriceRangeInputValueComponent(
//                modifier = Modifier.weight(1f),
//                value = priceRange.currentEnd,
//                onValueChange = { value ->
//                    onValueChange.invoke(priceRange.currentStart, value)
//                },
//                label = "До",
//            )
//        }
//        RangeSlider(
//            modifier = Modifier,
//            value = priceRange.currentStart.toFloat().rangeTo(priceRange.currentEnd.toFloat()),
//            valueRange = priceRange.start.toFloat().rangeTo(priceRange.end.toFloat()) ,
//            onValueChange = { newValueRange ->
//                valueRange = newValueRange
//            },
//            onValueChangeFinished = {
//                valueRange?.also {
//                    onValueChange.invoke(it.start.toInt(), it.endInclusive.toInt())
//                }
//            },
//        )
//    }
//}
//
//@Composable
//private fun PriceRangeInputValueComponent(
//    modifier: Modifier,
//    value: Int,
//    onValueChange: (Int) -> Unit,
//    label: String,
//) {
//    Card(
//        modifier = modifier,
//        backgroundColor = Color.Gray,
//    ) {
//        Row(modifier) {
//            Text(
//                text = label,
//            )
//            BasicTextField(
//                value = value.toString(),
//                onValueChange = { value -> value.toIntOrNull()?.also(onValueChange) },
//            )
//        }
//    }
//}
//
//@Preview
//@Composable
//fun ProductsFilterContentPreview() {
//
//    val state = ProductsFilterViewState(
//        isLoading = false,
//        priceRange = PriceRange(
//            start = 0,
//            end = 10_000,
//            currentStart = 1_000,
//            currentEnd = 8_000,
//        ),
//        categories = List(10) { id ->
//            SelectableItem(CategoryItem(id.toLong(), "Category $id"))
//        },
//        productCount = 10,
//    )
//
//    ProductsFilterContent(
//        state = state,
//        onBack = { },
//        onTryAgainLoading = { },
//        onPriceRangeChanged = { start, end -> },
//        onCategoryClicked = { },
//        onReset = { },
//        onApply = { },
//    )
//}