@file:OptIn(ExperimentalMaterialApi::class)

package com.pharmacy.ui.screen.admin_product_details.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.flowlayout.FlowRow
import com.pharmacy.R
import com.pharmacy.common.extensions.emptyString
import com.pharmacy.common.formatter.Formatters
import com.pharmacy.common.formatter.PercentFormatter
import com.pharmacy.ui.dialog.InputDialog
import com.pharmacy.ui.model.CategoryItem
import com.pharmacy.ui.model.ProductItem
import com.pharmacy.ui.screen.admin_product_details.AdminProductDetailsViewModel
import com.pharmacy.ui.screen.admin_product_details.model.mvi.AdminProductDetailsViewState
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun AdminProductDetailsContent(viewModel: AdminProductDetailsViewModel) {
    val state: AdminProductDetailsViewState by viewModel.collectAsState()
    ScreenContent(
        state = state,
        onBackAction = viewModel::back,
        onDeleteAction = viewModel::deleteAction,
        onUpdateAction = viewModel::update,
        onChangeCategoryName = viewModel::changeCategoryName,
        onDeleteCategory = viewModel::deleteCategory,
        onAddCategory = viewModel::addCategory,
    )
}

@Composable
private fun ScreenContent(
    state: AdminProductDetailsViewState,
    onBackAction: () -> Unit,
    onDeleteAction: () -> Unit,
    onUpdateAction: (ProductItem) -> Unit,
    onChangeCategoryName: (ProductItem, CategoryItem, String) -> Unit,
    onDeleteCategory: (ProductItem, CategoryItem) -> Unit,
    onAddCategory: (ProductItem, String) -> Unit,
) {
    val product: ProductItem = state.product

    var imageUrl: String by remember { mutableStateOf(product.imageUrl) }
    var name: String by remember { mutableStateOf(product.name) }
    var description: String by remember { mutableStateOf(product.description) }
    var startPrice: Double by remember { mutableStateOf(product.price.startPrice) }
    var discount: Double by remember { mutableStateOf(product.price.discount) }

    val finalPrice: Double by derivedStateOf {
        startPrice - startPrice * (discount / 100.0)
    }
    val currentProduct: ProductItem by derivedStateOf {
        product.copy(
            name = name,
            imageUrl = imageUrl,
            description = description,
            price = product.price.copy(
                startPrice = startPrice.coerceIn(0.0, 1_000_000.0), // TODO: Костыль
                finalPrice = finalPrice.coerceIn(0.0, 1_000_000.0),
                discount = discount.coerceIn(0.0, 100.0),
            )
        )
    }
    val isLoading: Boolean by derivedStateOf {
        with(state) { isContentLoading || isContentEditingLoading || isContentDeletionLoading }
    }

    Scaffold(
        topBar = {
            TopAppBarContent(
                title = product.name,
                onBackAction = onBackAction,
                isDeleteAction = !state.isNewProduct,
                isDeleteActionEnabled = !isLoading,
                onDeleteAction = onDeleteAction
            )
        }
    ) { paddings ->
        if (state.isContentDeletionLoading) {
            LinearProgressIndicator()
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            if (state.isContentLoading) {
                CircularProgressIndicator()
            } else {
                ProductDetailsContent(
                    product = currentProduct,
                    isSaveAvailable = product != currentProduct,
                    isLoading = state.isContentEditingLoading,
                    onImageUrlChanged = { newImageUrl -> imageUrl = newImageUrl },
                    onNameChanged = { newName -> name = newName },
                    onDescriptionChanged = { newDescription -> description = newDescription },
                    onPriceChanged = { newPrice -> startPrice = newPrice },
                    onDiscountChanged = { newDiscount -> discount = newDiscount },
                    onSaveAction = { onUpdateAction.invoke(currentProduct) },
                    onChangeCategoryName = onChangeCategoryName,
                    onDeleteCategory = onDeleteCategory,
                    onAddCategory = onAddCategory,
                )
            }
        }
    }
}

