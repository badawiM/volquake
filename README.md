![LOGO](./volquake.jpeg)

## Welcome to Volquake

(C) 2022 - John Edwards

This project demonstrates streaming prices over a reactive websocket then put over a stomp websocket into an angular UI

- You don't need to start any docker services because RabbitMQ is commented out for now. You can experiment with comparing reactive websocket vs RabbitMQ which is also now [reactive](https://projectreactor.io/docs/rabbitmq/snapshot/reference/)

- Start `price-request` (this is the where it asks for the price stream so simulating a blotter)

- Start `pricing` (this is the where the prices can be generated simulating a [Generalized Wiener](https://en.wikipedia.org/wiki/Generalized_Wiener_process#:~:text=In%20statistics%2C%20a%20generalized%20Wiener,at%20every%20point%20in%20time.) Process or Brownian Motion. This is cool finance shizzle and if you're interested I'll explain it to you )

- Start the UI in `price-request` with `npm serve` 

- Go to [http://localhost:4200/](http://localhost:4200/) and be awestruck by my Angular pimp game. ("_hey... did I tell you I was full-stack, baby?_") Actually, I think it hardcodes the ticker to `VOD.L` right now but you can change the UI to pass another ticker but they must be listed in the config otherwise it doesn't have the stochastic parameters.

- The stocks prices generated for are in `pricing/src/main/resources` and parameters are underlying,mu (i.e. the mean drift or expected yearly return),sigma (volatility) and startPrice - so if you can crank up volatility to make prices bounce around even more

- Now pour yourself a brandy - [possibly](https://www.cdc.gov/tobacco/basic_information/health_effects/cancer/index.htm) crack open a cuban cigar - and read Louis Bachelier's [Theory of Speculation](https://press.princeton.edu/books/hardcover/9780691117522/louis-bacheliers-theory-of-speculation). Possibly the second most famous French stockbroker after [Paul Gaughin](https://en.wikipedia.org/wiki/Paul_Gauguin) 


Trade safely, kids. And remember it's no fun being broke.
