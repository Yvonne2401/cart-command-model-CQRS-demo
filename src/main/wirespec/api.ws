type CartId /^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$/
type ProductId /^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$/

type CreateCart {
  cartid: CartId
}

type AddItemToCart{
  cartid: CartId,
  productid: ProductId,
  quantity: Number,
  baseprice: Number
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

type Error {
  code: String,
  description: String?
}

endpoint CreateCart POST CreateCart /carts -> {
    200 -> CartId
}

endpoint AddItemToCart POST AddItemToCart /carts/add-item -> {
    200 -> CartId
    404 -> Error
}

endpoint AdjustItemQuantityInCart POST AdjustItemQuantityInCart /carts/{cartId: CartId}/adjust-quantity -> {
    200 -> CartId
}

endpoint AdjustPriceOfItemInCart POST AdjustPriceOfItemInCart /carts/{cartId: CartId}/adjust-price -> {
    200 -> CartId
}

endpoint RemoveItemFromCart POST RemoveItemFromCart /carts/{cartId: CartId}/remove-item -> {
    200 -> CartId
}

endpoint PayForCart POST PayForCart /carts/{cartId: CartId}/pay -> {
    200 -> CartId
}
