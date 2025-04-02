Clone the git project.
1.Import project in eclipse IDE 
2. Right click on project name -> Run as -> maven build
3. Once build is successfully running. Right click on project name -> Run as -> maven test
4. Output on test cases will be displayed in console,
5. OR use provided json request packets to generate individual test response: -


GetProductById - http://localhost:8080/products/1
GetAllProducts - http://localhost:8080/products
AddProduct - http://localhost:8080/products
UpdateproductByID -  http://localhost:8080/products/1
DeleteProduct - http://localhost:8080/products/1
GetStockAvailablity - http://localhost:8080/products/1/stock?count=1
GetPriceSortedProducts - http://localhost:8080/products/sorted