@Composable
private fun TopAppBarContent(
    title: String,
    onBackAction: () -> Unit,
    isDeleteAction: Boolean,
    isDeleteActionEnabled: Boolean,
    onDeleteAction: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackAction) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.content_profile_admin_product_details_action_back)
                )
            }
        },
        title = { Text(text = title) },
        actions = {
            if (isDeleteAction) {
                IconButton(
                    onClick = onDeleteAction,
                    enabled = isDeleteActionEnabled
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.content_profile_admin_product_details_action_delete)
                    )
                }
            }
        }

    )
}

@Composable
private fun ProductDetailsContent(
    product: ProductItem,
    isSaveAvailable: Boolean,
    isLoading: Boolean,
    onImageUrlChanged: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onPriceChanged: (Double) -> Unit,
    onDiscountChanged: (Double) -> Unit,
    onSaveAction: () -> Unit,
    onChangeCategoryName: (ProductItem, CategoryItem, String) -> Unit,
    onDeleteCategory: (ProductItem, CategoryItem) -> Unit,
    onAddCategory: (ProductItem, String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val defaultImage = rememberVectorPainter(Icons.Default.Image)
        AsyncImage(
            modifier = Modifier
                .size(144.dp)
                .align(Alignment.CenterHorizontally),
            model = product.imageUrl,
            placeholder = defaultImage,
            error = defaultImage,
            fallback = defaultImage,
            contentDescription = null
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = product.imageUrl,
            onValueChange = onImageUrlChanged::invoke,
            enabled = !isLoading,
            label = { Text(text = stringResource(R.string.content_profile_admin_product_details_label_product_image)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = product.name,
            onValueChange = onNameChanged,
            enabled = !isLoading,
            label = { Text(text = stringResource(R.string.content_profile_admin_product_details_label_product_name)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = product.description,
            onValueChange = onDescriptionChanged,
            enabled = !isLoading,
            label = { Text(text = stringResource(R.string.content_profile_admin_product_details_label_product_description)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        var editCategoryDialog: CategoryItem? by remember { mutableStateOf(null) }

        editCategoryDialog?.also { category ->
            val isNew = category.name.isEmpty()
            InputDialog(
                text = category.name,
                hint = stringResource(R.string.category),
                onNext = { name ->
                    editCategoryDialog = null
                    if (isNew) {
                        onAddCategory.invoke(product, name)
                    } else {
                        onChangeCategoryName.invoke(product, category, name)
                    }
                },
                negativeText = if (isNew) null else stringResource(R.string.delete),
                onNegative = if (isNew) null else {
                    {
                        editCategoryDialog = null
                        onDeleteCategory.invoke(product, category)
                    }
                },
                onCancel = { editCategoryDialog = null }
            )
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            mainAxisSpacing = 4.dp,
        ) {
            product.categories.forEach { category ->
                Chip(
                    onClick = { editCategoryDialog = category },
                ) {
                    Text(text = category.name)
                }
            }
            Chip(
                onClick = {
                    editCategoryDialog = CategoryItem(emptyString())
                },
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = product.price.startPrice.toString(),
            onValueChange = { value ->
                val newPrice = value.toDoubleOrNull() ?: return@OutlinedTextField
                if (newPrice != product.price.startPrice) {
                    onPriceChanged.invoke(newPrice)
                }
            },
            enabled = !isLoading,
            label = { Text(text = stringResource(R.string.content_profile_admin_product_details_label_product_price)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = product.price.discount.toString(),
            onValueChange = { value ->
                val newDiscount = value.toDoubleOrNull() ?: return@OutlinedTextField
                if (newDiscount != product.price.discount) {
                    onDiscountChanged.invoke(newDiscount)
                }
            },
            enabled = !isLoading,
            label = { Text(text = stringResource(R.string.content_profile_admin_product_details_label_product_discount)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            enabled = !isLoading && isSaveAvailable,
            onClick = onSaveAction
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(44.dp)
                )
            } else {
                Text(text = stringResource(R.string.content_profile_admin_product_details_action_save))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ScreenContentPreview() {
    val state = AdminProductDetailsViewState(
        isContentLoading = false
    )
    ScreenContent(
        state = state,
        onBackAction = {},
        onDeleteAction = {},
        onUpdateAction = {},
        onChangeCategoryName = { _, _, _ -> },
        onAddCategory = { _, _ -> },
        onDeleteCategory = { _, _ -> }
    )
}