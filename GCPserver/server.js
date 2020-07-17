const express = require("express");
const cors = require('cors')
const urll = require('url');
const app = express();
const bodyparser = require("body-parser");
const axios = require("axios");
const url = "https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=YOURAPPKEY&RESPONSE-DATA-FORMAT=JSON&keywords=ps4&sortOrder=BestMatch&paginationInput.entriesPerPage=100&itemFilter(0).name=MinPrice&itemFilter(0).value=0&itemFilter(1).paramName=Currency&itemFilter(1).paramValue=USD&itemFilter(2).name=MaxPrice&itemFilter(2).value=9999999&itemFilter(3).paramName=Currency&itemFilter(3).paramValue=USD&itemFilter(4).name=ReturnsAcceptedOnly&itemFilter(4).value=false&itemFilter(5).name=Condition&itemFilter(5).value(0)=1000&itemFilter(5).value(1)=1500&itemFilter(5).value(2)=1750&itemFilter(5).value(3)=2000&itemFilter(5).value(4)=2500&itemFilter(5).value(5)=3000&itemFilter(5).value(6)=4000&itemFilter(5).value(7)=5000&itemFilter(5).value(8)=6000&itemFilter(5).value(9)=7000&itemFilter(6).name=FreeShippingOnly&itemFilter(6).value=false";
const port = process.env.PORT || 3200;

//Middle ware

app.use(bodyparser.json());
app.use(bodyparser.urlencoded({ extended: false }));
app.use(cors())

const orders = {'a':1,'b':2};

function Item(jsonData) {
    try {
        dict0 = {}
        dict0['image'] = 'https://thumbs1.ebaystatic.com/pict/04040_0.jpg'
        if('galleryURL' in jsonData)
            dict0['image'] = jsonData['galleryURL'][0]
        dict0['title'] = jsonData['title'][0]
        dict0['itemId'] = jsonData['itemId'][0]
        dict0['category'] = jsonData['primaryCategory'][0]['categoryName'][0]
        dict0['url'] = "https://www.ebay.com/"
        if('viewItemURL' in jsonData)
            dict0['url'] = jsonData['viewItemURL'][0]
        dict0['condition'] ="N/A"
        if('condition' in jsonData)
            dict0['condition'] = jsonData['condition'][0]['conditionDisplayName'][0]
        
        currentPrice = jsonData['sellingStatus'][0]['convertedCurrentPrice'][0]["__value__"]
        shipping = 0.0
        if('shippingServiceCost' in jsonData['shippingInfo'][0])
            shipping = jsonData['shippingInfo'][0]['shippingServiceCost'][0]["__value__"]
        dict0['currentPrice'] = currentPrice
        dict0['shippingCost'] = shipping
        // if(shipping > 0.0)
        //     dict0['priceStr'] = '$'+currentPrice+' (+ $'+shipping+' for shipping)'
        // else
        //     dict0['priceStr'] = '$'+currentPrice
        dict0['expeditedShipping'] = jsonData['shippingInfo'][0]['expeditedShipping'][0]
        dict0['location'] = jsonData['location'][0]
        //dict0['acceptReturn'] = jsonData['returnsAccepted'][0]
        dict0['topRatedListing'] = jsonData['topRatedListing'][0]
        dict0['shippingType'] = jsonData['shippingInfo'][0]['shippingType'][0]
        dict0['shipToLocations'] =jsonData['shippingInfo'][0]['shipToLocations'][0]
        dict0['oneDayShippingAvailable'] =jsonData['shippingInfo'][0]['oneDayShippingAvailable'][0]

        dict0['bestOfferEnabled'] =jsonData['listingInfo'][0]['bestOfferEnabled'][0]
        dict0['buyItNowAvailable'] =jsonData['listingInfo'][0]['buyItNowAvailable'][0]
        dict0['listingType'] =jsonData['listingInfo'][0]['listingType'][0]
        dict0['gift'] =jsonData['listingInfo'][0]['gift'][0]
        dict0['shippingInfo'] = jsonData['shippingInfo'][0]
        try {
            dict0['watchCount'] =jsonData['listingInfo'][0]['watchCount'][0]
        }
        catch 
        {
            dict0['watchCount'] = '-1'
        }
        
        return dict0
    }
    catch  (error) {
        console.log(error);
        return false
    }
    //console.log(jsonData)
    
}

function Single(jsonData) {
    try {
        dict0 = {}
        
        dict0['PictureURL'] =jsonData['Item']['PictureURL']
        dict0['Subtitle'] = null
        dict0['Brand'] = null
        dict0['Specifics'] = []
        dict0['Seller'] = jsonData['Item']['Seller']
        dict0['ReturnPolicy'] = jsonData['Item']['ReturnPolicy']
        if('ItemSpecifics' in jsonData['Item']) {
            ItemSpecifics = jsonData["Item"]['ItemSpecifics']['NameValueList']
            for(var i = 0; i < ItemSpecifics.length; i++) {
                specificName = ItemSpecifics[i]["Name"]
                specificValue = ItemSpecifics[i]["Value"]
                if(specificName == 'Subtitle')
                    dict0['Subtitle'] = specificValue
                else if(specificName == 'Brand')
                    dict0['Brand'] = specificValue
                else if(dict0['Specifics'].length < 5)
                    dict0['Specifics'].push(specificValue)

            }
        }
        

        return dict0
    }
    catch  (error) {
        console.log(error);
        return false
    }
    
}

