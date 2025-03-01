type CartId /^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$/
type ProductId /^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$/

type CreateCart {
  cartId: CartId
}

type AddItemToCart{
  productId: ProductId,
  quantity: Number,
  price: Number
}

type AdjustItemQuantityInCart{
  quantity: Number
}

type AdjustPriceOfItemInCart{
  price: Number
}

type PayForCart{
  amountPaid: Number
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

endpoint AdjustItemQuantityInCart POST AdjustItemQuantityInCart /carts/{cartId: String}/product/{productId: String}/adjust-quantity -> {
    200 -> CartId
    400 -> BadRequest
    404 -> NotFoundError
}

endpoint AdjustPriceOfItemInCart POST AdjustPriceOfItemInCart /carts/{cartId: String}/product/{productId: String}/adjust-price -> {
    200 -> CartId
    400 -> BadRequest
    404 -> NotFoundError
}

endpoint RemoveItemFromCart POST /carts/{cartId: String}/product/{productId: String}/remove-item -> {
    200 -> CartId
    400 -> BadRequest
    404 -> NotFoundError
}

endpoint PayForCart POST PayForCart /carts/{cartId: String}/pay -> {
    200 -> CartId
    400 -> BadRequest
    404 -> NotFoundError
}
