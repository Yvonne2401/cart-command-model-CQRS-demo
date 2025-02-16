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

type Cart{
    cartId: CartId,
    isOrder: Boolean,
    totalAmount: Number,
    amountPaid: Number,
    cartEntries: CartEntry[]
}

type CartEntry{
    productId: ProductId,
    quantity: Number,
    basePrice: Number
}

endpoint CreateCart POST CreateCart /carts -> {
    200 -> CartId
}

endpoint AddItemToCart POST AddItemToCart /carts/{cartId: CartId} -> {
    200 -> CartId
}

endpoint AdjustItemQuantityInCart POST AdjustItemQuantityInCart /carts/{cartId: CartId} -> {
    200 -> CartId
}

endpoint AdjustPriceOfItemInCart POST AdjustPriceOfItemInCart /carts/{cartId: CartId} -> {
    200 -> CartId
}

endpoint RemoveItemFromCart POST RemoveItemFromCart /carts/{cartId: CartId} -> {
    200 -> CartId
}

endpoint PayForCart POST PayForCart /carts/{cartId: CartId} -> {
    200 -> CartId
}