condDict = {
    "New":'New',
    "Used":'Used',
    "Very Good":'4000',
    "Good":'5000',
    "Acceptable":'6000',
    "Unspecified": "Unspecified"
}

String.prototype.format = function () {
    var a = this;
    for (var k in arguments) {
        a = a.replace(new RegExp("\\{" + k + "\\}", 'g'), arguments[k]);
    }
    return a
}

app.get("/req", (req, res) => {
    const getData = async url => {
        try {
            var params = urll.parse(req.url, true).query;
            keywords = params.Keywords
            minPrice = '0'
            if(params.minPrice.length > 0)
                minPrice = params.minPrice
            maxPrice = '9999999'
            if(params.maxPrice.length > 0)
                maxPrice = params.maxPrice
            condition = params.Condition
            seller = params.Seller
            shipping = params.Shipping
            sort = params.Sort
            console.log(keywords,minPrice,maxPrice,condition,seller,shipping,sort)
            console.log(params)

            acceptReturn='false'
            if(seller=='Return Accepted')
                acceptReturn='true'
            freeShipping='false'
            if(shipping == 'Free')
                freeShipping='true'
            else if(typeof(shipping)=="object"&& 'Free' in shipping)
                freeShipping='true'
            expeditedShipping=''
            if(shipping == 'Expedited')
                expeditedShipping="&itemFilter(7).name=ExpeditedShippingType&itemFilter(7).value=Expedited";
            else if(typeof(shipping)=="object"&& 'Expedited' in shipping)
                expeditedShipping="&itemFilter(7).name=ExpeditedShippingType&itemFilter(7).value=Expedited";
            conditionStr = "&itemFilter(5).name=Condition&itemFilter(5).value(0)=1000&itemFilter(5).value(1)=1500&itemFilter(5).value(2)=1750&itemFilter(5).value(3)=2000&itemFilter(5).value(4)=2500&itemFilter(5).value(5)=3000&itemFilter(5).value(6)=4000&itemFilter(5).value(7)=5000&itemFilter(5).value(8)=6000&itemFilter(5).value(9)=7000"
            if(typeof(condition)=="string"){
                conditionStr = "&itemFilter(5).name=Condition"
                cnt = 0
                conditionStr+='&itemFilter(5).value('+cnt+')='+condDict[condition]
            }
            else if(typeof(condition)=="object")
            {
                conditionStr = "&itemFilter(5).name=Condition"
                var cnt = 0
                for(i of condition){
                    conditionStr+='&itemFilter(5).value('+cnt+')='+condDict[i]
                    cnt = cnt+1
                }
                    
            }
            path = "https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=YOURAPPKEY&RESPONSE-DATA-FORMAT=JSON&keywords={0}&sortOrder={7}&paginationInput.entriesPerPage=50&itemFilter(0).name=MinPrice&itemFilter(0).value={1}&itemFilter(1).paramName=Currency&itemFilter(1).paramValue=USD&itemFilter(2).name=MaxPrice&itemFilter(2).value={2}&itemFilter(3).paramName=Currency&itemFilter(3).paramValue=USD&itemFilter(4).name=ReturnsAcceptedOnly&itemFilter(4).value={3}{4}&itemFilter(6).name=FreeShippingOnly&itemFilter(6).value={5}{6}".format(keywords,minPrice,maxPrice,acceptReturn,conditionStr,freeShipping,expeditedShipping,sort)
            console.log(path)
            const response = await axios.get(path);
            const data = response.data;
            //console.log(data);

            totalEntities = data['findItemsAdvancedResponse'][0]['paginationOutput'][0]["totalEntries"][0]
            itemList = []
            console.log(totalEntities)

            if(totalEntities != 0)
                for(const i of data['findItemsAdvancedResponse'][0]['searchResult'][0]['item']) {
                    if(Item(i)!=false)
                        itemList.push(Item(i))
                }
            itemJSON = {'totalEntities':totalEntities, "itemList":itemList}
            res.status(200).send(itemJSON);
        } catch (error) {
            console.log(error);
            res.status(200).send(error);
        }
      };
      
    getData(url);
    
});

app.get("/reqItem", (req, res) => {
    const getData = async url => {
        try {
            var params = urll.parse(req.url, true).query;
            id = params.id;
            console.log(id)
            console.log(params)

            
            path = "http://open.api.ebay.com/shopping?callname=GetSingleItem&responseencoding=JSON&appid=YOURAPPKEY&siteid=0&version=967&ItemID="+id+"&IncludeSelector=Description,Details,ItemSpecifics";
            console.log(path)
            const response = await axios.get(path);
            const data = response.data;
            //console.log(data);

            console.log(Single(data))
            res.status(200).send(Single(data));
        } catch (error) {
            console.log(error);
            res.status(200).send(error);
        }
      };
      
    getData(url);
    
});
app.listen(port, () => {
  console.log(`running at port ${port}`);
});