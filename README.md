# westerosBudget
Sample app to list the transactions and allow user to change the state of transactions

The app takes in the data from the JsonBlobApi. The link to the data is: The link to data is: https://jsonblob.com/563e061ce4b01190df3ef64a

Features:

1. Allows user to see a list of transactions fetched from the API
2. Allows the user to change the state of any transaction among these three states(Unverified, Verfied, Fraud)
3. There are only two categories of transactions (Recharge, Taxi). App allows user to filter the transactions
4. Allows user to do swipe to refresh the data.
5. App also polls the server every 1 minute to look for new data.

Third Party Library Used:

1. Volley: for the netwrok requests
2. gson: for parsing the JsonResponse to the Java Object and vice versa.

Assumptions

1. The transaction status cannot be changed from any where else. So when the no of transactions changes then only will the app 
populate the new data.
2. Category and State fields are mandatory.
3. At one point of time only one transaction state can be changed.

TODO 

1. Right now if there is no network or we don't get any data from the server the app will just say no transactions present. We can easily handle the type of error and show 
the customized error to the user.
2. When the state of a transaction is being updated we can show a loading spinner till we hear back from the server.

