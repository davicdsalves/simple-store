# Simple store
Simple project to store products and categories. Also convert product price if currency is not EUR.
The API use integers to represent product price, so 100 means 1,00.

Building project:
```
mvn clean package
```
Running project
```
java -jar target/store-1.0.0-SNAPSHOT.jar
``` 
You can also build the project using docker:
```
mvn clean package docker:build -DdockerImageTags=latest 
```
And run the image (it's on dockerhub so you can skip the docker build if you want):
```
docker run -p 8080:8080 davicdsalves/simple-store
```

Below you can find http requests to reproduce a simple scenario (using https://github.com/jakubroztocil/httpie). 

```
#Create Eletronics category
echo '{ "name": "Eletronics" }' | http -a someuser:somepassword PUT http://127.0.0.1:8080/v1/category
#Create Mobile category, child of Eletronics
echo '{ "name": "Mobile", "parentID": 1 }' | http -a someuser:somepassword PUT http://127.0.0.1:8080/v1/category
#Create iPhone category, child of Mobile
echo '{ "name": "iPhone", "parentID": 2 }' | http -a someuser:somepassword PUT http://127.0.0.1:8080/v1/category
#Retrieve all the categories
http -a someuser:somepassword GET http://127.0.0.1:8080/v1/category/

#Create Food category
echo '{ "name": "Food" }' | http -a someuser:somepassword PUT http://127.0.0.1:8080/v1/category
#Update Food category name to Not Food
echo '{ "name": "Not Food" }' | http -a someuser:somepassword POST http://127.0.0.1:8080/v1/category/4

#Add iPhone 6 product, with category 3 (iPhone)
echo '{ "name": "iPhone 6", "price": 54900, "category": { "id": 3 } }' | http -a someuser:somepassword PUT http://127.0.0.1:8080/v1/product
#Creae Samsung product using BRL currency
echo '{ "name": "samsung galaxy", "price": 189900, "currency": "BRL", "category": { "id": 3 } }' | http -a someuser:somepassword PUT http://127.0.0.1:8080/v1/product
```

Some improvements for next version:
- Use some pagination on searching products.
- Cache the search results. 
- Cache fixer api result.
- Add retrieve of full category path (should navigate through the category parents)
- Retrieve all products for a parent category 
- Use OAuth? Or at least credentials in db. 
- Add endpoint to remove category parent?
- Concurrency, use @Version?
- Add error treatment to fixer integration.
