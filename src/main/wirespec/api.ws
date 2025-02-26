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

type BadRequest {
  code: String,
  description: String?
}

type NotFoundError {
  code: String,
  description: String?
}

type ConflictError{
  code: String,
  description: String?
}

endpoint CreateCart POST CreateCart /carts -> {
    200 -> CartId
    400 -> BadRequest
    409 -> ConflictError
}

endpoint AddItemToCart POST AddItemToCart /carts/{cartId: String}/add-item -> {
    200 -> CartId
    400 -> BadRequest
    404 -> NotFoundError
}

endpoint AdjustItemQuantityInCart POST AdjustItemQuantityInCart /carts/{cartId: String}/adjust-quantity -> {
    200 -> CartId
    400 -> BadRequest
    404 -> NotFoundError
}

endpoint AdjustPriceOfItemInCart POST AdjustPriceOfItemInCart /carts/{cartId: String}/adjust-price -> {
    200 -> CartId
    400 -> BadRequest
    404 -> NotFoundError
}

endpoint RemoveItemFromCart POST RemoveItemFromCart /carts/{cartId: String}/remove-item -> {
    200 -> CartId
    400 -> BadRequest
    404 -> NotFoundError
}

endpoint PayForCart POST PayForCart /carts/{cartId: String}/pay -> {
    200 -> CartId
    400 -> BadRequest
    404 -> NotFoundError
}
