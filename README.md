# amialergic
App to check for allergens

Data dump for my first andriod app

Goal: Create an app that scans ingredients for allergens.

Credit to the resources that have brought me this far.

    How to make an app that scans barcodes: https://www.youtube.com/watch?v=xcWnVKnZBgk
    How to create your apk: https://www.youtube.com/watch?v=BfuOn5LuOA4
    API example: https://world.openfoodfacts.org/api/v0/product/737628064502.json

2021-02-05T04:42+00:00 First iteration, app only scans barcodes and returns the number.

TODO: Find what variable is holding the number that is returned by the barcode scanner. 
Use that number to curl the openfoodfacts REST API, something like this: curl -O https://world.openfoodfacts.org/api/v0/product/${scanResults}.json 
Create a list in a text file of known allergens Diff the ingrediants of the food item with known allergens. 
If found, turn screen red and list found allergens. 
Else do nothing.
