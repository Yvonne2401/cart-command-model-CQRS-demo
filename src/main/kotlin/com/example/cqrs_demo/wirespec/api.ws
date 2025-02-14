type CartId /[0-9a-fA-F]{8}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{12}$
type ProductId /[0-9a-fA-F]{8}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{12}$

type CreateCart {
  cartId: CartId
}

type AddItemToCart{
  cartId: CartId,
  productId: ProductId,
  quantity: Number,
  basePrice: Number
}

type AdjustItemQuantityInCart{
    cartId: CartId,
    productId: ProductId,
    quantityDelta: Number
}

type AdjustPriceOfItemInCart{
    cartId: CartId,
    productId: ProductId,
    newPrice: Number
}

type RemoveItemFromCart{
    cartId: CartId,
    productId: ProductId
}

type PayForCart{
  cartId: CartId,
  amountPaid: Number
}

endpoint PostCreateCart POST CreateCart /carts -> {
    200 -> CartId
}

endpoint PatchAddItemToCart PATCH AddItemToCart /carts/{cartId: CartId} -> {
    200 -> CartId
}

endpoint PatchAdjustItemQuantityInCart PATCH AdjustItemQuantityInCart /carts/{cartId: CartId} -> {
    200 -> CartId
}

endpoint PatchAdjustPriceOfItemInCart PATCH AdjustPriceOfItemInCart /carts/{cartId: CartId} -> {
    200 -> CartId
}

endpoint PatchRemoveItemFromCart PATCH RemoveItemFromCart /carts/{cartId: CartId} -> {
    200 -> CartId
}

endpoint PatchPayForCart PATCH PayForCart /carts/{cartId: CartId} -> {
    200 -> CartId
}