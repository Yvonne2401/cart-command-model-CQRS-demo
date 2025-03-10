# CQRS-command-model-demo
Demo explaining CQRS by using Axon

This is an example the command model of an Ecommerce checkout service.

  - Springboot
  - Maven
  - AxonFramework
  - AxonServer (external jar or in Docker)
  - WireSpec to generate the endpoints

This is an example the command model of an Ecommerce checkout service.

The aggregate is the Shopping(Cart) with the following events:

| Event                      | Description                                                         |
|----------------------------|---------------------------------------------------------------------|
| CartCreated                | a new Cart Aggregate was created                                    |
| ItemAddedToCart            | a certain amount of products were added                             |
| ItemRemovedFromCart        | a certain product was removed                                       |
| TotalAmountRecalculated    | the total amount of the cart was changed while handling the command |
| ItemQuantityAdjustedInCart | the number ordered for a product has been changed                   |
| PriceOfItemInCartAdjusted  | the price of a product has changed                                  |
| PaymentReceived            | a payment has been received                                         |
| OrderCreated               | the order has been paid and created                                 |

The goal is to map the incoming requests from REST endpoints into commands. In the Aggregate the commands will be handled in command handles in which the business logic is implemented

This is an example of an Ecommerce checkout service.
The aggregate is the (Shopping)Cart with the following events:

