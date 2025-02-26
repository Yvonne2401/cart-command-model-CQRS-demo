# CQRS-command-model-demo

This is an example how you could build a command side webapp using:

  - Springboot
  - Maven
  - AxonFramework
  - AxonServer (external jar or in Docker)
  - WireSpec to generate the endpoints


This is an example of an Ecommerce checkout service.
The aggregate is the Shopping(Cart) with the following events:

CartCreated -> new Cart Aggregate was created
ItemAddedToCart -> certain amount of products were added
ItemRemovedFromCart -> certain product was removed
TotalAmountRecalculated -> the total amount of the cart was changed while handling the command
ItemQuantityAdjustedInCart -> the number ordered for a product has been changed
PriceOfItemInCartAdjusted -> the price of a product has changed
PaymentReceived -> a payment has been done
OrderCreated -> the order has been paid and created
